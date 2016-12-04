package io.raysun.moodalert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriendsActivity extends AuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }
}
