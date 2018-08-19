package ir.oveissi.core.network;

import java.io.IOException;

import javax.inject.Inject;

import ir.oveissi.core.local.SettingsManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class TokenInterceptor implements Interceptor {

    private SettingsManager uspm;

    @Inject
    public TokenInterceptor(SettingsManager uspm) {
        this.uspm = uspm;
    }


    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        if (uspm.getAccessToken().length() > 0) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + uspm.getAccessToken())
                    .build();
        }
        Response response = chain.proceed(request);
        return response;
    }
}

