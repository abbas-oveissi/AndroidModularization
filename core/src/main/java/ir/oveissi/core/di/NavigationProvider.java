package ir.oveissi.core.di;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import ir.oveissi.core.navigation.EntryPointHolder;
import ir.oveissi.core.navigation.MainEntryPoint;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.core.navigation.SearchEntryPoint;

@Module
public class NavigationProvider {


    private AppCompatActivity activity;

    public NavigationProvider(AppCompatActivity activity) {
        this.activity = activity;
    }

    public NavigationProvider(Fragment fragment) {
        this.activity = (AppCompatActivity) fragment.getActivity();
    }

    @Provides
    public NavigationManager provideManager() {
        return new NavigationManager(activity);
    }


    @Provides
    public MainEntryPoint provideMainEntryPoint() {
        return ((EntryPointHolder) activity.getApplicationContext()).getMainEntryPoint();
    }

    @Provides
    public SearchEntryPoint provideSearchEntryPoint() {
        return ((EntryPointHolder) activity.getApplicationContext()).getSearchEntryPoint();
    }
}
