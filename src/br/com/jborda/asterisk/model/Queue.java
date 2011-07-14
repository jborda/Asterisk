package br.com.jborda.asterisk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "queues")
public class Queue implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "name", length = 128, nullable = false)
	private String name;

	@Column(name = "announce", length = 128)
	private String announce;

	@Column(name = "announce_frequency", length = 10)
	private Integer announceFrequency;

	@Column(name = "announce_holdtime", length = 128)
	private String announceHoldtime;

	@Column(name = "announce_round_seconds", length = 10)
	private Integer announceRoundSeconds;

	@Column(name = "context", length = 128)
	private String context;

	@Column(name = "eventmemberstatus", length = 4)
	private String eventmemberstatus;

	@Column(name = "eventwhencalled", length = 4)
	private String eventwhencalled;

	@Column(name = "joinempty", length = 128)
	private String joinempty;

	@Column(name = "leavewhenempty", length = 128)
	private String leavewhenempty;

	@Column(name = "maxlen", length = 10)
	private Integer maxlen;

	@Column(name = "memberdelay", length = 10)
	private Integer memberdelay;

	@Column(name = "monitor_format", length = 128)
	private String monitorFormat;

	@Column(name = "monitor_type", length = 50, nullable = false)
	private String monitorType = "";

	@Column(name = "musiconhold", length = 128)
	private String musiconhold;

	@Column(name = "reportholdtime")
	private Boolean reportholdtime;
	
	@Column(name = "retry", length = 10)
	private Integer retry;
	
	@Column(name = "ringinuse")
	private Boolean ringinuse;
	
	@Column(name = "servicelevel", length = 10)
	private Integer servicelevel;
	
	@Column(name = "strategy", length = 128)
	private String strategy;
	
	@Column(name = "timeout", length = 10)
	private Integer timeout;
	
	@Column(name = "timeoutrestart")
	private Boolean timeoutrestart;
	
	@Column(name = "weight", length = 10)
	private Integer weight;
	
	@Column(name = "wrapuptime", length = 10)
	private Integer wrapuptime;
	
	@Column(name = "periodic_announce", length = 50)
	private String periodicAnnounce;

	@Column(name = "periodic_announce_frequency", length = 10)
	private Integer periodicAnnounceFrequency;
	
	@Column(name = "queue_callswaiting", length = 128)
	private String queueCallswaiting;
	
	@Column(name = "queue_holdtime", length = 128)
	private String queueHoldtime;

	@Column(name = "queue_seconds", length = 128)
	private String queueSeconds;

	@Column(name = "queue_reporthold", length = 128)
	private String queueReporthold;

	@Column(name = "queue_minutes", length = 128)
	private String queueMinutes;

	@Column(name = "queue_lessthan", length = 128)
	private String queueLessthan;
	
	@Column(name = "queue_thankyou", length = 128)
	private String queueThankyou;
	
	@Column(name = "queue_thereare", length = 128)
	private String queueThereare;
	
	@Column(name = "queue_youarenext", length = 128)
	private String queueYouarenext;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAnnounce(String announce) {
		this.announce = announce;
	}

	public String getAnnounce() {
		return announce;
	}

	public void setAnnounceFrequency(Integer announceFrequency) {
		this.announceFrequency = announceFrequency;
	}

	public Integer getAnnounceFrequency() {
		return announceFrequency;
	}

	public void setAnnounceHoldtime(String announceHoldtime) {
		this.announceHoldtime = announceHoldtime;
	}

	public String getAnnounceHoldtime() {
		return announceHoldtime;
	}

	public void setAnnounceRoundSeconds(Integer announceRoundSeconds) {
		this.announceRoundSeconds = announceRoundSeconds;
	}

	public Integer getAnnounceRoundSeconds() {
		return announceRoundSeconds;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void setEventmemberstatus(String eventmemberstatus) {
		this.eventmemberstatus = eventmemberstatus;
	}

	public String getEventmemberstatus() {
		return eventmemberstatus;
	}

	public void setEventwhencalled(String eventwhencalled) {
		this.eventwhencalled = eventwhencalled;
	}

	public String getEventwhencalled() {
		return eventwhencalled;
	}

	public void setJoinempty(String joinempty) {
		this.joinempty = joinempty;
	}

	public String getJoinempty() {
		return joinempty;
	}

	public void setLeavewhenempty(String leavewhenempty) {
		this.leavewhenempty = leavewhenempty;
	}

	public String getLeavewhenempty() {
		return leavewhenempty;
	}

	public void setMaxlen(Integer maxlen) {
		this.maxlen = maxlen;
	}

	public Integer getMaxlen() {
		return maxlen;
	}

	public void setMemberdelay(Integer memberdelay) {
		this.memberdelay = memberdelay;
	}

	public Integer getMemberdelay() {
		return memberdelay;
	}

	public void setMonitorFormat(String monitorFormat) {
		this.monitorFormat = monitorFormat;
	}

	public String getMonitorFormat() {
		return monitorFormat;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMusiconhold(String musiconhold) {
		this.musiconhold = musiconhold;
	}

	public String getMusiconhold() {
		return musiconhold;
	}

	public void setPeriodicAnnounce(String periodicAnnounce) {
		this.periodicAnnounce = periodicAnnounce;
	}

	public String getPeriodicAnnounce() {
		return periodicAnnounce;
	}

	public void setPeriodicAnnounceFrequency(Integer periodicAnnounceFrequency) {
		this.periodicAnnounceFrequency = periodicAnnounceFrequency;
	}

	public Integer getPeriodicAnnounceFrequency() {
		return periodicAnnounceFrequency;
	}

	public void setQueueCallswaiting(String queueCallswaiting) {
		this.queueCallswaiting = queueCallswaiting;
	}

	public String getQueueCallswaiting() {
		return queueCallswaiting;
	}

	public void setQueueHoldtime(String queueHoldtime) {
		this.queueHoldtime = queueHoldtime;
	}

	public String getQueueHoldtime() {
		return queueHoldtime;
	}

	public void setQueueLessthan(String queueLessthan) {
		this.queueLessthan = queueLessthan;
	}

	public String getQueueLessthan() {
		return queueLessthan;
	}

	public void setQueueMinutes(String queueMinutes) {
		this.queueMinutes = queueMinutes;
	}

	public String getQueueMinutes() {
		return queueMinutes;
	}

	public void setQueueReporthold(String queueReporthold) {
		this.queueReporthold = queueReporthold;
	}

	public String getQueueReporthold() {
		return queueReporthold;
	}

	public void setQueueSeconds(String queueSeconds) {
		this.queueSeconds = queueSeconds;
	}

	public String getQueueSeconds() {
		return queueSeconds;
	}

	public void setQueueThankyou(String queueThankyou) {
		this.queueThankyou = queueThankyou;
	}

	public String getQueueThankyou() {
		return queueThankyou;
	}

	public void setQueueThereare(String queueThereare) {
		this.queueThereare = queueThereare;
	}

	public String getQueueThereare() {
		return queueThereare;
	}

	public void setQueueYouarenext(String queueYouarenext) {
		this.queueYouarenext = queueYouarenext;
	}

	public String getQueueYouarenext() {
		return queueYouarenext;
	}

	public void setReportholdtime(Boolean reportholdtime) {
		this.reportholdtime = reportholdtime;
	}

	public Boolean getReportholdtime() {
		return reportholdtime;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRinginuse(Boolean ringinuse) {
		this.ringinuse = ringinuse;
	}

	public Boolean getRinginuse() {
		return ringinuse;
	}

	public void setServicelevel(Integer servicelevel) {
		this.servicelevel = servicelevel;
	}

	public Integer getServicelevel() {
		return servicelevel;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeoutrestart(Boolean timeoutrestart) {
		this.timeoutrestart = timeoutrestart;
	}
	
	public Boolean getTimeoutrestart() {
		return timeoutrestart;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWrapuptime(Integer wrapuptime) {
		this.wrapuptime = wrapuptime;
	}

	public Integer getWrapuptime() {
		return wrapuptime;
	}
}