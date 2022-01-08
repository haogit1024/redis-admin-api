package com.czh.redis.framework.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.czh.redis.common.emums.ResultEnum;
import lombok.Data;

/**
 * 平台统一相应类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformResult {
    private Integer code;
    private Object data;
    private String message;
    private Long count;

    public void setResult(ResultEnum result) {
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    public PlatformResult append(String appendMsg) {
        this.message = this.message + "," + appendMsg;
        return this;
    }

    public static PlatformResult success(Object data) {
        PlatformResult platformResult = new PlatformResult();
        platformResult.setResult(ResultEnum.SUCCESS);
        platformResult.setData(data);
        return platformResult;
    }

    public static PlatformResult success(Page<?> page) {
        PlatformResult platformResult = new PlatformResult();
        platformResult.setResult(ResultEnum.SUCCESS);
        platformResult.setData(page.getResult());
        platformResult.setCount(page.getTotal());
        return platformResult;
    }

    /**
     * @param page
     * @return
     */
    public static PlatformResult success(PageInfo<?> page) {
        PlatformResult platformResult = new PlatformResult();
        platformResult.setResult(ResultEnum.SUCCESS);
        platformResult.setData(page.getList());
        platformResult.setCount(page.getTotal());
        return platformResult;
    }

    public static PlatformResult error(ResultEnum resultEnum) {
        PlatformResult platformResult = new PlatformResult();
        platformResult.setResult(resultEnum);
        return platformResult;
    }
}
