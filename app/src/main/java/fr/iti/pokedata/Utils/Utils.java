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

    public static String getFormattedId(int id) {
        if(id < 10)
            return "#00" + Integer.toString(id);
        if(id < 100)
            return "#0" + Integer.toString(id);
        return "#" + Integer.toString(id);
    }
}
