package br.com.jborda.asterisk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="voicemessages")
public class VoiceMessage implements Entidade {
	
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="id",length=10,nullable=false)
  private int id;

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Column(name="callerid",length=40)
  private String callerid;

  public void setCallerid(String callerid) {
    this.callerid = callerid;
  }

  public String getCallerid() {
    return callerid;
  }

  @Column(name="context",length=80)
  private String context;

  public void setContext(String context) {
    this.context = context;
  }

  public String getContext() {
    return context;
  }

  @Column(name="dir",length=80)
  private String dir;

  public void setDir(String dir) {
    this.dir = dir;
  }

  public String getDir() {
    return dir;
  }

  @Column(name="duration",length=20)
  private String duration;

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getDuration() {
    return duration;
  }

  @Column(name="macrocontext",length=80)
  private String macrocontext;

  public void setMacrocontext(String macrocontext) {
    this.macrocontext = macrocontext;
  }

  public String getMacrocontext() {
    return macrocontext;
  }

  @Column(name="mailboxcontext",length=80)
  private String mailboxcontext;

  public void setMailboxcontext(String mailboxcontext) {
    this.mailboxcontext = mailboxcontext;
  }

  public String getMailboxcontext() {
    return mailboxcontext;
  }

  @Column(name="mailboxuser",length=80)
  private String mailboxuser;

  public void setMailboxuser(String mailboxuser) {
    this.mailboxuser = mailboxuser;
  }

  public String getMailboxuser() {
    return mailboxuser;
  }

  @Column(name="msgnum",length=10,nullable=false)
  private int msgnum;

  public void setMsgnum(int msgnum) {
    this.msgnum = msgnum;
  }

  public int getMsgnum() {
    return msgnum;
  }

  @Column(name="origtime",length=40)
  private String origtime;

  public void setOrigtime(String origtime) {
    this.origtime = origtime;
  }

  public String getOrigtime() {
    return origtime;
  }

  @Column(name="recording",length=2147483647)
  private byte[] recording;

  public void setRecording(byte[] recording) {
    this.recording = recording;
  }

  public byte[] getRecording() {
    return recording;
  }
}