package Classes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.Objects;

public class Doctor {
    private StringProperty NameSurname;
    private StringProperty department;
    private StringProperty phoneNumber;
    private StringProperty id;

    public final StringProperty idProperty(){
        if(id == null){
            id = new SimpleStringProperty();
        }
        return id;
    }
    public final void setId(String id){ idProperty().set(id);}
    public final String getId(){return idProperty().get();}
    public final void setNameSurname(String nameSurname){
        NameSurnameProperty().set(nameSurname);
    }
    public final String getNameSurname(){
        return NameSurnameProperty().get();
    }
    public final StringProperty NameSurnameProperty(){
        if(NameSurname==null){
            NameSurname = new SimpleStringProperty();
        }
        return NameSurname;

    }
    public final void setDepartment(String department){
        departmentProperty().set(department);
    }
    public final String getDepartment(){
        return departmentProperty().get();
    }
    public final StringProperty departmentProperty(){
        if(department==null){
            department = new SimpleStringProperty();
        }
        return department;
    }

    public final void setPhoneNumber(String phoneN){
        phoneNumberProperty().set(phoneN);
    }
    public final String getPhoneNumber(){
        return phoneNumberProperty().get();
    }
    public final StringProperty phoneNumberProperty(){
    if(phoneNumber == null){
        phoneNumber = new SimpleStringProperty();
    }
    return phoneNumber;
    }
    public Doctor(String id, String name, String department, String phoneN){
        setId(id);
        setNameSurname(name);
        setDepartment(department);
        setPhoneNumber(phoneN);
    }
    public Doctor(String name, String department, String phoneN){
        setNameSurname(name);
        setDepartment(department);
        setPhoneNumber(phoneN);
    }

    public Doctor(){
        setId("");
        setNameSurname("");
        setDepartment("");
        setPhoneNumber("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return doctor.getDepartment().equals(this.getDepartment()) && doctor.getNameSurname().equals(this.getNameSurname());
    }

    @Override
    public int hashCode() {

        return Objects.hash(NameSurname);
    }
    public static boolean checkPresence(ObservableList<Doctor> doctorList, Doctor doctor){
        for(int i = 0; i < doctorList.size(); i++){
            if(doctor.equals(doctorList.get(i)))return true;

        }
        return false;
    }
}
