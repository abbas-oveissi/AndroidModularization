package ir.oveissi.authentication;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ir.oveissi.authentication.di.AuthComponent;
import ir.oveissi.authentication.di.DaggerAuthComponent;
import ir.oveissi.core.bases.AppComponentHolder;
import ir.oveissi.core.bases.BaseActivity;
import ir.oveissi.core.di.NavigationProvider;
import ir.oveissi.core.navigation.HasNavigatorManager;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.core.navigation.Navigator;

public class AuthMainActivity extends BaseActivity implements HasNavigatorManager, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    NavigationManager navigationManager;
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);


        AuthComponent authComponent =
                DaggerAuthComponent.builder()
                        .appComponent(((AppComponentHolder) getApplicationContext()).getAppComponent())
                        .navigationProvider(new NavigationProvider(this))
                        .build();
        authComponent.inject(this);

        navigator = navigationManager.init(this, getSupportFragmentManager(), R.id.container);

        if (savedInstanceState == null)
            navigator.openAsRoot(LoginFragment.newInstance());
    }


    @Override
    public Navigator provideNavigator() {
        return navigator;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
