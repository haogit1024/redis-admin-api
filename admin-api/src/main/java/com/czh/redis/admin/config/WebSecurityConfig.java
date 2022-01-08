package com.czh.redis.admin.config;

import com.czh.redis.framework.security.admin.AdminFilter;
import com.czh.redis.framework.security.admin.AdminUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author czh
 * @date 2020/6/17
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public AdminUserDetailsService adminUserDetailsService() {
        return new AdminUserDetailsService();
    }

    @Bean
    public AdminFilter adminFilter() {
        return new AdminFilter();
    }

    /**
     * 设置拦截路径和权限
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // csrf
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // cors
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .hasRole("USER")
                ;

        http.addFilterAfter(adminFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 设置忽略的路径
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/sysUser/login")
                .antMatchers(HttpMethod.OPTIONS, "/**")
                ;
    }
}
