package com.jlszkxa.pie.mail.mail;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

/**
 * @author 
 *
 */
public class MailSender {
    /**
     * 服务邮箱
     */
    private static MailServer mailServer = null;
 
	//
	private static String userName;
 
	
	private static String password;
 
	
	private static String stmp;
 
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		if(MailSender.userName==null)
			MailSender.userName = userName;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		if(MailSender.password==null)
			MailSender.password = password;
	}
	/**
	 * @param stmp the stmp to set
	 */
	public void setStmp(String stmp) {
		if(MailSender.stmp==null)
			MailSender.stmp = stmp;
	}
    /**
     * 使用默认的用户名和密码发送邮件
     * @param recipient
     * @param subject
     * @param content
     * @throws MessagingException 
     * @throws AddressException 
     */
    public static void sendHtml(String recipient, String subject, Object content, String fromname) throws AddressException, MessagingException, UnsupportedEncodingException {
    	if (mailServer == null) 
        	mailServer = new MailServer(stmp,userName,password);
    	mailServer.send(recipient, subject, content, fromname);
    }
    /**
     * 使用指定的用户名和密码发送邮件
     * @param server
     * @param password
     * @param recipient
     * @param subject
     * @param content
     * @throws MessagingException 
     * @throws AddressException 
     */
    public static void sendHtml(String fromname, String server,String password,String stmpIp, String recipient, String subject, Object content) throws AddressException, MessagingException, UnsupportedEncodingException {
    	 new MailServer(stmpIp,server,password).send(recipient, subject, content, fromname);
    }
    public static void main(String[] args) {
    	try {
    		String s = "这是一封来自公司内网的测试邮件，收到请勿回复！";
			sendHtml(null, "test@jlszkxa.com","test","localhost", "979831398@qq.com", "测试邮件", s);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}