package minhnq.gvn.com.openweathermap.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    static Retrofit.Builder builder;
    private String baseUri;

    public static Retrofit.Builder newInstance() {
        if (builder==null)
            builder = new Retrofit.Builder();
        return builder;
    }

    public RetrofitUtils setUril(String url){
        baseUri = url;
        return this;
    }




    private void initRetrofit(){
        Retrofit retru = builder
                .baseUrl(baseUri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static APIService getAPIService() {
        return APIClient.getClient(Constants.BASE_URL).create(APIService.class);
    }

}
