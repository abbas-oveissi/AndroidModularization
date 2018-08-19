package ir.oveissi.core.di;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ir.oveissi.core.BaseApplication;

/**
 * Created by Abbas on 5/17/16.
 */
@Module
public class AndroidModule {


    private BaseApplication baseApplication;

    public AndroidModule(BaseApplication baseApplication) {
        this.baseApplication = baseApplication;
    }

    @Provides
    @Singleton
    public Context provideAppContext() {
        return baseApplication;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(provideAppContext());
    }

}
