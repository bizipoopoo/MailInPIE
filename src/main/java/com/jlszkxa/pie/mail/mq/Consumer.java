package com.jlszkxa.pie.mail.mq;

import com.alibaba.fastjson.JSONObject;
import com.jlszkxa.pie.mail.entity.ForwardMail;
import com.jlszkxa.pie.mail.mail.MailSender;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Consumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-group");

        consumer.setNamesrvAddr("localhost:9876");
        consumer.setInstanceName("rmq-instance");
        consumer.subscribe("demo-topic", "demo-tag");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    ForwardMail forwardMail = JSONObject.parseObject(new String(msg.getBody()), ForwardMail.class);
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(forwardMail.getRecipients()));
                    String recipient = jsonObject.getString("user") + "@" + jsonObject.getString("host");
                    try {
                        System.out.printf("成功代理转发%s的邮件\r\n", recipient);
                        MailSender.sendHtml(forwardMail.getFrom(), "979831398@qq.com", "xxxxx", "smtp.qq.com", recipient, "转发自代理服务器由" + forwardMail.getFrom().split("@")[0] + "发出的邮件：" + forwardMail.getSubject(), forwardMail.getContent());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("Consumer Started.");
    }
}