package com.stone.simplemusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.stone.simplemusicplayer.databinding.ControllerLayoutBinding;

public class TestingPlayer extends AppCompatActivity {

    private ControllerLayoutBinding binding;
    private static  MediaPlayer mediaPlayer;
    private Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ControllerLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initializeSeekBar();
        // mediaPlayer.start();



        binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else mediaPlayer.start();
            }
        });
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b)
                    mediaPlayer.seekTo(i*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
    private void initializeSeekBar(){
        binding.seekbar.setMax(mediaPlayer.getDuration()/1000);
        binding.songEndTime.setText(getDurationTimer(mediaPlayer.getDuration()));
        TestingPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){

                    binding.songCurrentTime.setText(getDurationTimer(mediaPlayer.getCurrentPosition()));
//                    binding.songCurrentTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(currentDuration),
//                            TimeUnit.MILLISECONDS.toSeconds(currentDuration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration))));
                    int currentPosition=mediaPlayer.getCurrentPosition()/1000;
                    binding.seekbar.setProgress(currentPosition);
                }
                mHandler.postDelayed(this,1000);
            }
        });

    }
    public String getDurationTimer(long l){
        final long minutes=(l/1000)/60;
        final int seconds= (int) ((l/1000)%60);
        return minutes+ ":"+seconds;
    }
    public static Intent gotoPlayerActivity(Context context, MediaPlayer Player){
        mediaPlayer=Player;
        return new Intent(context,TestingPlayer.class);
    }


}