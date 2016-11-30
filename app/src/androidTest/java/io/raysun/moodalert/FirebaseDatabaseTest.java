package io.raysun.moodalert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Tests Firebase Realtime Database.
 * @author Ray Sun
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseDatabaseTest {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mUsers = database.getReference("users");
    private DatabaseReference mAlerts = database.getReference("alerts");

    /**
     * Writes a new user to the database.
     * @throws Exception Test failure
     */
    @Test
    public void writeUser() throws Exception {
        DatabaseUser user0 = new DatabaseUser("Jack Maxler");
        DatabaseUser user1 = new DatabaseUser("Martin Luthor");
        user0.friends.add("user1");
        user1.friends.add("user0");

        final CountDownLatch writeSignal = new CountDownLatch(2);

        mUsers.child("user0").setValue(user0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        mUsers.child("user1").setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
    }

    /**
     * Writes a new alert to the database.
     * @throws Exception Test failure
     */
    @Test
    public void writeAlert() throws Exception {
        String[] receiversArray = {"user0", "user1"};
        List<String> receivers = Arrays.asList(receiversArray);
        DatabaseAlert alert = new DatabaseAlert("Thomas Cooper",
                Calendar.getInstance().getTime(), "Sick today", receivers);

        final CountDownLatch writeSignal = new CountDownLatch(1);

        mAlerts.push().setValue(alert).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
