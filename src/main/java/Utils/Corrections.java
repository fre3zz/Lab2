package Utils;

import javafx.beans.property.StringProperty;

public class Corrections {
    public static String nameCorrection(String input) {
        String output = "";
        if (input != null && !input.equals("")) {
            char[] chars = input.toCharArray();

            if (!Character.isLetter(chars[0])) {
                if (input.length() > 1) return input.substring(1);
                else return output;
            }
            if (Character.isLetter(chars[0]) && Character.isLowerCase(chars[0])) {
                chars[0] = Character.toUpperCase(chars[0]);
            }
            for (int i = 0; i < chars.length; i++) {
                if (Character.isLetter(chars[i])
                        || (chars[i] == ' ' && chars[i - 1] != ' ')
                        //|| (chars[i] == '.' && chars[i - 1] != '.')
                        //|| (chars[i] == '-' && chars[i - 1] != '-')
                ) {
                    if(i>1 && (chars[i-1] == ' ' || chars[i-1] == '.' || chars[i-1] == '-') && Character.isLowerCase(chars[i])){
                        chars[i] = Character.toUpperCase(chars[i]);
                    }
                    if(i>=1 && Character.isLetter(chars[i-1]) && Character.isUpperCase(chars[i])){
                        chars[i] = Character.toLowerCase(chars[i]);
                    }
                    output += chars[i];
                }
                if(i>=1 && ((chars[i] == '.' && chars[i - 1] != '.') || (chars[i] == '-' && chars[i - 1] != '-')) && Character.isLetter(chars[i-1])){
                    output += chars[i];
                }
            }

            }
            return output;
        }

        public static String departmentCorrection(String input){
            String output = "";
            if (input != null && !input.equals("")) {
                char[] chars = input.toCharArray();

                if (!Character.isLetter(chars[0])) {
                    if (input.length() > 1) return input.substring(1);
                    else return output;
                }
                if (Character.isLetter(chars[0]) && Character.isLowerCase(chars[0])) {
                    chars[0] = Character.toUpperCase(chars[0]);
                }
                for (int i = 0; i < chars.length; i++) {
                    if (Character.isLetter(chars[i])
                            || (chars[i] == ' ' && chars[i - 1] != ' ')

                            ) {
                        if(i>=1 && Character.isLowerCase(chars[i])){
                            chars[i] = Character.toUpperCase(chars[i]);
                        }
                        if(i > 1 && i < chars.length - 2 && chars[i-1] == ' ' && chars[i+1] == ' ' ){
                            chars[i] = Character.toLowerCase(chars[i]);
                        }

                        output += chars[i];
                    }
                    if(i>=1 && ((chars[i] == '.' && chars[i - 1] != '.') || (chars[i] == '-' && chars[i - 1] != '-')) && Character.isLetter(chars[i-1])){
                        output += chars[i];
                    }
                }

            }
            return output;
        }
public static String phoneCorrection(String input){
        String output = "";
    if (input != null && !input.equals("")) {
        if(input.length() == 2){input+="(";}
        if(input.length() == 6){input+= ")";}
        if(input.length() == 10){input+="-";}
        if(input.length() == 13){input+="-";}
        char[] chars = input.toCharArray();

        if (!Character.isDigit(chars[0]) && chars[0] != '+') {
            if (input.length() > 1) return input.substring(1);
            else return output;
        }

    }


        return input;
}

    }



