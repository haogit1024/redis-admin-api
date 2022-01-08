package com.czh.redis.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czh.redis.common.emums.ResultEnum;
import com.czh.redis.common.entity.SysUser;
import com.czh.redis.common.mapper.SysUserMapper;
import com.czh.redis.common.util.JwtUtil;
import com.czh.redis.common.util.RedisUtil;
import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.common.util.Utils;
import com.czh.redis.common.view.SysUserView;
import com.czh.redis.framework.exception.BusinessException;
import com.czh.redis.framework.service.BaseCurdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author czh
 * @date 2020/6/10
 */
@Service
public class SysUserService extends BaseCurdService<SysUserMapper, SysUser, SysUserView> {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisUtil redisUtil;

    /**
     * @param id
     * @return
     */
    @Override
    public SysUserView get(Integer id) {
        return super.get(id);
    }

    /**
     * 添加用户
     * @param vo    新建用户参数
     * @return      id
     */
    @Override
    public Integer insert(SysUserView vo) {
        // TODO 验证权限
        boolean usernameUsed = this.count(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getUsername, vo.getUsername())) > 0;
        if (usernameUsed) {
            throw new BusinessException(ResultEnum.USER_HAS_EXISTED);
        }
        Integer author = SpringUtil.getJwtUserId();
        SysUser sysUser = new SysUser();
        Utils.copyPropertiesIgnoreNull(vo, sysUser);
        // 手机号, 邮箱加密
        sysUser.setCreator(author);
        sysUser.setUpdater(author);
        // 设置密码
        String salt = this.generateSalt();
        sysUser.setSalt(salt);;
        sysUser.setPassword(this.generatePassword(vo.getPassword(), salt));
        if (!save(sysUser)) {
            throw new BusinessException(ResultEnum.DATA_SAVE_ERROR);
        }
        return sysUser.getId();
    }

    @Override
    public Integer update(SysUserView vo) {
        // TODO 验证权限
        vo.setPassword(null);
        SysUser sysUser = new SysUser();
        Utils.copyPropertiesIgnoreNull(vo, sysUser);
        int ret = getBaseMapper().updateById(sysUser);
        if (ret != 1) {
            throw new BusinessException(ResultEnum.DATA_UPDATE_ERROR);
        }
        return ret;
    }

    /**
     * 登录
     * @param username  账号
     * @param password  密码
     * @return          jwt
     */
    public String login(@NotNull(message = "账号不能为空") String username,
                        @NotNull(message = "密码不能为空") String password) {
        SysUser sysUser = getByUserName(username);
        if (sysUser == null) {
            throw new BusinessException(ResultEnum.USER_NOT_EXISTED);
        }
        if (!sysUser.getPassword().equals(this.generatePassword(password, sysUser.getSalt()))) {
            throw new BusinessException(ResultEnum.USER_PASSWORD_NOT_MATCHES);
        }
        return jwtUtil.createJwt(sysUser.getId().toString(), sysUser.getUsername());
    }

    /**
     * 等出
     * @param userId    id
     * @return
     */
    public boolean logout(@NotNull Integer userId) {
        SysUser sysUser = getById(userId);
        String key = jwtUtil.getRedisKey(userId.toString());
        redisUtil.del(key);
        return true;
    }

    /**
     * 更新密码
     * @param id        id
     * @param oldPwd    旧密码
     * @param newPwd    新密码
     * @return
     */
    public boolean updatePwd(@NotNull(message = "id不能为空") Integer id,
                             @NotNull(message = "旧密码不能为空") String oldPwd,
                             @NotNull(message = "新密码不能为空") String newPwd) {
        // TODO 验证权限
        SysUser sysUser = getById(id);
        if (sysUser == null) {
            throw new BusinessException(ResultEnum.DATA_NOT_FOUND.format("用户"));
        }
        if (!sysUser.getPassword().equals(this.generatePassword(oldPwd, sysUser.getSalt()))) {
            throw new BusinessException(ResultEnum.USER_PASSWORD_NOT_MATCHES.append(", 请确认旧密码是否正确"));
        }
        sysUser.setUpdatedTime(LocalDateTime.now());
        sysUser.setUpdater(SpringUtil.getJwtUserId());
        sysUser.setPassword(this.generatePassword(newPwd, sysUser.getSalt()));
        if (!updateById(sysUser)) {
            throw new BusinessException(ResultEnum.DATA_UPDATE_ERROR);
        }
        return true;
    }

    private SysUser getByUserName(String username) {
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.eq(SysUser::getUsername, username).last("LIMIT 1");
        return getOne(query);
    }

    /**
     * 注册
     * 1. 生成用户
     * 2. 返回 aes key
     * @param username
     * @param password
     * @return
     */
    public Integer register(@NotNull(message = "用户名不能为空") String username,
                                @NotNull(message = "密码不能为空") String password,
                                String email,
                                String phone) {
        SysUserView vo = SysUserView.builder()
                .username(username)
                .password(password)
                .phone(phone)
                .email(email)
                .build();
        return this.insert(vo);
    }

    private String generateSalt() {
        return Utils.randomStr(6);
    }

    private String generatePassword(String password, String salt) {
        return Utils.md5(password + salt);
    }
}
