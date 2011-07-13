package com.objectgen.importdb;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.objectgen.codegenerator.JavaClass;
import com.objectgen.codegenerator.JavaCodeBase;
import com.objectgen.codegenerator.JavaMethod;
import com.objectgen.codegenerator.JavaVariable;
import com.objectgen.commons.ui.progress.ProgressHandler;
import com.objectgen.dynamic_util.MapOfLists;
import com.objectgen.dynamic_util.MapOfListsWithInverse;
import com.objectgen.jdbc.metadata.Column;
import com.objectgen.jdbc.metadata.DatabaseElement;
import com.objectgen.jdbc.metadata.DatabaseSchema;
import com.objectgen.jdbc.metadata.Table;
import com.objectgen.jdbc.metadata.Index;

public class ImportDB implements IImportDatabase {
   /** Use EnglishNouns to convert between singular and plural. (Not perfect.) */
   private com.objectgen.grammar.Grammar grammar = null; // new com.objectgen.grammar.EnglishNouns();
   
   private ImportDatabaseConfig config = null;
   
   /** Map SQL types to Java types. */
   private HashMap<String,String> sqlTypes2Java;
   
   /** Convert Java reserved words to legal Java identifiers. */
   private HashMap<String,String> fixes;

   private HashMap predefinedPKs = null;
   
   // For internal use:
   private ArrayList<JavaClass> classes = new ArrayList<JavaClass>();
   private HashMap<String,String> primaryKeys = new HashMap<String,String>();
   private HashMap<String,JavaClass> tableNames = new HashMap<String,JavaClass>();
   private HashMap<String,JavaClass> classNames = new HashMap<String,JavaClass>();
   private int worked;
   
   // Results:
   private MapOfListsWithInverse<DatabaseElement,JavaCodeBase> results = new MapOfListsWithInverse<DatabaseElement,JavaCodeBase>();
   private MapOfLists<DatabaseElement,String> errors = new MapOfLists<DatabaseElement,String>();
   
   public ImportDB() {
      initSqlTypes2Java();
      initFixes();
   }

   /**
    * The main method. Analyze a database schema and build code to be generated.
    * @param schema Contains the tables of a database.
    * @return Java classes to generate.
    */
   public List<JavaClass> build(DatabaseSchema schema) {
      clear();
      
      List<Table> tables = determineWhichTablesToImport(schema);

      findPrimaryKeys(schema, tables);

      ArrayList<Table> classTables = new ArrayList<Table>();
      ArrayList<Table> assocTables = new ArrayList<Table>();
      for(Table table : tables) {
         if(isAssociation(table))
            assocTables.add(table);
         else
            classTables.add(table);
      }
      
      for(Table table : classTables)
         buildClass(table);
      
      for(Table table : classTables)
         buildVariables(table);
      
      for(Table table : assocTables)
         buildManyToMany(table);
      
      checkForDuplicateVariableNames(schema);

      return classes;
   }

   private void clear() {
      classes.clear();
      results.clear();
      errors.clear();
   }

   private void increaseWorked(int inc) {
      worked += inc;
      ProgressHandler.setWorked(worked);
   }

   private ArrayList<Table> determineWhichTablesToImport(DatabaseSchema schema) {
      ArrayList<Table> tables = new ArrayList<Table>();
      for(Table table : schema.getTables()) {
         if(importTable(table))
            tables.add(table);
      }
      
      ProgressHandler.beginTask("Analyze tables", 10 + 10 + tables.size());
      worked = 0;
      increaseWorked(10);
      
      return tables;
   }
   
   /**
    * Find the primary key of all tables.
    * If another table has a column of that name,
    * it will be used as a reference, even if no foreign key is defined.
    */
   private void findPrimaryKeys(DatabaseSchema schema, List<Table> tables) {
      ProgressHandler.setSubTask("findPrimaryKeys");

      primaryKeys.clear();
        
// Use something like this to handle conflicts:
//      predefinedPKs = [
//         'group_id': 'phpbb_groups',
//         'user_id': 'phpbb_users',
//         'word_id': 'phpbb_words',
//         'user_id/topic_id': 'phpbb_topics_posted'
//      ]
      for(Table table : tables)
         findPrimaryKey(schema, table);
      
      increaseWorked(10);
   }

   /**
    *
    * @return Status for tracing.
    */
   private String findPrimaryKey(DatabaseSchema schema, Table table) {
     String pk = null;
     for(Column column : table.getColumns()) {
        if(column.primaryKey)
           pk = (pk != null ? pk + "/" : "") + column.name;
     }
     if(predefinedPKs != null && predefinedPKs.containsKey(pk) && predefinedPKs.get(pk) != table.name)
        return "ignored";
     else if(pk == null)
        return "no primary key";
     else if(pk.equals("id"))
        // If many tables have "id" as its PK, we cannot identify the table based on the column name.
        return "ignore id column";
     else {
        if(primaryKeys.containsKey(pk)) {
           errors.add(schema, pk + " is PK in " + primaryKeys.get(pk) + " and " + table.name);
           primaryKeys.put(pk, null);
           return pk + " is already there";
        } else {
           primaryKeys.put(pk, table.name);
           return pk;
        }
     }
   }

   private void checkForDuplicateVariableNames(DatabaseSchema schema) {
      for(JavaClass c : classes) {
         MapOfLists<String,DatabaseElement> varNameSources = new MapOfLists<String,DatabaseElement>();
         for(JavaVariable var : c.getVariables()) {
            DatabaseElement source = results.getKey(var);
            varNameSources.add(var.name, source);
         }
         for(String varName : varNameSources.keySet()) {
            List<DatabaseElement> list = varNameSources.get(varName);
            if(list.size() > 1) {
               for(DatabaseElement column : list)
                  errors.add(column, "Duplicate variable name: " + varName);
            }
         }
      }
   }
   
   /** @return <code>true<code> if this table should be imported. */
   boolean importTable(Table table) {
// Can filter tables like this:
//if(table.name.startsWith("phpbb_acl_"))
//   return false
      
      return true;
   }

   /**
    * @return <code>true</code> to import a Table as an association,
    * or <code>false</code> to import it as a class.
    */
   boolean isAssociation(Table table) {
// Can hard-code table names like this:
//    if(table.name == "orderline")
//        return true

      // Treat all tables with only 2 foreign keys as associations:
      int foreignKeys = 0;
      int attributes = 0;
      for(Column column : table.getColumns()) {
         if(column.foreignKey != null)
            foreignKeys++;
         else
            attributes++;
      }
     
      return (foreignKeys == 2 && attributes == 0);
   }
   
   private void buildClass(Table table) {
      JavaClass cl = new JavaClass();
      cl.name = className(table);
      buildAnnotations(cl, classAnnotations(table));
     
      classes.add(cl);
      tableNames.put(table.name, cl);
      classNames.put(cl.name, cl);
   }
   
   List<String> classAnnotations(Table table) {
     ArrayList<String> annotations = new ArrayList<String>();
     annotations.add("@javax.persistence.Entity");
     annotations.add("@javax.persistence.Table(name=" + quote(table.name) + ")");

     if(table.getUniqueConstraints().size() > 0) {
        String uniqueConstraints = "";
        for(Index c : table.getUniqueConstraints()) {
            boolean primaryKeyConstraint = true;
            String columnNames = "";
            for(String columnName : c.columns) {
                Column column = table.findColumn(columnName);
                if(!column.primaryKey)
                    primaryKeyConstraint = false;
                if(columnNames.length() > 0)
                    columnNames += ", ";
                columnNames += quote(columnName);
            }
            if(uniqueConstraints.length() > 0)
                uniqueConstraints += ", ";
            if(!primaryKeyConstraint)
                uniqueConstraints += "@javax.persistence.UniqueConstraint(columnNames={" + columnNames + "})";
        }
        if(uniqueConstraints.length() > 0)
           annotations.add("@javax.persistence.Table(uniqueConstraints={" + uniqueConstraints + "})");
     }
     
     return annotations;
   }
   
   private void buildAnnotations(JavaCodeBase cl, List<String> annotations) {
      for(String s : annotations) {
         if(s.indexOf('(') >= 0) {
            int paramStart = s.indexOf('(');
            int paramEnd = s.lastIndexOf(')')
            String annotationName = s.substring(0, paramStart);
            String annotationParams = s.substring(paramStart+1, paramEnd);
            cl.addAnnotation(annotationName, annotationParams, null);
         } else {
           cl.addAnnotation(s);
         }
      }
   }

   private void buildVariables(Table table) {
      println "buildVariables ${table.name}"
      ProgressHandler.setSubTask(table.name);
      JavaClass cl = tableNames.get(table.name);
      ArrayList<Column> idColumns = new ArrayList<Column>();
      ArrayList<Column> variableColumns = new ArrayList<Column>();
      for(Column column : table.getColumns()) {
         if(column.primaryKey)
            idColumns.add(column);
         else
            variableColumns.add(column);
      }
      if(idColumns.size() == 0) {
         errors.add(table, "Cannot import table; no primary key");
         classes.remove(cl);
         return;
      }
      results.add(table, cl);

      if(idColumns.size() == 1) {
         Column column = idColumns.get(0);
         JavaVariable var = buildVariable(cl, column, false);
         buildVariableAnnotations(column, var, true, false);
      } else if(idColumns.size() > 1) {
         JavaClass idType = buildCompositeIdClass(table);
         classes.add(idType);
         JavaVariable var = buildCompositeIdVariable(table, idType);
         addVariable(cl, var, null);
      }

      for(Column column : variableColumns) {
         // Build an attribute or association for a column in a table.
         JavaVariable var = buildVariable(cl, column, false);
         var.initialValue = buildInitialValue(column, var);
         buildVariableAnnotations(column, var, false, false);
      }
      
      increaseWorked(1);
   }
   
   /** Create a class to hold the composite id values. */
   private JavaClass buildCompositeIdClass(Table table) {
      JavaClass cl = new JavaClass();
      cl.name = className(table) + "Id";
      cl.addAnnotation("@javax.persistence.Embeddable");
      cl.addImplements("java.io.Serializable");
      
      for(Column column : table.getColumns()) {
         if(column.primaryKey) {
            JavaVariable var = buildVariable(cl, column, true);
            buildVariableAnnotations(column, var, false, true);
         }
      }
      
      buildHashCode(cl);
      buildEquals(cl);
      
      results.add(table, cl);
      return cl;
   }
   
   private void buildHashCode(JavaClass cl) {
      String value = "";
      String add = "";
      String mult = "";
      for(JavaVariable var : cl.getVariables()) {
         value += " " + add;
         if(var.type.equals("int") || var.type.equals("short") || var.type.equals("long") || var.type.equals("char"))
            value += mult + var.name;
         else if(var.type.equals("boolean"))
            value += "(" + var.name + " ? 17 : 0)";
         else if(var.type.equals("float")) 
            value += mult + "new Float(" + var.name + ").hashCode()";
         else if(var.type.equals("double"))
            value += mult + "new Double(" + var.name + ").hashCode()";
         else
            value += mult + "(" + var.name + " != null ? " + var.name + ".hashCode() : 0)";
         add = "+ ";
         mult = "17 * ";
      }
      JavaMethod method = new JavaMethod("int", "hashCode");
      method.body = "return" + (value.length() > 0 ? value : " 0") + ";";
      cl.addMethod(method);
   }

   private void buildEquals(JavaClass cl) {
      JavaMethod method = new JavaMethod("boolean", "equals");
      method.parameters = "Object obj";
      method.body =
         "if (obj == this) return true;\n" +
         "if (!(obj instanceof " + cl.name + ")) return false;\n" +
         cl.name + " other = (" + cl.name + ") obj;\n" +
         "return";
      String and = "";
      for(JavaVariable var : cl.getVariables()) {
         method.body += " " + and;
         if(var.type.equals("int") || var.type.equals("short") || var.type.equals("long") || var.type.equals("char") || var.type.equals("boolean"))
            method.body += "this." + var.name + " == other." + var.name;
         else if(var.type.equals("float"))
            method.body += "Float.floatToIntBits(this." + var.name + ") == Float.floatToIntBits(other." + var.name + ")";
         else if(var.type.equals("double"))
            method.body += "Double.doubleToLongBits(this." + var.name + ") == Double.doubleToLongBits(other." + var.name + ")";
         else
            method.body += "(this." + var.name + " != null ? this." + var.name + ".equals(other." + var.name + ") : other." + var.name + " == null)";
         and = "&& ";
      }
      method.body += ";";
      cl.addMethod(method);
   }

   /** Generate a variable to hold a composite id. */
   private JavaVariable buildCompositeIdVariable(Table table, JavaClass idType) {
      JavaVariable var = new JavaVariable();
      var.name = "id";
      var.type = idType.name;
      var.multiplicity = JavaVariable.ONE;
      var.composite = true;
      var.addAnnotation("@javax.persistence.EmbeddedId");
      return var;
   }
   
   private String buildInitialValue(Column column, JavaVariable var) {
       if(column.notNullable) {
          if(var.type == "String")
             return "\"\"";
       }
       return null;
   }
   
   /**
    * Build variables for a join table between 2 entity tables.
    * @return Status for tracing.
    */
   String buildManyToMany(Table table) {
      ProgressHandler.setSubTask(table.name);
      
     Column column1 = null;
     Column column2 = null;
     for(Column column : table.getColumns()) {
       if(column.foreignKey != null) {
         if(column1 == null)
            column1 = column;
         else if(column2 == null)
            column2 = column;
         else
            return "Table has more than 2 foreign keys";
       }
     }
     
     if(column1 == null || column2 == null)
        return "Table does not have 2 foreign keys";
     
     String fromClassName = column1.getReferences();
     String fromColumnName = column1.name;
     String toClassName = column2.getReferences();
     String toColumnName = column2.name;
     
     // Convert database names to better Java names.
     String toVarName = column1.name;
//     toVarName = removeSuffix(toVarName, "_id");
     toVarName = underscoreToCamelCase(toVarName, false);
     
     String fromVarName = column2.name;
//     fromVarName = removeSuffix(fromVarName, "_id");
     fromVarName = underscoreToCamelCase(fromVarName, false);
     
     String assocName = column1.getForeignKey().getTable();
//     assocName = removePrefix(assocName, "TB_")
     assocName = underscoreToCamelCase(assocName, false);

// Special handling, for instance:
//     if(column1.foreignKey.table == "tb_parent_child") {
//        fromVarName = fromVarName.replaceFirst("parent", "p")
//        toVarName = toVarName.replaceFirst("child", "c")
//     }

     fromVarName = fixIllegalIdentifier(toPlural(fromVarName));
     toVarName = fixIllegalIdentifier(toPlural(toVarName));
     
     JavaClass fromClass = tableNames.get(fromClassName);
     JavaClass toClass = tableNames.get(toClassName);
      if(fromClass == null)
         return "Class " + fromClassName + " not found";
      else if(toClass == null)
         return "Class " + toClassName + " not found";
      else {
         JavaVariable fromVar = createVariable(fromClass, column1, fromVarName, toClass, JavaVariable.MANY);
         JavaVariable toVar = createVariable(toClass, column2, toVarName, fromClass, JavaVariable.MANY);
         fromVar.inverse = toVar;
         fromVar.associationName = assocName;
         toVar.associationName = assocName;

         fromVar.addAnnotation("@javax.persistence.ManyToMany");
         fromVar.addAnnotation("@javax.persistence.JoinTable", "name", quote(table.name));
         fromVar.addAnnotation("@javax.persistence.JoinTable", "joinColumns", "{@javax.persistence.JoinColumn(name=\"" + column1.name + "\")}");
         fromVar.addAnnotation("@javax.persistence.JoinTable", "inverseJoinColumns", "{@javax.persistence.JoinColumn(name=\"" + column2.name + "\")}");
         
         toVar.addAnnotation("@javax.persistence.ManyToMany", "mappedBy", quote(fromVarName));
         
         results.add(column1, toVar);
         results.add(column2, fromVar);
      }
      
      increaseWorked(1);
      return null;
   }
   
   /** Build a variable for a database column. */
   JavaVariable buildVariable(JavaClass cl, Column column, boolean compositeId) {
      JavaVariable var = new JavaVariable();
     
      if(importRelation(column)) {
         if(!column.primaryKey) {
            if(column.foreignKey != null) {
               var.fkReferencedTable = column.foreignKey.referencedTable;
               var.type = className(var.fkReferencedTable);
            }
            String referencedTable = getReferencedTable(column);
            if(referencedTable != null)
               var.referencedPK = referencedTable;
            if(column.foreignKey == null && var.referencedPK != null)
               var.type = className(var.referencedPK);
        }
     }
      
     if(var.type == null) {
        if("array" == column.type) {
           String arrayType = removePrefix(column.typeName, "_");
           String javaType = sqlTypes2Java.get(arrayType);
           if(javaType == null)
              throw new IllegalArgumentException("SQL array type " + column.typeName + " is not mapped to a Java type");
           var.type = javaType;
           var.array = true;
        } else {
           var.type = sqlType2Java(column);
        }
     }

     var.name = variableName(column, compositeId);
     var.multiplicity = (column.notNullable ? JavaVariable.ONE : JavaVariable.ZERO_ONE);

     if(var.fkReferencedTable != null || var.referencedPK != null)
        // Generate a bidirectional association.
        var.inverseName = inverseRoleName(column);
     
     addVariable(cl, var, column);
     
     return var;
   }

   /** @return <code>true</code> to import a column as a relation. */
   private boolean importRelation(Column column) {
//      String fullName = column.table.name + "." + column.name;
//      return fullName.equals("user.account");
      return true;
   }

   private void addVariable(JavaClass cl, JavaVariable var, Column column) {
      cl.addVariable(var);
      
      if(column != null) {
         results.add(column, var);
         checkIfLegalJavaIdentifier(column, var.name);
      }

      JavaClass type = null;
      if(var.fkReferencedTable != null)
         type = tableNames.get(var.fkReferencedTable);
      if(type == null && var.referencedPK != null)
         type = tableNames.get(var.referencedPK);
      if(type == null)
         type = classNames.get(var.type);
      if(type != null && var.inverseName != null) {
         // Generate a bidirectional association.
         var.inverse = createVariable(type, column, var.inverseName, cl, JavaVariable.MANY);
         var.inverse.inverseName = var.name;
         var.inverse.addAnnotation("@javax.persistence.OneToMany", "mappedBy", quote(var.name));
         var.inverse.addAnnotation("@javax.persistence.OneToMany", "fetch", "javax.persistence.FetchType.LAZY");
         if(column != null) {
            results.add(column, var.inverse);
            checkIfLegalJavaIdentifier(column, var.inverse.name);
         }
      } else {
         var.inverse = null;
      }
   }
   
   private JavaVariable createVariable(JavaClass cl, Column column, String name, JavaClass type, String multiplicity) {
     JavaVariable var = new JavaVariable();
     var.name = name;
     var.type = (type != null ? type.name : null);
     var.multiplicity = multiplicity;
     if(multiplicity == JavaVariable.MANY)
        var.nameSingular = toSingular(name);
     cl.addVariable(var);
     return var;
   }

   private String getReferencedTable(Column column) {
// Uncomment this to generate associations when the column name is the same
// as a primary key in another table.
//   String referencedTable = primaryKeys[column.name]
//   if(referencedTable == column.table.name)
//      referencedTable = null
       
// Can hard-code values like this:
//   String key = column.table.name + "." + column.name
//   if(key == "phpbb_forums.parent_id")
//      referencedTable = "phpbb_forums"

//   return referencedTable

      return null;
   }
   
   String className(Table table) {
      return className(table.name);
   }
   
   /** Convert a database table name to a Java class name. */
   String className(String tableName) {
      if(tableName == null)
         throw new IllegalArgumentException("tableName is null");

      String className = tableName;
//      className = removePrefix(className, "tb_");
  
      className = toSingular(className);
      
      // Convert "my_table" to "MyTable" etc.
      className = underscoreToCamelCase(className, true);
      
      return className;
   }
   
   /**
    * To create a bidirectional association from a column,
    * return the name of the inverse end here.
    * Take care that the generated names are unique in the inverse class.
    */
   String inverseRoleName(Column column) {

// Return null to create unidirectional associations:
//      String ref = column.getReferences();
//      if(ref == "location")
//         return null;

// Special handling, for instance:
//     if(column.name == "parent_id")
//        return "child";
     
     // The variable name would be better without the column name,
     // but then we could get duplicate variable names.
     String s = column.name + "_" + className(column.table.name);
     s = toPlural(s);
     s = underscoreToCamelCase(s, false);
     s = fixIllegalIdentifier(s);
     return s;
   }
   
   /** Convert a database column name to a Java variable name. */
   String variableName(Column column, boolean compositeId) {
      String variableName = column.name;
      if(compositeId) {
         if(!column.isPrimaryKey())
            variableName = removeSuffix(column.name, "_id");
      } else {
/*
         if(!column.primaryKey)
            variableName = removeSuffix(variableName, "_id");
         else
            // Recommended for Grails:
            variableName = "id";
*/
      }
      variableName = underscoreToCamelCase(variableName, false);
      variableName = fixIllegalIdentifier(variableName);
      return variableName;
   }

   /**
    * Example:
    * @param underscored = "MY_CLASS_1"
    * @param firstUpperCase = true
    * @return "MyClass1"
    */
   private String underscoreToCamelCase(String underscored, boolean firstUpperCase) {
      StringBuffer buf = new StringBuffer();
      if(underscored.length() > 0 && underscored.charAt(0) == '_')
          buf.append('_');

      boolean upperCase = firstUpperCase;
      for(String word : underscored.split("_")) {
         if(word.length() == 1)
            // For example, column "c_date" should generate field "CDate", not "cDate",
            // because the getter getCDate() doesn't match "cDate".
            upperCase = true;
         if(word.length() > 0)
            buf.append(upperCase ? Character.toUpperCase(word.charAt(0)) : Character.toLowerCase(word.charAt(0)));
         if(word.length() > 1)
            buf.append(word.substring(1).toLowerCase());
         upperCase = true;
      }
      return buf.toString();
   }
   
   /**
    * Example:
    * @param s = "abc123"
    * @param prefix = "abc"
    * @return "123"
    */
   private String removePrefix(String s, String prefix) {
      if(s.toUpperCase().startsWith(prefix.toUpperCase()) && s.length() > prefix.length())
         return s.substring(prefix.length());
      else
         return s;
   }
   
   /**
    * Example:
    * @param s = "abc123"
    * @param suffix = "123"
    * @return "abc"
    */
   private String removeSuffix(String s, String suffix) {
      if(s.toUpperCase().endsWith(suffix.toUpperCase()) && s.length() > suffix.length())
         return s.substring(0, s.length() - suffix.length());
      else
         return s;
   }
   
   /** Convert an SQL type to a Java type. */
   private String sqlType2Java(Column column) {
      // If the column is notNullable, return a simple Java type like "long" instead of "Long"
      if(column.notNullable) {
         String javaType = sqlTypes2Java.get(column.type + "-not-null");
         if(javaType != null)
            return javaType;
      }

      String javaType = sqlTypes2Java.get(column.type);
      if(javaType != null)
         return javaType;
      
      throw new IllegalArgumentException("SQL type " + column.type + " is not mapped to a Java type");
   }

   void buildVariableAnnotations(Column column, JavaVariable var, boolean id, boolean compositeId) {
      buildAnnotations(var, variableAnnotations(column, var, id, compositeId));
   }
   
   List<String> variableAnnotations(Column column, JavaVariable var, boolean id, boolean compositeId) {
      ArrayList<String> annotations = new ArrayList<String>();
   
      if(!id && importRelation(column) && column.foreignKey != null || var.referencedPK != null) {
         if(!compositeId)
            annotations.add("@javax.persistence.ManyToOne");
         
         String joinColumnParams = "name=" + quote(column.name);
         if(column.notNullable)
            joinColumnParams += ",nullable=false";
         annotations.add("@javax.persistence.JoinColumn(" + joinColumnParams + ")");
      } else {
         if(id) {
            annotations.add("@javax.persistence.Id");
   
            // This works on MySQL if the PK column has "auto_increment":
            annotations.add("@javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.AUTO)");
         }
         String columnParams = variableColumnAnnotation(column);
         annotations.add("@javax.persistence.Column(" + columnParams + ")");

         if("date".equals(column.type) || "time".equals(column.type) || "timestamp".equals(column.type))
            annotations.add("@javax.persistence.Temporal(value=javax.persistence.TemporalType." + column.type.toUpperCase() + ")");
         if(var.type.equals("Object"))
            annotations.add("@javax.persistence.Lob");
//         if(column.name == "version")
//            annotations.add("@javax.persistence.Version")
      }

      return annotations;
   }
   
   String variableColumnAnnotation(Column column) {
      String columnParams = "name=" + quote(column.name);
      if(column.size != null && column.size != 0)
         columnParams += ",length=" + column.size;  // TODO  + 1 for MySQL?
      if(column.decimals != null && column.decimals != 0)
         columnParams += ",scale=" + column.decimals;
      if(column.notNullable)
         columnParams += ",nullable=false";
      return columnParams;
   }
   
   private void initSqlTypes2Java() {
      sqlTypes2Java = new HashMap<String,String>();
      addSqlType("integer",  "Integer", "int");
      addSqlType("serial",   "Integer", "int");   // Special for PostgreSQL
      addSqlType("long",     "Long",    "long");
      addSqlType("bigint",   "Long",    "long");
      addSqlType("smallint", "Short",   "short");
      addSqlType("tinyint",  "Short",   "short");
      addSqlType("decimal",  "Float",   "float");
      addSqlType("float",    "Float",   "float");
      addSqlType("real",     "Float",   "float");
      addSqlType("double",   "Double",  "double");
      addSqlType("boolean",  "Boolean", "boolean");
      addSqlType("bit",      "Boolean", "boolean");
      addSqlType("int8",     "Long",    "long");
      sqlTypes2Java.put("varchar",       "String");
      sqlTypes2Java.put("longvarchar",   "String");
      sqlTypes2Java.put("char",          "String");
      sqlTypes2Java.put("blob",          "java.sql.Blob");
      sqlTypes2Java.put("clob",          "java.sql.Clob");
      sqlTypes2Java.put("timestamp",     "java.util.Date");
      sqlTypes2Java.put("date",          "java.util.Date");
      sqlTypes2Java.put("time",          "java.util.Date");
      sqlTypes2Java.put("numeric",       "java.math.BigDecimal");
      sqlTypes2Java.put("binary",        "byte[]");
      sqlTypes2Java.put("varbinary",     "byte[]");
      sqlTypes2Java.put("longvarbinary", "byte[]");
   }
   
   private void addSqlType(String sqlType, String javaTypeNullable, String javaTypeNotNull) {
      sqlTypes2Java.put(sqlType,               javaTypeNullable);
      sqlTypes2Java.put(sqlType + "-not-null", javaTypeNotNull);
   }

   private void initFixes() {
      fixes = new HashMap<String,String>();
      fixes.put("class",        "clazz");
      fixes.put("const",        "constant");
      fixes.put("continue",     "cont");
      fixes.put("double",       "dbl");
      fixes.put("implements",   "impls");
      fixes.put("instanceof",   "instance_of");
      fixes.put("interface",    "iface");
      fixes.put("package",      "pkg");
      fixes.put("return",       "ret");
      fixes.put("super",        "sup");
      fixes.put("synchronized", "sync");
      String[] illegal = [ "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "default", "do", "else", "enum", "extends",
            "final", "finally", "float", "for", "goto", "if", "import", "int",
            "long", "native", "new", "private", "protected", "public", "short",
            "static", "strictfp", "switch", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while" ];
      for(String illegalIdentifier : illegal) {
         fixes.put(illegalIdentifier, illegalIdentifier + "_");
      }
   }

   String fixIllegalIdentifier(String s) {
      return (fixes.get(s) != null ? fixes.get(s) : s);
   }
   
   String toPlural(String s) {
      return grammar != null ? grammar.toPlural(s) : s;
   }

   String toSingular(String s) {
      return grammar != null ? grammar.toSingular(s) : s;
   }

   String quote(String s) {
      return "\"" + s + "\"";
   }
   
   private void checkIfLegalJavaIdentifier(DatabaseElement tableOrColumn, String name) {
      if(name.length() == 0) {
         errors.add(tableOrColumn, "Name is empty");
      } else if(fixes.containsKey(name)) {
         errors.add(tableOrColumn, "Reserved keyword: " + name);
      } else {
         boolean legal = Character.isJavaIdentifierStart(name.charAt(0));
         for(int i = 1; legal && i < name.length(); i++) {
            legal &= Character.isJavaIdentifierPart(name.charAt(i));
         }
         if(!legal) {
            errors.add(tableOrColumn, "Illegal Java identifier: " + name);
         }
      }
   }

   public List<JavaCodeBase> getResults(DatabaseElement tableOrColumn) {
      return results.get(tableOrColumn);
   }

   public List<String> getErrors(DatabaseElement target) {
      return errors.get(target);
   }
}