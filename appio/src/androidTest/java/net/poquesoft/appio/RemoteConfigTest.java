package net.poquesoft.appio;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.poquesoft.appio.config.RemoteConfig;
import net.poquesoft.appio.view.listeners.SuccessErrorListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RemoteConfigTest {

    @Test
    public void getRemoteConfig() throws Exception {
        final Context context = InstrumentationRegistry.getContext();
        final Object syncObject = new Object();

        RemoteConfig.getInstance(context).getConfig("collections.json",
                "http://titan.poquesoft.net/r/c/collections.json",
                new SuccessErrorListener() {
                    @Override
                    public void onSuccess() {
                        String s = null;
                        try {
                            s = RemoteConfig.getInstance(context).getLocalFile("collection.json");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assertTrue(s.contains("ID_382"));
                        synchronized (syncObject){
                            syncObject.notify();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        assertTrue(false);
                        synchronized (syncObject){
                            syncObject.notify();
                        }
                    }
                });
        synchronized (syncObject){
            syncObject.wait();
        }
    }
}