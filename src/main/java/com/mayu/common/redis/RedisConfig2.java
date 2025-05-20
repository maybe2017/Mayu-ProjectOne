package com.mayu.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig2 {

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer poolMaxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private Long poolMaxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer poolMaxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer poolMaxMinidle;

    @Value("${spring.redis.sentinel.master:}")
    private String master;

    @Value("${spring.redis.sentinel.nodes:}")
    private String sentinelNodes;

    @Value("${spring.redis.database}")
    private String database;

    @Value("${spring.redis.password}")
    private String redisPwd;


    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(poolMaxActive);
        config.setMaxIdle(poolMaxIdle);
        config.setMinIdle(poolMaxMinidle);
        config.setMaxWaitMillis(poolMaxWait);
        config.setTestOnBorrow(true);
        return config;
    }


//    @Bean
//    public RedisConnectionFactory getConnectionFactory(RedisSentinelConfiguration sentinelConf) {
//        JedisPoolConfig config = getRedisConfig();
//        return new JedisConnectionFactory(sentinelConf, config);
//    }

//    @Bean
//    public RedisSentinelConfiguration getRedisSentinelConfig() {
//        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
//
//        if (StringUtils.isNotBlank(master)) {
//            sentinelConfiguration.setMaster(master);
//        }
//        if (StringUtils.isNotBlank(sentinelNodes)) {
//            String[] nodes = sentinelNodes.split(",");
//
//            for (String str : nodes) {
//                String[] item = str.split(":");
//                if (item.length > 1) {
//                    sentinelConfiguration.sentinel(item[0], Integer.parseInt(item[1]));
//                }
//            }
//        }
//        if (StringUtils.isNotBlank(redisPwd)) {
//            sentinelConfiguration.setPassword(redisPwd);
//        }
//        if(StringUtils.isNotBlank(database)) {
//            sentinelConfiguration.setDatabase(Integer.valueOf(database));
//        }
//        return sentinelConfiguration;
//    }


    /**
     * 配置redis 引用
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);


        ObjectMapper mapper = new ObjectMapper();
        // 忽略序列化时存在不认识的字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 存储到redis里的数据将有类型
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 一、 Jackson2JsonRedisSerializer需要显示指定类型，比如Object.class
        // 可直接存对象，但是没有类型信；取的时候，强转不，对象信息会默认是LinkedHashMap的方式! 如何解决呢？有下面两种方式
        // 1. 存的时候用JSON字符串方式, 取时也使用JSON反序列化为对象或集合
        // 2. 借助配置ObjectMapper使其支持对象序列化到Redis、及其反序列化
        // 支持管道，但是存储的值还是变为LinkedHashMa，需先转Json，再转对象。
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        // 二、GenericJackson2JsonRedisSerializer不需要显示指定Java类型。直接存对象，从Redis获取到然后直接强转! List<T>也可以!
        // 为啥能知道类型？因为它的默认构造器创建的objectMapper配置了序列化类型信息
        // 坑: 反序列化时，泛型会丢失，比如存的Long，但是取的时候却是Integer..List<Long>也是如此。。
        // 所以还是要先用Object接收，再转成字符串再调用Long.valueOf()去间接转换为想要的Long类型。

        // 不支持管道操作的反序列化(实体类@class属性会丢失)
        // GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);


        // 三、使用StringRedisTemplate，减少存储到redis中对象的多余信息(实体类@class属性)
        // 1. 存的时候，先用mapper转为Json String
        // 2. 取的时候，取到的Json String再用mapper去转成实体类型对象。
        // 3. 把存取的方法做成工具类，其实也还蛮好的。


        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

}
