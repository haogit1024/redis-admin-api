package com.czh.redis.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.redis.common.emums.ResultEnum;
import com.czh.redis.common.entity.SysUser;
import com.czh.redis.common.mapper.SysUserMapper;
import com.czh.redis.common.util.JwtUtil;
import com.czh.redis.common.util.RedisUtil;
import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.common.util.Utils;
import com.czh.redis.common.view.SysUserView;
import com.czh.redis.framework.exception.BusinessException;
import com.czh.redis.framework.security.admin.AdminUserDetails;
import com.czh.redis.framework.service.BaseCurdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.czh.redis.common.constants.CommonConstants.Admin.ADMIN_USER_DETAILS_REQ_KEY;
import static com.czh.redis.common.emums.CommonEnum.Role.ADMIN;

/**
 * @author czh
 * @date 2020/6/10
 */
@Service
public class SysUserService extends BaseCurdService<SysUserMapper, SysUser, SysUserView> {
    @Autowired
    PasswordEncoder passwordEncoder;
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
        AdminUserDetails userDetails = SpringUtil.getRequestAttribute(ADMIN_USER_DETAILS_REQ_KEY, AdminUserDetails.class);
        String encodePassword = passwordEncoder.encode(vo.getPassword());
        Integer author = SpringUtil.getJwtUserId();
        SysUser sysUser = new SysUser();
        if (userDetails != null && Objects.equals(userDetails.getRoles(), ADMIN.getValue())) {
            // 管理员创建默认开启
            sysUser.setIsEnable(true);
        } else {
            // 用户注册, 需要管理员审核
            sysUser.setIsEnable(false);
        }
        Utils.copyPropertiesIgnoreNull(vo, sysUser);
        // 手机号, 邮箱加密
        sysUser.setCreator(author);
        sysUser.setUpdater(author);
        sysUser.setPassword(encodePassword);
        if (!save(sysUser)) {
            throw new BusinessException(ResultEnum.DATA_SAVE_ERROR);
        }
        return sysUser.getId();
    }

    @Override
    public Integer update(SysUserView vo) {
        AdminUserDetails userDetails = SpringUtil.getRequestAttribute(ADMIN_USER_DETAILS_REQ_KEY, AdminUserDetails.class);
        vo.setPassword(null);
        SysUser sysUser = new SysUser();
        Utils.copyPropertiesIgnoreNull(vo, sysUser);
        if (userDetails == null || !Objects.equals(userDetails.getRoles(), ADMIN.getValue())) {
            // 不是管理员
            sysUser.setIsEnable(null);
        }
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
        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            throw new BusinessException(ResultEnum.USER_LOGIN_ERROR);
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
        String key = jwtUtil.getRedisKey(userId.toString(), sysUser.getUsername());
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
        SysUser sysUser = getById(id);
        if (!passwordEncoder.matches(oldPwd, sysUser.getPassword())) {
            throw new BusinessException(ResultEnum.USER_PASSWORD_NOT_MATCHES);
        }
        String encodePassword = passwordEncoder.encode(newPwd);
        sysUser.setPassword(encodePassword);
        sysUser.setUpdater(SpringUtil.getJwtUserId());
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
    public SysUserView register(@NotNull(message = "用户名不能为空") String username,
                                @NotNull(message = "密码不能为空") String password,
                                String email,
                                String phone) {
        String encodePassword = passwordEncoder.encode(password);
        return null;
    }
}
