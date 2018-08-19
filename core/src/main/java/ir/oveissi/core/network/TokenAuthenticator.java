package ir.oveissi.core.network;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ir.oveissi.core.pojo.Token;
import ir.oveissi.core.user.User;
import ir.oveissi.core.user.UserManager;
import ir.oveissi.core.utils.Constants;
import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;


public class TokenAuthenticator implements Authenticator {
    static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private UserManager uspm;

    @Inject
    public TokenAuthenticator(UserManager uspm) {
        this.uspm = uspm;
    }


    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        if (response.request().url().toString().equals(Constants.BASE_URL + "/oauth/token"))
            return null;

        Map<String, String> map = new HashMap<>();


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "refresh_token")
                .addFormDataPart("refresh_token", uspm.getCurrentUser().getRefreshToken())
                .build();

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/oauth/token")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + uspm.getCurrentUser().getAccessToken())
                .build();

        Response r = client.newCall(request).execute();
        if (r.code() == 401) {
            uspm.logout();
            return null;
        }


        String jsonResponse = r.body().string();
        Gson gson = new Gson();
        Token token = gson.fromJson(jsonResponse, Token.class);

        String email = uspm.getCurrentUser().getUsername();
        String name = uspm.getCurrentUser().getName();

        User user = new User(name,
                email,
                token.access_token,
                token.refresh_token);
        uspm.login(user);

        return response.request().newBuilder().header("Authorization", "Bearer " + token.access_token).build();
    }
}

