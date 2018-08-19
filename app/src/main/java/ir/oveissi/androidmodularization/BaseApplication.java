package ir.oveissi.androidmodularization;


import ir.oveissi.core.navigation.EntryPointHolder;
import ir.oveissi.core.navigation.MainEntryPoint;
import ir.oveissi.core.navigation.SearchEntryPoint;
import ir.oveissi.main.MainEntryPointImpl;
import ir.oveissi.search.SearchEntryPointImpl;

public class BaseApplication extends ir.oveissi.core.BaseApplication implements EntryPointHolder {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public SearchEntryPoint getSearchEntryPoint() {
        return new SearchEntryPointImpl();
    }

    @Override
    public MainEntryPoint getMainEntryPoint() {
        return new MainEntryPointImpl();
    }

}
