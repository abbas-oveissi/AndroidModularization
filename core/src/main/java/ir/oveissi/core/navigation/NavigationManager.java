package ir.oveissi.core.navigation;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class NavigationManager {


    private AppCompatActivity context;

    public NavigationManager(AppCompatActivity Context) {
        context = Context;
    }

    public NavigationManager(Fragment fragment) {
        context = (AppCompatActivity) fragment.getActivity();
    }

    void checkImplementInterface(Object object) throws IllegalStateException {
        if (!(object instanceof HasNavigatorManager))
            throw new IllegalStateException("implement HasNavigationManager");
    }

    public Navigator init(Fragment fragment, FragmentManager mFragmentManager, int container) {
        if (!(fragment instanceof HasNavigatorManager))
            throw new IllegalStateException("implement HasNavigationManager");
        return init((Object) fragment, mFragmentManager, container);
    }

    Navigator init(Object obj, FragmentManager mFragmentManager, int container) {
        Navigator navigator = new Navigator(context, mFragmentManager, container);
        return navigator;
    }

    public Navigator init(AppCompatActivity activity, FragmentManager mFragmentManager, int container) {
        if (!(activity instanceof HasNavigatorManager))
            throw new IllegalStateException("implement HasNavigationManager");
        return init((Object) activity, mFragmentManager, container);
    }

    public Navigator getNavigator(Fragment fragment) {
        Fragment parentFrag = fragment;
        while (true) {
            parentFrag = parentFrag.getParentFragment();
            if (parentFrag == null)
                break;
            if (parentFrag instanceof HasNavigatorManager) {
                return ((HasNavigatorManager) parentFrag).provideNavigator();
            }
        }
        checkImplementInterface(fragment.getActivity());
        return ((HasNavigatorManager) context).provideNavigator();
    }

    public void openActivity(Intent intent) {
        context.startActivity(intent);
    }

}
