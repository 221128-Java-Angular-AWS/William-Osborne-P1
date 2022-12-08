package com.revature.persistence;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Building this first to build up from the persistence layer to the objects to be associated
// with the tables (User and Ticket)
public class ConnectionManager {
    private static Connection connection;

    // prevents java default constructor
    private ConnectionManager(){

    }

    public static Connection getConnection(){
        if(connection == null){
            connection = connect();
        }
        return connection;
    }

    private static Connection connect(){
        try {
            // Load the jdbc.properties file from the class path
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream("jdbc.properties");

            // used to parse the key value pairs in jdbc.properties
            // never include database information anywhere to be pushed to git
            Properties props = new Properties();
            props.load(input);

            // loads the driver, should only be applicable in .war
            Class.forName(props.getProperty("driver"));

            // build the database url string
            // uses hardcoded strings as well as the properties in the jdbc.properties file
            StringBuilder builder = new StringBuilder();
            builder.append("jdbc://postgresql://");
            builder.append(props.getProperty("host"));
            builder.append(":");
            builder.append(props.getProperty("port"));
            builder.append("/");
            builder.append(props.getProperty("dbname"));
            builder.append("?user=");
            builder.append(props.getProperty("username"));
            builder.append("&password");
            builder.append(props.getProperty("password"));

            connection = DriverManager.getConnection(builder.toString());


        } catch (IOException e) {
            System.out.println("Error in props.load()");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver from jdbc.properties (props.getProperty");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error getting connection from the database url string");
            e.printStackTrace();
        }
        return connection;
    }
}
