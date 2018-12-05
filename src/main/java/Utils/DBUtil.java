package Utils;
import java.sql.*;

import Main.Lab;
import com.sun.rowset.CachedRowSetImpl;

public class DBUtil {
private static Connection conn = null;
private static String URL = null;
private static String LOGIN = null;
private static String PASSWORD = null;
    private static Statement stmt;

    private static ResultSet resultSet;
    private static CachedRowSetImpl crs;
public static String getURL(){
    return "jdbc:mysql://" + Lab.getDbPort() +"/" + Lab.getDbName() + "?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
}
public static String getLOGIN(){
    return "root";
}
public static String getPASSWORD(){
    return "root";
}

    public static void dbConnect() {
    //throws SQLException, ClassNotFoundException



        try {
            conn = DriverManager.getConnection(DBUtil.getURL(), DBUtil.getLOGIN(), DBUtil.getPASSWORD());
System.out.print("conn set");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            //throw e;
        }
    }
    public static void dbDisconnect(){
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            System.out.println("Could not close conn");
        }
    }
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        stmt = null;
        resultSet = null;
        crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            System.out.println("Select statement: " + queryStmt + "\n");

            //Create statement
            stmt = conn.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet

        return crs;
    }

    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            //throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
}
