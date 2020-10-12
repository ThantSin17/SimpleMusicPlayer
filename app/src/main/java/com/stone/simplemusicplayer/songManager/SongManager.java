package com.stone.simplemusicplayer.songManager;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.stone.simplemusicplayer.listener.CloseMusic;

public class SongManager {
    private static SongManager Instance;
    public static MediaPlayer mediaPlayer;
    public static SongManager getInstance() {
        if (Instance==null){
            Instance=new SongManager();
            mediaPlayer=new MediaPlayer();
        }
        return Instance;
    }

    public static void createSong(Context context, String url, CloseMusic closeMusic){
        mediaPlayer=MediaPlayer.create(context, Uri.parse(url));
        closeMusic.onCloseMusic(mediaPlayer);
    }
    public static void createSongs(Context context, String url){
        mediaPlayer=MediaPlayer.create(context, Uri.parse(url));
        //closeMusic.onCloseMusic(mediaPlayer);
    }
    public static void playSong(){

        //initializeSeekBar();
            mediaPlayer.start();

    }
    public static void closeSong(){

        mediaPlayer.release();
        mediaPlayer=null;
    }
}
