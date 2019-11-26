package com.example.cs4531.interviewapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import static com.mongodb.stitch.android.core.Stitch.hasAppClient;


public class GiveFeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Integer questionId;
    private EditText feedbackText;
    private String feedback;
    public RestRequests requests; //our RestRequests class
    public RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.nav_drawer);
        mToggle = new ActionBarDrawerToggle(GiveFeedbackActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        requests = RestRequests.getInstance(getApplicationContext());

        questionId = DataStore.getInstance().getQuestionId();

        feedbackText = (EditText) findViewById(R.id.feedbackText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        feedbackText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                feedback = s.toString();
            }
        });

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
        if(id == R.id.nav_adminHome){
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_userSubmissions){
            Intent intent = new Intent(this, UserSubmissionActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_myAccount){
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }
        return false;
    }

    public void submitFeedbackToDatabase(View view) {

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

        RemoteMongoCollection<Document> dataCollection = db.getCollection("TestData");

        Document filterDoc = new Document().append("question_id", questionId);
        Document updateDoc = new Document().append("$set",
                new Document().append("feedback", feedback));

        final Task<RemoteUpdateResult> updateTask = dataCollection.updateOne(filterDoc, updateDoc);

        updateTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long numMatched = task.getResult().getMatchedCount();
                long numModified = task.getResult().getModifiedCount();
                Log.d("app", String.format("successfully matched %d and modified %d documents",
                        numMatched, numModified));
            } else {
                Log.e("app", "failed to update document with: ", task.getException());
            }
        });

        submitFeedbackRatingToDatabase(view);

        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    public void submitFeedbackRatingToDatabase(View view) {
        double ratingValue = (double) ratingBar.getRating();

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

        RemoteMongoCollection<Document> dataCollection = db.getCollection("TestData");

        Document filterDoc = new Document().append("question_id", questionId);
        Document updateDoc = new Document().append("$set",
                new Document().append("feedback_rating", ratingValue));

        final Task<RemoteUpdateResult> updateTask = dataCollection.updateOne(filterDoc, updateDoc);

        updateTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long numMatched = task.getResult().getMatchedCount();
                long numModified = task.getResult().getModifiedCount();
                Log.d("app", String.format("successfully matched %d and modified %d documents",
                        numMatched, numModified));
            } else {
                Log.e("app", "failed to update document with: ", task.getException());
            }
        });
    }

}