package com.cashflow;

import roboguice.RoboGuice;
import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Init roboguice.");

        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE, 
 RoboGuice.newDefaultRoboModule(this), new AppModule());
    }
}