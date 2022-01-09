package com.czh.redis.common.view;

import com.czh.redis.common.entity.Redis;
import com.czh.redis.common.util.RSAUtil;
import com.czh.redis.common.util.Utils;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class RedisView extends BaseView {
    public RedisView(Redis redis, String privateKey) {
        Utils.copyPropertiesIgnoreNull(redis, this);
        if (StringUtils.isNotBlank(redis.getHost())) {
            this.setHost(RSAUtil.decryptByPriKey(redis.getHost(), privateKey));
        }
        if (StringUtils.isNotBlank(redis.getPort())) {
            this.setPort(RSAUtil.decryptByPriKey(redis.getPort(), privateKey));
        }
        if (StringUtils.isNotBlank(redis.getPassword())) {
            this.setPassword(RSAUtil.decryptByPriKey(redis.getPassword(), privateKey));
        }
    }

    /**
     * host
     */
    @NotBlank
    private String host;

    /**
     * 端口
     */
    @NotBlank
    private String port;

    /**
     * 密码
     */
    private String password;

    /**
     * 最后连接时间
     */
    private LocalDateTime lastConnectionTime;

    /**
     * 最后登陆id
     */
    private String lastConnectionIp;

    /**
     * 名称
     */
    private String name;

    /**
     * 数据库数
     */
    private Integer databases;

    /**
     * 数据库大小
     */
    private List<Integer> databasesSize;
}
