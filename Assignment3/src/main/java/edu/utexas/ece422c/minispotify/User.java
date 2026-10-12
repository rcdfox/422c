package edu.utexas.ece422c.minispotify;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** Internal user state. */
public final class User {
    static final int HISTORY_LIMIT = 50;

    private final String username;
    private final Map<String, PlaylistState> playlists = new HashMap<>();
    private final Set<Song> favorites = new LinkedHashSet<>();
    private final Deque<Song> recentHistory = new ArrayDeque<>();
    private final Set<String> everPlayedSongIds = new HashSet<>();
    private final Set<PlaylistState> followedPlaylists = new LinkedHashSet<>();

    public User(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username cannot be null");
        }
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    Map<String, PlaylistState> playlists() {
        return playlists;
    }

    Set<Song> favorites() {
        return favorites;
    }

    Deque<Song> recentHistory() {
        return recentHistory;
    }

    Set<String> everPlayedSongIds() {
        return everPlayedSongIds;
    }

    Set<PlaylistState> followedPlaylists() {
        return followedPlaylists;
    }

    void recordPlay(Song song) {
        if (recentHistory.size() == HISTORY_LIMIT) {
            recentHistory.removeFirst();
        }
        recentHistory.addLast(song);
        everPlayedSongIds.add(song.getId());
    }
}
