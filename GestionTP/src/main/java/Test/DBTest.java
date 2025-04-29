package Test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalTime;

public class DBTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/project_db?useSSL=false",
                    "root",
                    "45689001h"
            );
            LocalTime currentTime = LocalTime.now();
            System.out.println("Current Time: " + currentTime);
            int hour = currentTime.getHour();
            System.out.println("Hour: " + hour);

            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}