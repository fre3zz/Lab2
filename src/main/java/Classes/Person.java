package Classes;

import Utils.Validations;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Супер-класс для людей. Поле - StringProperty NameSurname, также проверка валидности
public class Person {
    protected StringProperty nameSurname;

    public Person(String nameSurname){
        setNameSurname(nameSurname);
    }

    Person(){
        this(null);
    }

    public final void setNameSurname(String nameSurname){
        NameSurnameProperty().set(nameSurname);
    }

    public final String getNameSurname(){
        return NameSurnameProperty().get();
    }

    public final StringProperty NameSurnameProperty(){
        if(nameSurname==null){
            nameSurname = new SimpleStringProperty();
        }
        return nameSurname;
    }

    public boolean isNameValid(){
        return getNameSurname()!=null &&
                (getNameSurname().matches(Validations.VALID_NAME_LONG)||getNameSurname().matches(Validations.VALID_NAME_SHORT));
    }

    //Тут метод equals не совсем в привычном понимании
    //Проверяется только поле name и если они совпадают вне зависимости от регистра, возвращается true

    public boolean equals(Object obj) {
        if (obj instanceof Doctor)
            return getNameSurname().equalsIgnoreCase(((Doctor) obj).getNameSurname());
        return false;
    }
}
