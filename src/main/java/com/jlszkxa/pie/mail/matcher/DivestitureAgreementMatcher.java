package com.jlszkxa.pie.mail.matcher;

import com.jlszkxa.pie.mail.db.DbOperation;
import org.apache.mailet.GenericRecipientMatcher;
import org.apache.mailet.MailAddress;
import java.sql.SQLException;

/**
 * @ClassName DivestitureAgreementMatcher
 * @Description 判断接收人是否是内网用户，是则匹配给Mailet进行协议剥离，否则放行
 * @Author chenwj
 * @Date 2020/2/20 14:53
 * @Version 1.0
 **/
public class DivestitureAgreementMatcher extends GenericRecipientMatcher {

    @Override
    public boolean matchRecipient(MailAddress mailAddress) {

        DbOperation dbOperation = new DbOperation();

        try {
            dbOperation.connectDatabase();
            String userName = mailAddress.getUser();
            String host = mailAddress.getHost();
            System.out.printf("截取到到发送给%s@%s的邮件\r\n", userName, host);
            return !dbOperation.isInnerUser(userName + "@" + host);
        } catch (Exception e) {
            System.out.printf("发生异常 异常信息： %s\r\n", e.getMessage());
            e.printStackTrace();
            return true;
        } finally {
            try {
                dbOperation.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
