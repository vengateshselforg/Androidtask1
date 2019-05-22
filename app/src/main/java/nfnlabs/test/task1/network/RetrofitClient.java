package nfnlabs.test.task1.network;

import android.util.Log;

import nfnlabs.test.task1.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit INSTANCE = null;
    private static final String TAG = "RetrofitClient";
    private static final String BASEURL = BuildConfig.BASE_URL;

    public static Retrofit getClient() {
        return getClient(false);
    }

    public static Retrofit getClient(boolean isLoggable) {
        if (INSTANCE == null) {
            OkHttpClient.Builder httpClient;
            if (isLoggable)
                httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor());
            else
                httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(getAuthInterceptor());

            INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return INSTANCE;
    }

    private static Interceptor getAuthInterceptor() {
        return chain -> {
            Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer keyNAyN5rbpM3QZT3").build();
            return chain.proceed(newRequest);
        };
    }

    private static HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                message -> {
                    if (Log.isLoggable(TAG, Log.DEBUG))
                        Log.d(TAG, "loggingInterceptor: " + message);
                });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}