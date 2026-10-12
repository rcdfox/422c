package edu.utexas.ece422c.minispotify;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FavoritesHistoryTest {
    @Test
    void favoritesAreUniqueAndRemovalOfMissingKnownFavoriteIsNoOp() {
        MusicService service = MusicServices.create();
        service.addSong(new Song("a", "A", "Artist", "Rock", 100));
        service.addSong(new Song("b", "B", "Artist", "Rock", 100));
        service.createUser("u");

        service.addFavorite("u", "a");
        service.addFavorite("u", "a");
        assertEquals(Set.of("a"), ids(service.getFavorites("u")));

        assertDoesNotThrow(() -> service.removeFavorite("u", "b"));
        service.removeFavorite("u", "a");
        assertTrue(service.getFavorites("u").isEmpty());

        assertThrows(IllegalArgumentException.class, () -> service.addFavorite("u", "missing"));
        assertThrows(IllegalArgumentException.class, () -> service.removeFavorite("u", "missing"));
        assertThrows(IllegalArgumentException.class, () -> service.addFavorite("missing", "a"));
    }

    @Test
    void favoritesReturnedToCallerCannotMutateServiceState() {
        MusicService service = MusicServices.create();
        Song a = new Song("a", "A", "Artist", "Rock", 100);
        service.addSong(a);
        service.createUser("u");
        service.addFavorite("u", "a");

        Set<Song> favorites = service.getFavorites("u");
        assertThrows(UnsupportedOperationException.class, favorites::clear);
        assertEquals(Set.of("a"), ids(service.getFavorites("u")));
    }

    @Test
    void historyBoundaryAt49_50And51PlaysEvictsOldest() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        for (int i = 1; i <= 51; i++) {
            String id = "s" + i;
            service.addSong(new Song(id, "Song " + i, "Artist", "Rock", 100));
        }

        for (int i = 1; i <= 49; i++) {
            service.playSong("u", "s" + i);
        }
        assertEquals(49, service.getRecentlyPlayed("u", 100).size());
        assertEquals("s49", service.getRecentlyPlayed("u", 1).get(0).getId());

        service.playSong("u", "s50");
        assertEquals(50, service.getRecentlyPlayed("u", 100).size());
        assertEquals("s1", service.getRecentlyPlayed("u", 100).get(49).getId());

        service.playSong("u", "s51");
        List<String> recent = service.getRecentlyPlayed("u", 100).stream().map(Song::getId).toList();
        assertEquals(50, recent.size());
        assertEquals("s51", recent.get(0));
        assertEquals("s2", recent.get(49));
        assertFalse(recent.contains("s1"));
    }

    @Test
    void recentlyPlayedIsUniqueAndMostRecentOccurrenceWins() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        for (String id : List.of("A", "B", "C", "D")) {
            service.addSong(new Song(id, id, "Artist", "Rock", 100));
        }

        for (String id : List.of("A", "B", "C", "A", "D", "B")) {
            service.playSong("u", id);
        }

        assertEquals(List.of("B", "D", "A", "C"),
                service.getRecentlyPlayed("u", 10).stream().map(Song::getId).toList());
        assertEquals(List.of("B", "D"),
                service.getRecentlyPlayed("u", 2).stream().map(Song::getId).toList());
        assertTrue(service.getRecentlyPlayed("u", 0).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> service.getRecentlyPlayed("u", -1));
    }

    @Test
    void recentlyPlayedReturnedListIsNotARepresentationLeak() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        service.addSong(new Song("a", "A", "Artist", "Rock", 100));
        service.playSong("u", "a");

        List<Song> recent = service.getRecentlyPlayed("u", 10);
        assertThrows(UnsupportedOperationException.class, recent::clear);
        assertEquals("a", service.getRecentlyPlayed("u", 10).get(0).getId());
    }

    private static Set<String> ids(Set<Song> songs) {
        return songs.stream().map(Song::getId).collect(java.util.stream.Collectors.toSet());
    }
}
