package io.raysun.moodalert;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Screen for registering a new user.
 * @author Ray Sun
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * Firebase authentication object.
     */
    private FirebaseAuth mAuth;
    /**
     * Firebase authentication listener.
     */
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * The app database.
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    /**
     * The users table.
     */
    private DatabaseReference mUsers = database.getReference("users");

    /**
     * Initalize screen and authentication listeners.
     * @param savedInstanceState Unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    /**
     * Start listening to authentication.
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Stop listening to authentication.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Register a new user.
     * @param v The register button
     */
    public void register(View v) {
        EditText nameInput = (EditText) findViewById(R.id.editText_name);
        EditText emailInput = (EditText) findViewById(R.id.editText_email);
        EditText passwordInput = (EditText) findViewById(R.id.editText_password);
        final EditText confirmInput = (EditText) findViewById(R.id.editText_passwordConfirm);
        final String name = nameInput.getText().toString();
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirm = confirmInput.getText().toString();

        if (name.length() == 0 || email.length() == 0 || password.length() == 0 || confirm.length() == 0) {
            Toast.makeText(RegisterActivity.this, R.string.register_err_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, R.string.mismatch_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.register_err_toast,
                                    Toast.LENGTH_SHORT).show();
                        }
                        addUserToDatabase(task.getResult().getUser().getUid(), name, email);
                    }
                });
    }

    /**
     * Add a new user to the users database under their UID.
     * @param uid The UID to add under
     * @param name The name to add
     * @param email The email to add
     */
    private void addUserToDatabase(String uid, String name, String email) {
        DatabaseUser user = new DatabaseUser(name, email);
        mUsers.child(uid).setValue(user);
    }
}
