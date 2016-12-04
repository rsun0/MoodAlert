package io.raysun.moodalert;

import java.util.Date;
import java.util.List;

/**
 * An alert in the database.
 * @author Ray Sun
 */
public class DatabaseAlert {

    /**
     * The number of hours in a day.
     */
    public static final int HOURS_PER_DAY = 24;

    /**
     * The key to the alerts table.
     */
    public static final String KEY = "alerts";
    /**
     * The reference to the name field in the database.
     */
    public static final String NAME = "name";
    /**
     * The reference to the description field in the database.
     */
    public static final String DESCRIPTION = "description";
    /**
     * The reference to the timestamp field in the database.
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     * The reference to the receivers field in the databse.
     */
    public static final String RECEIVERS = "receivers";

    /**
     * The reference to the time field in Date.
     */
    public static final String DATE_TIME = "time";

    /**
     * The name of the subject person.
     */
    public String name;
    /**
     * The short description of mood.
     */
    public String description;
    /**
     * The timestamp.
     */
    public Date timestamp;
    /**
     * The usernames of users who receive this alert.
     */
    public List<String> receivers;

    /**
     * Constructor
     * @param name Subject name
     * @param description Short description
     * @param timestamp Timestamp
     * @param receivers Users to send to
     */
    public DatabaseAlert(String name, String description, Date timestamp, List<String> receivers) {
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.receivers = receivers;
    }

    /**
     * For Firebase.
     */
    public DatabaseAlert() {

    }
}
