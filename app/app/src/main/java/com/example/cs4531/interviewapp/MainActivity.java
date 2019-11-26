package com.example.cs4531.interviewapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.stitch.android.core.Stitch.hasAppClient;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LogIn logIn;
    private String userEmail;
    int dataSetSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.nav_drawer);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * @author smatthys
     * @param item
     * This function allows the menu toggle button and other menu buttons
     * properly function when clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @author smatthys
     * @param item
     * This function takes a boolean value to transition between different activities.
     * It holds all the logic necessary for the navigation side bar.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_recordVideo){
            Intent intent = new Intent(this, RecordVideoActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_flashcards){
            Intent intent = new Intent(this, FlashcardsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_resources){
            Intent intent = new Intent(this, ResourcesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_myAccount){
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }
        return false;
    }

    /**
     * @author ghutch
     * @param myView
     * This function transitions to RecordVideoActivity
     */
    public void switchToVideo(View myView) {
        Intent myIntent = new Intent(this, RecordVideoActivity.class);
        startActivity(myIntent);
    }

    /**
     * @author ghutch
     * @param myView
     * This function transitions to FlashcardsActivity
     */
    public void switchToFlashcards(View myView) {
        Intent myIntent = new Intent(this, FlashcardsActivity.class);
        startActivity(myIntent);
    }


    /**
     * @author David
     * @param myView
     * This function transitions to ViewFeedbackActivity
     */
    public void switchToViewFeedback(View myView) {
        Intent myIntent = new Intent(this, ViewFeedbackActivity.class);
        myIntent.putExtra("email", userEmail);
        startActivity(myIntent);
    }

    public void addData(View myView) {
        if (hasAppClient(getString(R.string.my_app_id))) {
        } else {
            Stitch.initializeDefaultAppClient(
                    getString(R.string.my_app_id)
            );
        }

        StitchAppClient stitchAppClient = Stitch.getDefaultAppClient();

        stitchAppClient.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(new Continuation<StitchUser, Task<Void>>() {
            @Override
            public Task<Void> then(@com.mongodb.lang.NonNull Task<StitchUser> task) throws Exception {
                if (task.isSuccessful()) {
                    Log.d("stitch", "logged in anonymously as user " + task.getResult());
                } else {
                    Log.e("stitch", "failed to log in anonymously", task.getException());
                }
                return null;
            }
        });

        RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(
                RemoteMongoClient.factory,
                "mongodb-atlas"
        );

        RemoteMongoDatabase db = mongoClient.getDatabase("TechPrep");

        RemoteMongoCollection<Document> dataDetails = db.getCollection("TestData");

        dataDetails.count(new Document())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Long numDocs = task.getResult();
                        dataSetSize = numDocs.intValue();



                        Document newItem = new Document()
                                .append("student_id", "1")
                                .append("email", "gagli032@d.umn.edu")
                                .append("question_id", ++dataSetSize)
                                .append("student", "David Gagliardi")
                                .append("question", "Test question?")
                                .append("submission", "Test Answer")
                                .append("feedback", "")
                                .append("feedback_rating", 0.0);


                        final Task<RemoteInsertOneResult> insertTask = dataDetails.insertOne(newItem);
                        insertTask.addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("app", String.format("successfully inserted item with id %s",
                                        task1.getResult().getInsertedId()));

                                Toast toast = Toast.makeText(getApplicationContext(),"Addition Successful",Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Log.e("app", "failed to insert document with: ", task1.getException());

                                Toast toast = Toast.makeText(getApplicationContext(),"Error: Addition Failed",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    } else {
                        Log.e("app", "Failed to count documents with exception: ", task.getException());
                    }
                });

        dataSetSize = 0;
    }

}