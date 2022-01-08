package com.czh.redis.common.view;

import lombok.Data;

import java.util.List;

/**
 * @author chenzh
 * @date 2021/2/1
 */
@Data
public class PageView<T> {
    private Long total;
    private List<T> list;
}
