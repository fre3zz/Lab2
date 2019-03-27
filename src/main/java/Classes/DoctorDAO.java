package Classes;

import Utils.DBUtil;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;


/*
Структура базы данных:
doctor_id - int
doctor_name - varchar
doctor_phone - varchar
department_id - int

Класс содержит список всех объектов типа Doctor, загруженных из таблицы.
Необходимые операции - SELECT, UPDATE, INSERT
 */
public class DoctorDAO {

    private static final String DOCTOR_ID = "doctor_id";
    private static final String DOCTOR_NAME = "doctor_name";
    private static final String DOCTOR_PHONE = "doctor_phone";
    private static final String DEPARTMENT_ID = "department_id";
    private static final String TABLE_NAME = "lab.doctor";

    private static ObservableList<Doctor> doctorList = FXCollections.observableArrayList();

    public static ObservableList<Doctor> getDoctorListWithLoad() {
        try {
            loadList();
        }
        catch (Exception e){
            System.out.println(e);
        }
        return doctorList;
    }

    public static ObservableList<Doctor> getDoctorList(){
        return doctorList;
    }

    //Метод для заполнения doctorList всеми записями из таблицы
    //Отделение заполняется на основании присоединенной с помощью INNER JOIN таблицы отделений
    //Возвращает true, если список заполнился
    //Возвращает false, если что-то пошло не так

    public static boolean loadList() throws SQLException, ClassNotFoundException {
        String selectStatement = "SELECT * FROM " + TABLE_NAME + " INNER JOIN lab.department ON doctor.department_id = department.department_id";
        ResultSet rs = null;
        try{
            rs = DBUtil.dbExecuteQuery(selectStatement);
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        if(doctorList!=null){
            doctorList.removeAll();
            doctorList.clear();
        }
        if(rs == null) return false;
        while(rs.next()){
            Doctor doctor = new Doctor(rs.getString(DOCTOR_NAME));
            doctor.setPhoneNumber(rs.getString(DOCTOR_PHONE));
            doctor.setDoctorId(rs.getInt(DOCTOR_ID));
            Department dept = new Department();
            dept.setDepartmentId(rs.getInt(DEPARTMENT_ID));
            dept.setDepartmantName(rs.getString("department_name"));
            doctor.setDepartment(dept);
            doctorList.add(doctor);
        }
        return true;
    }

    /*
    Метод для добавления нового доктора в список.
    Возвращает doctor_id добавленного доктора, или id доктора с таким же именем в списке.
    Если что-то пошло не так, то возвращается -1.
    department_id получается, выполнив метод @DepartmentDAO.addDepartment(department.name) с указанным именем отделения
    таким образом для добавления нужно doctor_name, и department.name, также опционально номер телефона.
     */

    public static int addDoctor(Doctor doctor, String deptName){
        try{
            loadList();
        }
        catch (Exception e){
            System.out.println(e);
            return -1;
        }
        for(Doctor d : doctorList){
            if(d.equals(doctor))return d.getDoctorId();
        }
        doctor.setDoctorId(doctorList.size()+1);

        int deptId = DepartmentDAO.addDepartment(new Department(deptName));

        String insertStatement = "INSERT INTO " + TABLE_NAME + " VALUES ('"
                + doctor.getDoctorId() + "', '"+ doctor.getNameSurname() + "', '"
                + doctor.getPhoneNumber() + "', '" + deptId + "');";

        try{
            DBUtil.dbExecuteUpdate(insertStatement);
            loadList();
        }
        catch(Exception e){
            System.out.println(e);
            return -1;
        }
        return doctorList.size();
    }
    //Метод для полного обновления записи.
    //обновляет на основании объекта класса Doctor
    public static boolean updateDoctor(Doctor doctor, boolean nameChanged){
        String updateStatementWithName = "UPDATE " + TABLE_NAME + " SET " + DOCTOR_NAME + "='" + doctor.getNameSurname() + "', "
                + DOCTOR_PHONE + "='" + doctor.getPhoneNumber() + "', " + DEPARTMENT_ID + "='" + doctor.getDepartment().getDepartmentId() + "' WHERE "
                + DOCTOR_ID + "='" + doctor.getDoctorId() +"';";
        String updateStatement = "UPDATE " + TABLE_NAME + " SET "
                + DOCTOR_PHONE + "='" + doctor.getPhoneNumber() + "', " + DEPARTMENT_ID + "='" + doctor.getDepartment().getDepartmentId() + "' WHERE "
                + DOCTOR_ID + "='" + doctor.getDoctorId() +"';";
        try{
            if(nameChanged) {
                DBUtil.dbExecuteUpdate(updateStatementWithName);
            }
            else{
                DBUtil.dbExecuteUpdate(updateStatement);
            }
            loadList();
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;

    }

}

