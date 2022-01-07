package rw.benax.callblocker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rw.benax.callblocker.api.APIClient;
import rw.benax.callblocker.api.APIInterface;


public class Model {
    @SerializedName("success")
    public Integer success;
    @SerializedName("licenced")
    public Integer licenced;

    @SerializedName("data")
    public List<Download> data = new ArrayList<>();
    public static class Download {
        @SerializedName("id")
        public String id;
        @SerializedName("data0")
        public String data0;
        @SerializedName("data1")
        public String data1;
    }

    static APIInterface api;
    public static void reportEvent(String phoneNumber, String msg, Context context){
        api = APIClient.getClient("https://smser.benax.rw/").create(APIInterface.class);
        try {
            Call<Model> call = api.uploadSMS("Gabriel BAZIRAMWABO", msg);
            call.enqueue(new Callback<Model>(){
                @Override
                public void onResponse(@NotNull Call<Model> call, @NotNull Response<Model> response) {
                    Log.d("TAG",response.code()+"");
                }

                @Override
                public void onFailure(@NotNull Call<Model> call, @NotNull Throwable t) {
                    call.cancel();
                }
            });
            Toast.makeText(context, "Event reported...", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(context, "Unable to report the event.", Toast.LENGTH_LONG)
                    .show();
        }
    }

}
