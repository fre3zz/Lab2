package Classes;

import Utils.DBUtil;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorDAO {
    private static ObservableList<Doctor> docList;
    static{
        docList = FXCollections.observableArrayList();
    }
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
    public static void reloadList(){
        docList.clear();
        try {
            searchDoctors();
        }
        catch (Exception e){
            System.out.println(e);
        }
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
            //docList = getDoctorList(rsEmps);
            loadDoctorList(rsEmps);

            return docList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            //throw e;
        }
        return null;
    }
    public static void loadDoctorList(ResultSet rs) throws SQLException{
        while (rs.next()) {
            Doctor doctor = new Doctor();
            doctor.setId(Integer.toString(rs.getInt("DOC_ID")));
            doctor.setNameSurname(rs.getString("NAME"));
            doctor.setDepartment(rs.getString("DEPARTMENT"));
            doctor.setPhoneNumber(rs.getString("PHONE"));
            //Add employee to the ObservableList
            docList.add(doctor);
            System.out.println("doc added");
        }
    }
    public static ObservableList<Doctor> getDoctorList() {
        //Declare a observable List which comprises of Employee objects
        //ObservableList<Doctor> docList = FXCollections.observableArrayList();


        //return empList (ObservableList of Employees)
        return docList;
    }
    public static void insertDoc (String name, String department, String phoneNumber) throws SQLException, ClassNotFoundException {

        String updateStmt = null;
        if(phoneNumber == null) {
           updateStmt = "INSERT INTO doctors (NAME, DEPARTMENT) values ('" + name + "', '" + department + "')";
        }
        else{
            updateStmt = "INSERT INTO doctors (NAME, DEPARTMENT, PHONE) values ('" + name + "', '" + department + "', '" + phoneNumber + "')" ;
        }

        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);

        }
    }
    public static void updateDocName(String newName, String id) throws SQLException, ClassNotFoundException{
        String updateStmt = "Update doctors set NAME = '" + newName +"'where DOC_ID = " + Integer.parseInt(id);
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);

        }
    }
    public static void updateDocDepartment(String newDept, String id) throws SQLException, ClassNotFoundException{
        String updateStmt = "Update doctors set DEPARTMENT = '" + newDept +"'where DOC_ID = " + Integer.parseInt(id);
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);

        }
    }
    public static void updateDocPhoneN(String newPhoneN, String id) throws SQLException, ClassNotFoundException{
        String updateStmt = "Update doctors set PHONE = '" + newPhoneN +"'where DOC_ID = " + Integer.parseInt(id);
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);

        }
    }
}
