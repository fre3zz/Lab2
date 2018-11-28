package Utils;

public class Validations {
    public static boolean isNameValid(String s){
        return s.matches("[а-яА-я\\-]{1,20}\\s[а-яА-я]{1,2}[.][а-яА-я]{1,2}[.]")||s.matches("[а-яА-я]{1,20}\\s[а-яА-я]{1,20}\\s[а-яА-я]{1,20}");
    }
    public static boolean isDepartmentValid(String s){
        return s.matches("[a-яА-Я\\s]{1,20}");
    }
    public static boolean isPhoneValid(String s){
        return s.matches("\\+7\\([0-9]{3}\\)[0-9]{3}\\-[0-9]{2}\\-[0-9]{2}") || s.equals("") || s.matches("[0-9]{2}\\-[0-9]{2}");
    }
}
