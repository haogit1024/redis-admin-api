package com.czh.redis.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author czh
 * @date 2020/7/21
 */
public class LocalDateTimeExcelConverter implements Converter<LocalDateTime> {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用
     *
     * @param cellData            excel数据 (NotNull)
     * @param contentProperty     excel属性 (Nullable)
     * @param globalConfiguration 全局配置 (NotNull)
     * @return 读取到内存中的数据
     */
    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
//        LocalDateTimeFormat annotation = contentProperty.getField().getAnnotation(LocalDateTimeFormat.class);
        return LocalDateTime.parse(cellData.getStringValue(),
                DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }


    /**
     * 写的时候会调用
     *
     * @param value               java value (NotNull)
     * @param contentProperty     excel属性 (Nullable)
     * @param globalConfiguration 全局配置 (NotNull)
     * @return 写出到excel文件的数据
     */
    @Override
    public CellData convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
//        LocalDateTimeFormat annotation = contentProperty.getField().getAnnotation(LocalDateTimeFormat.class);
        return new CellData(value.format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN)));
    }
}

