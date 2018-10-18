package minhnq.gvn.com.openweathermap.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit.Builder builder;
    private static APIClient apiClient;
    private static Retrofit retrofit = null;

    public static APIClient newInstance() {
        if (apiClient == null)
            apiClient = new APIClient();
        return apiClient;
    }

    public static Retrofit.Builder getBuilder() {
        if (builder == null)
            builder = new Retrofit.Builder();
        return builder;
    }

    public static Retrofit getClient(String url) {
        if (retrofit == null) {
            retrofit = getBuilder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
