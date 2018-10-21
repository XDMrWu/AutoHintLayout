package wulinpeng.com.hintlayout;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AutoHintLayout mHintLayout;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHintLayout = (AutoHintLayout) findViewById(R.id.hint_layout);

        mHintLayout.setHint("hint", false);

        handler = new Handler(getMainLooper());

        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mHintLayout.setHint("提示语", true);
                    }
                });
            }
        }, 1000, 1000);
    }
}
