package Classes;

import Utils.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
Класс для заполнения и управления массивом объектов @Department данными из базы данных
В базе данных таблица - department, поля - 'department_id' - int, 'department_name' - varchar.
Массив типа ObservableList, так как будет использован для создания выпадющих списков.
Необходимости в удалении записей из таблицы нет.
 */

public class DepartmentDAO {
    private static final String DEPARTMENT_ID = "department_id";
    private static final String DEPARTMENT_NAME = "department_name";
    private static final String DEPARTMENT_TABLE_NAME = "lab.department";
    private static ObservableList<Department> departmentList = FXCollections.observableArrayList();

    //метод для получения ссылки на лист
    //при вызове загружает содержимое листа

    public static ObservableList<Department> getDepartmentList() {
        try {
            loadList();
        }
        catch (Exception e){
            System.out.println(e);
        }
        return departmentList;
    }

    //Метод для заполнения departmentList всеми записями из таблицы
    //Возвращает true, если список заполнился
    //Возвращает false, если что-то пошло не так

    public static boolean loadList() throws SQLException, ClassNotFoundException {
        String selectStatement = "SELECT * FROM " + DEPARTMENT_TABLE_NAME;
        ResultSet rs = null;
        try{
            rs = DBUtil.dbExecuteQuery(selectStatement);
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        if(departmentList!=null){
            departmentList.removeAll();
            departmentList.clear();
        }
        if(rs == null) return false;
        while(rs.next()){
            Department dept = new Department();
            dept.setDepartmentId(rs.getInt(DEPARTMENT_ID));
            dept.setDepartmantName(rs.getString(DEPARTMENT_NAME));
            departmentList.add(dept);
        }
        return true;
    }

    //метод, добавляющий новую запись в список
    //возвращает id новой записи, если она была добавлена или
    //если id отделения с таким названием, независимо от регистра в поле departmentName
    //Если в списке нет такой записи, то id выставляется на 1 больше, количества записей
    //Возвращает -1, если что-то пошло не так

    public static int addDepartment(Department dept){
        try{
            loadList();
        }
        catch (Exception e){
            System.out.println(e);
            return -1;
        }
        for(Department d : departmentList){
            if(d.equals(dept))return d.getDepartmentId();
        }

        dept.setDepartmentId(departmentList.size()+1);
        String insertStatement = "INSERT INTO "+DEPARTMENT_TABLE_NAME+" ("+DEPARTMENT_ID+","+DEPARTMENT_NAME+") " +
                "VALUES ('" + dept.getDepartmentId() + "', '" + dept.getDepartmantName()+"');";
        try{
            DBUtil.dbExecuteUpdate(insertStatement);
            loadList();
        }
        catch (Exception e){
            System.out.println(e);
            return -1;
        }
        return departmentList.size();

    }

    //Метод для формирования объекта Department по номеру id

    public static Department getDepartmentById(int id){
        String selectStatement = "SELECT * FROM " +DEPARTMENT_TABLE_NAME+" WHERE " + DEPARTMENT_ID + "=" + id;
        ResultSet rs = null;
        Department department = new Department();
        try{
            rs = DBUtil.dbExecuteQuery(selectStatement);
            rs.next();
            department.setDepartmentId(id);
            department.setDepartmantName(rs.getString(DEPARTMENT_NAME));
        }
        catch (Exception e){
            System.out.println(e);
        }
        return department;
    }

}
