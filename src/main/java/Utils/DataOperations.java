package Utils;

import Classes.Doctor;
import Classes.DoctorDAO;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class DataOperations {
    private static ObservableList<Doctor> doctorList = null;
    private static FilteredList<Doctor> doctorFilteredList = null;
    private static SortedList<Doctor>  doctorSortedList = null;

    public static void populateDoctorList() throws SQLException, ClassNotFoundException {
        if(doctorList != null) doctorList.clear();
        doctorList = DoctorDAO.searchDoctors();
    }
    public static SortedList<Doctor> getDoctorSortedList(ObservableList<Doctor> doclist, TextField searchField){
        if(doctorFilteredList != null) doctorFilteredList.clear();
        doctorFilteredList = new FilteredList<>(doclist, p->true);

        searchField.textProperty().addListener((ov, o, nv) -> {
            doctorFilteredList.setPredicate(doctor -> {
                if (nv == null || nv.equals("")) {
                    return true;
                }
                String filter = nv.toLowerCase();
                if (doctor.getNameSurname().toLowerCase().contains(filter)) {
                    return true;
                } else if (doctor.getDepartment().toLowerCase().contains(filter)) {
                    return true;
                } else return false;


            });

        });
        if(doctorSortedList != null)doctorSortedList.clear();
        doctorSortedList = new SortedList<>(doctorFilteredList);
        return doctorSortedList;
    }
    public static ObservableList<Doctor> getDoctorList(){
        return doctorList;
    }
}
