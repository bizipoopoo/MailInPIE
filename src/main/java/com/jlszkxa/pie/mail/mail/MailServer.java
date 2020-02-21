package com.jlszkxa.pie.mail.mail;

import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * 简单邮件发送器，可单发，群发。
 * 
 * @author humingfeng
 * 
 */
public class MailServer {
 
	/**
	 * 发送邮件的props文件
	 */
	private final transient Properties props = System.getProperties();
	/**
	 * 邮件服务器登录验证
	 */
	private transient MailAuthenticator authenticator;
 
	/**
	 * 邮箱session
	 */
	private transient Session session;
 
	/**
	 * 初始化邮件发送器
	 * 
	 * @param smtpHostName
	 *            SMTP邮件服务器地址
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            发送邮件的密码
	 */
	public MailServer(final String smtpHostName, final String username,
			final String password) {
		init(username, password, smtpHostName);
	}
 
	/**
	 * 初始化邮件发送器
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)，并以此解析SMTP服务器地址
	 * @param password
	 *            发送邮件的密码
	 */
	public MailServer(final String username, final String password) {
		// 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);
 
	}
 
	/**
	 * 初始化
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            密码
	 * @param smtpHostName
	 *            SMTP主机地址
	 */
	private void init(String username, String password, String smtpHostName) {
		// 初始化props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		if(smtpHostName==null)props.put("mail.smtp.host", smtpHostName);
		// 验证
		authenticator = new MailAuthenticator(username, password);
		// 创建session
		session = Session.getInstance(props, authenticator);
	}
 
	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, Object content, String fromname)
			throws AddressException, MessagingException, UnsupportedEncodingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		if(StringUtils.isBlank(fromname)) {
			message.setFrom(new InternetAddress(authenticator.username, fromname));
		} else {
			message.setFrom(new InternetAddress(authenticator.username));
		}
		// 设置收件人
		if(recipient!=null&&recipient.indexOf(";")!=-1){
			//多收件人
			String[] rec = recipient.split(";");
			int len = rec.length;
			InternetAddress[] iad = new InternetAddress[len];
			for(int i=0; i<len; i++){
				iad[i] =  new InternetAddress(rec[i]);
			}
			message.setRecipients(MimeMessage.RecipientType.TO, iad);
		}else{
			//单收件人
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
		}
		// 设置主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
//		message.setText(content.toString(), "GBK");
		// 发送
		Transport.send(message);
	}
 
	/**
	 * 群发邮件
	 * 
	 * @param recipients
	 *            收件人们
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, String subject, Object content, String fromname)
			throws AddressException, MessagingException, UnsupportedEncodingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		if(StringUtils.isBlank(fromname)) {
			message.setFrom(new InternetAddress(authenticator.username, fromname));
		} else {
			message.setFrom(new InternetAddress(authenticator.username));
		}
		// 设置收件人们
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(MimeMessage.RecipientType.TO, addresses);
		// 设置主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}
 
	/**
	 * 服务器邮箱登录验证
	 * 
	 * @author MZULE
	 * 
	 */
	public class MailAuthenticator extends Authenticator {
 
		/**
		 * 用户名（登录邮箱）
		 */
		private String username;
		/**
		 * 密码
		 */
		private String password;
 
		/**
		 * 初始化邮箱和密码
		 * 
		 * @param username
		 *            邮箱
		 * @param password
		 *            密码
		 */
		public MailAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}
 
		String getPassword() {
			return password;
		}
 
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
 
		String getUsername() {
			return username;
		}
 
		public void setPassword(String password) {
			this.password = password;
		}
 
		public void setUsername(String username) {
			this.username = username;
		}
 
	}
}