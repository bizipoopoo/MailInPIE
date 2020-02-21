package com.jlszkxa.pie.mail.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @ClassName DbOperation
 * @Description 数据库操作
 * @Author chenwj
 * @Date 2020/2/20 17:34
 * @Version 1.0
 **/

public class DbOperation {

    private static final Logger logger = LoggerFactory.getLogger(DbOperation.class);

    private Connection connection;

    /*
   连接数据库
    */
    public void connectDatabase() {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/mail?characterEncoding=UTF-8";
        String userName = "root";
        String password = "123456";
        logger.info("开始连接数据库");
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
            logger.info("数据库连接成功");
        } catch (Exception e) {
            logger.warn("数据库连接出现异常 异常信息： {}", e.getMessage());
        }
    }

    /**
     * 判断该用户是否为内网用户
     *
     * @param userName
     * @return
     * @throws Exception
     */
    public boolean isInnerUser(String userName) throws Exception {
        String sql = "select USER_NAME from james_user where USER_NAME = \'" + userName + "\';";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        try {
            if (rs.next()) {
                return true;
            }
            return false;
        } finally {
            rs.close();
            pstmt.close();
        }
    }

    /*
    关闭连接
     */
    public void closeConnection() throws SQLException {
        if (null != connection) {
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception {
        DbOperation test = new DbOperation();
        test.connectDatabase();
        boolean innerUser = test.isInnerUser("97983398@qq.com");
        System.out.printf("结果为: %s\r\n", "true");
        test.closeConnection();
    }
}
