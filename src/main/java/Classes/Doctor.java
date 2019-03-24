package Classes;

//Класс для врачей
//Поле для имени из супер-класса
//personId - идентификатор в таблице врачей
//phoneNumber - необязательное поле, может быть null
//Department - ссылка на объект из списка отделений

public class Doctor extends Person{
    private int doctorId;
    private String phoneNumber;
    private Department department;

    public Doctor(String name){
        super(name);
        doctorId = -1;
        phoneNumber = "";
        department = null;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int personId) {
        this.doctorId = personId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


}
