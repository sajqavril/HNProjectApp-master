package as.hn.com.hnprojectapp;

import android.app.Application;
import android.content.Context;

import com.hn.business.Tools.CrashHandler;

public class CrashApplication extends Application {

    public static CrashApplication instance;

    @Override

    public void onCreate() {

        super.onCreate();

        instance  = this;

        CrashHandler crashHandler = CrashHandler.getInstance();

        crashHandler.init(getApplicationContext());


    }

    public static CrashApplication getInstance(){
        return instance;
    }




}
