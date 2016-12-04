package io.raysun.moodalert;

import java.util.Date;
import java.util.List;

/**
 * An alert in the database.
 * @author Ray Sun
 */
public class DatabaseAlert {
    /**
     * The name of the subject person.
     */
    public String name;
    /**
     * The timestamp.
     */
    public Date timestamp;
    /**
     * The short description of mood.
     */
    public String description;

    /**
     * The usernames of users who receive this alert.
     */
    public List<String> receivers;

    /**
     * Constructor
     * @param name Subject name
     * @param timestamp Timestamp
     * @param description Short description
     * @param receivers Users to send to
     */
    public DatabaseAlert(String name, Date timestamp, String description, List<String> receivers) {
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
        this.receivers = receivers;
    }

    /**
     * For Firebase.
     */
    public DatabaseAlert() {

    }
}
