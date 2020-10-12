package com.stone.simplemusicplayer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import com.stone.simplemusicplayer.R;
import com.stone.simplemusicplayer.adapter.MusicAdapter;
import com.stone.simplemusicplayer.data.Song;
import com.stone.simplemusicplayer.databinding.ActivityMainBinding;
import com.stone.simplemusicplayer.listener.GetCurrentPosition;
import com.stone.simplemusicplayer.listener.ItemClickListener;
import com.stone.simplemusicplayer.model.MainActivityViewModel;
import com.stone.simplemusicplayer.songManager.GetAllSong;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


public class MainActivity extends AppCompatActivity implements ItemClickListener, GetCurrentPosition {
    private MusicAdapter adapter;
    private MainActivityViewModel mModel;
    private List<Song> songInfo=new ArrayList<>();
    private ActivityMainBinding binding;
    private boolean isPause=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        mModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        GetAllSong getAllSong = new GetAllSong();
        songInfo= getAllSong.getSongLists();


        adapter = new MusicAdapter(this, this);
        adapter.setSongList(songInfo);
        binding.songSize.setText(songInfo.size()+" songs");
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recycler.setAdapter(adapter);
        binding.playerPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPause){
                    TestingPlayer.play();
                    binding.playerPlayPause.setImageResource(R.drawable.ic_pause);
                    isPause=false;
                }else {
                    isPause=true;
                    binding.playerPlayPause.setImageResource(R.drawable.ic_play);
                    TestingPlayer.pause();
                }
            }
        });
        binding.playerForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestingPlayer.playNext(MainActivity.this);
            }
        });
        binding.playerBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestingPlayer.playBack(MainActivity.this);
            }
        });
        binding.playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestingPlayer.gotoPlayerActivity(MainActivity.this,songInfo,mModel.getCurrentPosition(),MainActivity.this));
            }
        });


    }

    @Override
    public void onItemClick(Song song, int position) {
        setupPlayer(position);
        mModel.setCurrentPosition(position);
        startActivity(TestingPlayer.gotoPlayerActivity(this,songInfo,position,this));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
//        if (preferences.getInt("curPos",-1)!=-1){
//            adapter.setSelectedPosition(preferences.getInt("curPos",-1));
//        }
        if (TestingPlayer.isPlaying()!=-1){
            adapter.setSelectedPosition(TestingPlayer.isPlaying());
            setupPlayer(TestingPlayer.isPlaying());
        }else {
            binding.playerLayout.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    public void getCurrentPosition(int pos) {
        mModel.setCurrentPosition(pos);
        adapter.setSelectedPosition(pos);
        setupPlayer(pos);
    }
    public void setupPlayer(int pos){
        binding.playerLayout.setVisibility(View.VISIBLE);
        binding.playerTitle.setText(songInfo.get(pos).getTitle());
        binding.playerSinger.setText(songInfo.get(pos).getSinger());

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}