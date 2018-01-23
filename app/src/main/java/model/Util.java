package model;

import java.util.Random;

/**
 * Util class
 */

public class Util {

    public static String[] funPhrases ={
            " Katyperry Songs",
            " KidCudi Albums",
            " Thesis notes",
            " taking beautiful pictures",
            " Pokemon Go" };

    public static boolean editTextIsEmpty(String text)
    {

        if(text.trim().length()==0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
