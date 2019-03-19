package wdx.musgig.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApi {

    @GET("/api/venue")
    Call<List<Venues>> getData();
//@Query("name") String resourceName, @Query("num") int count

    @POST("/api/venue")
    Call<Venues> addVenue(@Body Test Test);

//    @POST("/api/venue")
    //  Venues registerUser(@Body Venues Venues);


}
