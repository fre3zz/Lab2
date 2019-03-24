package Classes;


/*Класс содержит два поля - id и name
Метод isValid проверяет на соответствие регулярному выражению имя отделения
 */

import Utils.Validations;

public class Department {
    private int departmentId = 0;
    private String departmantName;

    public Department(int id, String name){
        this.departmentId = id;
        this.departmantName = name;
    }
    public Department(String departmantName){
        this(0, departmantName);
    }
    public Department(){
        this(null);
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmantName() {
        return departmantName;
    }

    public void setDepartmantName(String departmantName) {
        this.departmantName = departmantName;
    }

    public boolean isValid(){
        return departmantName!=null && departmantName.matches(Validations.VALID_DEPARTMENT);
    }

    @Override
    //Тут метод equals не совсем в привычном понимании
    //Проверяется только поле name и если они совпадают вне зависимости от регистра, возвращается true

    public boolean equals(Object obj) {
        if (obj instanceof Department)
            return departmantName.equalsIgnoreCase(((Department) obj).departmantName);
        return false;
    }
}
