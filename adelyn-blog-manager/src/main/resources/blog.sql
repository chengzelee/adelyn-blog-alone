-- auth_account: table
CREATE TABLE `auth_account` (
                                `id` bigint NOT NULL,
                                `user_name` varchar(255) NOT NULL,
                                `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                `status` int NOT NULL,
                                `create_time` datetime DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- blog_content: table
CREATE TABLE `blog_content` (
                                `id` bigint NOT NULL,
                                `blog_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
                                `create_time` datetime DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- blog_info: table
CREATE TABLE `blog_info` (
                             `id` bigint NOT NULL,
                             `blog_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                             `user_id` bigint NOT NULL,
                             `blog_visible` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                             `create_time` datetime DEFAULT NULL,
                             `update_time` datetime DEFAULT NULL,
                             PRIMARY KEY (`id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- blog_pic_mapping: table
CREATE TABLE `blog_pic_mapping` (
                                    `id` bigint NOT NULL,
                                    `blog_id` bigint NOT NULL,
                                    `pic_id` bigint NOT NULL,
                                    `create_time` datetime DEFAULT NULL,
                                    `update_time` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`,`blog_id`,`pic_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- blog_tag_info: table
CREATE TABLE `blog_tag_info` (
                                 `id` bigint NOT NULL,
                                 `tag_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                 `user_id` bigint DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- blog_tag_mapping: table
CREATE TABLE `blog_tag_mapping` (
                                    `id` bigint NOT NULL,
                                    `blog_id` bigint NOT NULL,
                                    `tag_id` bigint NOT NULL,
                                    `create_time` datetime DEFAULT NULL,
                                    `update_time` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`,`blog_id`,`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- resource_info: table
CREATE TABLE `resource_info` (
                                 `id` bigint NOT NULL,
                                 `resource_name` varchar(255) DEFAULT NULL,
                                 `absolute_path` varchar(255) DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

