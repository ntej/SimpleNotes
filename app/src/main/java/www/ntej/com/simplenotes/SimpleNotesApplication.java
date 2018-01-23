package www.ntej.com.simplenotes;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.ui.SignInActivity;
import com.amazonaws.regions.Regions;

import aws.AWSProvider;

/**
 * Created by hz7d7v on 1/19/18.
 */

public class SimpleNotesApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        AWSProvider.initialize(getApplicationContext());

        registerActivityLifecycleCallbacks(new ActivityLifeCycle());
    }

    class ActivityLifeCycle implements android.app.Application.ActivityLifecycleCallbacks {
        private int depth = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (depth == 0) {
                Log.d("ActivityLifeCycle", "Application entered foreground");
                AWSProvider.getInstance().getPinpointManager().getSessionClient().startSession();
                AWSProvider.getInstance().getPinpointManager().getAnalyticsClient().submitEvents();
            }
            depth++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            depth--;
            if (depth == 0) {
                Log.d("ActivityLifeCycle", "Application entered background");
                AWSProvider.getInstance().getPinpointManager().getSessionClient().stopSession();
                AWSProvider.getInstance().getPinpointManager().getAnalyticsClient().submitEvents();
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
