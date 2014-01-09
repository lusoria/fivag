package fivagest.util;

import java.sql.*;
 
public class DataBaseHelper {

    private static final String NOME_DB = "fivag2";
    private static final String USER = "bruno";
    private static final String PASS = "bruno";
    private static final String HOST = "jdbc:mysql://192.168.101.1:3306/";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    /*
    private static Connection connection;
    private static Statement state;
    private static ResultSet result;
    */
    
 
    
    public static Connection getConnection(){
    	Connection conn = null;
    	try {
			Class.forName(DRIVER).newInstance();
			conn = DriverManager.getConnection(HOST+NOME_DB,USER,PASS);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			System.err.println("Errore durante la connessione al Database:");
			e.printStackTrace();
			System.exit(1);
		}
    	return conn;
    }
   
    
    public static void manageError(SQLException erroreSQL, String query) {

    	System.err.println("Query: "+query);
		DataBaseHelper.manageError(erroreSQL);
    }
    
    
    public static void manageError(SQLException erroreSQL) {

    	System.err.println("Errore nell'interrogazione del Database");
		System.err.println("SQLState: " + erroreSQL.getSQLState());
        System.err.println("Error Code: " + erroreSQL.getErrorCode());
        System.err.println("Message: " + erroreSQL.getMessage());
        System.exit(1);
    }
    
}