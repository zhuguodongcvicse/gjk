/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : gjk

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-11-29 09:38:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gjk_batch_approval
-- ----------------------------
DROP TABLE IF EXISTS `gjk_batch_approval`;
CREATE TABLE `gjk_batch_approval` (
  `id` varchar(64) NOT NULL,
  `lib_type` varchar(64) DEFAULT NULL COMMENT '库类型',
  `id_list_json` varchar(10000) DEFAULT NULL COMMENT '审批对象id集合',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
