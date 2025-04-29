package com.example.gestiontp;

import java.sql.Connection;
import java.sql.DriverManager;


public class Database {

    public static Connection connectDB()
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect= DriverManager.getConnection("jdbc:mysql://localhost:3306/project_db","root","45689001h");

            if (connect != null) {
                System.out.println("Connected to the database successfully!"); // Debugging line
            }
            return connect;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}