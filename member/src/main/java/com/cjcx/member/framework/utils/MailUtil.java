package com.cjcx.member.framework.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 阿里云邮件发送
 */
public class MailUtil {
	private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
	private static final int ALIDM_SMTP_PORT = 25;// 或80

	// 发件人的账号 和 密码
	private String user;
	private String password;

	public MailUtil() {
		this("report@mt.yunpiaobox.cn", "TranslogicSMTP01");
	}
	
	public MailUtil(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	public static void main(String[] args) {
		String title = "标题1";
		String body = loadHtml("C:\\Users\\guo\\workspace\\idea\\cjcx\\learn\\report\\zomble.html");
		System.out.println(body);
		
        new MailUtil().send("1161588342@qq.com;46857430@qq.com", title, body);
        //new MailUtil().send("1161588342@qq.com", "测试1", "nihao显示");
        //new MailUtil().send("1161588342@qq.com", "测试1", "市劳动纠纷联赛积分了","C:/Users/guo/Desktop/Proguard.xml");
	}
	
	
	public static String loadHtml(String path){
		StringBuffer buff = new StringBuffer();
		File file = new File(path);
		BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                line++;
                
                buff.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return buff.toString();
	}
	
	/**
	 * 发送邮件
	 * @param toEmail  收件人邮箱地址 ; 分割多个收件人
	 * @param subject 邮件标题
	 * @param content  邮件内容 可以是html内容
	 */
	public void send(String toEmail, String subject, String content) {
		String [] emails = toEmail.split(";");
		Session session = loadMailSession();
		// 创建邮件消息
		MimeMessage message = new MimeMessage(session);
		for (int i = 0; i < emails.length; i++) {
			// session.setDebug(true);
			try {
				// 设置发件人
				message.setFrom(new InternetAddress(user));
				Address[] a = new Address[1];
				a[0] = new InternetAddress(user);
				message.setReplyTo(a);
				// 设置收件人
				InternetAddress to = new InternetAddress(emails[i]);
				message.setRecipient(MimeMessage.RecipientType.TO, to);
				// 设置邮件标题
				message.setSubject(subject);
				// 设置邮件的内容体
				message.setContent(content, "text/html;charset=UTF-8");
				// 发送邮件
				Transport.send(message);
			} catch (MessagingException e) {
				String err = e.getMessage();
				// 在这里处理message内容， 格式是固定的
				System.out.println(err);
			}
		}
	}

	
	/**
	 * 发送邮件 带附件
	 * @param toEmail  收件人邮箱地址
	 * @param subject  邮件标题
	 * @param content  邮件内容 可以是html内容
	 * @param attachPath 附件路径
	 */
	public void send(String toEmail, String subject, String content, String attachPath) {
		String [] emails = toEmail.split(";");
		Session session = loadMailSession();
		MimeMessage mm = new MimeMessage(session);
		try {
			for (int i = 0; i < emails.length; i++) {
				//发件人
				mm.setFrom(new InternetAddress(user));
				//收件人
				mm.setRecipient(Message.RecipientType.TO, new InternetAddress(emails[i])); // 设置收件人
				// mm.setRecipient(Message.RecipientType.CC, new
				// InternetAddress("XXXX@qq.com")); //设置抄送人
				//标题
				mm.setSubject(subject);
				//内容
				Multipart multipart = new MimeMultipart();
				//body部分
				BodyPart contentPart = new MimeBodyPart();
				contentPart.setContent(content, "text/html;charset=utf-8");
				multipart.addBodyPart(contentPart);
				
				//附件部分
				BodyPart attachPart = new MimeBodyPart();
				FileDataSource fileDataSource = new FileDataSource(attachPath);
				attachPart.setDataHandler(new DataHandler(fileDataSource));
				attachPart.setFileName(MimeUtility.encodeText(fileDataSource.getName()));
				multipart.addBodyPart(attachPart);
				
				mm.setContent(multipart);
				Transport.send(mm);
			}
		} catch (Exception e) {
			String err = e.getMessage();
			// 在这里处理message内容， 格式是固定的
			System.out.println(err);
		}
		
	}

	private Session loadMailSession() {
		try {
			// 配置发送邮件的环境属性
			final Properties props = new Properties();
			// 表示SMTP发送邮件，需要进行身份验证
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", ALIDM_SMTP_HOST);
			props.put("mail.smtp.port", ALIDM_SMTP_PORT);
			// 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
			// props.put("mail.smtp.socketFactory.class",
			// "javax.net.ssl.SSLSocketFactory");
			// props.put("mail.smtp.socketFactory.port", "465");
			// props.put("mail.smtp.port", "465");
			// 发件人的账号
			props.put("mail.user", user);
			// 访问SMTP服务时需要提供的密码
			props.put("mail.password", password);
			// 构建授权信息，用于进行SMTP进行身份验证
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
					String userName = props.getProperty("mail.user");
					String password = props.getProperty("mail.password");
					return new PasswordAuthentication(userName, password);
				}
			};
			// 使用环境属性和授权信息，创建邮件会话
			return Session.getInstance(props, authenticator);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("mail session is null");
		}
		return null;
	}

}
