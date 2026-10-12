package edu.utexas.ece422c.minispotify;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RankingRecommendationTest {
    @Test
    void topSongsIncludesZeroPlaySongsAndUsesAllTieBreakers() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        service.addSong(new Song("id4", "Zulu", "A", "Rock", 100));
        service.addSong(new Song("id3", "beta", "A", "Rock", 100));
        service.addSong(new Song("id2", "Alpha", "A", "Rock", 100));
        service.addSong(new Song("id1", "alpha", "A", "Rock", 100));

        // All zero plays: title (case-insensitive), then ID.
        assertEquals(List.of("id1", "id2", "id3", "id4"), ids(service.getTopSongs(10)));

        service.playSong("u", "id4");
        service.playSong("u", "id3");
        service.playSong("u", "id3");
        assertEquals(List.of("id3", "id4", "id1", "id2"), ids(service.getTopSongs(10)));

        assertTrue(service.getTopSongs(0).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> service.getTopSongs(-1));
    }

    @Test
    void topSongsReturnedListCannotMutateInternalState() {
        MusicService service = MusicServices.create();
        service.addSong(new Song("a", "A", "A", "Rock", 100));
        List<Song> top = service.getTopSongs(1);
        assertThrows(UnsupportedOperationException.class, top::clear);
        assertEquals("a", service.getTopSongs(1).get(0).getId());
    }

    @Test
    void recommendationsUseFavoriteGenresAndExcludeFavoritesAndEverPlayedSongs() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        service.createUser("listener");

        add(service, "favRock", "Favorite Rock", "Rock");
        add(service, "favJazz", "Favorite Jazz", "jAzZ");
        add(service, "rockPlayed", "Played", "ROCK");
        add(service, "rockHigh", "Zeta", "Rock");
        add(service, "rockLow", "Alpha", "rock");
        add(service, "jazzHigh", "Beta", "JAZZ");
        add(service, "pop", "Pop Song", "Pop");

        service.addFavorite("u", "favRock");
        service.addFavorite("u", "favJazz");
        service.playSong("u", "rockPlayed");

        // Give candidate songs distinct global counts using a different user.
        play(service, "listener", "rockHigh", 4);
        play(service, "listener", "jazzHigh", 3);
        play(service, "listener", "rockLow", 1);
        play(service, "listener", "pop", 20);

        assertEquals(List.of("rockHigh", "jazzHigh", "rockLow"),
                ids(service.getRecommendations("u", 10)));
        assertEquals(List.of("rockHigh", "jazzHigh"), ids(service.getRecommendations("u", 2)));
        assertTrue(service.getRecommendations("u", 0).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> service.getRecommendations("u", -1));
    }

    @Test
    void recommendationsExcludeSongsThatHaveFallenOutOfRecentHistory() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        add(service, "fav", "Favorite", "Rock");
        add(service, "old", "Old Played Song", "Rock");
        add(service, "candidate", "Candidate", "Rock");
        service.addFavorite("u", "fav");

        service.playSong("u", "old");
        for (int i = 0; i < 50; i++) {
            String id = "other" + i;
            add(service, id, "Other " + i, "Pop");
            service.playSong("u", id);
        }

        assertFalse(ids(service.getRecentlyPlayed("u", 50)).contains("old"));
        assertEquals(List.of("candidate"), ids(service.getRecommendations("u", 10)));
    }

    @Test
    void recommendationsUseCaseInsensitiveTitleThenIdForTies() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        add(service, "fav", "Favorite", "Rock");
        add(service, "b", "alpha", "Rock");
        add(service, "a", "Alpha", "Rock");
        add(service, "c", "Beta", "Rock");
        service.addFavorite("u", "fav");

        assertEquals(List.of("a", "b", "c"), ids(service.getRecommendations("u", 10)));
    }

    @Test
    void userWithNoFavoritesGetsNoRecommendations() {
        MusicService service = MusicServices.create();
        service.createUser("u");
        add(service, "a", "A", "Rock");
        assertTrue(service.getRecommendations("u", 10).isEmpty());
    }

    private static void add(MusicService service, String id, String title, String genre) {
        service.addSong(new Song(id, title, "Artist", genre, 100));
    }

    private static void play(MusicService service, String username, String songId, int count) {
        for (int i = 0; i < count; i++) {
            service.playSong(username, songId);
        }
    }

    private static List<String> ids(List<Song> songs) {
        List<String> ids = new ArrayList<>();
        for (Song song : songs) {
            ids.add(song.getId());
        }
        return ids;
    }
}
