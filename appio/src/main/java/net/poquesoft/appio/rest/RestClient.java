package net.poquesoft.appio.rest;

import net.poquesoft.appio.view.listeners.SuccessErrorListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {
    OkHttpClient client;

    public RestClient() {
        client = new OkHttpClient();
    }

    public String get(String remoteUrl, Map<String,String> params) {

        HttpUrl.Builder httpBuilder = HttpUrl.parse(remoteUrl).newBuilder();
        try {
            if (params != null) {
                for(Map.Entry<String, String> param : params.entrySet()) {
                        httpBuilder.addQueryParameter(param.getKey(), URLEncoder.encode(param.getValue(),"UTF-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url(httpBuilder.build()).build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void get(String url, Map<String,String>params, final SuccessErrorListener successErrorListener) {
        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        Request request = new Request.Builder().url(httpBuider.build()).build();
        Callback responseCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                successErrorListener.onError(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                successErrorListener.onSuccess();
            }
        };

        client.newCall(request).enqueue(responseCallback);
    }

    public void post(String remoteUrl, Map<String,String> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (String key: params.keySet()){
            builder.addFormDataPart(key,params.get(key));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(remoteUrl)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void post(String remoteUrl, Map<String,String> params, final SuccessErrorListener successErrorListener) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (String key: params.keySet()){
            builder.addFormDataPart(key,params.get(key));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(remoteUrl)
                .post(requestBody)
                .build();
        Callback responseCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                successErrorListener.onError(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                successErrorListener.onSuccess();
            }
        };
        client.newCall(request).enqueue(responseCallback);
    }
}
