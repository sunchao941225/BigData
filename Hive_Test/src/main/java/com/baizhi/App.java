package com.baizhi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {

    public static void main(String[] args) throws Exception {

        /*
         *获取连接
         * */
        Class.forName("org.apache.hive.jdbc.HiveDriver");

        Connection connection = DriverManager.getConnection("jdbc:hive2://HadoopNode00:10000/baizhi", "root", null);


        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select name from t_user");

        while (resultSet.next()) {

            String name = resultSet.getString("name");

            System.out.println(name);

        }

        resultSet.close();
        statement.close();
        connection.close();


    }
}
