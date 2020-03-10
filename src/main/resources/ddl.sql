CREATE TABLE `recommendedsystem`.`user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `create_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `telphone` varchar(40) NOT NULL DEFAULT '' COMMENT '电话',
  `password` varchar(200) NOT NULL DEFAULT '' COMMENT '密码',
  `nick_name` varchar(40) NOT NULL DEFAULT '' COMMENT '昵称',
  `gender` int(0) NOT NULL DEFAULT 0 COMMENT '性别',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `telphone_unique_index`(`telphone`) USING BTREE COMMENT 'telphone 的唯一索引'
);

CREATE TABLE `recommendedsystem`.`seller`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `name` varchar(80) NOT NULL DEFAULT '' COMMENT '商户名称',
  `created_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '注册时间',
  `updated_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `remark_score` decimal(2, 1) NOT NULL DEFAULT 0 COMMENT '评分',
  `disabled_flag` int(0) NOT NULL DEFAULT 0 COMMENT '启用和停用标记',
  PRIMARY KEY (`id`)
);

CREATE TABLE `recommendedsystem`.`category`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `created_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `updated_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '品类名称',
  `icon_url` varchar(200) NOT NULL DEFAULT '' COMMENT '品类图片url',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '品类排序',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_unique_index`(`name`) USING BTREE COMMENT '品类名称唯一索引'
);

CREATE TABLE `recommendedsystem`.`shop`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `created_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `updated_at` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `name` varchar(80) NOT NULL DEFAULT '' COMMENT '门店名称',
  `remark_score` decimal(2, 1) NOT NULL DEFAULT 0 COMMENT '评分',
  `price_per_man` int(0) NOT NULL DEFAULT 0 COMMENT '人均价格',
  `latitude` decimal(10, 6) NOT NULL DEFAULT 0 COMMENT '维度',
  `longitude` decimal(10, 6) NOT NULL DEFAULT 0 COMMENT '经度',
  `category_id` int(0) NOT NULL DEFAULT 0 COMMENT '品类 id',
  `tags` varchar(2000) NOT NULL DEFAULT '' COMMENT '标签',
  `start_time` varchar(200) NOT NULL DEFAULT '' COMMENT '门店开始运营时间',
  `end_time` varchar(200) NOT NULL DEFAULT '' COMMENT '门店结束运营时间',
  `address` varchar(200) NOT NULL DEFAULT '' COMMENT '门店地址',
  `seller_id` int(0) NOT NULL DEFAULT 0 COMMENT 'seller id',
  `icon_url` varchar(100) NOT NULL DEFAULT '' COMMENT '门店图片',
  PRIMARY KEY (`id`)
);