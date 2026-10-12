package edu.utexas.ece422c.minispotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Refactored MiniSpotify implementation for Assignment #3.
 *
 * The class name is retained from the starter project, but the implementation is
 * redesigned around indexed catalog access and collections whose semantics match
 * the specification.
 */
public class LegacyMusicService implements MusicService {
    /** ID lookup plus insertion-order catalog iteration. */
    private final Map<String, Song> songsById = new LinkedHashMap<>();

    /** Case-insensitive exact-search indexes. Bucket order is catalog insertion order. */
    private final Map<String, List<Song>> songsByArtist = new HashMap<>();
    private final Map<String, List<Song>> songsByGenre = new HashMap<>();

    /** Unique, case-sensitive username lookup. */
    private final Map<String, User> usersByName = new HashMap<>();

    /** Global play counts by case-sensitive song ID. Missing entries mean zero plays. */
    private final Map<String, Integer> playCounts = new HashMap<>();

    @Override
    public void addSong(Song song) {
        requireArgument(song, "song");
        if (songsById.containsKey(song.getId())) {
            throw new IllegalArgumentException("Duplicate song id: " + song.getId());
        }

        songsById.put(song.getId(), song);
        songsByArtist.computeIfAbsent(normalize(song.getArtist()), ignored -> new ArrayList<>()).add(song);
        songsByGenre.computeIfAbsent(normalize(song.getGenre()), ignored -> new ArrayList<>()).add(song);
    }

    @Override
    public Song getSong(String songId) {
        requireArgument(songId, "songId");
        return songsById.get(songId);
    }

    @Override
    public List<Song> searchByArtist(String artist) {
        requireArgument(artist, "artist");
        List<Song> matches = songsByArtist.get(normalize(artist));
        return matches == null ? List.of() : List.copyOf(matches);
    }

    @Override
    public List<Song> searchByGenre(String genre) {
        requireArgument(genre, "genre");
        List<Song> matches = songsByGenre.get(normalize(genre));
        return matches == null ? List.of() : List.copyOf(matches);
    }

    @Override
    public List<Song> searchByTitle(String query) {
        requireArgument(query, "query");
        String normalizedQuery = normalize(query);
        List<Song> matches = new ArrayList<>();
        for (Song song : songsById.values()) {
            if (normalize(song.getTitle()).contains(normalizedQuery)) {
                matches.add(song);
            }
        }
        return List.copyOf(matches);
    }

    @Override
    public void createUser(String username) {
        requireArgument(username, "username");
        if (usersByName.containsKey(username)) {
            throw new IllegalArgumentException("Duplicate user: " + username);
        }
        usersByName.put(username, new User(username));
    }

    @Override
    public void createPlaylist(String username, String playlistName) {
        User owner = requireUser(username);
        requireArgument(playlistName, "playlistName");

        if (owner.playlists().containsKey(playlistName)) {
            throw new IllegalArgumentException("Duplicate playlist: " + playlistName);
        }
        owner.playlists().put(playlistName, new PlaylistState(playlistName, username));
    }

    @Override
    public void addToPlaylist(String actingUser, String owner, String playlistName, String songId) {
        User actor = requireUser(actingUser);
        User ownerUser = requireUser(owner);
        requireArgument(playlistName, "playlistName");
        Song song = requireSong(songId);
        PlaylistState playlist = requirePlaylist(ownerUser, playlistName);

        requireCanModify(actor.getUsername(), playlist);
        playlist.addSong(song);
    }

    @Override
    public void removeFromPlaylist(String actingUser, String owner, String playlistName, String songId) {
        User actor = requireUser(actingUser);
        User ownerUser = requireUser(owner);
        requireArgument(playlistName, "playlistName");
        Song song = requireSong(songId);
        PlaylistState playlist = requirePlaylist(ownerUser, playlistName);

        requireCanModify(actor.getUsername(), playlist);
        if (!playlist.removeFirst(song)) {
            throw new IllegalArgumentException("Song is not in playlist: " + songId);
        }
    }

    @Override
    public List<Song> getPlaylist(String owner, String playlistName) {
        User ownerUser = requireUser(owner);
        requireArgument(playlistName, "playlistName");
        return requirePlaylist(ownerUser, playlistName).songSnapshot();
    }

    @Override
    public void addCollaborator(String actingUser, String owner, String playlistName, String collaborator) {
        User actor = requireUser(actingUser);
        User ownerUser = requireUser(owner);
        requireArgument(playlistName, "playlistName");
        User collaboratorUser = requireUser(collaborator);
        PlaylistState playlist = requirePlaylist(ownerUser, playlistName);

        if (!actor.getUsername().equals(ownerUser.getUsername())) {
            throw new IllegalArgumentException("Only the playlist owner may add collaborators");
        }
        if (collaboratorUser.getUsername().equals(ownerUser.getUsername())) {
            throw new IllegalArgumentException("The owner cannot be a collaborator of their own playlist");
        }

        playlist.addCollaborator(collaboratorUser.getUsername());
    }

    @Override
    public void followPlaylist(String username, String owner, String playlistName) {
        User follower = requireUser(username);
        User ownerUser = requireUser(owner);
        requireArgument(playlistName, "playlistName");
        PlaylistState playlist = requirePlaylist(ownerUser, playlistName);

        if (follower.getUsername().equals(ownerUser.getUsername())) {
            throw new IllegalArgumentException("A user may not follow their own playlist");
        }

        follower.followedPlaylists().add(playlist);
    }

    @Override
    public List<Playlist> getFollowedPlaylists(String username) {
        User user = requireUser(username);
        List<Playlist> result = new ArrayList<>(user.followedPlaylists().size());
        for (PlaylistState playlist : user.followedPlaylists()) {
            result.add(playlist.publicPlaylist());
        }
        return List.copyOf(result);
    }

    @Override
    public void addFavorite(String username, String songId) {
        User user = requireUser(username);
        Song song = requireSong(songId);
        user.favorites().add(song);
    }

    @Override
    public void removeFavorite(String username, String songId) {
        User user = requireUser(username);
        Song song = requireSong(songId);
        user.favorites().remove(song);
    }

    @Override
    public Set<Song> getFavorites(String username) {
        User user = requireUser(username);
        return Collections.unmodifiableSet(new LinkedHashSet<>(user.favorites()));
    }

    @Override
    public void playSong(String username, String songId) {
        User user = requireUser(username);
        Song song = requireSong(songId);

        user.recordPlay(song);
        playCounts.merge(song.getId(), 1, Integer::sum);
    }

    @Override
    public List<Song> getRecentlyPlayed(String username, int n) {
        User user = requireUser(username);
        requireNonNegative(n, "n");
        if (n == 0) {
            return List.of();
        }

        List<Song> result = new ArrayList<>(Math.min(n, User.HISTORY_LIMIT));
        Set<String> seenSongIds = new HashSet<>();
        Iterator<Song> iterator = user.recentHistory().descendingIterator();

        while (iterator.hasNext() && result.size() < n) {
            Song song = iterator.next();
            if (seenSongIds.add(song.getId())) {
                result.add(song);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public List<Song> getTopSongs(int n) {
        requireNonNegative(n, "n");
        if (n == 0 || songsById.isEmpty()) {
            return List.of();
        }

        List<Song> ranked = new ArrayList<>(songsById.values());
        ranked.sort(rankingComparator());
        int end = Math.min(n, ranked.size());
        return List.copyOf(ranked.subList(0, end));
    }

    @Override
    public List<Song> getRecommendations(String username, int n) {
        User user = requireUser(username);
        requireNonNegative(n, "n");
        if (n == 0 || user.favorites().isEmpty()) {
            return List.of();
        }

        Set<String> favoriteGenres = new HashSet<>();
        for (Song favorite : user.favorites()) {
            favoriteGenres.add(normalize(favorite.getGenre()));
        }

        List<Song> candidates = new ArrayList<>();
        Set<String> addedCandidateIds = new HashSet<>();
        for (String genre : favoriteGenres) {
            List<Song> songs = songsByGenre.get(genre);
            if (songs == null) {
                continue;
            }
            for (Song song : songs) {
                if (user.favorites().contains(song)) {
                    continue;
                }
                if (user.everPlayedSongIds().contains(song.getId())) {
                    continue;
                }
                if (addedCandidateIds.add(song.getId())) {
                    candidates.add(song);
                }
            }
        }

        candidates.sort(rankingComparator());
        int end = Math.min(n, candidates.size());
        return List.copyOf(candidates.subList(0, end));
    }

    private static void requireArgument(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }

    private static void requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must be nonnegative");
        }
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    private User requireUser(String username) {
        requireArgument(username, "username");
        User user = usersByName.get(username);
        if (user == null) {
            throw new IllegalArgumentException("Unknown user: " + username);
        }
        return user;
    }

    private Song requireSong(String songId) {
        requireArgument(songId, "songId");
        Song song = songsById.get(songId);
        if (song == null) {
            throw new IllegalArgumentException("Unknown song: " + songId);
        }
        return song;
    }

    private static PlaylistState requirePlaylist(User owner, String playlistName) {
        PlaylistState playlist = owner.playlists().get(playlistName);
        if (playlist == null) {
            throw new IllegalArgumentException("Unknown playlist: " + playlistName);
        }
        return playlist;
    }

    private static void requireCanModify(String actingUser, PlaylistState playlist) {
        if (!actingUser.equals(playlist.owner()) && !playlist.isCollaborator(actingUser)) {
            throw new IllegalArgumentException("User is not allowed to modify this playlist");
        }
    }

    private Comparator<Song> rankingComparator() {
        return (left, right) -> {
            int countOrder = Integer.compare(
                    playCounts.getOrDefault(right.getId(), 0),
                    playCounts.getOrDefault(left.getId(), 0));
            if (countOrder != 0) {
                return countOrder;
            }

            int titleOrder = String.CASE_INSENSITIVE_ORDER.compare(left.getTitle(), right.getTitle());
            if (titleOrder != 0) {
                return titleOrder;
            }

            return left.getId().compareTo(right.getId());
        };
    }
}
