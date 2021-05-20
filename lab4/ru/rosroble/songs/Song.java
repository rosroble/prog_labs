package ru.rosroble.songs;

import ru.rosroble.animals.Animal;
import ru.rosroble.exceptions.InvalidParameterException;

public class Song {
    private final String name;
    private final Animal artist;
    private final int duration;
    private Volume volume;

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public Animal getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public static class Builder {
        private String name = "Unnamed";
        private Animal artist = null;
        private int duration = 0;
        private Volume volume = null;

        public Builder duration(int duration) {
            if (duration <= 0) {
                throw new InvalidParameterException("Song duration should be greater than zero");
            }
            this.duration = duration;
            return this;
        }
        public Builder name(String name) {
            if (name == null) {
                throw new InvalidParameterException("A song cannot have a null name");
            }
            this.name = name;
            return this;
        }
        public Builder artist(Animal artist) {
            if (artist == null) {
                throw new InvalidParameterException("Artist cannot be a null");
            }
            this.artist = artist;
            return this;
        }

        public Builder volume(Volume volume) {
            this.volume = volume;
            return this;
        }

        public Song build() {
            return new Song(this);
        }

    }

    private Song(Builder builder) {
        this.name = builder.name;
        this.artist = builder.artist;
        this.duration = builder.duration;
        this.volume = builder.volume;
    }

}
