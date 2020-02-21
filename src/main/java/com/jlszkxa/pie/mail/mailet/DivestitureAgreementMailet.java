package com.jlszkxa.pie.mail.mailet;


import com.alibaba.fastjson.JSONObject;
import com.jlszkxa.pie.mail.entity.ForwardMail;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import javax.mail.Address;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author chenwj
 * @version 1.0
 * @className DivestitureAgreementMailet
 * @description 截取邮件，剥离协议，并放入队列等待网闸摆渡
 * @date 2020/2/20 14:53
 **/

public class DivestitureAgreementMailet extends GenericMailet {
    @Override
    public void service(Mail mail) throws MessagingException {
        String sender = mail.getSender().toString();
        String name = mail.getName();
        String subject = mail.getMessage().getSubject();
        String content = null;
        try {
            content = (String) mail.getMessage().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("截取到%s的邮件 name:%s subject:%s content:%s\r\n", sender, name, subject, content);
        System.out.println("将截取邮件发送到消息队列中...");
        DefaultMQProducer producer = new DefaultMQProducer("DivestitureAgreementGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("rmq-instance");
        try {
            producer.start();
            System.out.println("开启消息队列");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        try {
            ForwardMail forwardMail = ForwardMail.newBuilder()
                    .content(mail.getMessage().getContent())
                    .from(mail.getSender().getUser() + "@" + mail.getSender().getHost())
                    .hostName(mail.getRemoteHost())
                    .recipients(mail.getRecipients().iterator().next())
                    .subject(mail.getMessage().getSubject())
                    .build();
            Message message = new Message("demo-topic", "demo-tag", JSONObject.toJSONString(forwardMail).getBytes("UTF-8"));
            producer.send(message);
            System.out.println("消息成功转发到消息队列中");
            System.out.printf("转发内容如下： %s\r\n", JSONObject.toJSONString(forwardMail));
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            producer.shutdown();
            System.out.println("关闭消息队列");
        }
    }
}
