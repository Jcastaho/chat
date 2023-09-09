package com.straccion.chat.providers;

import com.straccion.chat.models.FCMBody;
import com.straccion.chat.models.FCMResponse;
import com.straccion.chat.retrofit.IFCMApi;
import com.straccion.chat.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {
    private String url = "https://fcm.googleapis.com";
    public NotificationProvider(){

    }
    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
