package net.poquesoft.appio.config;

import android.content.Context;
import android.util.Log;

import net.poquesoft.appio.view.listeners.StringListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RemoteConfig {

    private static final String TAG = "RemoteConfig";
    private static final int MIN_LENGTH = 100;
    private static final long CACHE_SIZE_BYTES = 256000;
    String localFile;
    String remoteUrl;
    StringListener resultListener;
    Context context;

    private OkHttpClient client;

    String contentToReturn;

    public RemoteConfig(Context context, String localFile, String remoteUrl, StringListener resultListener) {
        this.context = context;
        this.localFile = localFile;
        this.remoteUrl = remoteUrl;
        this.resultListener = resultListener;
        this.contentToReturn = "";

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.cache(
                new Cache(context.getCacheDir(), CACHE_SIZE_BYTES));
        client = builder.build();
    }

    public void getConfig() {
        contentToReturn = getLocalFile();
        long localLastModified = getLocalFileTimestamp();
        getRemoteConfig(localLastModified);
    }

    public void getRemoteConfig(final long newerThan) {
        if (newerThan <= 0) {
            getRemoteConfig();
            return;
        }

        //Get HEAD only
        Request request = new Request.Builder()
                .head()
                .url(remoteUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                resultListener.onAction(contentToReturn);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                if (!response.isSuccessful()){
                    Log.e(TAG,"Response not succesful: "+response.code());
                    resultListener.onAction(contentToReturn);
                    return;
                }
                String lastModified = headers.get("Last-Modified");;
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                try {
                    Date d = format.parse(lastModified);
                    if (d.getTime() < newerThan) {
                        resultListener.onAction(contentToReturn);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                getRemoteConfig();
            }
        });
    }

    public void getRemoteConfig() {
        Request.Builder requestBuilder = new Request.Builder()
                .url(remoteUrl);

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                resultListener.onAction(contentToReturn);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    if (response.code() == HttpURLConnection.HTTP_NOT_MODIFIED){
                        return;
                    }

                    if (response.networkResponse() != null &&
                            response.networkResponse().code() ==
                                    HttpURLConnection.HTTP_NOT_MODIFIED) {
                        // not modified, no need to do anything.
                        return;
                    }

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    String json = responseBody.string();
                    if (json.length() > MIN_LENGTH) {
                        //TODO: Add content validation (correct JSON)
                        contentToReturn = json;
                        saveToDisk(localFile, contentToReturn);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                resultListener.onAction(contentToReturn);
            }
        });
    }

    public long getLocalFileTimestamp() {
        File file = new File(context.getFilesDir(), localFile);
        return file.lastModified();
    }

    public String getLocalFile() {
        try {
            File file = new File(context.getFilesDir(), localFile);
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException ioe) {
            Log.e(TAG, "Error reading file");
            return "";
        }
    }

    private void saveToDisk(String filename, String fileContents) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
