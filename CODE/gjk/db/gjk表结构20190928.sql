/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : gjk

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 28/09/2019 23:53:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for active_rule_parameters
-- ----------------------------
DROP TABLE IF EXISTS `active_rule_parameters`;
CREATE TABLE `active_rule_parameters`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active_rule_id` int(11) NOT NULL,
  `rules_parameter_id` int(11) NOT NULL,
  `value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `rules_parameter_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ix_arp_on_active_rule_id`(`active_rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 169 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for active_rules
-- ----------------------------
DROP TABLE IF EXISTS `active_rules`;
CREATE TABLE `active_rules`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profile_id` int(11) NOT NULL,
  `rule_id` int(11) NOT NULL,
  `failure_level` int(11) NOT NULL,
  `inheritance` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_profile_rule_ids`(`profile_id`, `rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1541 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alm_app_installs
-- ----------------------------
DROP TABLE IF EXISTS `alm_app_installs`;
CREATE TABLE `alm_app_installs`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `alm_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `owner_id` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `install_id` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_owner_user` tinyint(1) NOT NULL,
  `user_external_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `alm_app_installs_owner`(`alm_id`, `owner_id`(255)) USING BTREE,
  UNIQUE INDEX `alm_app_installs_install`(`alm_id`, `install_id`(255)) USING BTREE,
  INDEX `alm_app_installs_external_id`(`user_external_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for analysis_properties
-- ----------------------------
DROP TABLE IF EXISTS `analysis_properties`;
CREATE TABLE `analysis_properties`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `snapshot_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `clob_value` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `is_empty` tinyint(1) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `ix_snapshot_uuid`(`snapshot_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ce_activity
-- ----------------------------
DROP TABLE IF EXISTS `ce_activity`;
CREATE TABLE `ce_activity`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `task_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `status` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `submitter_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `submitted_at` bigint(20) NOT NULL,
  `started_at` bigint(20) NULL DEFAULT NULL,
  `executed_at` bigint(20) NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `execution_time_ms` bigint(20) NULL DEFAULT NULL,
  `analysis_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `error_message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `error_stacktrace` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `worker_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `execution_count` int(11) NOT NULL,
  `error_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `main_component_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `component_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `is_last` tinyint(1) NOT NULL,
  `is_last_key` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `main_is_last` tinyint(1) NOT NULL,
  `main_is_last_key` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ce_activity_uuid`(`uuid`) USING BTREE,
  INDEX `ce_activity_component`(`component_uuid`) USING BTREE,
  INDEX `ce_activity_main_component`(`main_component_uuid`) USING BTREE,
  INDEX `ce_activity_islast_key`(`is_last_key`) USING BTREE,
  INDEX `ce_activity_islast`(`is_last`, `status`) USING BTREE,
  INDEX `ce_activity_main_islast_key`(`main_is_last_key`) USING BTREE,
  INDEX `ce_activity_main_islast`(`main_is_last`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ce_queue
-- ----------------------------
DROP TABLE IF EXISTS `ce_queue`;
CREATE TABLE `ce_queue`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `task_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `status` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `submitter_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `started_at` bigint(20) NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `worker_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `execution_count` int(11) NOT NULL,
  `main_component_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `component_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ce_queue_uuid`(`uuid`) USING BTREE,
  INDEX `ce_queue_component`(`component_uuid`) USING BTREE,
  INDEX `ce_queue_main_component`(`main_component_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ce_scanner_context
-- ----------------------------
DROP TABLE IF EXISTS `ce_scanner_context`;
CREATE TABLE `ce_scanner_context`  (
  `task_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `context_data` longblob NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`task_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ce_task_characteristics
-- ----------------------------
DROP TABLE IF EXISTS `ce_task_characteristics`;
CREATE TABLE `ce_task_characteristics`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `task_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `text_value` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `ce_characteristics_task_uuid`(`task_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ce_task_input
-- ----------------------------
DROP TABLE IF EXISTS `ce_task_input`;
CREATE TABLE `ce_task_input`  (
  `task_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `input_data` longblob NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`task_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ce_task_message
-- ----------------------------
DROP TABLE IF EXISTS `ce_task_message`;
CREATE TABLE `ce_task_message`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `task_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `message` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `ce_task_message_task`(`task_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for default_qprofiles
-- ----------------------------
DROP TABLE IF EXISTS `default_qprofiles`;
CREATE TABLE `default_qprofiles`  (
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `qprofile_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`organization_uuid`, `language`) USING BTREE,
  UNIQUE INDEX `uniq_default_qprofiles_uuid`(`qprofile_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for deprecated_rule_keys
-- ----------------------------
DROP TABLE IF EXISTS `deprecated_rule_keys`;
CREATE TABLE `deprecated_rule_keys`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `rule_id` int(11) NOT NULL,
  `old_repository_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `old_rule_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `uniq_deprecated_rule_keys`(`old_repository_key`, `old_rule_key`) USING BTREE,
  UNIQUE INDEX `rule_id_deprecated_rule_keys`(`rule_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for duplications_index
-- ----------------------------
DROP TABLE IF EXISTS `duplications_index`;
CREATE TABLE `duplications_index`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `index_in_file` int(11) NOT NULL,
  `start_line` int(11) NOT NULL,
  `end_line` int(11) NOT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `analysis_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `duplications_index_hash`(`hash`) USING BTREE,
  INDEX `duplication_analysis_component`(`analysis_uuid`, `component_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for es_queue
-- ----------------------------
DROP TABLE IF EXISTS `es_queue`;
CREATE TABLE `es_queue`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `doc_type` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `doc_id` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `doc_id_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `doc_routing` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `es_queue_created_at`(`created_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for event_component_changes
-- ----------------------------
DROP TABLE IF EXISTS `event_component_changes`;
CREATE TABLE `event_component_changes`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `event_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `event_component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `event_analysis_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `change_category` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `component_key` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `component_name` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `component_branch_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `event_component_changes_unique`(`event_uuid`, `change_category`, `component_uuid`) USING BTREE,
  INDEX `event_cpnt_changes_cpnt`(`event_component_uuid`) USING BTREE,
  INDEX `event_cpnt_changes_analysis`(`event_analysis_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for events
-- ----------------------------
DROP TABLE IF EXISTS `events`;
CREATE TABLE `events`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `event_data` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `event_date` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `analysis_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `events_uuid`(`uuid`) USING BTREE,
  INDEX `events_analysis`(`analysis_uuid`) USING BTREE,
  INDEX `events_component_uuid`(`component_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_sources
-- ----------------------------
DROP TABLE IF EXISTS `file_sources`;
CREATE TABLE `file_sources`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `file_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `line_hashes` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `data_hash` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `src_hash` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `binary_data` longblob NULL,
  `revision` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `line_hashes_version` int(11) NULL DEFAULT NULL,
  `line_count` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `file_sources_file_uuid`(`file_uuid`) USING BTREE,
  INDEX `file_sources_project_uuid`(`project_uuid`) USING BTREE,
  INDEX `file_sources_updated_at`(`updated_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 279 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_algorithm
-- ----------------------------
DROP TABLE IF EXISTS `gjk_algorithm`;
CREATE TABLE `gjk_algorithm`  (
  `algorithm_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '算法ID',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '算法名称',
  `permission` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '算法权限标识',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父算法ID',
  `sort` int(11) NULL DEFAULT 1 COMMENT '排序值',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`algorithm_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_app
-- ----------------------------
DROP TABLE IF EXISTS `gjk_app`;
CREATE TABLE `gjk_app`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键id',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'app文件全路径',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'APP名',
  `process_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程id',
  `back_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '背景图片路径',
  `partname_platform` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '<组件名称,对应平台大类属性>的对应关系，存json字符串',
  `sysconfig_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '系统配置xml文件路径',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `app_state` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'Init' COMMENT 'app组件运行状态，Init-初始值',
  `local_deployment_plan` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '0-true，1-false',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标识，0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_app_part_platform_software
-- ----------------------------
DROP TABLE IF EXISTS `gjk_app_part_platform_software`;
CREATE TABLE `gjk_app_part_platform_software`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键id',
  `software_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '软件框架库名字',
  `software_version` double(64, 1) NULL DEFAULT NULL COMMENT '版本',
  `software_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件路径',
  `software_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '审批状态0：未审批，1：已审批',
  `procedure_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '流程ID',
  `platform_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '平台名称--多个平台之间用分号隔开',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_approval
-- ----------------------------
DROP TABLE IF EXISTS `gjk_approval`;
CREATE TABLE `gjk_approval`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户',
  `apply_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `apply_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、入库；2、出库',
  `library_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、构件库；2、硬件库',
  `apply_time` datetime(0) NULL DEFAULT NULL,
  `apply_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批人',
  `approval_state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1：已申请；2：已通过；3：已驳回',
  `approval_time` datetime(0) NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_approval_apply
-- ----------------------------
DROP TABLE IF EXISTS `gjk_approval_apply`;
CREATE TABLE `gjk_approval_apply`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `approval_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `apply_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `approval_state` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_approval_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_approval_detail`;
CREATE TABLE `gjk_approval_detail`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `approval_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_type` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、文件夹；2、c文件；3、h文件；4、doc文件;5、待扩展',
  `file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `paraent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `paraent_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_base_template
-- ----------------------------
DROP TABLE IF EXISTS `gjk_base_template`;
CREATE TABLE `gjk_base_template`  (
  `temp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板标识',
  `temp_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板名称',
  `temp_path` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板路径',
  `temp_type` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'libs_temp_type（配置字典）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '0-正常，1-删除',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`temp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_bsp
-- ----------------------------
DROP TABLE IF EXISTS `gjk_bsp`;
CREATE TABLE `gjk_bsp`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `bsp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'bsp库的名称',
  `version` double(64, 1) NULL DEFAULT NULL COMMENT '版本',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `apply_state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批状态0：未审批，1：已审批',
  `apply_desc` varbinary(255) NULL DEFAULT NULL COMMENT '审批描述',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_bsp_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_bsp_detail`;
CREATE TABLE `gjk_bsp_detail`  (
  `bsp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'bsp库id',
  `platform_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '平台库id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_bsp_file
-- ----------------------------
DROP TABLE IF EXISTS `gjk_bsp_file`;
CREATE TABLE `gjk_bsp_file`  (
  `bsp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'bsp表id',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件名称',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件路径'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_chipsfromhardwarelibs
-- ----------------------------
DROP TABLE IF EXISTS `gjk_chipsfromhardwarelibs`;
CREATE TABLE `gjk_chipsfromhardwarelibs`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id、就是硬件建模id',
  `chips` json NULL COMMENT '硬件建模的芯片数据',
  `projectId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目id',
  `flowId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程id',
  `modelId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模型id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_common_component
-- ----------------------------
DROP TABLE IF EXISTS `gjk_common_component`;
CREATE TABLE `gjk_common_component`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `comp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '构件编号',
  `comp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '构件名称',
  `comp_funcname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件函数名',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本',
  `comp_img` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件图片',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '构件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_common_component_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_common_component_detail`;
CREATE TABLE `gjk_common_component_detail`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `comp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、文件夹；2、c文件；3、h文件；4、doc文件;5、待扩展',
  `file_path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `paraent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `paraent_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `libs_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '库目录树节点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_comp_img
-- ----------------------------
DROP TABLE IF EXISTS `gjk_comp_img`;
CREATE TABLE `gjk_comp_img`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标识',
  `comp_detId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构建明细编号',
  `img_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `img_show_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '显示名字',
  `img_height` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '80' COMMENT '图片高度',
  `img_width` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '110' COMMENT '图片宽度',
  `img_backColor` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '背景颜色',
  `img_borderPx` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '边框像素',
  `img_borderBl` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '#000' COMMENT '边框粗细',
  `img_borderSo` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'solid' COMMENT '边框样式',
  `img_borderRadius` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '5' COMMENT '边框圆角',
  `img_html` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片html',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_compmanage
-- ----------------------------
DROP TABLE IF EXISTS `gjk_compmanage`;
CREATE TABLE `gjk_compmanage`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `comp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '构件id',
  `comp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件名称',
  `comp_funcname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件函数名',
  `comp_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件编号',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本号',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '提交人',
  `date_time` datetime(0) NULL DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_component
-- ----------------------------
DROP TABLE IF EXISTS `gjk_component`;
CREATE TABLE `gjk_component`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `comp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '构件编号',
  `comp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '构件名称',
  `comp_funcname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件函数名',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本',
  `comp_img` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '构件图片',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `apply_state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批状态',
  `apply_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '构件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_component_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_component_detail`;
CREATE TABLE `gjk_component_detail`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `comp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、文件夹；2、c文件；3、h文件；4、doc文件;5、待扩展',
  `file_path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `paraent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `paraent_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `libs_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '库目录树节点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_directory
-- ----------------------------
DROP TABLE IF EXISTS `gjk_directory`;
CREATE TABLE `gjk_directory`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、平台库；2、算法库；3、测试库',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `parent_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib__board_chip_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib__board_chip_detail`;
CREATE TABLE `gjk_hardwarelib__board_chip_detail`  (
  `board_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `chip_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`board_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib__board_inf_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib__board_inf_detail`;
CREATE TABLE `gjk_hardwarelib__board_inf_detail`  (
  `chip_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `inf_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`chip_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib__case_board_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib__case_board_detail`;
CREATE TABLE `gjk_hardwarelib__case_board_detail`  (
  `case_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `board_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`case_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib_board
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib_board`;
CREATE TABLE `gjk_hardwarelib_board`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `board_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `board_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `cpu_num` decimal(5, 0) NULL DEFAULT NULL,
  `hr_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `update_time` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` int(10) NULL DEFAULT NULL,
  `del_flag` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `board_type` decimal(5, 0) NULL DEFAULT NULL,
  `board_json` json NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib_case
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib_case`;
CREATE TABLE `gjk_hardwarelib_case`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `case_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `case_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `update_time` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` int(10) NULL DEFAULT NULL,
  `del_flag` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `bd_num` decimal(5, 0) NULL DEFAULT NULL,
  `link_relation` json NULL COMMENT '连线关系',
  `front_case` json NULL COMMENT '机箱前侧数据',
  `back_case` json NULL COMMENT '机箱后侧数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib_chip
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib_chip`;
CREATE TABLE `gjk_hardwarelib_chip`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `chip_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '芯片名称',
  `sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'SN',
  `chip_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '顺序号',
  `ip_confige` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'IP',
  `hr_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '硬件类型名称',
  `recv_rate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接收速率',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改时间',
  `version` int(10) NULL DEFAULT NULL COMMENT '版本',
  `del_flag` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '删除标志',
  `core_num` decimal(5, 0) NULL DEFAULT NULL COMMENT '内核数量',
  `mem_size` decimal(10, 0) NULL DEFAULT NULL COMMENT '内存大小',
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '节点ID',
  `chip_data` json NULL COMMENT '芯片数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '芯片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib_chip_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib_chip_detail`;
CREATE TABLE `gjk_hardwarelib_chip_detail`  (
  `chip_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `inf_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`chip_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelib_inf
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelib_inf`;
CREATE TABLE `gjk_hardwarelib_inf`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `inf_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接口名称',
  `sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'SN',
  `inf_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '顺序号',
  `inf_rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接口速率',
  `inf_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接口类型',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改时间',
  `project_img` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` int(10) NULL DEFAULT NULL COMMENT '版本',
  `del_flag` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '删除标志',
  `optical_num` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '光纤数量',
  `io_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'io类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_hardwarelibs
-- ----------------------------
DROP TABLE IF EXISTS `gjk_hardwarelibs`;
CREATE TABLE `gjk_hardwarelibs`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `flow_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程id',
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模型id',
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目id',
  `hardware_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '硬件建模名称',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `update_time` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `link` json NULL,
  `description` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `del_flag` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `front_json` json NULL COMMENT '正面json',
  `back_json` json NULL COMMENT '反面json',
  `link_relation` json NULL COMMENT '连线关系',
  `frontCaseForDeployment` json NULL COMMENT '部署图需要',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_platform
-- ----------------------------
DROP TABLE IF EXISTS `gjk_platform`;
CREATE TABLE `gjk_platform`  (
  `platform_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台ID',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台名称',
  `permission` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台权限标识',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父平台ID',
  `sort` int(11) NULL DEFAULT 1 COMMENT '排序值',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`platform_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_project
-- ----------------------------
DROP TABLE IF EXISTS `gjk_project`;
CREATE TABLE `gjk_project`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '项目编号',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `project_img` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目图标',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `default_software_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目默认对应软件框架',
  `default_bsp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目默认对应bsp',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_project_comp
-- ----------------------------
DROP TABLE IF EXISTS `gjk_project_comp`;
CREATE TABLE `gjk_project_comp`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `comp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '构件编号',
  `project_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '项目编号',
  `can_use` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '是否可用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_project_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_project_detail`;
CREATE TABLE `gjk_project_detail`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目编号',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_type` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、文件夹；2、c文件；3、h文件；4、doc文件;5、待扩展',
  `file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `json_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程文件加载路径',
  `software_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程所选软件框架',
  `bsp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程所选bsp',
  `newfile_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调用接口新生成xml文件',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_project_plan
-- ----------------------------
DROP TABLE IF EXISTS `gjk_project_plan`;
CREATE TABLE `gjk_project_plan`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_software
-- ----------------------------
DROP TABLE IF EXISTS `gjk_software`;
CREATE TABLE `gjk_software`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键id',
  `software_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '软件框架库名字',
  `version` double(64, 1) NULL DEFAULT NULL COMMENT '版本',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件路径',
  `apply_state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批状态0：未审批，1：已审批',
  `apply_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批描述',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标识0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_software_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_software_detail`;
CREATE TABLE `gjk_software_detail`  (
  `software_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '软件框架库版本id',
  `platform_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '平台id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_software_file
-- ----------------------------
DROP TABLE IF EXISTS `gjk_software_file`;
CREATE TABLE `gjk_software_file`  (
  `software_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件框架库id',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'git文件全路径'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_software_frame
-- ----------------------------
DROP TABLE IF EXISTS `gjk_software_frame`;
CREATE TABLE `gjk_software_frame`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `file_path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件路径',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本号',
  `apply_state` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批状态',
  `apply_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批描述',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常； 1-删除',
  `platform_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '平台库id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_structlibs
-- ----------------------------
DROP TABLE IF EXISTS `gjk_structlibs`;
CREATE TABLE `gjk_structlibs`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '父ID',
  `root_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '结构体类型根节点id',
  `children_ids` varchar(6400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '子结构体id集合',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `version` double(64, 1) NOT NULL COMMENT '版本',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型（0--基本类型 1--基本指针类型 2--结构体类型 3--结构体指针类型',
  `data_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据类型',
  `struct_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '结构体类型（0--公共结构体 1--组件参数结构体 2--其他结构体）',
  `data_length` int(11) NULL DEFAULT NULL COMMENT '数据长度',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '测试权限标识',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序值',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `storage_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_structlibs_copy
-- ----------------------------
DROP TABLE IF EXISTS `gjk_structlibs_copy`;
CREATE TABLE `gjk_structlibs_copy`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '父ID',
  `root_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '结构体类型根节点id',
  `children_ids` varchar(6400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '子结构体id集合',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `version` double(64, 1) NOT NULL COMMENT '版本',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型（0--基本类型 1--基本指针类型 2--结构体类型 3--结构体指针类型',
  `data_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据类型',
  `struct_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '结构体类型（0--公共结构体 1--组件参数结构体 2--其他结构体）',
  `data_length` int(11) NULL DEFAULT NULL COMMENT '数据长度',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '测试权限标识',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序值',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `storage_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '入库逻辑字段（0--未入库；1--已入库；2,3--其他）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_temp
-- ----------------------------
DROP TABLE IF EXISTS `gjk_temp`;
CREATE TABLE `gjk_temp`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1、流程；2、系统配置；3、部署；4、硬件实例；5、硬件库；6、方案',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `parent_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_temp_attr_detail
-- ----------------------------
DROP TABLE IF EXISTS `gjk_temp_attr_detail`;
CREATE TABLE `gjk_temp_attr_detail`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gjk_test
-- ----------------------------
DROP TABLE IF EXISTS `gjk_test`;
CREATE TABLE `gjk_test`  (
  `test_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测试ID',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测试名称',
  `permission` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测试权限标识',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父测试ID',
  `sort` int(11) NULL DEFAULT 1 COMMENT '排序值',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`test_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_roles
-- ----------------------------
DROP TABLE IF EXISTS `group_roles`;
CREATE TABLE `group_roles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NULL DEFAULT NULL,
  `resource_id` int(11) NULL DEFAULT NULL,
  `role` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_group_roles`(`organization_uuid`, `group_id`, `resource_id`, `role`) USING BTREE,
  INDEX `group_roles_resource`(`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for groups_users
-- ----------------------------
DROP TABLE IF EXISTS `groups_users`;
CREATE TABLE `groups_users`  (
  `user_id` bigint(20) NULL DEFAULT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL,
  UNIQUE INDEX `groups_users_unique`(`group_id`, `user_id`) USING BTREE,
  INDEX `index_groups_users_on_user_id`(`user_id`) USING BTREE,
  INDEX `index_groups_users_on_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for internal_properties
-- ----------------------------
DROP TABLE IF EXISTS `internal_properties`;
CREATE TABLE `internal_properties`  (
  `kee` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `is_empty` tinyint(1) NOT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `clob_value` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`kee`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for issue_changes
-- ----------------------------
DROP TABLE IF EXISTS `issue_changes`;
CREATE TABLE `issue_changes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kee` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `issue_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_login` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `change_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `change_data` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  `issue_change_creation_date` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `issue_changes_issue_key`(`issue_key`) USING BTREE,
  INDEX `issue_changes_kee`(`kee`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 481 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for issues
-- ----------------------------
DROP TABLE IF EXISTS `issues`;
CREATE TABLE `issues`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kee` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `rule_id` int(11) NULL DEFAULT NULL,
  `severity` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `manual_severity` tinyint(1) NOT NULL,
  `message` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `line` int(11) NULL DEFAULT NULL,
  `gap` decimal(30, 20) NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `resolution` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `checksum` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `reporter` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `assignee` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `author_login` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `action_plan_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `issue_attributes` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effort` int(11) NULL DEFAULT NULL,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  `issue_creation_date` bigint(20) NULL DEFAULT NULL,
  `issue_update_date` bigint(20) NULL DEFAULT NULL,
  `issue_close_date` bigint(20) NULL DEFAULT NULL,
  `tags` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `locations` longblob NULL,
  `issue_type` tinyint(2) NULL DEFAULT NULL,
  `from_hotspot` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `issues_kee`(`kee`) USING BTREE,
  INDEX `issues_assignee`(`assignee`) USING BTREE,
  INDEX `issues_component_uuid`(`component_uuid`) USING BTREE,
  INDEX `issues_creation_date`(`issue_creation_date`) USING BTREE,
  INDEX `issues_project_uuid`(`project_uuid`) USING BTREE,
  INDEX `issues_resolution`(`resolution`) USING BTREE,
  INDEX `issues_rule_id`(`rule_id`) USING BTREE,
  INDEX `issues_updated_at`(`updated_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1459 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for key_object_maxkey
-- ----------------------------
DROP TABLE IF EXISTS `key_object_maxkey`;
CREATE TABLE `key_object_maxkey`  (
  `KEY_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `MAX_VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`KEY_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for live_measures
-- ----------------------------
DROP TABLE IF EXISTS `live_measures`;
CREATE TABLE `live_measures`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `metric_id` int(11) NOT NULL,
  `value` decimal(38, 20) NULL DEFAULT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `variation` decimal(38, 20) NULL DEFAULT NULL,
  `measure_data` longblob NULL,
  `update_marker` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `live_measures_component`(`component_uuid`, `metric_id`) USING BTREE,
  INDEX `live_measures_project`(`project_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for manual_measures
-- ----------------------------
DROP TABLE IF EXISTS `manual_measures`;
CREATE TABLE `manual_measures`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `metric_id` int(11) NOT NULL,
  `value` decimal(38, 20) NULL DEFAULT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `manual_measures_component_uuid`(`component_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metrics
-- ----------------------------
DROP TABLE IF EXISTS `metrics`;
CREATE TABLE `metrics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `direction` int(11) NOT NULL DEFAULT 0,
  `domain` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `short_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `qualitative` tinyint(1) NOT NULL DEFAULT 0,
  `val_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_managed` tinyint(1) NULL DEFAULT 0,
  `enabled` tinyint(1) NULL DEFAULT 1,
  `worst_value` decimal(38, 20) NULL DEFAULT NULL,
  `best_value` decimal(38, 20) NULL DEFAULT NULL,
  `optimized_best_value` tinyint(1) NULL DEFAULT NULL,
  `hidden` tinyint(1) NULL DEFAULT NULL,
  `delete_historical_data` tinyint(1) NULL DEFAULT NULL,
  `decimal_scale` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `metrics_unique_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` longblob NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_qprofiles
-- ----------------------------
DROP TABLE IF EXISTS `org_qprofiles`;
CREATE TABLE `org_qprofiles`  (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `rules_profile_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `parent_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `last_used` bigint(20) NULL DEFAULT NULL,
  `user_updated_at` bigint(20) NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `qprofiles_org_uuid`(`organization_uuid`) USING BTREE,
  INDEX `qprofiles_rp_uuid`(`rules_profile_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_quality_gates
-- ----------------------------
DROP TABLE IF EXISTS `org_quality_gates`;
CREATE TABLE `org_quality_gates`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `quality_gate_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `uniq_org_quality_gates`(`organization_uuid`, `quality_gate_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for organization_alm_bindings
-- ----------------------------
DROP TABLE IF EXISTS `organization_alm_bindings`;
CREATE TABLE `organization_alm_bindings`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `alm_app_install_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `alm_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `url` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `members_sync_enabled` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `org_alm_bindings_org`(`organization_uuid`) USING BTREE,
  UNIQUE INDEX `org_alm_bindings_install`(`alm_app_install_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for organization_members
-- ----------------------------
DROP TABLE IF EXISTS `organization_members`;
CREATE TABLE `organization_members`  (
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`organization_uuid`, `user_id`) USING BTREE,
  INDEX `ix_org_members_on_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for organizations
-- ----------------------------
DROP TABLE IF EXISTS `organizations`;
CREATE TABLE `organizations`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `avatar_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `default_perm_template_project` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `guarded` tinyint(1) NOT NULL,
  `default_group_id` int(11) NULL DEFAULT NULL,
  `new_project_private` tinyint(1) NOT NULL,
  `default_quality_gate_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `subscription` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `default_perm_template_app` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `default_perm_template_port` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `organization_key`(`kee`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for perm_templates_groups
-- ----------------------------
DROP TABLE IF EXISTS `perm_templates_groups`;
CREATE TABLE `perm_templates_groups`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NULL DEFAULT NULL,
  `template_id` int(11) NOT NULL,
  `permission_reference` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for perm_templates_users
-- ----------------------------
DROP TABLE IF EXISTS `perm_templates_users`;
CREATE TABLE `perm_templates_users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `permission_reference` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for perm_tpl_characteristics
-- ----------------------------
DROP TABLE IF EXISTS `perm_tpl_characteristics`;
CREATE TABLE `perm_tpl_characteristics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) NOT NULL,
  `permission_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `with_project_creator` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_perm_tpl_charac`(`template_id`, `permission_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission_templates
-- ----------------------------
DROP TABLE IF EXISTS `permission_templates`;
CREATE TABLE `permission_templates`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `key_pattern` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plugins
-- ----------------------------
DROP TABLE IF EXISTS `plugins`;
CREATE TABLE `plugins`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `base_plugin_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `file_hash` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `plugins_key`(`kee`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_alm_bindings
-- ----------------------------
DROP TABLE IF EXISTS `project_alm_bindings`;
CREATE TABLE `project_alm_bindings`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `alm_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `repo_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `github_slug` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `url` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `project_alm_bindings_project`(`project_uuid`) USING BTREE,
  UNIQUE INDEX `project_alm_bindings_alm_repo`(`alm_id`, `repo_id`(255)) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_branches
-- ----------------------------
DROP TABLE IF EXISTS `project_branches`;
CREATE TABLE `project_branches`  (
  `uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `branch_type` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `merge_branch_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `key_type` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `pull_request_binary` longblob NULL,
  `manual_baseline_analysis_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `project_branches_kee_key_type`(`project_uuid`, `kee`, `key_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_links
-- ----------------------------
DROP TABLE IF EXISTS `project_links`;
CREATE TABLE `project_links`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `link_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `href` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `project_links_project`(`project_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_mappings
-- ----------------------------
DROP TABLE IF EXISTS `project_mappings`;
CREATE TABLE `project_mappings`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `key_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `key_type_kee`(`key_type`, `kee`(255)) USING BTREE,
  INDEX `project_uuid`(`project_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_measures
-- ----------------------------
DROP TABLE IF EXISTS `project_measures`;
CREATE TABLE `project_measures`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` decimal(38, 20) NULL DEFAULT NULL,
  `metric_id` int(11) NOT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `alert_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `alert_text` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `person_id` int(11) NULL DEFAULT NULL,
  `variation_value_1` decimal(38, 20) NULL DEFAULT NULL,
  `variation_value_2` decimal(38, 20) NULL DEFAULT NULL,
  `variation_value_3` decimal(38, 20) NULL DEFAULT NULL,
  `variation_value_4` decimal(38, 20) NULL DEFAULT NULL,
  `variation_value_5` decimal(38, 20) NULL DEFAULT NULL,
  `measure_data` longblob NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `analysis_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `measures_component_uuid`(`component_uuid`) USING BTREE,
  INDEX `measures_analysis_metric`(`analysis_uuid`, `metric_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 947 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_qprofiles
-- ----------------------------
DROP TABLE IF EXISTS `project_qprofiles`;
CREATE TABLE `project_qprofiles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `profile_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_project_qprofiles`(`project_uuid`, `profile_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `description` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `scope` varchar(3) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `qualifier` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `kee` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `long_name` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `path` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deprecated_kee` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `module_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `module_uuid_path` varchar(1500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `authorization_updated_at` bigint(20) NULL DEFAULT NULL,
  `root_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `copy_component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `developer_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `uuid_path` varchar(1500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `b_changed` tinyint(1) NULL DEFAULT NULL,
  `b_copy_component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_description` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_enabled` tinyint(1) NULL DEFAULT NULL,
  `b_language` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_long_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_module_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_module_uuid_path` varchar(1500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_path` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_qualifier` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `b_uuid_path` varchar(1500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `tags` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `private` tinyint(1) NOT NULL,
  `main_branch_project_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `projects_kee`(`kee`(255)) USING BTREE,
  INDEX `projects_module_uuid`(`module_uuid`) USING BTREE,
  INDEX `projects_qualifier`(`qualifier`) USING BTREE,
  INDEX `projects_root_uuid`(`root_uuid`) USING BTREE,
  INDEX `projects_uuid`(`uuid`) USING BTREE,
  INDEX `projects_organization`(`organization_uuid`) USING BTREE,
  INDEX `projects_project_uuid`(`project_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 364 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for properties
-- ----------------------------
DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_key` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `resource_id` bigint(20) NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `is_empty` tinyint(1) NOT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `clob_value` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `properties_key`(`prop_key`(255)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qprofile_changes
-- ----------------------------
DROP TABLE IF EXISTS `qprofile_changes`;
CREATE TABLE `qprofile_changes`  (
  `kee` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `rules_profile_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `change_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `change_data` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`kee`) USING BTREE,
  INDEX `qp_changes_rules_profile_uuid`(`rules_profile_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qprofile_edit_groups
-- ----------------------------
DROP TABLE IF EXISTS `qprofile_edit_groups`;
CREATE TABLE `qprofile_edit_groups`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` int(11) NOT NULL,
  `qprofile_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `qprofile_edit_groups_unique`(`group_id`, `qprofile_uuid`) USING BTREE,
  INDEX `qprofile_edit_groups_qprofile`(`qprofile_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qprofile_edit_users
-- ----------------------------
DROP TABLE IF EXISTS `qprofile_edit_users`;
CREATE TABLE `qprofile_edit_users`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_id` int(11) NOT NULL,
  `qprofile_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `qprofile_edit_users_unique`(`user_id`, `qprofile_uuid`) USING BTREE,
  INDEX `qprofile_edit_users_qprofile`(`qprofile_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for quality_gate_conditions
-- ----------------------------
DROP TABLE IF EXISTS `quality_gate_conditions`;
CREATE TABLE `quality_gate_conditions`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `qgate_id` int(11) NULL DEFAULT NULL,
  `metric_id` int(11) NULL DEFAULT NULL,
  `period` int(11) NULL DEFAULT NULL,
  `operator` varchar(3) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `value_error` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `value_warning` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for quality_gates
-- ----------------------------
DROP TABLE IF EXISTS `quality_gates`;
CREATE TABLE `quality_gates`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `is_built_in` tinyint(1) NOT NULL,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_quality_gates_uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rule_repositories
-- ----------------------------
DROP TABLE IF EXISTS `rule_repositories`;
CREATE TABLE `rule_repositories`  (
  `kee` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`kee`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rules
-- ----------------------------
DROP TABLE IF EXISTS `rules`;
CREATE TABLE `rules`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `plugin_rule_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `plugin_config_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `plugin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `priority` int(11) NULL DEFAULT NULL,
  `template_id` int(11) NULL DEFAULT NULL,
  `status` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `def_remediation_function` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `def_remediation_gap_mult` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `def_remediation_base_effort` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gap_description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_tags` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `is_template` tinyint(1) NOT NULL DEFAULT 0,
  `description_format` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  `rule_type` tinyint(2) NULL DEFAULT NULL,
  `plugin_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `scope` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `is_external` tinyint(1) NOT NULL,
  `security_standards` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `is_ad_hoc` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rules_repo_key`(`plugin_rule_key`, `plugin_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5589 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rules_metadata
-- ----------------------------
DROP TABLE IF EXISTS `rules_metadata`;
CREATE TABLE `rules_metadata`  (
  `rule_id` int(11) NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `note_data` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `note_user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `note_created_at` bigint(20) NULL DEFAULT NULL,
  `note_updated_at` bigint(20) NULL DEFAULT NULL,
  `remediation_function` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `remediation_gap_mult` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `remediation_base_effort` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tags` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `ad_hoc_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ad_hoc_description` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `ad_hoc_severity` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ad_hoc_type` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`rule_id`, `organization_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rules_parameters
-- ----------------------------
DROP TABLE IF EXISTS `rules_parameters`;
CREATE TABLE `rules_parameters`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_id` int(11) NOT NULL,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `param_type` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `default_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rules_parameters_unique`(`rule_id`, `name`) USING BTREE,
  INDEX `rules_parameters_rule_id`(`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 411 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rules_profiles
-- ----------------------------
DROP TABLE IF EXISTS `rules_profiles`;
CREATE TABLE `rules_profiles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `kee` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `rules_updated_at` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `is_built_in` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_qprof_key`(`kee`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schema_migrations
-- ----------------------------
DROP TABLE IF EXISTS `schema_migrations`;
CREATE TABLE `schema_migrations`  (
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for snapshots
-- ----------------------------
DROP TABLE IF EXISTS `snapshots`;
CREATE TABLE `snapshots`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'U',
  `islast` tinyint(1) NOT NULL DEFAULT 0,
  `version` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `purge_status` int(11) NULL DEFAULT NULL,
  `period1_mode` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period1_param` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period2_mode` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period2_param` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period3_mode` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period3_param` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period4_mode` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period4_param` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period5_mode` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `period5_param` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `build_date` bigint(20) NULL DEFAULT NULL,
  `period1_date` bigint(20) NULL DEFAULT NULL,
  `period2_date` bigint(20) NULL DEFAULT NULL,
  `period3_date` bigint(20) NULL DEFAULT NULL,
  `period4_date` bigint(20) NULL DEFAULT NULL,
  `period5_date` bigint(20) NULL DEFAULT NULL,
  `component_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `build_string` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `analyses_uuid`(`uuid`) USING BTREE,
  INDEX `snapshot_component`(`component_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `parent_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation`  (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`, `descendant`) USING BTREE,
  INDEX `idx1`(`ancestor`) USING BTREE,
  INDEX `idx2`(`descendant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '部门关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据值',
  `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `sort` int(10) NOT NULL COMMENT '排序（升序）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_dict_value`(`value`) USING BTREE,
  INDEX `sys_dict_label`(`label`) USING BTREE,
  INDEX `sys_dict_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志标题',
  `service_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remote_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作方式',
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作提交的数据',
  `time` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '执行时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标记',
  `exception` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_log_create_by`(`create_by`) USING BTREE,
  INDEX `sys_log_request_uri`(`request_uri`) USING BTREE,
  INDEX `sys_log_type`(`type`) USING BTREE,
  INDEX `sys_log_create_date`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13396 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `permission` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端URL',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `component` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'VUE页面',
  `sort` int(11) NULL DEFAULT 1 COMMENT '排序值',
  `keep_alive` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '0-开启，1- 关闭',
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details`  (
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '终端信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_idx1_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(20) NULL DEFAULT NULL COMMENT '角色ID',
  `dept_id` int(20) NULL DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与部门对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '姓名',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '随机盐',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '简介',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，9-锁定',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，1-删除',
  `wx_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信openid',
  `qq_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'QQ openid',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `user_wx_openid`(`wx_openid`) USING BTREE,
  INDEX `user_qq_openid`(`qq_openid`) USING BTREE,
  INDEX `user_idx1_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_properties
-- ----------------------------
DROP TABLE IF EXISTS `user_properties`;
CREATE TABLE `user_properties`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `kee` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `text_value` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `user_properties_user_uuid_kee`(`user_uuid`, `kee`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `resource_id` int(11) NULL DEFAULT NULL,
  `role` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_roles_resource`(`resource_id`) USING BTREE,
  INDEX `user_roles_user`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_tokens
-- ----------------------------
DROP TABLE IF EXISTS `user_tokens`;
CREATE TABLE `user_tokens`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `token_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `last_connection_date` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_tokens_token_hash`(`token_hash`) USING BTREE,
  UNIQUE INDEX `user_tokens_user_uuid_name`(`user_uuid`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `crypted_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `salt` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `active` tinyint(1) NULL DEFAULT 1,
  `created_at` bigint(20) NULL DEFAULT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  `scm_accounts` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `external_login` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `external_identity_provider` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_local` tinyint(1) NULL DEFAULT NULL,
  `is_root` tinyint(1) NOT NULL,
  `onboarded` tinyint(1) NOT NULL,
  `homepage_type` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `homepage_parameter` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `hash_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `external_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `last_connection_date` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `users_login`(`login`) USING BTREE,
  UNIQUE INDEX `users_uuid`(`uuid`) USING BTREE,
  UNIQUE INDEX `uniq_external_id`(`external_identity_provider`, `external_id`) USING BTREE,
  UNIQUE INDEX `uniq_external_login`(`external_identity_provider`, `external_login`) USING BTREE,
  INDEX `users_updated_at`(`updated_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for webhook_deliveries
-- ----------------------------
DROP TABLE IF EXISTS `webhook_deliveries`;
CREATE TABLE `webhook_deliveries`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `component_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ce_task_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `url` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `success` tinyint(1) NOT NULL,
  `http_status` int(11) NULL DEFAULT NULL,
  `duration_ms` bigint(20) NOT NULL,
  `payload` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `error_stacktrace` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `created_at` bigint(20) NOT NULL,
  `analysis_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `webhook_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `component_uuid`(`component_uuid`) USING BTREE,
  INDEX `ce_task_uuid`(`ce_task_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for webhooks
-- ----------------------------
DROP TABLE IF EXISTS `webhooks`;
CREATE TABLE `webhooks`  (
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `url` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `organization_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `project_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `organization_webhook`(`organization_uuid`) USING BTREE,
  INDEX `project_webhook`(`project_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zipkin_annotations
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_annotations`;
CREATE TABLE `zipkin_annotations`  (
  `trace_id_high` bigint(20) NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` blob NULL COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` int(11) NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` bigint(20) NULL DEFAULT NULL COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` int(11) NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` binary(16) NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` smallint(6) NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  UNIQUE INDEX `trace_id_high`(`trace_id_high`, `trace_id`, `span_id`, `a_key`, `a_timestamp`) USING BTREE COMMENT 'Ignore insert on duplicate',
  INDEX `trace_id_high_2`(`trace_id_high`, `trace_id`, `span_id`) USING BTREE COMMENT 'for joining with zipkin_spans',
  INDEX `trace_id_high_3`(`trace_id_high`, `trace_id`) USING BTREE COMMENT 'for getTraces/ByIds',
  INDEX `endpoint_service_name`(`endpoint_service_name`) USING BTREE COMMENT 'for getTraces and getServiceNames',
  INDEX `a_type`(`a_type`) USING BTREE COMMENT 'for getTraces and autocomplete values',
  INDEX `a_key`(`a_key`) USING BTREE COMMENT 'for getTraces and autocomplete values',
  INDEX `trace_id`(`trace_id`, `span_id`, `a_key`) USING BTREE COMMENT 'for dependencies job'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compressed;

-- ----------------------------
-- Table structure for zipkin_dependencies
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_dependencies`;
CREATE TABLE `zipkin_dependencies`  (
  `day` date NOT NULL,
  `parent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `child` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `call_count` bigint(20) NULL DEFAULT NULL,
  `error_count` bigint(20) NULL DEFAULT NULL,
  UNIQUE INDEX `day`(`day`, `parent`, `child`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compressed;

-- ----------------------------
-- Table structure for zipkin_spans
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_spans`;
CREATE TABLE `zipkin_spans`  (
  `trace_id_high` bigint(20) NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `debug` bit(1) NULL DEFAULT NULL,
  `start_ts` bigint(20) NULL DEFAULT NULL COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` bigint(20) NULL DEFAULT NULL COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  UNIQUE INDEX `trace_id_high`(`trace_id_high`, `trace_id`, `id`) USING BTREE COMMENT 'ignore insert on duplicate',
  INDEX `trace_id_high_2`(`trace_id_high`, `trace_id`, `id`) USING BTREE COMMENT 'for joining with zipkin_annotations',
  INDEX `trace_id_high_3`(`trace_id_high`, `trace_id`) USING BTREE COMMENT 'for getTracesByIds',
  INDEX `name`(`name`) USING BTREE COMMENT 'for getTraces and getSpanNames',
  INDEX `start_ts`(`start_ts`) USING BTREE COMMENT 'for getTraces ordering and range'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compressed;

SET FOREIGN_KEY_CHECKS = 1;
