package minhnq.gvn.com.openweathermap.utils;


public class APIUtils {
    public static APIService getAPIService() {
        return APIClient.getClient(Constans.BASE_URL).create(APIService.class);
    }
}
