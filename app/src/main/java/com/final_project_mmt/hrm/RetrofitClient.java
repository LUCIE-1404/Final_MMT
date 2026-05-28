package com.final_project_mmt.hrm;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.82:3000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            // Thiết lập Bộ chặn (Interceptor) của OkHttpClient để tự động cấu hình HTTP Request Header
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();

                        // Nếu đã có Token từ Firebase, tự động chèn Header Authorization: Bearer <TOKEN>
                        if (!AuthTokenManager.TOKEN.isEmpty()) {
                            Request authenticatedRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + AuthTokenManager.TOKEN)
                                    .build();
                            return chain.proceed(authenticatedRequest);
                        }

                        return chain.proceed(originalRequest);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}