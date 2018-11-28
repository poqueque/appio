package net.poquesoft.appio;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.poquesoft.appio.config.RemoteConfig;
import net.poquesoft.appio.view.listeners.StringListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RemoteConfigTest {

    @Test
    public void getRemoteConfig() throws Exception {
        final Context context = InstrumentationRegistry.getContext();
        final Object syncObject = new Object();

        RemoteConfig remoteConfig = new RemoteConfig(context,"collections.json",
                "http://titan.poquesoft.net/r/c/collections.json",
                new StringListener() {
                    @Override
                    public void onAction(String string) {
                        assertTrue(string.contains("futbolpanini2018"));
                        synchronized (syncObject){
                            syncObject.notify();
                        }
                    }
                });
        remoteConfig.getRemoteConfig();
        synchronized (syncObject){
            syncObject.wait();
        }
        //Read local file
        String localFile = remoteConfig.getLocalFile();
        assertTrue(localFile.contains("futbolpanini2018"));
    }
}