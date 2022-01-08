package com.czh.redis.common.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author czh
 * @date 2020/6/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseView implements Serializable {
    private Integer id;
    /**
     * 创建人
     */
    private Integer creator;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 更新人
     */
    private Integer updater;
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
    /**
     * 逻辑删除 0：未删除 1: 已删除
     */
    private Boolean isDelete;

    /**
     * 选中的id
     */
    private List<Integer> selectedIds;

    /**
     * 页码
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer pageNum;

    /**
     * 每页最大数量
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer pageSize;

    /**
     * 开始时间
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime endTime;

    public static final String FIELD_PAGE_NUM = "page_num";

    public static final String FIELD_PAGE_SIZE = "page_size";

    public static final String FIELD_START_TIME = "start_time";

    public static final String FIELD_END_TIME = "end_time";
}
