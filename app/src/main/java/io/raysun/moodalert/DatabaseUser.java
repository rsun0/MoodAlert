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
     * The reference to the email in the database.
     */
    public static final String EMAIL = "email";
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
     * A list of the UIDs of friends.
     */
    public List<String> friends;

    /**
     * Constructor
     * @param name The real name
     * @param email The email
     */
    public DatabaseUser(String name, String email) {
        this(name, email, new ArrayList<String>());
    }

    /**
     * Constructor
     * @param name The name
     * @param email The email
     * @param friends The list of friend UIDs
     */
    public DatabaseUser(String name, String email, List<String> friends) {
        this.name = name;
        this.email = email;
        this.friends = friends;
    }

    /**
     * For Firebase.
     */
    public DatabaseUser() {

    }

    /**
     * Add a friend.
     * @param uid The UID of the friend
     */
    public void addFriend(String uid) {
        friends.add(uid);
    }
}
