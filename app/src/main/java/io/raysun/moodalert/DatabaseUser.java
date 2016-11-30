package io.raysun.moodalert;

import java.util.ArrayList;
import java.util.List;

/**
 * A user object in the database.
 * @author Ray Sun
 */

public class DatabaseUser {
    /**
     * The user's real name.
     */
    public String name;
    /**
     * A list of the usernames of friends.
     */
    public List<String> friends;

    /**
     * Constructor
     * @param name The real name
     */
    public DatabaseUser(String name) {
        this.name = name;
        friends = new ArrayList<String>();
    }

    /**
     * For Firebase.
     */
    public DatabaseUser() {

    }
}
