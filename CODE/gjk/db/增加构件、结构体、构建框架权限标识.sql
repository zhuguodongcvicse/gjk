
-- 在test_table 表的 valid_status 字段之后，新增一个字段，设置对应的类型，长度，是否为null，默认值，注释

ALTER TABLE gjk_compframe ADD COLUMN `user_id` int(11) NOT NULL COMMENT '用户编号';

ALTER TABLE gjk_structlibs ADD COLUMN `user_id` int(11) NOT NULL COMMENT '用户编号';
-- 修改构件 gjk_component 的类型
ALTER TABLE gjk_component MODIFY user_id int(11) NOT NULL COMMENT '用户编号';

-- 修改构件 gjk_common_component 的类型
-- ALTER TABLE gjk_common_component MODIFY user_id int(11) NOT NULL COMMENT '用户编号';




#构件框架
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (3804, '构件框架库查看', 'libs_compframe_view', NULL, 3800, NULL, NULL, 1, '0', '1', '2019-12-31 10:42:57', '2019-12-31 10:43:32', '0');
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (3805, '构件框架库入库', 'libs_compframe_apply', NULL, 3800, NULL, NULL, 1, '0', '1', '2019-12-31 10:43:19', '2019-12-31 10:43:39', '0');
#构件
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (4104, '构件查看', 'comp_component_view', NULL, 4100, NULL, NULL, 1, '0', '1', '2019-12-31 10:10:02', '2019-12-31 10:13:15', '0');
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (4105, '构件入库申请', 'comp_component_apply', NULL, 4100, NULL, NULL, 1, '0', '1', '2019-12-31 10:10:56', '2019-12-31 10:14:49', '0');
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (4106, '构件模型导出', 'comp_component_export', NULL, 4100, NULL, NULL, 1, '0', '1', '2019-12-31 10:11:48', '2019-12-31 10:15:15', '0');
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (4107, '构件模型导入', 'comp_component_import', NULL, 4100, NULL, NULL, 1, '0', '1', '2019-12-31 10:12:17', '2019-12-31 10:15:31', '0');
#结构体
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (3504, '结构体库查看', 'libs_structlibs_view', NULL, 3500, NULL, NULL, 1, '0', '1', '2019-12-31 10:17:33', '2019-12-31 10:18:40', '0');
INSERT INTO `gjk`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `component`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`) VALUES (3505, '结构体库入库', 'libs_structlibs_apply', NULL, 3500, NULL, NULL, 1, '0', '1', '2019-12-31 10:18:17', '2019-12-31 10:18:44', '0');

