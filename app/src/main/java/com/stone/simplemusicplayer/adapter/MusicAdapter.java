package com.stone.simplemusicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stone.simplemusicplayer.R;
import com.stone.simplemusicplayer.data.Song;
import com.stone.simplemusicplayer.databinding.MusicItemLayoutBinding;
import com.stone.simplemusicplayer.listener.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Song> songList;
    private Context context;
    private ItemClickListener listener;
    private int selectedPosition = -1;

    public MusicAdapter(Context context, ItemClickListener listener) {
        songList = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MusicViewHolder(MusicItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MusicViewHolder holder, final int position) {
        final Song song = songList.get(position);
        holder.bind(song);
        holder.binding.musicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(song, position);
                selectedPosition = 0;
                selectedPosition = position;

                notifyDataSetChanged();

                listener.onItemClick(song, position);

            }
        });

        if (selectedPosition == position) {
            holder.binding.basinImagePlay.setVisibility(View.GONE);
            holder.binding.basinImagePause.setVisibility(View.VISIBLE);
            holder.binding.musicCardView.setCardBackgroundColor(Color.parseColor("#66000000"));

        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        MusicItemLayoutBinding binding;

        public MusicViewHolder(@NonNull MusicItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }


        public void bind(Song song) {
            binding.musicTitle.setText(song.getTitle());
            binding.musicSinger.setText(song.getSinger());
            binding.basinImagePlay.setVisibility(View.VISIBLE);
            binding.basinImagePause.setVisibility(View.GONE);
            binding.musicCardView.setCardBackgroundColor(Color.parseColor("#202328"));

        }
    }
}
