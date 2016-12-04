package io.raysun.moodalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The home screen of the app after logging in.
 * @author Ray Sun
 */
public class HomeActivity extends AuthenticatedActivity {

    /**
     * The app database.
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    /**
     * The users table.
     */
    private DatabaseReference mAlerts = database.getReference(DatabaseAlert.KEY);
    /**
     * The list view adapter.
     */
    private AlertAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        listViewAdapter = new AlertAdapter(this, R.layout.alert);
        ListView listView = (ListView) findViewById(R.id.listView_alerts);
        listView.setAdapter(listViewAdapter);

        mAlerts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DatabaseAlert> alerts = new ArrayList<>();
                for (DataSnapshot alert : dataSnapshot.getChildren()) {
                    String name = (String) alert.child(DatabaseAlert.NAME).getValue();
                    String description = (String) alert.child(DatabaseAlert.DESCRIPTION).getValue();
                    List<String> receivers = (List<String>) alert.child(DatabaseAlert.RECEIVERS).getValue();

                    long time = (long) alert.child(DatabaseAlert.TIMESTAMP).
                            child(DatabaseAlert.DATE_TIME).getValue();
                    Calendar timestamp = Calendar.getInstance();
                    timestamp.setTime(new Date(time));

                    // Check if older than 24 hours
                    Calendar currentTime = Calendar.getInstance();
                    int diffDay = currentTime.get(Calendar.DAY_OF_YEAR) - timestamp.get(Calendar.DAY_OF_YEAR);
                    int diffHour = currentTime.get(Calendar.HOUR) - timestamp.get(Calendar.HOUR);
                    diffHour += diffDay * DatabaseAlert.HOURS_PER_DAY;
                    if (diffHour >= DatabaseAlert.HOURS_PER_DAY) {
                        continue;
                    }

                    if (receivers.contains(mAuth.getCurrentUser().getUid())) {
                        DatabaseAlert item = new DatabaseAlert(name, description, timestamp.getTime(), receivers);
                        alerts.add(item);
                    }
                }

                listViewAdapter.clear();
                listViewAdapter.addAll(alerts);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Update timestamps.
     */
    @Override
    public void onStart() {
        super.onStart();
        listViewAdapter.notifyDataSetChanged();
    }

    /**
     * Go to the send alert screen.
     * @param v The send alert button
     */
    public void sendAlert(View v) {
        Intent intent = new Intent(this, AlertActivity.class);
        startActivity(intent);
    }

    /**
     * Go to the friends screen.
     * @param v The friends button
     */
    public void viewFriends(View v) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }
}
