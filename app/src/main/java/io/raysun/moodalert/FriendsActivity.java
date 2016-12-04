package io.raysun.moodalert;

import android.os.Bundle;
import android.provider.ContactsContract;
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
     * A map of all UIDs to users.
     */
    private Map<String, DatabaseUser> userMap = new HashMap<>();
    /**
     * A map of all emails to UIDs.
     */
    private Map<String, String> emailMap = new HashMap<>();
    /**
     * A map of all names to UIDs.
     */
    private Map<String, String> nameMap = new HashMap<>();
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
                    String name = (String) user.child(DatabaseUser.NAME).getValue();
                    String email = (String) user.child(DatabaseUser.EMAIL).getValue();

                    nameMap.put(name, user.getKey());
                    emailMap.put(email, user.getKey());

                    List<String> friends;
                    if (user.hasChild(DatabaseUser.FRIENDS)) {
                        friends = (List<String>) user.child(DatabaseUser.FRIENDS).getValue();
                    }
                    else {
                        friends = new ArrayList<String>();
                    }

                    userMap.put(user.getKey(), new DatabaseUser(name, email, friends));
                }

                // Update current user
                String name = (String) userData.child(DatabaseUser.NAME).getValue();
                String email = (String) userData.child(DatabaseUser.EMAIL).getValue();
                currentUser = new DatabaseUser(name, email, friendUIDs);

//                for (String uid : friendUIDs) {
//                    String friendName = (String) dataSnapshot.child(uid).child(DatabaseUser.NAME).getValue();
//                    String friendEmail = (String) dataSnapshot.child(uid).child(DatabaseUser.EMAIL).getValue();
//                    List<String> friendFriends = (List<String>) dataSnapshot.child(uid).
//                            child(DatabaseUser.FRIENDS).getValue();
//                    userMap.put(uid, new DatabaseUser(friendName, friendEmail, friendFriends));
//                }
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
        String friendUid = emailMap.get(friendEmail);

        if (friendUid == null) {
            Toast.makeText(this, R.string.friend_not_found_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.addFriend(friendUid);
        DatabaseUser friend = userMap.get(friendUid);
        friend.addFriend(mAuth.getCurrentUser().getUid());
        mUsers.child(mAuth.getCurrentUser().getUid()).setValue(currentUser);
        mUsers.child(friendUid).setValue(friend);
    }

    /**
     * Removes a friend.
     * @param name The name of the friend to remove.
     */
    public void removeFriend(String name) {
        String friendUid = nameMap.get(name);

        currentUser.removeFriend(friendUid);
        DatabaseUser friend = userMap.get(friendUid);
        friend.removeFriend(mAuth.getCurrentUser().getUid());
        mUsers.child(mAuth.getCurrentUser().getUid()).setValue(currentUser);
        mUsers.child(friendUid).setValue(friend);
    }
}
