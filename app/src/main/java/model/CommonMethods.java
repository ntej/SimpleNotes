package model;

import java.util.Random;

/**
 * Created by navatejareddy on 11/1/16.
 */

public class CommonMethods {

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

    public static int ranNumGenerator(int seed)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(seed);
        return randomNum;
    }

}
