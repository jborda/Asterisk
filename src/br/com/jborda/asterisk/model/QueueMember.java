package br.com.jborda.asterisk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="queue_members", uniqueConstraints={@UniqueConstraint(columnNames={"queue_name", "interface"})})
public class QueueMember implements Entidade {
	
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="uniqueid",length=10,nullable=false)
  private int uniqueid;

  public void setUniqueid(int uniqueid) {
    this.uniqueid = uniqueid;
  }

  public int getUniqueid() {
    return uniqueid;
  }

  @Column(name="interface",length=128)
  private String iface;

  public void setIface(String iface) {
    this.iface = iface;
  }

  public String getIface() {
    return iface;
  }

  @Column(name="membername",length=40)
  private String membername;

  public void setMembername(String membername) {
    this.membername = membername;
  }

  public String getMembername() {
    return membername;
  }

  @Column(name="paused")
  private Boolean paused;

  public void setPaused(Boolean paused) {
    this.paused = paused;
  }

  public Boolean getPaused() {
    return paused;
  }

  @Column(name="penalty",length=10)
  private Integer penalty;

  public void setPenalty(Integer penalty) {
    this.penalty = penalty;
  }

  public Integer getPenalty() {
    return penalty;
  }

  @Column(name="queue_name",length=128)
  private String queueName;

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public String getQueueName() {
    return queueName;
  }
}