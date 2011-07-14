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