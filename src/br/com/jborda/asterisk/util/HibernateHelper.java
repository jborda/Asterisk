package br.com.jborda.asterisk.util;


/**
 * @generated
 */
public class HibernateHelper {
	/**
	 * @generated
	 */
	private static HibernateHelper singleton = new HibernateHelper();
	/**
	 * @generated
	 */
	private javax.persistence.EntityManagerFactory factory;
	/**
	 * @generated
	 */
	private ThreadLocal currentEntityManager = new ThreadLocal();

	/**
	 * @generated
	 */
	private HibernateHelper() {
	}

	/**
	 * @generated
	 */
	public static void main(String[] args) throws Exception {
		String sqlFile = null;
		if (args.length > 0) {
			sqlFile = args[0];
		}
		boolean print = (sqlFile == null);
		boolean export = (sqlFile == null);
		org.hibernate.cfg.Configuration config = getInstance()
				.getConfiguration();
		org.hibernate.tool.hbm2ddl.SchemaExport exporter = new org.hibernate.tool.hbm2ddl.SchemaExport(
				config);
		if (sqlFile != null) {
			exporter.setOutputFile(sqlFile);
		}
		exporter.create(print, export);
	}

	/**
	 * @generated
	 */
	public static HibernateHelper getInstance() {
		return singleton;
	}

	/**
	 * @generated
	 */
	public synchronized javax.persistence.EntityManagerFactory getFactory() {
		if (factory == null) {
			factory = javax.persistence.Persistence
					.createEntityManagerFactory("Asterisk");
		}
		return factory;
	}

	/**
	 * @generated
	 */
	public synchronized void close() {
		closeEntityManager();
		if (factory != null) {
			factory.close();
			factory = null;
		}
	}

	/**
	 * @generated
	 */
	public org.hibernate.cfg.Configuration getConfiguration()
			throws org.hibernate.MappingException {
		org.hibernate.cfg.AnnotationConfiguration config = new org.hibernate.cfg.AnnotationConfiguration();
		return config;
	}

	/**
	 * @generated
	 */
	public javax.persistence.EntityManager getEntityManager() {
		javax.persistence.EntityManager entityManager = (javax.persistence.EntityManager) currentEntityManager
				.get();
		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = getFactory().createEntityManager();
			currentEntityManager.set(entityManager);
		}
		return entityManager;
	}

	/**
	 * @generated
	 */
	public void closeEntityManager() {
		javax.persistence.EntityManager entityManager = (javax.persistence.EntityManager) currentEntityManager
				.get();
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
		currentEntityManager.set(null);
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "HibernateHelper";
	}
}