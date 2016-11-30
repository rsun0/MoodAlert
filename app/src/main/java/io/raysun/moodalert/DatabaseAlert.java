package io.raysun.moodalert;

import java.util.Date;
import java.util.List;

/**
 * An alert in the database.
 * @author Ray Sun
 */

public class DatabaseAlert {
    public String name;
    public Date timestamp;
    public String description;

    /**
     * The usernames of users who receive this alert.
     */
    public List<String> receivers;

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
