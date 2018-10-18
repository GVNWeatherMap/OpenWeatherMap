package minhnq.gvn.com.openweathermap.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Retrofit.Builder builder = null;

    public static Retrofit getRetrofitClient(String baseUrl){
        if(retrofit==null){
         retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
