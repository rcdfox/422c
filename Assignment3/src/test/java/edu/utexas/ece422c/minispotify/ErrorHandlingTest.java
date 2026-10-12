package edu.utexas.ece422c.minispotify;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ErrorHandlingTest {
    private MusicService service() {
        MusicService service = MusicServices.create();
        service.addSong(new Song("s", "Song", "Artist", "Rock", 100));
        service.createUser("alice");
        service.createUser("bob");
        service.createPlaylist("alice", "p");
        return service;
    }

    @Test
    void allMusicServiceNullArgumentsAreRejectedWithIllegalArgumentException() {
        MusicService s = service();
        assertThrows(IllegalArgumentException.class, () -> s.createPlaylist(null, "p"));
        assertThrows(IllegalArgumentException.class, () -> s.createPlaylist("alice", null));
        assertThrows(IllegalArgumentException.class, () -> s.addToPlaylist(null, "alice", "p", "s"));
        assertThrows(IllegalArgumentException.class, () -> s.addToPlaylist("alice", null, "p", "s"));
        assertThrows(IllegalArgumentException.class, () -> s.addToPlaylist("alice", "alice", null, "s"));
        assertThrows(IllegalArgumentException.class, () -> s.addToPlaylist("alice", "alice", "p", null));
        assertThrows(IllegalArgumentException.class, () -> s.removeFromPlaylist(null, "alice", "p", "s"));
        assertThrows(IllegalArgumentException.class, () -> s.getPlaylist(null, "p"));
        assertThrows(IllegalArgumentException.class, () -> s.getPlaylist("alice", null));
        assertThrows(IllegalArgumentException.class, () -> s.addCollaborator(null, "alice", "p", "bob"));
        assertThrows(IllegalArgumentException.class, () -> s.addCollaborator("alice", null, "p", "bob"));
        assertThrows(IllegalArgumentException.class, () -> s.addCollaborator("alice", "alice", null, "bob"));
        assertThrows(IllegalArgumentException.class, () -> s.addCollaborator("alice", "alice", "p", null));
        assertThrows(IllegalArgumentException.class, () -> s.followPlaylist(null, "alice", "p"));
        assertThrows(IllegalArgumentException.class, () -> s.followPlaylist("bob", null, "p"));
        assertThrows(IllegalArgumentException.class, () -> s.followPlaylist("bob", "alice", null));
        assertThrows(IllegalArgumentException.class, () -> s.getFollowedPlaylists(null));
        assertThrows(IllegalArgumentException.class, () -> s.addFavorite(null, "s"));
        assertThrows(IllegalArgumentException.class, () -> s.addFavorite("alice", null));
        assertThrows(IllegalArgumentException.class, () -> s.removeFavorite(null, "s"));
        assertThrows(IllegalArgumentException.class, () -> s.removeFavorite("alice", null));
        assertThrows(IllegalArgumentException.class, () -> s.getFavorites(null));
        assertThrows(IllegalArgumentException.class, () -> s.playSong(null, "s"));
        assertThrows(IllegalArgumentException.class, () -> s.playSong("alice", null));
        assertThrows(IllegalArgumentException.class, () -> s.getRecentlyPlayed(null, 1));
        assertThrows(IllegalArgumentException.class, () -> s.getRecommendations(null, 1));
    }

    @Test
    void unknownReferencesFailClearlyExceptGetSong() {
        MusicService s = service();
        assertThrows(IllegalArgumentException.class, () -> s.createPlaylist("missing", "p"));
        assertThrows(IllegalArgumentException.class, () -> s.addFavorite("missing", "s"));
        assertThrows(IllegalArgumentException.class, () -> s.addFavorite("alice", "missing"));
        assertThrows(IllegalArgumentException.class, () -> s.playSong("alice", "missing"));
        assertThrows(IllegalArgumentException.class, () -> s.followPlaylist("bob", "alice", "missing"));
        org.junit.jupiter.api.Assertions.assertNull(s.getSong("missing"));
    }
}
