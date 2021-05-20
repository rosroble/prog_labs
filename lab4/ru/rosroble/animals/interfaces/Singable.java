package ru.rosroble.animals.interfaces;

import ru.rosroble.songs.Song;

public interface Singable {
    public void sing(Song song, ISingMode singMode);
}
