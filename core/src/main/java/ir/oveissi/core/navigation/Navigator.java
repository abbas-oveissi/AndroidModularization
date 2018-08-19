package ir.oveissi.core.navigation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Navigator {

    private Context appcompat;
    private FragmentManager mFragmentManager;
    private int container;
    private NavigationListener navigationListener;


    public Navigator(Context appcompat, FragmentManager mFragmentManager, int container) {
        this.appcompat = appcompat;

        this.mFragmentManager = mFragmentManager;
        this.container = container;

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (navigationListener != null)
                    navigationListener.change();
            }
        });
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public boolean isRootFragmentVisible() {
        return mFragmentManager.getBackStackEntryCount() <= 1;
    }

    public void open(Fragment fragment) {
        FragmentTransaction fragTransaction = mFragmentManager.beginTransaction();
        fragTransaction.replace(container, fragment);
        fragTransaction.addToBackStack(fragment.toString());
        fragTransaction.commit();
    }

    public void openFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragTransaction = mFragmentManager.beginTransaction();
        fragTransaction.replace(container, fragment);
        if (addToBackStack)
            fragTransaction.addToBackStack(fragment.toString());
        fragTransaction.commit();
    }

    public void openAsRoot(Fragment fragment) {
        popEveryFragment();
        openFragment(fragment, false);
    }

    private void popEveryFragment() {
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();
            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public boolean navigateBack() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            return false;
        } else {
            mFragmentManager.popBackStackImmediate();
            return true;
        }
    }


    public interface NavigationListener {
        public void change();
    }

}