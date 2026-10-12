package edu.utexas.ece422c.minispotify;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistCollaborationFollowTest {
    private MusicService service() {
        MusicService service = MusicServices.create();
        service.addSong(new Song("a", "A", "Artist", "Rock", 100));
        service.addSong(new Song("b", "B", "Artist", "Rock", 100));
        service.addSong(new Song("c", "C", "Artist", "Rock", 100));
        service.createUser("alice");
        service.createUser("bob");
        service.createUser("carol");
        return service;
    }

    @Test
    void usernamesAreUniqueButCaseSensitive() {
        MusicService service = service();
        assertThrows(IllegalArgumentException.class, () -> service.createUser("alice"));
        assertDoesNotThrow(() -> service.createUser("Alice"));
        assertThrows(IllegalArgumentException.class, () -> service.createUser(null));
    }

    @Test
    void playlistNamesAreUniqueOnlyWithinOwner() {
        MusicService service = service();
        service.createPlaylist("alice", "Running");
        assertThrows(IllegalArgumentException.class, () -> service.createPlaylist("alice", "Running"));
        assertDoesNotThrow(() -> service.createPlaylist("bob", "Running"));
        assertDoesNotThrow(() -> service.createPlaylist("alice", "running"));
    }

    @Test
    void playlistPreservesOrderAllowsDuplicatesAndRemovesFirstOccurrence() {
        MusicService service = service();
        service.createPlaylist("alice", "p");
        service.addToPlaylist("alice", "alice", "p", "a");
        service.addToPlaylist("alice", "alice", "p", "b");
        service.addToPlaylist("alice", "alice", "p", "a");
        service.addToPlaylist("alice", "alice", "p", "c");

        assertEquals(List.of("a", "b", "a", "c"), ids(service.getPlaylist("alice", "p")));
        service.removeFromPlaylist("alice", "alice", "p", "a");
        assertEquals(List.of("b", "a", "c"), ids(service.getPlaylist("alice", "p")));

        assertThrows(IllegalArgumentException.class,
                () -> service.removeFromPlaylist("alice", "alice", "p", "a-missing"));
        service.removeFromPlaylist("alice", "alice", "p", "b");
        assertThrows(IllegalArgumentException.class,
                () -> service.removeFromPlaylist("alice", "alice", "p", "b"));
    }

    @Test
    void ownerAndCollaboratorMayModifyButOthersMayNot() {
        MusicService service = service();
        service.createPlaylist("alice", "shared");

        assertThrows(IllegalArgumentException.class,
                () -> service.addToPlaylist("bob", "alice", "shared", "a"));

        service.addCollaborator("alice", "alice", "shared", "bob");
        service.addCollaborator("alice", "alice", "shared", "bob"); // duplicate permission is a no-op
        service.addToPlaylist("bob", "alice", "shared", "a");
        service.addToPlaylist("alice", "alice", "shared", "b");
        service.removeFromPlaylist("bob", "alice", "shared", "a");
        assertEquals(List.of("b"), ids(service.getPlaylist("alice", "shared")));

        assertThrows(IllegalArgumentException.class,
                () -> service.addToPlaylist("carol", "alice", "shared", "c"));
        assertThrows(IllegalArgumentException.class,
                () -> service.removeFromPlaylist("carol", "alice", "shared", "b"));
    }

    @Test
    void onlyOwnerMayGrantCollaborationAndOwnerCannotCollaborateWithSelf() {
        MusicService service = service();
        service.createPlaylist("alice", "shared");
        service.addCollaborator("alice", "alice", "shared", "bob");

        assertThrows(IllegalArgumentException.class,
                () -> service.addCollaborator("bob", "alice", "shared", "carol"));
        assertThrows(IllegalArgumentException.class,
                () -> service.addCollaborator("carol", "alice", "shared", "bob"));
        assertThrows(IllegalArgumentException.class,
                () -> service.addCollaborator("alice", "alice", "shared", "alice"));
        assertThrows(IllegalArgumentException.class,
                () -> service.addCollaborator("alice", "alice", "shared", "nobody"));
    }

    @Test
    void followingIsUniqueCrossOwnerOnlyAndReturnsSafePlaylistDescriptors() {
        MusicService service = service();
        service.createPlaylist("alice", "mix");
        service.createPlaylist("bob", "mix");

        service.followPlaylist("bob", "alice", "mix");
        service.followPlaylist("bob", "alice", "mix");
        List<Playlist> follows = service.getFollowedPlaylists("bob");

        assertEquals(1, follows.size());
        assertEquals("alice", follows.get(0).getOwner());
        assertEquals("mix", follows.get(0).getName());
        assertThrows(UnsupportedOperationException.class,
                () -> follows.add(new Playlist("other", "carol")));

        assertThrows(IllegalArgumentException.class,
                () -> service.followPlaylist("alice", "alice", "mix"));
    }

    @Test
    void playlistAndFollowCollectionsDoNotExposeMutableInternalState() {
        MusicService service = service();
        service.createPlaylist("alice", "p");
        service.addToPlaylist("alice", "alice", "p", "a");

        List<Song> returned = service.getPlaylist("alice", "p");
        assertThrows(UnsupportedOperationException.class, returned::clear);
        assertEquals(List.of("a"), ids(service.getPlaylist("alice", "p")));
    }

    @Test
    void unknownAndNullPlaylistArgumentsFailClearly() {
        MusicService service = service();
        assertThrows(IllegalArgumentException.class, () -> service.createPlaylist("nobody", "p"));
        assertThrows(IllegalArgumentException.class, () -> service.createPlaylist("alice", null));
        assertThrows(IllegalArgumentException.class,
                () -> service.getPlaylist("alice", "missing"));
        assertThrows(IllegalArgumentException.class,
                () -> service.addToPlaylist(null, "alice", "p", "a"));
    }

    private static List<String> ids(List<Song> songs) {
        return songs.stream().map(Song::getId).toList();
    }
}
