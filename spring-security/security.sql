/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : security

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 29/01/2021 16:54:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `u_id` bigint(20) NOT NULL COMMENT '主键',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实名称',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码（bcrypt加密）',
  `birthday` date NULL DEFAULT NULL COMMENT '生日,格式：yyyy-MM-dd',
  `sex` tinyint(4) NULL DEFAULT NULL COMMENT '性别，1：男 2：女',
  `enabled` tinyint(4) NULL DEFAULT NULL COMMENT '可用性 :true:1 false:0',
  `account_non_expired` tinyint(4) NULL DEFAULT NULL COMMENT '过期性 :true:1 false:0',
  `credentials_non_expired` tinyint(4) NULL DEFAULT NULL COMMENT '有效性 :true:1 false:0',
  `account_non_locked` tinyint(4) NULL DEFAULT NULL COMMENT '锁定性 :true:1 false:0',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`u_id`) USING BTREE,
  UNIQUE INDEX `IDX_USER_NAME`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '【系统表】用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1000001, '刘立俊', 'liulijun', '$2a$10$z58RO2CZFiKWdtovUrVs.e6pzGi42BtbACxu3tb.kNRrn1C/8zwvy', '1994-06-10', 1, 1, 1, 1, 1, '2021-01-29 16:42:49');
INSERT INTO `sys_user` VALUES (1000002, '叶均明', 'yejunming', '$2a$10$z58RO2CZFiKWdtovUrVs.e6pzGi42BtbACxu3tb.kNRrn1C/8zwvy', '1992-06-10', 1, 1, 1, 1, 1, '2021-01-29 16:42:49');

SET FOREIGN_KEY_CHECKS = 1;
