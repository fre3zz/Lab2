package Classes;

import Utils.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorDAO {
    private static ObservableList<Doctor> docList;
    public static Doctor searchDoctor (String docId) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM doctors WHERE doc_id="+docId;

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsDoc = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            Doctor doctor = getDoctorFromResultSet(rsDoc);

            //Return employee object
            return doctor;
        } catch (SQLException e) {
            System.out.println("While searching an employee with " + docId + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }
    private static Doctor getDoctorFromResultSet(ResultSet rs) throws SQLException
    {
        Doctor doctor = null;
        if (rs.next()) {
            doctor = new Doctor();
            doctor.setId(Integer.toString(rs.getInt("DOC_ID")));
            doctor.setNameSurname(rs.getString("NAME"));
            doctor.setDepartment(rs.getString("DEPARTMENT"));
            doctor.setPhoneNumber(rs.getString("PHONE"));

        }
        return doctor;
    }

    public static ObservableList<Doctor> searchDoctors() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM doctors";

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeList method and get employee object
            if(docList!=null)docList.clear();
            docList = getDoctorList(rsEmps);

            //Return employee object
            return docList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            //throw e;
        }
        return null;
    }
    public static ObservableList<Doctor> getDoctorList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<Doctor> docList = FXCollections.observableArrayList();

        while (rs.next()) {
            Doctor doctor = new Doctor();
            doctor.setId(Integer.toString(rs.getInt("DOC_ID")));
            doctor.setNameSurname(rs.getString("NAME"));
            doctor.setDepartment(rs.getString("DEPARTMENT"));
            doctor.setPhoneNumber(rs.getString("PHONE"));
            //Add employee to the ObservableList
            docList.add(doctor);
        }
        //return empList (ObservableList of Employees)
        return docList;
    }
    public static void insertDoc (String name, String department, String phoneNumber) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt= null;
        if(phoneNumber == null) {
           updateStmt = "INSERT INTO doctors (NAME, DEPARTMENT) values ('" + name + "', '" + department + "')";
        }
        else{
            updateStmt = "INSERT INTO doctors (NAME, DEPARTMENT, PHONE) values ('" + name + "', '" + department + "', '" + phoneNumber + "')" ;
        }
        //Execute DELETE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            //throw e;
        }
    }
}
