package com.mayu.qw.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;

@Slf4j
@Repository
public class BaseRepository {

    /**
     * 获取单个对象所有键值
     */
    protected Update getKeyAndValue(Object param) {
        // 更新项目集合
        Update update = new Update();
        // 得到类中的所有属性集合
        Field[] declaredFields = param.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            // 设置些属性是可以访问的
            field.setAccessible(true);
            try {
                // 设置键值
                update.set(field.getName(), field.get(param));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("反射获取类字段失败。field:{},错误信息：{}", field.getName(), e.getMessage(), e);
            }
        }
        return update;
    }
}
