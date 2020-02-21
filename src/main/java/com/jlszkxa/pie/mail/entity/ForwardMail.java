package com.jlszkxa.pie.mail.entity;

/**
 * @ClassName Mail
 * @Description TODO
 * @Author chenwj
 * @Date 2020/2/21 13:42
 * @Version 1.0
 **/

public class ForwardMail {

    private String hostName;

    private String from;

    private String subject;

    private Object recipients;

    private Object content;

    public ForwardMail() {
    }

    private ForwardMail(Builder builder) {
        setHostName(builder.hostName);
        setFrom(builder.from);
        setSubject(builder.subject);
        setRecipients(builder.recipients);
        setContent(builder.content);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Object getRecipients() {
        return recipients;
    }

    public void setRecipients(Object recipients) {
        this.recipients = recipients;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static final class Builder {
        private String hostName;
        private String from;
        private String subject;
        private Object recipients;
        private Object content;

        private Builder() {
        }

        public Builder hostName(String val) {
            hostName = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder subject(String val) {
            subject = val;
            return this;
        }

        public Builder recipients(Object val) {
            recipients = val;
            return this;
        }

        public Builder content(Object val) {
            content = val;
            return this;
        }

        public ForwardMail build() {
            return new ForwardMail(this);
        }
    }
}
