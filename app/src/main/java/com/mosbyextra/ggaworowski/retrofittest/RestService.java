package com.mosbyextra.ggaworowski.retrofittest;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Graphicbox on 23.03.2017.
 */

public interface RestService {

    @GET("users/{id}")
    Single<DataModel<UserModel>> getUser(@Path("id") int id);

}
