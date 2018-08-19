package ir.oveissi.core.network;


import java.util.Map;

import io.reactivex.Flowable;
import ir.oveissi.core.pojo.Movie;
import ir.oveissi.core.pojo.Pagination;
import ir.oveissi.core.pojo.Register;
import ir.oveissi.core.pojo.RegisterBody;
import ir.oveissi.core.pojo.Token;
import ir.oveissi.core.pojo.UserInfo;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Abbas on 24/05/2016.
 */
public interface ApiInterface {

    @GET("/api/v1/movies")
    Flowable<Pagination<Movie>> getMoviesByTitle(@Query("q") String query, @Query("page") Integer page);

    @GET("/api/v1/movies/{id}")
    Flowable<Movie> getMovieById(@Path("id") String id);

    @FormUrlEncoded
    @POST("/oauth/token")
    Flowable<Token> getToken(@FieldMap Map<String, String> params);

    @POST("/api/v1/register")
    Flowable<Register> register(@Body RegisterBody registerBody);

    @GET("/api/user")
    Flowable<UserInfo> getUserInfo();
}
