package edu.utexas.ece422c.minispotify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Internal mutable state associated with an immutable public Playlist descriptor. */
final class PlaylistState {
    private final Playlist playlist;
    private final List<Song> songs = new ArrayList<>();
    private final Set<String> collaborators = new HashSet<>();

    PlaylistState(String name, String owner) {
        this.playlist = new Playlist(name, owner);
    }

    Playlist publicPlaylist() {
        return playlist;
    }

    String owner() {
        return playlist.getOwner();
    }

    void addSong(Song song) {
        songs.add(song);
    }

    boolean removeFirst(Song song) {
        return songs.remove(song);
    }

    List<Song> songSnapshot() {
        return List.copyOf(songs);
    }

    boolean isCollaborator(String username) {
        return collaborators.contains(username);
    }

    void addCollaborator(String username) {
        collaborators.add(username);
    }
}
