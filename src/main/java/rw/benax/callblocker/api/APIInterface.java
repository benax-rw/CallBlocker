package rw.benax.callblocker.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rw.benax.callblocker.Model;

public interface APIInterface {
    @GET("api.php")
    Call<Model> downloadInfo();

    @FormUrlEncoded
    @POST("api.php")
    Call<Model> uploadInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("api.php")
    Call<Model> deleteInfo(@Field("del_id") String id);

    @FormUrlEncoded
    @POST("auth.php")
    Call<Model> checkAuthenticity(@Field("device") String device);

    @FormUrlEncoded
    @POST("handleIncomingSMS.php")
    Call<Model> uploadSMS(@Field("sender") String sender, @Field("SMS") String SMS);

}
