package com.stone.simplemusicplayer.model;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private int currentPosition=-1;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
