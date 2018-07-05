package net.poquesoft.appio.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.poquesoft.appio.R;

public class SplashAppioActivity extends AppioActivity {

    private static final String TAG = "SplashAppioActivity";
    private static final String SELECTED_ITEM = "arg_selected_item";

    private ImageView imageView;
    private TextView message;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindViews();
    }

    private void bindViews() {
        imageView = findViewById(R.id.image);
        message = findViewById(R.id.splash_message);
        progress = findViewById(R.id.progress);
    }

    protected synchronized void setImage(Drawable drawable){
        imageView.setImageDrawable(drawable);
    }

    protected synchronized void setMessage(final String messageText){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message.setText(messageText);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
