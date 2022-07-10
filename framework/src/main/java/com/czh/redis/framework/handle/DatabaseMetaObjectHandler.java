package com.czh.redis.framework.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.czh.redis.common.util.JwtUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class DatabaseMetaObjectHandler implements MetaObjectHandler {

    /**
     * insert 时调用
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

    }

    /**
     * update 时调用
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
