package fr.iti.pokedata.Utils;

/**
 * Created by Antonin on 29/01/2018.
 */

public class Utils {

    public static String getFormattedString(String string) {
        String formattedString = string.trim();
        if(formattedString.length() <= 0)
            return formattedString;
        return formattedString.substring(0, 1).toUpperCase() + formattedString.substring(1);
    }
}
