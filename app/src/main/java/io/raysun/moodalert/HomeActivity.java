package io.raysun.moodalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * The home screen of the app after logging in.
 * @author Ray Sun
 */
public class HomeActivity extends AuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
