package com.aliyun.sls.otel.demo.ons;

import com.aliyun.openservices.ons.api.*;

import java.util.Date;
import java.util.Properties;

public class ProducerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
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

        // 设置发送超时时间，单位：毫秒。
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置为您从消息队列RocketMQ版控制台获取的接入点，类似“rmq-cn-XXXX.rmq.aliyuncs.com:8080”。
        // 注意！！！直接填写控制台提供的域名和端口即可，请勿添加http://或https://前缀标识，也不要用IP解析地址。
        properties.put(PropertyKeyConst.NAMESRV_ADDR, System.getenv("NAMESRV_ADDR"));
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();

        // 循环发送消息。
        for (int i = 0; i < 5; i++) {
            Message msg = new Message(
                    // 设置为您在消息队列RocketMQ版控制台上创建的Topic。
                    // 普通消息所属的Topic，切勿使用普通消息的Topic来收发其他类型的消息。
                    "ons-plugin",
                    // Message Tag可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在消息队列RocketMQ版的服务器过滤。
                    // Tag的具体格式和设置方法，请参见消息过滤。
                    "TagA",
                    // Message Body可以是任何二进制形式的数据，消息队列RocketMQ版不做任何干预。
                    // 需要Producer与Consumer协商好一致的序列化和反序列化方式。
                    "Hello MQ".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过消息队列RocketMQ版控制台查询消息并补发。
            // 注意：不设置也不会影响消息正常收发。
            msg.setKey("ORDERID_" + i);

            try {
                SendResult sendResult = producer.send(msg);
                // 同步发送消息，只要不抛异常就是成功。
                if (sendResult != null) {
                    System.out.println(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId());
                }
            } catch (Exception e) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
                System.out.println(new Date() + " Send mq message failed. Topic is:" + msg.getTopic());
                e.printStackTrace();
            }
        }

        // 在应用退出前，销毁Producer对象。
        // 注意：销毁Producer对象可以节约系统内存，若您需要频繁发送消息，则无需销毁Producer对象。
        producer.shutdown();
    }
}