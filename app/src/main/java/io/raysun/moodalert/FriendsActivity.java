package io.raysun.moodalert;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The screen for viewing and adding friends.
 * @author Ray Sun
 */
public class FriendsActivity extends AuthenticatedActivity {

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
     * The list view adapter.
     */
    private FriendAdapter listViewAdapter;
    /**
     * A map of all emails to UIDs.
     */
    private Map<String, String> userMap = new HashMap<>();
    /**
     * The current user.
     */
    private DatabaseUser currentUser;

    public FriendsActivity() {
    }

    /**
     * Initialize screen and friends list.
     * @param savedInstanceState Unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mAuth = FirebaseAuth.getInstance();

        listViewAdapter = new FriendAdapter(this, R.layout.friend);
        ListView listView = (ListView) findViewById(R.id.listView_friends);
        listView.setAdapter(listViewAdapter);

        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot userData = dataSnapshot.child(mAuth.getCurrentUser().getUid());

                // Update list view
                List<String> friendUIDs;
                if (userData.hasChild(DatabaseUser.FRIENDS)) {
                    friendUIDs = (List<String>) userData.child(DatabaseUser.FRIENDS).getValue();
                }
                else {
                    friendUIDs = new ArrayList<String>();
                }

                List<String> friendNames = new ArrayList<>(friendUIDs.size());
                for (String uid : friendUIDs) {
                    String name = (String) dataSnapshot.child(uid).child(DatabaseUser.NAME).getValue();
                    friendNames.add(name);
                }

                listViewAdapter.clear();
                listViewAdapter.addAll(friendNames);
                listViewAdapter.notifyDataSetChanged();

                // Update user map
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    userMap.put((String) user.child(DatabaseUser.EMAIL).getValue(), user.getKey());
                }

                // Update current user
                String name = (String) userData.child(DatabaseUser.NAME).getValue();
                String email = (String) userData.child(DatabaseUser.EMAIL).getValue();
                currentUser = new DatabaseUser(name, email, friendUIDs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add a friend.
     * @param v The add friend button
     */
    public void addFriend(View v) {
        EditText emailInput = (EditText) findViewById(R.id.editText_email);
        String friendEmail = emailInput.getText().toString();
        String friendUid = userMap.get(friendEmail);

        if (friendUid == null) {
            Toast.makeText(this, R.string.friend_not_found_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.addFriend(friendUid);
        mUsers.child(mAuth.getCurrentUser().getUid()).setValue(currentUser);
    }
}
