package io.raysun.moodalert;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The screen for sending alerts.
 * @author Ray Sun
 */
public class AlertActivity extends AuthenticatedActivity implements AdapterView.OnItemSelectedListener {

    /**
     * The default option for the spinner.
     */
    private static final String SPINNER_DEFAULT = "Not a user";

    /**
     * Firebase authentication object.
     */
    private FirebaseAuth mAuth;
    /**
     * The app database.
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    /**
     * The users table.
     */
    private DatabaseReference mUsers = database.getReference("users");
    /**
     * The alerts table.
     */
    private DatabaseReference mAlerts = database.getReference("alerts");
    /**
     * The list of friend UIDs.
     */
    private List<String> friendUIDs;
    /**
     * The map from friend names to UIDs.
     */
    private Map<String, String> friendReference = new HashMap<>();
    /**
     * The friend UID to exclude from receiving.
     */
    private String excludedUid;

    /**
     * Initialize screen and records of friends.
     * @param savedInstanceState Unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        mAuth = FirebaseAuth.getInstance();

        Spinner spinner = (Spinner) findViewById(R.id.spinner_exclude);
        spinner.setOnItemSelectedListener(this);

        // Inflate spinner
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mAuth.getCurrentUser().getUid())
                        .hasChild(DatabaseUser.FRIENDS)) {
                    Object friendData = dataSnapshot.child(mAuth.getCurrentUser().getUid())
                            .child(DatabaseUser.FRIENDS).getValue();
                    friendUIDs = (List<String>) friendData;
                }
                else {
                    friendUIDs = new ArrayList<String>();
                }

                List<String> friendNames = new ArrayList<>(friendUIDs.size());
                for (String uid : friendUIDs) {
                    String name = (String) dataSnapshot.child(uid).child(DatabaseUser.NAME).getValue();
                    friendNames.add(name);

                    friendReference.put(name, uid);
                }

                friendNames.add(0, SPINNER_DEFAULT);
                Spinner spinner = (Spinner) findViewById(R.id.spinner_exclude);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlertActivity.this,
                        R.layout.spinner_item, friendNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Sets the subject.
     * @param parent The spinner
     * @param view The item view
     * @param pos The position of the item
     * @param id The row id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos == 0) {
            excludedUid = null;
            return;
        }

        String name = (String) parent.getItemAtPosition(pos);

        EditText nameInput = (EditText) findViewById(R.id.editText_name);
        nameInput.setText(name);

        excludedUid = friendReference.get(name);
    }

    /**
     * Removes the subject user.
     * @param parent The spinner
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        excludedUid = null;
        return;
    }

    /**
     * Send an alert.
     * @param v The send button
     */
    public void sendAlert(View v) {
        EditText nameInput = (EditText) findViewById(R.id.editText_name);
        EditText moodInput = (EditText) findViewById(R.id.editText_mood);
        String name = nameInput.getText().toString();
        String mood = moodInput.getText().toString();

        List<String> receivers = new ArrayList<>(friendUIDs);
        receivers.add(mAuth.getCurrentUser().getUid());
        if (excludedUid != null) {
            receivers.remove(excludedUid);
        }

        Date timestamp = Calendar.getInstance().getTime();

        DatabaseAlert alert = new DatabaseAlert(name, timestamp, mood, receivers);
        mAlerts.push().setValue(alert);
    }
}
