/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : gjk

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2019-11-28 15:18:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gjk_app_part_platform_bsp
-- ----------------------------
DROP TABLE IF EXISTS `gjk_app_part_platform_bsp`;
CREATE TABLE `gjk_app_part_platform_bsp` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键id',
  `bsp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'BSP库名字',
  `bsp_version` double(64,1) DEFAULT NULL COMMENT '版本',
  `bsp_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文件路径',
  `bsp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'BSP库ID',
  `procedure_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '流程ID',
  `platform_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台名称--多个平台之间用分号隔开',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
SET FOREIGN_KEY_CHECKS=1;
