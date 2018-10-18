package minhnq.gvn.com.openweathermap.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("/photos")
    Call<List<RetroPhoto>> getAllPhotos();
}
