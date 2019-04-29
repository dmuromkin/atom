package ru.atom.chat;

import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.sql.*;
import java.util.ArrayList;



public  class PostSQLBase {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/ChatBase";
    static final String USER = "postgres";
    static final String PASS = "Araghorn47";


    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_URL,USER,PASS);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }


    public void chat_say(String user, String message){
       Connection dbConnection = getDBConnection() ;
       if (dbConnection != null) {
           System.out.println("You successfully connected to database now");
       } else {
           System.out.println("Failed to make connection to database");
       }
       PreparedStatement statement= null;
       try {
           statement = dbConnection.prepareStatement("INSERT INTO chathistory (username, message) VALUES (?, ?)");
           statement.setString(1,user);
           statement.setString(2,message);
           statement.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
        if(statement==null)
       System.out.println("Statement is not created");

   }


   public ArrayList getdata()
   {
       Connection dbConnection = getDBConnection() ;
       ArrayList<String> history = new ArrayList<String>();

       if (dbConnection != null) {
           System.out.println("You successfully connected to database");
       } else {
           System.out.println("Failed to make connection to database");
       }
       Statement statement= null;
       try {
           statement = dbConnection.createStatement();
           ResultSet result1 = statement.executeQuery("SELECT * FROM chathistory ");
           //result это указатель на первую строку с выборки
           //чтобы вывести данные мы будем использовать
           //метод next() , с помощью которого переходим к следующему элементу
           while (result1.next()) {
               history.add(result1.getString("username")+": "+result1.getString("message"));
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       if(statement==null)
           System.out.println("Statement is not created");
       return history;
   }
}
