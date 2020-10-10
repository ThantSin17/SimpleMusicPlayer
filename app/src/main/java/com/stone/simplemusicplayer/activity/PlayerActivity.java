package com.stone.simplemusicplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.stone.simplemusicplayer.R;
import com.stone.simplemusicplayer.data.Song;
import com.stone.simplemusicplayer.databinding.ControllerLayoutBinding;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private ControllerLayoutBinding binding;
    private static Song playSong=new Song();
    private static MediaPlayer mediaPlayer;
    private Handler mHandler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ControllerLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.songTitle.setText(playSong.getTitle());
        binding.songSinger.setText(playSong.getSinger());
        initializeSeekBar();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource((playSong.getUrl()));
        try {
            byte[] art= retriever.getEmbeddedPicture();
            binding.songPhoto.setImageBitmap(BitmapFactory.decodeByteArray(art,0,art.length));

        }catch (Exception e){

         //   Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    binding.playPause.setImageResource(R.drawable.ic_play);
                    binding.playPause.setShapeType(1);
                }else {
                    mediaPlayer.start();
                    binding.playPause.setImageResource(R.drawable.ic_pause);
                    binding.playPause.setShapeType(0);
                }
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
        binding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                //Toast.makeText(PlayerActivity.this, ""+mediaPlayer.getCurrentPosition(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);

            }
        });

        binding.backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
    private void initializeSeekBar(){
        binding.seekbar.setMax(mediaPlayer.getDuration()/1000);
        binding.songEndTime.setText(getDurationTimer(mediaPlayer.getDuration()));
        PlayerActivity.this.runOnUiThread(new Runnable() {
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
    public static Intent gotoPlayerActivity(Context context, Song song,MediaPlayer player){
        playSong=song;
        mediaPlayer=player;
        return new Intent(context,PlayerActivity.class);
    }


}