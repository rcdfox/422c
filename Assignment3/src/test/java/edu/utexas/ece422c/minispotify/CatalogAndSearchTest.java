package edu.utexas.ece422c.minispotify;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CatalogAndSearchTest {
    private MusicService serviceWithCatalog() {
        MusicService service = MusicServices.create();
        service.addSong(new Song("s1", "Midnight Drive", "The Echoes", "Rock", 180));
        service.addSong(new Song("s2", "Blue Midnight", "the echoes", "Jazz", 200));
        service.addSong(new Song("s3", "Sunrise", "Other Artist", "ROCK", 210));
        service.addSong(new Song("S1", "Case Sensitive ID", "Other Artist", "Pop", 120));
        return service;
    }

    @Test
    void songLookupAndDuplicateRules() {
        MusicService service = serviceWithCatalog();
        assertEquals("Midnight Drive", service.getSong("s1").getTitle());
        assertEquals("Case Sensitive ID", service.getSong("S1").getTitle());
        assertNull(service.getSong("missing"));
        assertThrows(IllegalArgumentException.class,
                () -> service.addSong(new Song("s1", "Duplicate", "A", "B", 1)));
    }

    @Test
    void songConstructorValidatesRequiredFieldsAndDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Song(null, "T", "A", "G", 1));
        assertThrows(IllegalArgumentException.class, () -> new Song(" ", "T", "A", "G", 1));
        assertThrows(IllegalArgumentException.class, () -> new Song("x", null, "A", "G", 1));
        assertThrows(IllegalArgumentException.class, () -> new Song("x", "T", null, "G", 1));
        assertThrows(IllegalArgumentException.class, () -> new Song("x", "T", "A", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Song("x", "T", "A", "G", 0));
        assertThrows(IllegalArgumentException.class, () -> new Song("x", "T", "A", "G", -1));
    }

    @Test
    void artistAndGenreSearchAreCaseInsensitiveAndPreserveInsertionOrder() {
        MusicService service = serviceWithCatalog();

        assertEquals(List.of("s1", "s2"),
                service.searchByArtist("THE ECHOES").stream().map(Song::getId).toList());
        assertEquals(List.of("s1", "s3"),
                service.searchByGenre("rock").stream().map(Song::getId).toList());
        assertTrue(service.searchByArtist("Nobody").isEmpty());
        assertTrue(service.searchByGenre("Nobody").isEmpty());
    }

    @Test
    void titleSearchIsCaseInsensitiveSubstringSearchInInsertionOrder() {
        MusicService service = serviceWithCatalog();
        assertEquals(List.of("s1", "s2"),
                service.searchByTitle("MIDNIGHT").stream().map(Song::getId).toList());
        assertEquals(List.of("s1", "s2", "s3", "S1"),
                service.searchByTitle("").stream().map(Song::getId).toList());
        assertTrue(service.searchByTitle("not present").isEmpty());
    }

    @Test
    void nullCatalogArgumentsAreRejected() {
        MusicService service = serviceWithCatalog();
        assertThrows(IllegalArgumentException.class, () -> service.addSong(null));
        assertThrows(IllegalArgumentException.class, () -> service.getSong(null));
        assertThrows(IllegalArgumentException.class, () -> service.searchByArtist(null));
        assertThrows(IllegalArgumentException.class, () -> service.searchByGenre(null));
        assertThrows(IllegalArgumentException.class, () -> service.searchByTitle(null));
    }
}
