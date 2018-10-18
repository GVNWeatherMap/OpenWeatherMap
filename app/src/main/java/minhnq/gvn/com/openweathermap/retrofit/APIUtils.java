package minhnq.gvn.com.openweathermap.retrofit;

public class APIUtils
{
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    public static APIService getAPIService(){
        return RetrofitClient.getRetrofitClient(BASE_URL).create(APIService.class);
    }
}
