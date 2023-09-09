package com.straccion.chat.retrofit;

import com.straccion.chat.models.FCMBody;
import com.straccion.chat.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAk3ft-ws:APA91bGO6ToetMZ5fUaLXFF0NomFCpzOxVGzgCYA3zi_Nb9CBL1CKBVNbnNuHf6RYPUtrKUL_gBd_HtfiOoJ4FF1RO4VFqEdDu-ij0e4a8c_WqAF9qgBhguqt2v99C0rqWQ_AQtImE9W"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
