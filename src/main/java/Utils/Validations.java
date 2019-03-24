package Utils;

public class Validations {
    public static final String VALID_DEPARTMENT = "[a-яА-Я\\s\\.\\-]{1,40}";
    public static boolean isNameValid(String s){
        return s.matches("[А-Я][а-я\\-]{1,20}\\s[а-яА-Я]{1,2}[.][а-яА-Я]{1,2}[.]")||s.matches("[А-Я][а-я]{1,20}\\s[А-Я][а-я]{1,20}\\s[А-Я][а-я]{1,20}");
    }
    public static boolean isDepartmentValid(String s){
        return s.matches("[a-яА-Я\\s\\.\\-]{1,40}");
    }
    public static boolean isPhoneValid(String s){
        return s.matches("\\+7\\([0-9]{3}\\)[0-9]{3}\\-[0-9]{2}\\-[0-9]{2}") || s.equals("") || s.matches("[0-9]{2}\\-[0-9]{2}");
    }
}
