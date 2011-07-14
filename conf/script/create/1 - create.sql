DROP DATABASE asterisk;
CREATE DATABASE asterisk;

CREATE TABLE asterisk.extensions (
id int(11) NOT NULL auto_increment,
context varchar(20) NOT NULL default '',
exten varchar(20) NOT NULL default '',
priority tinyint(4) NOT NULL default '0',
app varchar(20) NOT NULL default '',
appdata varchar(128) NOT NULL default '',
PRIMARY KEY (id)
) ENGINE=MyISAM;

CREATE TABLE asterisk.queue_members (
uniqueid int(10) unsigned NOT NULL auto_increment,
membername varchar(40) default NULL,
queue_name varchar(128) default NULL,
interface varchar(128) default NULL,
penalty int(11) default NULL,
paused tinyint(1) default NULL,
PRIMARY KEY (uniqueid),
UNIQUE KEY queue_interface (queue_name,interface)
) ENGINE=MyISAM;

CREATE TABLE asterisk.queues (
name varchar(128) NOT NULL,
musiconhold varchar(128) default NULL,
announce varchar(128) default NULL,
context varchar(128) default NULL,
timeout int(11) default NULL,
monitor_type varchar(50) NOT NULL,
monitor_format varchar(128) default NULL,
queue_youarenext varchar(128) default NULL,
queue_thereare varchar(128) default NULL,
queue_callswaiting varchar(128) default NULL,
queue_holdtime varchar(128) default NULL,
queue_minutes varchar(128) default NULL,
queue_seconds varchar(128) default NULL,
queue_lessthan varchar(128) default NULL,
queue_thankyou varchar(128) default NULL,
queue_reporthold varchar(128) default NULL,
announce_frequency int(11) default NULL,
announce_round_seconds int(11) default NULL,
announce_holdtime varchar(128) default NULL,
retry int(11) default NULL,
wrapuptime int(11) default NULL,
maxlen int(11) default NULL,
servicelevel int(11) default NULL,
strategy varchar(128) default NULL,
joinempty varchar(128) default NULL,
leavewhenempty varchar(128) default NULL,
eventmemberstatus varchar(4) default NULL,
eventwhencalled varchar(4) default NULL,
reportholdtime tinyint(1) default NULL,
memberdelay int(11) default NULL,
weight int(11) default NULL,
timeoutrestart tinyint(1) default NULL,
periodic_announce varchar(50) default NULL,
periodic_announce_frequency int(11) default NULL,
ringinuse tinyint(1) default NULL,
PRIMARY KEY (name)
) ENGINE=MyISAM;

CREATE TABLE asterisk.sip_buddies (
id int(11) NOT NULL auto_increment,
name varchar(80) NOT NULL default '',
accountcode varchar(20) default NULL,
amaflags varchar(7) default NULL,
callgroup varchar(10) default NULL,
callerid varchar(80) default NULL,
canreinvite char(3) default 'yes',
context varchar(80) default NULL,
defaultip varchar(15) default NULL,
dtmfmode varchar(7) default NULL,
fromuser varchar(80) default NULL,
fromdomain varchar(80) default NULL,
fullcontact varchar(80) default NULL,
host varchar(31) NOT NULL default '',
insecure varchar(20) default NULL,
language char(2) default NULL,
mailbox varchar(50) default NULL,
md5secret varchar(80) default NULL,
nat varchar(5) NOT NULL default 'no',
deny varchar(95) default NULL,
permit varchar(95) default NULL,
mask varchar(95) default NULL,
pickupgroup varchar(10) default NULL,
port varchar(5) NOT NULL default '',
qualify char(3) default NULL,
restrictcid char(1) default NULL,
rtptimeout char(3) default NULL,
rtpholdtimeout char(3) default NULL,
secret varchar(80) default NULL,
type varchar(6) NOT NULL default 'friend',
username varchar(80) NOT NULL default '',
disallow varchar(100) default 'all',
allow varchar(100) default 'g729;ilbc;gsm;ulaw;alaw',
musiconhold varchar(100) default NULL,
regseconds int(11) NOT NULL default '0',
ipaddr varchar(15) NOT NULL default '',
regexten varchar(80) NOT NULL default '',
cancallforward char(3) default 'yes',
PRIMARY KEY (id),
UNIQUE KEY name (name),
KEY name_2 (name)
) ENGINE=MyISAM;

CREATE TABLE asterisk.voicemessages (
id int(11) NOT NULL auto_increment,
msgnum int(11) NOT NULL default '0',
dir varchar(80) default '',
context varchar(80) default '',
macrocontext varchar(80) default '',
callerid varchar(40) default '',
origtime varchar(40) default '',
duration varchar(20) default '',
mailboxuser varchar(80) default '',
mailboxcontext varchar(80) default '',
recording longblob,
PRIMARY KEY (id),
KEY dir (dir)
) ENGINE=MyISAM;