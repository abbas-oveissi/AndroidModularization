package ir.oveissi.core.local;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsManager {
    private static final String NAME = "name";
    private static final String ACCESS_TOKEN = "accesstoken";
    private static final String REFRESH_TOKEN = "refreshtoken";
    private static final String USERNAME = "username";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Inject
    public SettingsManager(SharedPreferences sharedPreferences) {
        pref = sharedPreferences;
        editor = pref.edit();
    }

    public String getName() {
        return pref.getString(NAME, "");
    }

    public void setName(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public String getRefreshToken() {
        return pref.getString(REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String refreshToken) {
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(USERNAME, "");
    }

    public void setUsername(String username) {
        editor.putString(USERNAME, username);
        editor.commit();
    }


}