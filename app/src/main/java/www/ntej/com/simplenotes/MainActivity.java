package www.ntej.com.simplenotes;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity

{

    boolean isLargeOrXlargeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //for tablets(large/x-large screens) locking screen orientation to landscape
        if(isScreenLarge())
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isLargeOrXlargeScreen = true;

            //handle double pan
        }
        else
        {
            //handle single pan
        }
    }



    //for checking screen size
    public  boolean isScreenLarge()
    {
        final int screenSize =
                getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;

    }
}
