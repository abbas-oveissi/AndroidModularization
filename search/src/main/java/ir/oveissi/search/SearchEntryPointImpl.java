package ir.oveissi.search;

import android.support.v4.app.Fragment;

import ir.oveissi.core.navigation.SearchEntryPoint;

public class SearchEntryPointImpl implements SearchEntryPoint {
    @Override
    public Fragment openMain() {
        return MovieSearchFragment.newInstance();
    }
}
