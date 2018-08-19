package ir.oveissi.androidmodularization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ir.oveissi.androidmodularization.databinding.ActivityMainBinding;
import ir.oveissi.authentication.AuthMainActivity;
import ir.oveissi.core.bases.AppComponentHolder;
import ir.oveissi.core.di.NavigationProvider;
import ir.oveissi.core.navigation.HasNavigatorManager;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.core.navigation.Navigator;
import ir.oveissi.core.network.ApiInterface;
import ir.oveissi.core.pojo.UserInfo;
import ir.oveissi.core.user.UserManager;
import ir.oveissi.main.MoviesFragment;

public class MainActivity extends AppCompatActivity implements HasNavigatorManager, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    NavigationManager navigationManager;

    @Inject
    ApiInterface apiInterface;
    @Inject
    UserManager userManager;
    private MainActivityComponent mainComponent;
    private Navigator childNavigator;
    private ActionBarDrawerToggle mDrawerToggle;


    private ActivityMainBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainComponent = DaggerMainActivityComponent.builder()
                .appComponent(((AppComponentHolder) getApplicationContext()).getAppComponent())
                .navigationProvider(new NavigationProvider(this))
                .build();
        mainComponent.inject(this);

        childNavigator = navigationManager.init(this, getSupportFragmentManager(), R.id.container);
        childNavigator.open(MoviesFragment.newInstance(""));

        mDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                binding.includedToolbar.myToolbar,
                R.string.cancel,
                R.string.cancel);
        mDrawerToggle.syncState();
        binding.drawerLayout.addDrawerListener(mDrawerToggle);

        setSupportActionBar(binding.includedToolbar.myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Movies");

        Menu navMenu = binding.navigation.getMenu();
        binding.navigation.setNavigationItemSelectedListener(this);

//        navMenu.findItem(R.id.navigation_subheader_debug).isVisible = BuildConfig.DEBUG

    }

    @Override
    public Navigator provideNavigator() {
        return childNavigator;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.navigation_login:
                startActivity(new Intent(this, AuthMainActivity.class));
                return true;
            case R.id.navigation_logout:
                userManager.logout();
                Menu navMenu = binding.navigation.getMenu();
                navMenu.findItem(R.id.navigation_logout).setVisible(false);
                navMenu.findItem(R.id.navigation_profile).setVisible(false);
                navMenu.findItem(R.id.navigation_login).setVisible(true);
                return true;
            case R.id.navigation_profile:
                getUserInfo();
                return true;
        }
        return false;
    }

    private void getUserInfo() {
        progressDialog = ProgressDialog.show(this, "Loading", "Please Wait...");
        DisposableSubscriber<UserInfo> dispose = apiInterface
                .getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<UserInfo>() {
                    @Override
                    public void onNext(UserInfo userInfo) {
                        Toast.makeText(MainActivity.this, "Hi " + userInfo.name + "!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();

        Menu navMenu = binding.navigation.getMenu();
        if (userManager.isUserLogged()) {
            navMenu.findItem(R.id.navigation_logout).setVisible(true);
            navMenu.findItem(R.id.navigation_profile).setVisible(true);

            navMenu.findItem(R.id.navigation_login).setVisible(false);
        } else {
            navMenu.findItem(R.id.navigation_logout).setVisible(false);
            navMenu.findItem(R.id.navigation_profile).setVisible(false);

            navMenu.findItem(R.id.navigation_login).setVisible(true);
        }
    }
}
