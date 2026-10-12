package edu.utexas.ece422c.minispotify;

/**
 * Immutable song value object. Song identity is the case-sensitive song ID.
 */
public final class Song {
    private final String id;
    private final String title;
    private final String artist;
    private final String genre;
    private final int durationSeconds;

    public Song(String id, String title, String artist, String genre, int durationSeconds) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must be non-null and non-blank");
        }
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null");
        }
        if (artist == null) {
            throw new IllegalArgumentException("artist cannot be null");
        }
        if (genre == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("durationSeconds must be positive");
        }

        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Song song && id.equals(song.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return title + " - " + artist + " [" + id + "]";
    }
}
