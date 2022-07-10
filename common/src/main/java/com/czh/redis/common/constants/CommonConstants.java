package com.czh.redis.common.constants;

/**
 * @author czh
 * @date 2020/6/10
 */
public interface CommonConstants {
    class Admin{
        public static final String ADMIN_USER_DETAILS_REQ_KEY = "adminUserDetails";
    }

    class Spring {
        public static final String SPRING_BOOT_ACTIVE_DEV = "dev";

        public static final String SPRING_BOOT_ACTIVE_TEST = "test";
    }

    class Number {
        public static final Integer ADMIN_ID = 1;
    }

    class RedisKey {
        public static final String REDIS_TOKEN = "jwt_token:user_id_{0}";

        public static final String REDIS_REDIS = "redis:id_";

        public static final String REDIS_FILE_LOAD_PATH_FILE_LIST = "file_load_path:id_{0}:path_{1}";
    }

    class Jwt {
        public static final String JWT_DIGEST = "digest";
    }

    class Symbol {
        public static final String DOT = ".";

        public static final String COMMA = ",";

        public static final String LINEAR = "-";

        public static final String ASTERISK = "*";

        public static final String SLASH = "/";

        public static final String SEMICOLON = ";";
    }

    class Request {
        public static final String REQ_ADMIN_USER_DETAILS = "adminUserDetails";

        public static final String REQ_APP_USER_DETAILS = "appUserDetails";

        public static final String REQ_USER_ID  = "userId";

        public static final String REQ_DIGEST = "digest";
    }

    class DatabaseCommonField {
        public static final String PUBLIC_FIELD_IS_DELETE = "isDelete";

        public static final String PUBLIC_FIELD_CREATED_TIME = "createdTime";

        public static final String PUBLIC_FIELD_UPDATED_TIME = "updatedTime";

        public static final String PUBLIC_FIELD_CREATOR = "creator";

        public static final String PUBLIC_FIELD_UPDATER = "updater";
    }
}
