package com.ivkeapp.adjustabletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextView, logTextView;
    boolean isCounterActive;
    Button startButton, logButton;
    CountDownTimer countDownTimer;
    long timeLeft;
    String mTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekBar = findViewById(R.id.seekBar);
        timerTextView = findViewById(R.id.text_view_time);
        startButton = findViewById(R.id.button_start);
        logButton = findViewById(R.id.button_log_time);
        logTextView = findViewById(R.id.text_view_log);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        mTimeLeft = "";

        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void buttonClicked (View v) {

        if (isCounterActive) {

            resetTimer();

        } else {

            isCounterActive = true;
            timerSeekBar.setEnabled(false);
            startButton.setText("stop");

            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000, 100) {

                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);
                    timeLeft = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.foghorn);
                    mPlayer.start();
                    resetTimer();
                    timeLeft = 0;
                    mTimeLeft = "";
                }
            }.start();
        }
    }

    private void updateTimer (int seconds){
        int minutes = seconds / 60;
        int second = seconds - (minutes * 60);

        String mSeconds = String.valueOf(second);
        if(second <= 9)
            mSeconds = "0" + mSeconds;

        timerTextView.setText(minutes + ":" + mSeconds);
    }

    private void resetTimer (){
        timerTextView.setText("0:30");
        timerSeekBar.setProgress(30);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();
        startButton.setText("start");
        isCounterActive = false;
        timeLeft = 0;
        logTextView.setText("");
        mTimeLeft = "";
    }

    public void takeValue (View v){
        if(timeLeft > 0) {
            mTimeLeft += System.getProperty("line.separator") + timeLeft + " milliseconds";
            logTextView.setText(mTimeLeft);
        } else {
            logTextView.setText("");
            Toast.makeText(this, "Times up! Please restart timer.", Toast.LENGTH_SHORT).show();
        }
    }
}
