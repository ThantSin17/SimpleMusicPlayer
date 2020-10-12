package com.stone.simplemusicplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.stone.simplemusicplayer.R;
import com.stone.simplemusicplayer.data.Song;
import com.stone.simplemusicplayer.databinding.ControllerLayoutBinding;
import com.stone.simplemusicplayer.listener.CloseMusic;
import com.stone.simplemusicplayer.listener.GetCurrentPosition;
import com.stone.simplemusicplayer.songManager.SongManager;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class TestingPlayer extends AppCompatActivity implements CloseMusic {

    private ControllerLayoutBinding binding;
    private static int position = -1;
    static SongManager manager = SongManager.getInstance();
    private static MediaPlayer mediaPlayer;
    private static List<Song> songs = new ArrayList<>();
    private Handler mHandler = new Handler();
    private static  int currPos=-1;

    private static GetCurrentPosition listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ControllerLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());




        try {


            if (mediaPlayer!=null){
                if (position !=currPos) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    position=currPos;
                    manager.createSong(this, songs.get(position).getUrl(), this);
                    manager.playSong();
                }
            }else {
                manager.createSong(this, songs.get(position).getUrl(), this);
                manager.playSong();
            }


        }catch (Exception e){
            Toast.makeText(this, e.getMessage() + position, Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, position+""+currPos, Toast.LENGTH_LONG).show();

        binding.songTitle.setText(songs.get(position).getTitle());
        binding.songSinger.setText(songs.get(position).getSinger());
        initializeSeekBar();

        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource((songs.get(position).getUrl()));

        try {
            byte[] art = retriever.getEmbeddedPicture();
            binding.songPhoto.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.length));
        }catch (Exception ignored){

        }


        binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    binding.playPause.setImageResource(R.drawable.ic_play);
                    binding.playPause.setShapeType(1);
                } else {
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
                    mediaPlayer.seekTo(i * 1000);
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
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }


                ++position;
                recreate();
                listener.getCurrentPosition(position);
               // playNext();
               // recreate();

            }
        });
        binding.backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }


                --position;
                recreate();
                listener.getCurrentPosition(position);

            }
        });

        binding.backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void initializeSeekBar() {
        binding.seekbar.setMax(mediaPlayer.getDuration() / 1000);
        binding.songEndTime.setText(getDurationTimer(mediaPlayer.getDuration()));
        TestingPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {

                    binding.songCurrentTime.setText(getDurationTimer(mediaPlayer.getCurrentPosition()));
//                    binding.songCurrentTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(currentDuration),
//                            TimeUnit.MILLISECONDS.toSeconds(currentDuration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration))));
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekbar.setProgress(currentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

    }

    public String getDurationTimer(long l) {
        final long minutes = (l / 1000) / 60;
        final int seconds = (int) ((l / 1000) % 60);
        return minutes + ":" + seconds;
    }

    public static Intent gotoPlayerActivity(Context context, List<Song> list, int pos, GetCurrentPosition currentPosition) {

        try {

            if (position == -1) {
                position = pos;
            }
            currPos=pos;
            songs = list;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("error",e.getMessage());
            Log.d("error",e.getMessage());
        }
        listener=currentPosition;

        return new Intent(context, TestingPlayer.class);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    @Override
    public void onCloseMusic(MediaPlayer Player) {
        mediaPlayer = Player;
    }
    public static int isPlaying(){
        if (mediaPlayer!=null){
            return position;
        }else return -1;

    }
    public static void pause(){
        mediaPlayer.pause();
    }
    public static void play(){
        mediaPlayer.start();
    }
    public static void playNext(Context context){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        ++position;
        listener.getCurrentPosition(position);
        mediaPlayer=MediaPlayer.create(context, Uri.parse(songs.get(position).getUrl()));
        mediaPlayer.start();

    }
    public static void playBack(Context context){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }


        --position;
        listener.getCurrentPosition(position);
        mediaPlayer=MediaPlayer.create(context, Uri.parse(songs.get(position).getUrl()));
        mediaPlayer.start();

    }

}