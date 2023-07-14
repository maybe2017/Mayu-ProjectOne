package com.mayu.practice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
public class RedisConfig {

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer poolMaxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private Long poolMaxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer poolMaxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer poolMaxMinidle;

    @Value("${spring.redis.sentinel.master}")
    private String master;

    @Value("${spring.redis.sentinel.nodes}")
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


    @Bean
    public RedisConnectionFactory getConnectionFactory(RedisSentinelConfiguration sentinelConf) {
        JedisPoolConfig config = getRedisConfig();
        return new JedisConnectionFactory(sentinelConf, config);
    }

    @Bean
    public RedisSentinelConfiguration getRedisSentinelConfig() {
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();

        if (StringUtils.isNotBlank(master)) {
            sentinelConfiguration.setMaster(master);
        }
        if (StringUtils.isNotBlank(sentinelNodes)) {
            String[] nodes = sentinelNodes.split(",");

            for (String str : nodes) {
                String[] item = str.split(":");
                if (item.length > 1) {
                    sentinelConfiguration.sentinel(item[0], Integer.parseInt(item[1]));
                }
            }
        }
        if (StringUtils.isNotBlank(redisPwd)) {
            sentinelConfiguration.setPassword(redisPwd);
        }
        if(StringUtils.isNotBlank(database)) {
            sentinelConfiguration.setDatabase(Integer.valueOf(database));
        }
        return sentinelConfiguration;
    }


    /**
     * 配置redis 引用
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());


        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);

        return template;
    }

}
