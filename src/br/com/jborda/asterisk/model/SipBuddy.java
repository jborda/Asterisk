package br.com.jborda.asterisk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="sip_buddies", uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class SipBuddy implements Entidade {
	
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

  @Column(name="accountcode",length=20)
  private String accountcode;

  public void setAccountcode(String accountcode) {
    this.accountcode = accountcode;
  }

  public String getAccountcode() {
    return accountcode;
  }

  @Column(name="allow",length=100)
  private String allow;

  public void setAllow(String allow) {
    this.allow = allow;
  }

  public String getAllow() {
    return allow;
  }

  @Column(name="amaflags",length=7)
  private String amaflags;

  public void setAmaflags(String amaflags) {
    this.amaflags = amaflags;
  }

  public String getAmaflags() {
    return amaflags;
  }

  @Column(name="callerid",length=80)
  private String callerid;

  public void setCallerid(String callerid) {
    this.callerid = callerid;
  }

  public String getCallerid() {
    return callerid;
  }

  @Column(name="callgroup",length=10)
  private String callgroup;

  public void setCallgroup(String callgroup) {
    this.callgroup = callgroup;
  }

  public String getCallgroup() {
    return callgroup;
  }

  @Column(name="cancallforward",length=3)
  private String cancallforward;

  public void setCancallforward(String cancallforward) {
    this.cancallforward = cancallforward;
  }

  public String getCancallforward() {
    return cancallforward;
  }

  @Column(name="canreinvite",length=3)
  private String canreinvite;

  public void setCanreinvite(String canreinvite) {
    this.canreinvite = canreinvite;
  }

  public String getCanreinvite() {
    return canreinvite;
  }

  @Column(name="context",length=80)
  private String context;

  public void setContext(String context) {
    this.context = context;
  }

  public String getContext() {
    return context;
  }

  @Column(name="defaultip",length=15)
  private String defaultip;

  public void setDefaultip(String defaultip) {
    this.defaultip = defaultip;
  }

  public String getDefaultip() {
    return defaultip;
  }

  @Column(name="deny",length=95)
  private String deny;

  public void setDeny(String deny) {
    this.deny = deny;
  }

  public String getDeny() {
    return deny;
  }

  @Column(name="disallow",length=100)
  private String disallow;

  public void setDisallow(String disallow) {
    this.disallow = disallow;
  }

  public String getDisallow() {
    return disallow;
  }

  @Column(name="dtmfmode",length=7)
  private String dtmfmode;

  public void setDtmfmode(String dtmfmode) {
    this.dtmfmode = dtmfmode;
  }

  public String getDtmfmode() {
    return dtmfmode;
  }

  @Column(name="fromdomain",length=80)
  private String fromdomain;

  public void setFromdomain(String fromdomain) {
    this.fromdomain = fromdomain;
  }

  public String getFromdomain() {
    return fromdomain;
  }

  @Column(name="fromuser",length=80)
  private String fromuser;

  public void setFromuser(String fromuser) {
    this.fromuser = fromuser;
  }

  public String getFromuser() {
    return fromuser;
  }

  @Column(name="fullcontact",length=80)
  private String fullcontact;

  public void setFullcontact(String fullcontact) {
    this.fullcontact = fullcontact;
  }

  public String getFullcontact() {
    return fullcontact;
  }

  @Column(name="host",length=31,nullable=false)
  private String host = "";

  public void setHost(String host) {
    this.host = host;
  }

  public String getHost() {
    return host;
  }

  @Column(name="insecure",length=20)
  private String insecure;

  public void setInsecure(String insecure) {
    this.insecure = insecure;
  }

  public String getInsecure() {
    return insecure;
  }

  @Column(name="ipaddr",length=15,nullable=false)
  private String ipaddr = "";

  public void setIpaddr(String ipaddr) {
    this.ipaddr = ipaddr;
  }

  public String getIpaddr() {
    return ipaddr;
  }

  @Column(name="language",length=2)
  private String language;

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return language;
  }

  @Column(name="mailbox",length=50)
  private String mailbox;

  public void setMailbox(String mailbox) {
    this.mailbox = mailbox;
  }

  public String getMailbox() {
    return mailbox;
  }

  @Column(name="mask",length=95)
  private String mask;

  public void setMask(String mask) {
    this.mask = mask;
  }

  public String getMask() {
    return mask;
  }

  @Column(name="md5secret",length=80)
  private String md5secret;

  public void setMd5secret(String md5secret) {
    this.md5secret = md5secret;
  }

  public String getMd5secret() {
    return md5secret;
  }

  @Column(name="musiconhold",length=100)
  private String musiconhold;

  public void setMusiconhold(String musiconhold) {
    this.musiconhold = musiconhold;
  }

  public String getMusiconhold() {
    return musiconhold;
  }

  @Column(name="name",length=80,nullable=false)
  private String name = "";

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Column(name="nat",length=5,nullable=false)
  private String nat = "";

  public void setNat(String nat) {
    this.nat = nat;
  }

  public String getNat() {
    return nat;
  }

  @Column(name="permit",length=95)
  private String permit;

  public void setPermit(String permit) {
    this.permit = permit;
  }

  public String getPermit() {
    return permit;
  }

  @Column(name="pickupgroup",length=10)
  private String pickupgroup;

  public void setPickupgroup(String pickupgroup) {
    this.pickupgroup = pickupgroup;
  }

  public String getPickupgroup() {
    return pickupgroup;
  }

  @Column(name="port",length=5,nullable=false)
  private String port = "";

  public void setPort(String port) {
    this.port = port;
  }

  public String getPort() {
    return port;
  }

  @Column(name="qualify",length=3)
  private String qualify;

  public void setQualify(String qualify) {
    this.qualify = qualify;
  }

  public String getQualify() {
    return qualify;
  }

  @Column(name="regexten",length=80,nullable=false)
  private String regexten = "";

  public void setRegexten(String regexten) {
    this.regexten = regexten;
  }

  public String getRegexten() {
    return regexten;
  }

  @Column(name="regseconds",length=10,nullable=false)
  private int regseconds;

  public void setRegseconds(int regseconds) {
    this.regseconds = regseconds;
  }

  public int getRegseconds() {
    return regseconds;
  }

  @Column(name="restrictcid",length=1)
  private String restrictcid;

  public void setRestrictcid(String restrictcid) {
    this.restrictcid = restrictcid;
  }

  public String getRestrictcid() {
    return restrictcid;
  }

  @Column(name="rtpholdtimeout",length=3)
  private String rtpholdtimeout;

  public void setRtpholdtimeout(String rtpholdtimeout) {
    this.rtpholdtimeout = rtpholdtimeout;
  }

  public String getRtpholdtimeout() {
    return rtpholdtimeout;
  }

  @Column(name="rtptimeout",length=3)
  private String rtptimeout;

  public void setRtptimeout(String rtptimeout) {
    this.rtptimeout = rtptimeout;
  }

  public String getRtptimeout() {
    return rtptimeout;
  }

  @Column(name="secret",length=80)
  private String secret;

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getSecret() {
    return secret;
  }

  @Column(name="type",length=6,nullable=false)
  private String type = "";

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Column(name="username",length=80,nullable=false)
  private String username = "";

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}