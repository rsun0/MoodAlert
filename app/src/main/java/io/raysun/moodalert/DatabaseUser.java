package io.raysun.moodalert;

import java.util.ArrayList;
import java.util.List;

/**
 * A user object in the database.
 * @author Ray Sun
 */

public class DatabaseUser {
    /**
     * The reference to the name in the database.
     */
    public static final String NAME = "name";
    /**
     * The reference to the friends list in the database.
     */
    public static final String FRIENDS = "friends";

    /**
     * The user's real name.
     */
    public String name;
    /**
     * The user's email.
     */
    public String email;
    /**
     * A list of the usernames of friends.
     */
    public List<String> friends;

    /**
     * Constructor
     * @param name The real name
     * @param email The email
     */
    public DatabaseUser(String name, String email) {
        this.name = name;
        this.email = email;
        friends = new ArrayList<String>();
    }

    /**
     * For Firebase.
     */
    public DatabaseUser() {

    }
}
