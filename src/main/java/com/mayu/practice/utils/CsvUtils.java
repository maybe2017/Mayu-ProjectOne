package com.mayu.practice.utils;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mayu.practice.po.csv.CsvColumn;
import com.mayu.practice.po.csv.RhOrderInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 马瑜
 * @Date: 2025/3/14 16:43
 * @Description: 读取csv文件
 */
@Slf4j
public class CsvUtils {

    /**
     * 解析csv(无表头)
     * Charset.forName("GBK")
     */
    public static <T> List<T> analyzeCsvFile(String csvFilePath, boolean existHeaderLine, Class<T> clazz, Charset charset) {
        Field[] fields = getClassFields(clazz, CsvColumn.class);
        Map<Integer, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(it -> it.getAnnotation(CsvColumn.class).columnIndex(), it -> it));
        List<T> dataList = new ArrayList<>();

        CsvReader reader = CsvUtil.getReader();  // 进行创建csv读取器
        // 设置文件读取的分隔符
        reader.setFieldSeparator(','); // 默认为, 可不进行设置
        // 读取csv文件，并且指定文件的编码
        CsvData data = reader.read(new File(csvFilePath), charset);
        // 获取到csv文件的每一行的数据存放到list
        List<CsvRow> rows = data.getRows();

        // 若有标题，则获取首行标题
        int index = 0;
        for (CsvRow csvRow : rows) {
            List<String> rawList = csvRow.getRawList();
            if (existHeaderLine && index++ == 0) {
                log.info("解析csv文件[{}], 跳过首行数据信息:{}", csvFilePath, JSON.toJSONString(rawList));
                continue;
            }
            dataList.add(handleRow(rawList, index, clazz, fieldMap));
        }
        return dataList;
    }

    /**
     * 递归获取有指定注解的所有字段, 包括父类(Object除外)
     */
    public static Field[] getClassFields(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        List<Field> list = Lists.newArrayList();
        while (clazz != null && !Object.class.equals(clazz)) {
            List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
            list.addAll(fields.stream().filter(it -> it.getAnnotation(annotationClazz) != null).collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        }
        return list.toArray(new Field[]{});
    }

    private static <T> T handleRow(List<String> singleLineValues, int lineIndex, Class<T> clazz, Map<Integer, Field> fieldMap) {
        try {
            T obj = clazz.newInstance();
            for (Map.Entry<Integer, Field> entry : fieldMap.entrySet()) {
                Field field = entry.getValue();
                int columnIndex = entry.getKey();
                String value = getValue(singleLineValues, columnIndex);
                // 处理数据
                field.setAccessible(Boolean.TRUE);
                field.set(obj, value);
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("【csv解析】第[{}]行数据[{}]转对象异常:{}", lineIndex, JSON.toJSONString(singleLineValues), e.getMessage(), e);
        }
        return null;
    }

    private static String getValue(List<String> singleLineValues, int columnIndex) {
        try {
            return singleLineValues.get(columnIndex);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        List<RhOrderInfo> lines = analyzeCsvFile("/Users/mayu/Desktop/北京家宽音频测试/rh/20250301/rh.csv", true, RhOrderInfo.class, Charset.forName("GBK"));
        System.out.println(lines);
    }

}
