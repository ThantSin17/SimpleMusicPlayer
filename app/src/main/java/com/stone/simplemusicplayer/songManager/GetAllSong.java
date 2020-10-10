package com.stone.simplemusicplayer.songManager;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.stone.simplemusicplayer.data.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAllSong {
    final String MEDIA_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/";
    private List<Song> songList;
    public GetAllSong() {
        songList=new ArrayList<>();
    }


    private MediaMetadataRetriever metadataRetriever;
    public  List<Song> getSongLists(){
        File home=new File(Environment.getExternalStorageDirectory().getPath()+"/");
        File[] listFiles = home.listFiles();
        if (listFiles != null && listFiles.length>0) {

            for (File file : listFiles) {
                System.out.println(file.getAbsolutePath());
                if (file.isDirectory() && !file.getName().contains(".")) {
                    scanDirectory(file);

                } else {
                    addSong(file);
                }
            }
        }

        return songList;

    }

    private void addSong(File file) {

        if (file.getName().endsWith(".mp3")&& !file.getName().contains("@")){

            metadataRetriever=new MediaMetadataRetriever();
            metadataRetriever.setDataSource(file.getAbsolutePath());
            String title=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String singer=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            if (title==null)
                title=file.getName();

            if (singer==null)
                singer="unknown artist";


            songList.add(new Song(title,singer,file.getAbsolutePath(),false));
        }
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory() && !file.getName().contains(".")) {
                        scanDirectory(file);
                    } else {
                        addSong(file);
                    }

                }
            }
        }
    }
}
