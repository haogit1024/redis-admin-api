package com.czh.redis.admin.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisValueView {
    private String key;
    private Long ttl;
    private Object value;
    private String type;
    private Long len;
}

