package br.com.jborda.asterisk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "extensions")
public class Extension implements Entidade {

	@Column(name = "id", length = 10, nullable = false)
	@Id
	private int id;

	@Column(name = "context", length = 20, nullable = false)
	private String context;

	@Column(name = "app", length = 20, nullable = false)
	private String app = "";

	@Column(name = "exten", length = 20, nullable = false)
	private String exten;

	@Column(name = "priority", length = 3, nullable = false)
	private short priority;

	@Column(name = "appdata", length = 128, nullable = false)
	private String appdata = "";

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public String getExten() {
		return exten;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public short getPriority() {
		return priority;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getApp() {
		return app;
	}

	public void setAppdata(String appdata) {
		this.appdata = appdata;
	}

	public String getAppdata() {
		return appdata;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}