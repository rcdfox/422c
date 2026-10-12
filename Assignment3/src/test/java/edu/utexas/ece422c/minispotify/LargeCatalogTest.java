package edu.utexas.ece422c.minispotify;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LargeCatalogTest {
    @Test
    void suppliedHundredThousandSongCatalogLoadsAndIndexesCorrectly() {
        assertTimeout(Duration.ofSeconds(20), () -> {
            MusicService service = MusicServices.create();
            try (InputStream in = getClass().getResourceAsStream("/minispotify_catalog_100000.csv")) {
                assertNotNull(in);
                List<Song> songs = CatalogLoader.load(in);
                assertEquals(100_000, songs.size());
                for (Song song : songs) {
                    service.addSong(song);
                }
            }

            assertEquals("Quiet Harbor 000001", service.getSong("S000001").getTitle());
            assertEquals("Wild Arcade 100000", service.getSong("S100000").getTitle());

            List<Song> artistMatches = service.searchByArtist("quiet atlas 0242");
            assertFalse(artistMatches.isEmpty());
            assertEquals("S000001", artistMatches.get(0).getId());
            assertTrue(artistMatches.stream().allMatch(song -> song.getArtist().equalsIgnoreCase("Quiet Atlas 0242")));

            List<Song> reggae = service.searchByGenre("REGGAE");
            assertFalse(reggae.isEmpty());
            assertEquals("S000005", reggae.get(0).getId());

            for (int i = 1; i <= 10_000; i++) {
                String id = String.format("S%06d", ((i * 37) % 100_000) + 1);
                assertNotNull(service.getSong(id));
            }
            for (int i = 0; i < 1_000; i++) {
                assertFalse(service.searchByArtist("Quiet Atlas 0242").isEmpty());
            }
        });
    }
}
