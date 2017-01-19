package github.com.anurag145.srmhackathon;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;



public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        EstimoteSDK.initialize(getApplicationContext(), "chawlaaditya8-gmail-com-s--35a", "c0f762eb508e0c842d7e175930474746");


    }
}
