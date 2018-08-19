package ir.oveissi.main;


import android.support.v4.app.Fragment;

import ir.oveissi.core.navigation.MainEntryPoint;

public class MainEntryPointImpl implements MainEntryPoint {
    @Override
    public Fragment openMain(String query) {
        return MoviesFragment.newInstance(query);
    }
}
