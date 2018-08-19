package ir.oveissi.authentication;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import ir.oveissi.core.navigation.AuthEntryPoint;


public class AuthNavigator implements AuthEntryPoint {


    @Override
    public Intent openMain() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("dsvs://dsfood/main"));
    }


    @Override
    public Fragment openFrag3() {
        return RegisterFragment.newInstance();
    }
}
