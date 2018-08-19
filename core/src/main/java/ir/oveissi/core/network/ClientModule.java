package ir.oveissi.core.network;


import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ir.oveissi.core.BuildConfig;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Abbas on 5/17/16.
 */
@Module
public class ClientModule {
    @Singleton
    @Provides
    public static OkHttpClient provideOkHttpClient(
            HttpLoggingInterceptor loggingInterceptor,
            Authenticator authenticator,
            @Named("tokenInterceptor") Interceptor tokenInterceptor) {

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(tokenInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS);

        okHttpClient.authenticator(authenticator);


        if (BuildConfig.DEBUG)
            okHttpClient.addInterceptor(loggingInterceptor);

        return okHttpClient.build();
    }

    @Singleton
    @Provides
    public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;

    }


    @Singleton
    @Provides
    @Named("tokenInterceptor")
    public Interceptor provideTokenInterceptor(TokenInterceptor tokenInterceptor) {
        return tokenInterceptor;

    }

    @Singleton
    @Provides
    public Authenticator provideTokenAuthenticator(TokenAuthenticator tokenInterceptor) {
        return tokenInterceptor;

    }


}
