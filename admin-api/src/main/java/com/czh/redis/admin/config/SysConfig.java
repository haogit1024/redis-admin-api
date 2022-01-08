package com.czh.redis.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 公钥加密数据, 私钥解密数据
 * @author czh
 * @date 2020/6/20
 */
@Component
@Data
@ConfigurationProperties(prefix = "sys-config")
public class SysConfig {
    private String privateKey;
    private String publicKey;
}
