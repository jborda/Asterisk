package br.com.jborda.asterisk.negocio;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.jborda.asterisk.model.Entidade;
import br.com.jborda.asterisk.util.HibernateHelper;

public class Negocio {
	
	HibernateHelper instance = HibernateHelper.getInstance();
	
	public void add(Entidade entidade) {
		
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		session.persist(entidade);
		transaction.commit();
	}
	
	private Session getSession() {
		return (Session) instance.getEntityManager().getDelegate();
	}

	public void edit(Entidade entidade) {
		instance.getEntityManager().persist(entidade);
	}
	
	public void delete(Entidade entidade) {
		instance.getEntityManager().remove(entidade);
	}
	
	public boolean exists(Entidade entidade) {
		return instance.getEntityManager().contains(entidade);
	}
	
	public List<Entidade> list(Class classe) {
		
		String sql = "SELECT e FROM " + classe.getSimpleName() + " e";
		Query query = instance.getEntityManager().createQuery(sql);
		
		return (List<Entidade>) query.getResultList();
	}
	
	public static void main(String[] args) {
		
	}
}