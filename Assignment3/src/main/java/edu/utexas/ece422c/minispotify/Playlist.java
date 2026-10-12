package edu.utexas.ece422c.minispotify;

import java.util.Objects;

/**
 * Immutable public playlist descriptor.
 *
 * Mutable playlist state is intentionally kept inside the service so a Playlist
 * returned to a caller cannot be used to modify songs or permissions.
 */
public final class Playlist {
    private final String name;
    private final String owner;

    public Playlist(String name, String owner) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (owner == null) {
            throw new IllegalArgumentException("owner cannot be null");
        }
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Playlist playlist)) {
            return false;
        }
        return name.equals(playlist.name) && owner.equals(playlist.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }

    @Override
    public String toString() {
        return owner + "/" + name;
    }
}
