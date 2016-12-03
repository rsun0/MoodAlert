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

/**
 * Startup screen for signing in.
 * @author Ray Sun
 */
public class SignInActivity extends AppCompatActivity {

    /**
     * The name of the extra for the user ID.
     */
    public static final String UID_EXTRA = "uid";

    /**
     * Firebase authentication object.
     */
    private FirebaseAuth mAuth;
    /**
     * Firebase authentication listener.
     */
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * Initialize screen and authentication.
     * @param savedInstanceState Unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra(UID_EXTRA, user.getUid());
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
     * Sign a user in.
     * @param v The sign in button
     */
    public void signIn(View v) {
        EditText emailInput = (EditText) findViewById(R.id.editText_email);
        EditText passwordInput = (EditText) findViewById(R.id.editText_password);
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
//                            Toast.makeText(SignInActivity.this, R.string.auth_failed_toast,
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Go to the register screen.
     * @param v The register button
     */
    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
