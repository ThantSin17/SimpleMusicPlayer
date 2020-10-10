package com.stone.simplemusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.stone.simplemusicplayer.adapter.MusicAdapter;
import com.stone.simplemusicplayer.data.Song;
import com.stone.simplemusicplayer.databinding.ActivityMainBinding;

import com.stone.simplemusicplayer.listener.CloseMusic;
import com.stone.simplemusicplayer.listener.ItemClickListener;
import com.stone.simplemusicplayer.model.MainActivityViewModel;
import com.stone.simplemusicplayer.songManager.GetAllSong;
import com.stone.simplemusicplayer.songManager.SongManager;



public class MainActivity extends AppCompatActivity implements ItemClickListener, CloseMusic {
    private ActivityMainBinding binding;
    private GetAllSong getAllSong;
    private MusicAdapter adapter;
    private SongManager manager;
    private MediaPlayer mediaPlayer;
    private MainActivityViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        mModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        getAllSong = new GetAllSong();

        manager = SongManager.getInstance();

        adapter = new MusicAdapter(this, this);
        adapter.setSongList(getAllSong.getSongLists());
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recycler.setAdapter(adapter);

    }

    @Override
    public void onItemClick(Song song, int position) {

        if (mModel.getCurrentPosition() == -1) {
            mModel.setCurrentPosition(position);


            manager.createSong(this, song.getUrl(), this);
            manager.playSong();
        }else if (position != mModel.getCurrentPosition() && mModel.getCurrentPosition() != -1) {

            mModel.setCurrentPosition(position);
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;

            }

            manager.createSong(this, song.getUrl(), this);
            manager.playSong();

        }

        startActivity(PlayerActivity.gotoPlayerActivity(this, song, mediaPlayer));
    }

    @Override
    public void clearAdapter() {
        binding.recycler.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCloseMusic(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}