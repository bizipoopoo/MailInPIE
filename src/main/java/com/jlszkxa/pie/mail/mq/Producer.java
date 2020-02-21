package com.jlszkxa.pie.mail.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.Scanner;

public class Producer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("my-group");
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("rmq-instance");
        producer.start();
        try {
            Message message = new Message("demo-topic", "demo-tag", "这是一条测试消息".getBytes());
            producer.send(message);

            while (true) {
                String text = new Scanner(System.in).next();
                Message msg = new Message("demo-topic",// topic
                        "demo-tag",// tag
                        text.getBytes() // body
                );
                SendResult sendResult = producer.send(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }
    }
}