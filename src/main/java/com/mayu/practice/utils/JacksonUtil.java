package com.mayu.practice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * 1. 注意对泛型对象json串进行 反序列化时，要用TypeReference带具体的泛型类型进行操作才行
 * 2. 忽略某个字段的序列化与反序列化 字段上加注解@JsonIgnore
 * 3. 映射不同名称的字段 @JsonProperty，或者用@JsonSetter或@JsonSetter分别对序列化、反序列化进行字段映射设置
 * 4. 对象的更新，更新的对象里面的属性值会覆盖旧对象里面的属性值（如果不为null），否则依然用旧对象中的属性值。mapper.updateValue()
 */
@Slf4j
public class JacksonUtil {
    private JacksonUtil() {
    }

    // 是线程安全的
    private static ObjectMapper mapper;
    /**
     * 序列化级别，默认只序列化属性值发生过改变的字段
     */
    private static JsonInclude.Include DEFAULT_PROPERTY_INCLUSION = JsonInclude.Include.NON_NULL;

    /**
     * 是否缩进JSON格式
     */
    private static boolean IS_ENABLE_INDENT_OUTPUT = false;

    /**
     * CSV默认分隔符
     */
    private static String CSV_DEFAULT_COLUMN_SEPARATOR = ",";

    static {
        try {
            //初始化
            initMapper();
            //配置序列化级别
            configPropertyInclusion();
            //配置JSON缩进支持
            configIndentOutput();
            //配置普通属性
            configCommon();
        } catch (Exception e) {
            log.error("jackson config error", e);
        }
    }

    private static void initMapper() {
        mapper = new ObjectMapper();
    }

    private static void configCommon() {
        config(mapper);
    }

    private static void configPropertyInclusion() {
        mapper.setSerializationInclusion(DEFAULT_PROPERTY_INCLUSION);
    }

    private static void configIndentOutput() {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, IS_ENABLE_INDENT_OUTPUT);
    }

    private static void config(ObjectMapper objectMapper) {
        //序列化BigDecimal时之间输出原始数字还是科学计数, 默认false, 即是否以toPlainString()科学计数方式来输出
        objectMapper.disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        //允许将JSON空字符串强制转换为null对象值
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        //允许单个数值当做数组处理
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //禁止重复键, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        //禁止使用int代表Enum的order()來反序列化Enum, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //有属性不能映射的时候不报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //使用null表示集合类型字段是时不抛异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        //对象为空时不抛异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //允许在JSON中使用c/c++风格注释
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        //强制转义非ascii字符
        objectMapper.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        //允许未知字段
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        //在JSON中允许未引用的字段名
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        //时间格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 这里只能对Date生效，而且不建议放在全局配置，因为SimpleDateFormat不是线程安全的
        // 最好在类中字段上面单独加DateFormatter进行配置
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        //识别单引号
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        //识别Java8时间
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());

        // 手动配置
        // 使序列化后的时间字符串格式不再是"2023-09-16T18:51:48.425"
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)));
        // spi机制去发现注册的
        objectMapper.registerModule(javaTimeModule);
    }

    /**
     * 设置序列化级别
     * NON_NULL：序列化非空的字段
     * NON_EMPTY：序列化非空字符串和非空的字段
     * NON_DEFAULT：序列化属性值发生过改变的字段
     */
    public static void setSerializationInclusion(JsonInclude.Include inclusion) {
        JacksonUtil.DEFAULT_PROPERTY_INCLUSION = inclusion;
        configPropertyInclusion();
    }

    /**
     * 设置是否开启JSON格式美化
     *
     * @param isEnable 为true表示开启, 默认false, 有些场合为了便于排版阅读则需要对输出做缩放排列
     */
    public static void setIndentOutput(boolean isEnable) {
        JacksonUtil.IS_ENABLE_INDENT_OUTPUT = isEnable;
        configIndentOutput();
    }


    /**
     * JSON反序列化
     */
    public static <V> V from(URL url, Class<V> c) {
        try {
            return mapper.readValue(url, c);
        } catch (IOException e) {
            //log.error("jackson from error, url: {}, type: {}", url.getPath(), c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(InputStream inputStream, Class<V> c) {
        try {
            return mapper.readValue(inputStream, c);
        } catch (IOException e) {
            //log.error("jackson from error, type: {}", c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(File file, Class<V> c) {
        try {
            return mapper.readValue(file, c);
        } catch (IOException e) {
            log.error("jackson from error, file path: {}, type: {}", file.getPath(), c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(Object jsonObj, Class<V> c) {
        try {
            return mapper.readValue(jsonObj.toString(), c);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", jsonObj.toString(), c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, Class<V> c) {
        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", json, c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(URL url, TypeReference<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            log.error("jackson from error, url: {}, type: {}", url.getPath(), type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(InputStream inputStream, TypeReference<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            //log.error("jackson from error, type: {}", type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(File file, TypeReference<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            //log.error("jackson from error, file path: {}, type: {}", file.getPath(), type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(Object jsonObj, TypeReference<V> type) {
        try {
            return mapper.readValue(jsonObj.toString(), type);
        } catch (IOException e) {
            //log.error("jackson from error, json: {}, type: {}", jsonObj.toString(), type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, TypeReference<V> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            //log.error("jackson from error, json: {}, type: {}", json, type, e);
            return null;
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("jackson to error, obj: {}", list, e);
            return null;
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            log.error("jackson to error, obj: {}", v, e);
            throw new RuntimeException();
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> void toFile(String path, List<V> list) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            mapper.writer().writeValues(writer).writeAll(list);
            writer.flush();
        } catch (Exception e) {
            log.error("jackson to file error, path: {}, list: {}", path, list, e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> void toFile(String path, V v) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            mapper.writer().writeValues(writer).write(v);
            writer.flush();
        } catch (Exception e) {
            log.error("jackson to file error, path: {}, obj: {}", path, v, e);
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return String
     */
    public static String getString(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get string error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return int
     */
    public static Integer getInt(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).intValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get int error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return long
     */
    public static Long getLong(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).longValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get long error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static Double getDouble(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).doubleValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get double error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static BigInteger getBigInteger(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return new BigInteger(String.valueOf(0.00));
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).bigIntegerValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get biginteger error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static BigDecimal getBigDecimal(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).decimalValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get bigdecimal error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static boolean getBoolean(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return false;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).booleanValue();
            } else {
                return false;
            }
        } catch (IOException e) {
            //log.error("jackson get boolean error, json: {}, pullWaybillKey: {}", json, key, e);
            return false;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static byte[] getByte(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).binaryValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get byte error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static <T> ArrayList<T> getList(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        String string = getString(json, key);
        return from(string, new TypeReference<ArrayList<T>>() {
        });
    }

    /**
     * 向json中添加属性
     *
     * @return json
     */
    public static <T> String add(String json, String key, T value) {
        try {
            JsonNode node = mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            // log.error("jackson add error, json: {}, pullWaybillKey: {}, value: {}", json, key, value, e);
            return json;
        }
    }

    /**
     * 向json中添加属性
     */
    private static <T> void add(JsonNode jsonNode, String key, T value) {
        if (value instanceof String) {
            ((ObjectNode) jsonNode).put(key, (String) value);
        } else if (value instanceof Short) {
            ((ObjectNode) jsonNode).put(key, (Short) value);
        } else if (value instanceof Integer) {
            ((ObjectNode) jsonNode).put(key, (Integer) value);
        } else if (value instanceof Long) {
            ((ObjectNode) jsonNode).put(key, (Long) value);
        } else if (value instanceof Float) {
            ((ObjectNode) jsonNode).put(key, (Float) value);
        } else if (value instanceof Double) {
            ((ObjectNode) jsonNode).put(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode) jsonNode).put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode) jsonNode).put(key, (BigInteger) value);
        } else if (value instanceof Boolean) {
            ((ObjectNode) jsonNode).put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            ((ObjectNode) jsonNode).put(key, (byte[]) value);
        } else {
            ((ObjectNode) jsonNode).put(key, to(value));
        }
    }

    /**
     * 除去json中的某个属性
     *
     * @return json
     */
    public static String remove(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            return node.toString();
        } catch (IOException e) {
            //log.error("jackson remove error, json: {}, pullWaybillKey: {}", json, key, e);
            return json;
        }
    }

    /**
     * 修改json中的属性
     */
    public static <T> String update(String json, String key, T value) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            //log.error("jackson update error, json: {}, pullWaybillKey: {}, value: {}", json, key, value, e);
            return json;
        }
    }

    /**
     * 格式化Json(美化)
     *
     * @return json
     */
    public static String format(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            //log.error("jackson format json error, json: {}", json, e);
            return json;
        }
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            //log.error("jackson check json error, json: {}", json, e);
            return false;
        }
    }

    private static InputStream getResourceStream(String name) {
        return JacksonUtil.class.getClassLoader().getResourceAsStream(name);
    }

    private static InputStreamReader getResourceReader(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

}
