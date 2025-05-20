//package com.mayu.practice.service;
//
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.MessageProperties;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class RabbitMQUtil {
//
//    @Value("${spring.rabbitmq.host}")
//    String host;
//
//    @Value("${spring.rabbitmq.username}")
//    String username;
//
//    @Value("${spring.rabbitmq.password}")
//    String password;
//
//    @Value("${spring.rabbitmq.virtual-host}")
//    String virtualHost;
//
//    /**
//     * @param msg        消息内容
//     * @param queue_name 队列名称
//     * @throws Exception
//     */
//    public void send(String msg, String queue_name) throws Exception {
//        //创建连接工厂
//        ConnectionFactory factory = new ConnectionFactory();
//
//        //设置rabbitmq地址
//        factory.setHost(host);
//
//        //创建一个新的连接
//        CreateConnection(factory, queue_name, msg);
//    }
//
//    /**
//     * @param msg        消息内容
//     * @param queue_name 队列名称
//     * @param host       rabbitmq地址
//     * @throws Exception
//     */
//    public void send(String msg, String queue_name, String host) throws Exception {
//        //创建连接工厂
//        ConnectionFactory factory = new ConnectionFactory();
//
//        //设置rabbitmq地址
//        factory.setHost(host);
//
//        //创建一个新的连接
//        CreateConnection(factory, queue_name, msg);
//    }
//
//    /**
//     * @param msg        消息内容
//     * @param queue_name 队列名称
//     * @param host       rabbitmq地址
//     * @param port       rabbitmq端口
//     * @throws Exception
//     */
//    public void send(String msg, String queue_name, String host, int port) throws Exception {
//        //创建连接工厂
//        ConnectionFactory factory = new ConnectionFactory();
//
//        //设置rabbitmq地址
//        factory.setHost(host);
//
//        //设置rabbitmq端口号
//        factory.setPort(port);
//
//        //创建一个新的连接
//        CreateConnection(factory, queue_name, msg);
//    }
//
//
//    /**
//     * 创建一个新的连接
//     *
//     * @param factory
//     * @param queue_name
//     * @param msg
//     * @throws Exception
//     */
//    public void CreateConnection(ConnectionFactory factory, String queue_name, String msg) throws Exception {
//        factory.setUsername(username);
//        factory.setPassword(password);
//        factory.setVirtualHost(virtualHost);
//
//        //创建一个新的连接
//        Connection connection = factory.newConnection();
//
//        //创建一个频道
//        Channel channel = connection.createChannel();
//
//        //为Channel定义queue的属性
//        channel.queueDeclare(queue_name, true, false, false, null);
//
//        //发送消息到队列中
//        channel.basicPublish("", queue_name, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
//
//        //关闭频道和连接
//        channel.close();
//        connection.close();
//    }
//
//}
