package io.raysun.moodalert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * A custom adapter for viewing alerts.
 */
public class AlertAdapter extends ArrayAdapter<DatabaseAlert> {

    /**
     * The number of hours in a day.
     */
    private static final int HOURS_PER_DAY = 24;
    /**
     * The error message for an alert older than 24 hours.
     */
    private static final String OLD_ALERT_ERR = "Alert older than 24 hours in ListView adapter";

    /**
     * Constructor.
     * @param context The context
     * @param resource The resource
     */
    public AlertAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     * Get a view of the data set.
     * @param position The position
     * @param v The old view to reuse.
     * @param parent The parent of the view
     * @return The updated view
     */
    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.alert, null);
        }

        DatabaseAlert alertData = getItem(position);
        String subjectName = alertData.name;
        String description = alertData.description;
        Date timestampDate = alertData.timestamp;

        // Calculate relative time
        String relativeTimestamp;

        Calendar currentTime = Calendar.getInstance();
        Calendar timestamp = Calendar.getInstance();
        timestamp.setTime(timestampDate);
        int diffDay = currentTime.get(Calendar.DATE) - timestamp.get(Calendar.DATE);
        int diffHour = currentTime.get(Calendar.HOUR) - timestamp.get(Calendar.HOUR);
        int diffMinute = currentTime.get(Calendar.MINUTE) - timestamp.get(Calendar.MINUTE);
        int diffSecond = currentTime.get(Calendar.SECOND) - timestamp.get(Calendar.SECOND);
        diffHour += diffDay * HOURS_PER_DAY;
        if (diffHour >= HOURS_PER_DAY) {
            throw new RuntimeException(OLD_ALERT_ERR);
        }
        if (diffHour > 0) {
            relativeTimestamp = Integer.toString(diffHour) + R.string.hour_abbreviation;
        }
        else if (diffMinute > 0) {
            relativeTimestamp = Integer.toString(diffMinute) + R.string.minute_abbreviation;
        }
        else {
            relativeTimestamp = Integer.toString(diffSecond) + R.string.second_abbreviation;
        }
        relativeTimestamp += " " + R.string.timestamp_suffix;

        TextView nameView = (TextView) v.findViewById(R.id.textView_name);
        TextView descView = (TextView) v.findViewById(R.id.textView_description);
        TextView timeView = (TextView) v.findViewById(R.id.textView_timestamp);
        nameView.setText(subjectName);
        descView.setText(description);
        timeView.setText(relativeTimestamp);

        return v;
    }
}
