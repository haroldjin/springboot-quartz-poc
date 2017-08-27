CREATE DATABASE IF NOT EXISTS scheduler;
use scheduler;

DROP TABLE task_schedule_job;
CREATE TABLE `task_schedule_job` (
  `job_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` TIMESTAMP NOT NULL,
  `update_time` TIMESTAMP NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `job_status` varchar(200) NOT NULL,
  `cron_expression` varchar(200) NOT NULL,
  `description` varchar(200) NOT NULL,
  `bean_class` varchar(200) NOT NULL,
  `is_concurrent` varchar(200) NOT NULL,
  `spring_id` varchar(200) NOT NULL,
  `method_name` varchar(200) NOT NULL,
  PRIMARY KEY (`job_id`),
  FULLTEXT KEY `job_name` (`job_name`, `job_group`, `method_name`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
