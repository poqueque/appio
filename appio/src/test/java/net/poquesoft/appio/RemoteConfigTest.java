package net.poquesoft.appio;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import net.poquesoft.appio.config.RemoteConfig;
import net.poquesoft.appio.view.listeners.SuccessErrorListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RemoteConfigTest {

    @Test
    public void getRemoteConfig() throws Exception {
        final Context context = InstrumentationRegistry.getContext();

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
                    }

                    @Override
                    public void onError(String error) {
                        assertTrue(false);
                    }
                });
    }
}