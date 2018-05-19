package com.mosbyextra.ggaworowski.retrofittest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Graphicbox on 23.03.2017.
 */


public class RestClient implements Interceptor {

    private static RestClient instance;

    public static RestClient getInstance() {
        if(instance==null){
            instance = new RestClient();
        }
        return instance;
    }

    public static final String PATH = "https://reqres.in/";


    public static final String API_PATH = PATH + "api/";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private String mUserToken;
    private RestService mService;


    private RestClient() {

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.addInterceptor(this);
        client.connectTimeout(20, TimeUnit.SECONDS);
        client.readTimeout(20, TimeUnit.SECONDS);
        client.writeTimeout(20, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);
        }

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build();

        RestService sscRestService = retrofit.create(RestService.class);
        mService = sscRestService;
    }

    public String getToken() {
        return mUserToken;
    }

    public RestService getService() {
        return mService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newBuilder = chain.request().newBuilder();
        if (mUserToken != null) {   // Usuń jeśli nie chcesz dodawać headera autoryzacji
            newBuilder.addHeader(AUTHORIZATION_HEADER, "Bearer " + mUserToken);
            return chain.proceed(newBuilder.build());
        }
        return chain.proceed(chain.request());
    }

    public void setUserToken(String mUserToken) {
        this.mUserToken = mUserToken;
    }
}
