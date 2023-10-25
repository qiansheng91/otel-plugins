package com.aliyun.sls.otel.demo.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

import java.util.Properties;

public class ConsumerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // 设置为您在消息队列RocketMQ版控制台创建的Group ID。
        properties.put(PropertyKeyConst.GROUP_ID, "ons_plugin");
        /**
         * 如果是使用公网接入点访问，则必须设置AccessKey和SecretKey，里面填写实例的用户名和密码。实例用户名和密码在控制台实例详情页面获取。
         * 注意！！！这里填写的不是阿里云账号的AccessKey ID和AccessKey Secret，请务必区分开。
         * 如果是在阿里云ECS内网访问，则无需配置，服务端会根据内网VPC信息智能获取。
         */
        // 设置为消息队列RocketMQ版控制台实例详情页的实例用户名。
        properties.put(PropertyKeyConst.AccessKey, System.getenv("ACCESS_KEY"));
        // 设置为消息队列RocketMQ版控制台实例详情页的实例密码。
        properties.put(PropertyKeyConst.SecretKey, System.getenv("SECRET_KEY"));
        //注意！！！使用ONS SDK访问RocketMQ 5.x实例时，InstanceID属性不需要设置，否则会导致失败。

        // 设置为您从消息队列RocketMQ版控制台获取的接入点，类似“rmq-cn-XXXX.rmq.aliyuncs.com:8080”。
        // 注意！！！直接填写控制台提供的域名和端口即可，请勿添加http://或https://前缀标识，也不要用IP解析地址。
        properties.put(PropertyKeyConst.NAMESRV_ADDR, System.getenv("NAMESRV_ADDR"));
        // 集群订阅方式（默认）。
        // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        // 广播订阅方式。
        // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);

        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("ons_plugin", "*", new MessageListener() { //订阅多个Tag。
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                return Action.CommitMessage;
            }
        });

        consumer.start();
        System.out.println("Consumer Started");
    }
}            