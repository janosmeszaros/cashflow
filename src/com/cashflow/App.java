package com.cashflow;

import roboguice.RoboGuice;
import android.app.Application;

/**
 * Application context for the android app.
 * @author Janos_Gyula_Meszaros
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this), new AppModule());
    }
}