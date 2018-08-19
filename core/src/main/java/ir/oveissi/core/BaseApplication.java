package ir.oveissi.core;

import android.app.Application;

import ir.oveissi.core.bases.AppComponentHolder;
import ir.oveissi.core.di.AndroidModule;
import ir.oveissi.core.di.AppComponent;
import ir.oveissi.core.di.DaggerAppComponent;

public class BaseApplication extends Application implements AppComponentHolder {


    private AppComponent appCommponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appCommponent = DaggerAppComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
    }


    @Override
    public AppComponent getAppComponent() {
        return appCommponent;
    }

}
