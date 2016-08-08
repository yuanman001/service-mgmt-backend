alter table ses_user_mapping add column update_time datetime DEFAULT NULL;
alter table ses_resource_pool drop user_path;
alter table ses_resource_pool drop agent_port;
alter table ses_resource_pool change bin_path data_path varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'ses安装路径  例如:/aifs01/devusers/devrds01/data';
alter table ses_dataimport_ds modify column alias varchar(256);
alter table ses_dataimport_sql modify column ds_alias varchar(256);
alter table ses_dataimport_sql modify column alias varchar(256);


CREATE TABLE `ses_web_pool` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键，非空，id',
  `web_url` varchar(256) COLLATE utf8_bin NOT NULL COMMENT 'web端访问地址',
  `status` int(1) unsigned DEFAULT NULL COMMENT '状态 1生效中  2不可用',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '租户标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `ses_user_web` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键，非空，id',
  `web_id` varchar(256) COLLATE utf8_bin NOT NULL COMMENT 'web端访问地址',
  `status` int(1) unsigned DEFAULT NULL COMMENT '状态 1生效中  2不可用',
  `user_id` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '租户标识',
  `service_id` varchar(12) COLLATE utf8_bin NOT NULL COMMENT '用户serviceid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE ses_user_index_word
  ADD INDEX idx_user_index_word (user_id, service_id);
  
ALTER TABLE ses_user_stop_word
  ADD INDEX idx_user_stop_word (user_id, service_id);

commit;
