package rw.benax.offguard;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rw.benax.offguard.api.APIClient;
import rw.benax.offguard.api.APIInterface;


public class Model {

    static APIInterface api;
    public static void reportEvent(String source, String msg, Context context){
        api = APIClient.getClient("https://smser.benax.rw/").create(APIInterface.class);
        try {
            Call<Model> call = api.uploadSMS(source, msg);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<Model> call, @NotNull Response<Model> response) {
                    Log.d("TAG", response.code() + "");
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
