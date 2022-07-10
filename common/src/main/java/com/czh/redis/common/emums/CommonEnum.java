package com.czh.redis.common.emums;

import lombok.Getter;

/**
 * @author czh
 * @date 2020/6/10
 */
public interface CommonEnum {
    @Getter
    enum BooleanType {
        /**
         * TRUE
         **/
        TRUE(1, "TRUE"),
        /**
         * FALSE
         **/
        FALSE(0, "FALSE"),

        ;

        BooleanType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        private final Integer value;
        private final String name;
    }

    @Getter
    enum DelFlag {
        /**
         * 有效
         **/
        VALID(0, "有效"),
        /**
         * 无效
         **/
        INVALID(1, "无效"),
        ;

        DelFlag(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        private final Integer value;
        private final String name;
    }

    @Getter
    @Deprecated
    enum IsEnable {
        /**
         * 未启用
         */
        DISABLE(0, "未启用"),
        /**
         * 启用
         */
        ENABLE(1, "启用"),
        ;
        private final Integer value;
        private final String name;
        IsEnable(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    @Getter
    enum Role {
        /**
         * 管理员
         */
        ADMIN("ROLE_ADMIN"),
        /**
         * 普通用户
         */
        USER("ROLE_USER"),
        ;
        private final String value;
        Role(String value) {
            this.value = value;
        }
    }

    @Getter
    enum FileType {
        DIR(1, "文件夹"),
        FILE(2, "文件"),
        ;
        private final Integer value;
        private final String name;
        FileType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
    }
}
