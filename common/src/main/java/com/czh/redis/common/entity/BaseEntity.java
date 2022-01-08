package com.czh.redis.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author czh
 * @date 2020/6/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {
    @TableField(value = "id")
    private Integer id;
    /**
     * 创建人
     */
    @TableField(value = "creator")
    private Integer creator;
    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;
    /**
     * 更新人
     */
    @TableField(value = "updater")
    private Integer updater;
    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private LocalDateTime updatedTime;
    /**
     * 逻辑删除 0：未删除 1: 已删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Boolean isDelete;

    public static final String COL_ID = "id";

    public static final String COL_CREATOR = "creator";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_UPDATER = "updater";

    public static final String COL_UPDATED_TIME = "updated_time";

    public static final String COL_IS_DELETE = "is_delete";}
