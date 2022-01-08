package com.czh.redis.framework.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.redis.common.constants.CommonConstants;
import com.czh.redis.common.converter.LocalDateTimeExcelConverter;
import com.czh.redis.common.emums.CommonEnum;
import com.czh.redis.common.entity.BaseEntity;
import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.common.util.Utils;
import com.czh.redis.common.view.BaseView;
import com.czh.redis.common.view.PageView;
import com.czh.redis.framework.exception.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.czh.redis.common.emums.ResultEnum.*;

/**
 * @author czh
 * @date 2020/6/10
 */
@Slf4j
public class BaseCurdService<M extends BaseMapper<E>, E extends BaseEntity, V extends BaseView> extends BaseService<M, E> {
    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    Class<E> entityClass = (Class<E>) pt.getActualTypeArguments()[1];
    Class<V> viewClass = (Class<V>) pt.getActualTypeArguments()[2];

    /**
     * 获取单个数据
     * @param id
     * @return
     */
    public V get(Integer id) {
        E e = getById(id);
        if (e == null) {
            throw new BusinessException(DATA_NOT_FOUND);
        }
        V vo = getViewNewInstance();
        Utils.copyPropertiesIgnoreNull(e, vo);
        return vo;
    }

    /**
     * 列表分页查询 可以返回  PageInfo<E> 和 PageInfo<V> 默认返回 PageInfo<E>
     * @param map
     * @return
     */
    public PageView<?> list(Map<String, Object> map) {
        V vo = getViewFormMap(map);
        if (vo == null) {
            vo = getViewNewInstance();
        }
        return list(vo);
    }

    /**
     * 列表分页查询 可以返回  PageInfo<E> 和 PageInfo<V> 默认返回 PageInfo<E>
     * @param vo
     * @return
     */
    public PageView<?> list(V vo) {
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        int pageSize = vo.getPageSize() == null ? 10 : vo.getPageSize();
        Page<E> page = PageHelper.startPage(pageNum, pageSize);
        list(listQueryWrapper(vo));
        return Utils.getPageView(page);
    }

    public void export(Map<String, Object> map) throws IOException {
        export(getViewFormMap(map));
    }

    /**
     * 导出 excel 方法, 可以根据需要重写 搜索表达式和转换器方法
     * @param vo
     */
    public void export(V vo) throws IOException {
        QueryWrapper<E> query = exportQueryWrapper(vo);
        List<E> data = list(query);
        List<?> excelData = converterToExcelData(data);
        if (CollectionUtils.isEmpty(excelData)) {
            throw new BusinessException(EXPORT_DATA_NONE);
        }
        HttpServletResponse response = SpringUtil.getResponse();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8");
        String fileName = URLEncoder.encode(getExcelFileName(vo), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), excelData.get(0).getClass())
                .registerConverter(new LocalDateTimeExcelConverter()).sheet("数据").doWrite(excelData);
    }

    protected V getViewFormMap(Map<String, Object> map) {
        // 类似2,3的数据转成数组
        map.forEach((k, v) -> {
            if (v.toString().contains(CommonConstants.Symbol.COMMA) && k.contains("List")) {
                map.put(k, v.toString().split(CommonConstants.Symbol.COMMA));
            }
        });
        V vo = Utils.mapToObject(map, viewClass);
        if (vo == null) {
            vo = getViewNewInstance();
        }
        return vo;
    }

    protected QueryWrapper<E> baseQueryWrapper(V vo) {
        return baseQueryWrapper(vo, false);
    }

    protected QueryWrapper<E> baseQueryWrapper(V vo, boolean isAsc) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = Utils.beanToMap(vo, true);
        map.remove(V.FIELD_PAGE_NUM);
        map.remove(V.FIELD_PAGE_SIZE);
        map.remove(V.FIELD_START_TIME);
        map.remove(V.FIELD_END_TIME);
        queryWrapper.allEq(map, false);
        queryWrapper.orderBy(true, isAsc, E.COL_ID);
        queryWrapper.ge(vo.getStartTime() != null, E.COL_CREATED_TIME, vo.getStartTime());
        queryWrapper.le(vo.getEndTime() != null, E.COL_CREATED_TIME, vo.getEndTime());
        return queryWrapper;
    }

    /**
     * 列表查询表达式
     * @param vo
     * @return
     */
    protected QueryWrapper<E> listQueryWrapper(V vo) {
        return baseQueryWrapper(vo);
    }

    /**
     * 导出搜索表达式
     * @param vo
     * @return
     */
    protected QueryWrapper<E> exportQueryWrapper(V vo) {
        return baseQueryWrapper(vo);
    }

    /**
     * 转换成 easy excel 需要的类
     * @see <a href="https://www.yuque.com/easyexcel/doc/write#713a5706">easy excel</a>
     * @param data  需要转换的数据
     * @return
     */
    protected List<?> converterToExcelData(List<E> data) {
        List<V> ret = new ArrayList<>(data.size());
        for (E e : data) {
            V v = getViewNewInstance();
            Utils.copyPropertiesIgnoreNull(e, v);
            ret.add(v);
        }
        return ret;
    }

    protected String getExcelFileName(V vo) {
        return "数据";
    }

    public Integer insert(String data) {
        return insert(Utils.json2Object(data, viewClass));
    }

    /**
     * 添加数据
     * @param vo
     * @return
     */
    public Integer insert(V vo) {
        if (vo == null) {
            throw new BusinessException(PARAM_ERROR.format("提交数据有误"));
        }
        E e = this.getEntityNewInstance();
        BeanUtils.copyProperties(vo, e);
        e.setCreator(SpringUtil.getJwtUserId());
        e.setUpdater(SpringUtil.getJwtUserId());
        if (!save(e)) {
            throw new BusinessException(DATA_SAVE_ERROR.format(""));
        }
        return e.getId();
    }

    public Integer update(String data) {
        return update(Utils.json2Object(data, viewClass));
    }

    public Integer update(V vo) {
        if (vo == null || vo.getId() == null) {
            throw new BusinessException(PARAM_ERROR.format("提交数据有误或id为空"));
        }
        E e = this.getEntityNewInstance();
        BeanUtils.copyProperties(vo, e);
        e.setUpdatedTime(LocalDateTime.now());
        e.setUpdater(SpringUtil.getJwtUserId());
        int updateRet = getBaseMapper().updateById(e);
        if (updateRet != 1) {
            throw new BusinessException(DATA_UPDATE_ERROR.format(""));
        }
        return updateRet;
    }

    public Integer updateStatus(@NotNull Integer id, @NotNull Integer status) {
        UpdateWrapper<E> wrapper = new UpdateWrapper<>();
        wrapper.eq(E.COL_ID, id).set("status", status);
        return getBaseMapper().update(null, wrapper);
    }

    public Integer delete(Integer id) {
        UpdateWrapper<E> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(E.COL_IS_DELETE, CommonEnum.DelFlag.INVALID.getValue());
        updateWrapper.set(E.COL_UPDATER, SpringUtil.getJwtUserId());
        updateWrapper.eq(E.COL_ID, id);
        if (getBaseMapper().update(null, updateWrapper) != 1) {
            throw new BusinessException(DATA_UPDATE_ERROR.format(""));
        }
        return 1;
    }

    public Integer deletes(Map<String, Object> map) {
        V vo = getViewFormMap(map);
        if (vo == null) {
            vo = getViewNewInstance();
        }
        return deletes(vo);
    }

    public Integer deletes(V vo) {
        UpdateWrapper<E> update = new UpdateWrapper<>();
        Map<String, Object> map = Utils.beanToMap(vo, true);
        update.allEq(map);
        return getBaseMapper().update(null, update);
    }

    private E getEntityNewInstance() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private V getViewNewInstance() {
        try {
            return viewClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
