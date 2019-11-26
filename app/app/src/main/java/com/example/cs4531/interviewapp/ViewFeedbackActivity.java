package com.example.cs4531.interviewapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.stitch.android.core.Stitch.hasAppClient;

public class ViewFeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TextView userSubmissionText;
    private EditText idNumberText;
    private String userEmail;
    public RestRequests requests; //our RestRequests class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.nav_drawer);
        mToggle = new ActionBarDrawerToggle(ViewFeedbackActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        requests = RestRequests.getInstance(getApplicationContext());
        idNumberText = (EditText) findViewById(R.id.id_number);

        userSubmissionText = findViewById(R.id.userSubmission_text);
        userSubmissionText.setMovementMethod(new ScrollingMovementMethod());

        userEmail = DataStore.getInstance().getEmail();

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

        dataDetails.count(new Document("email", userEmail))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Long numDocs = task.getResult();
                        collectionSize = numDocs.intValue();
                        numDocs = null;
                    } else {
                        Log.e("app", "Failed to count documents with exception: ", task.getException());
                    }
                });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataDetails.find(eq("email", userEmail)).projection(new Document()
                .append("_id", 0)
                .append("question_id", 1)
                .append("student", 1)
                .append("question", 1)
                .append("submission", 1)
                .append("feedback", 1))
                .sort(new Document()
                        .append("question_id", 1))
                .forEach(document -> {
                    try {
                        saveData(document
                                .getInteger("question_id"), document
                                .getString("student"), document
                                .getString("question"), document
                                .getString("submission"), document
                                .getString("feedback"),
                                collectionSize);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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


    public void onBackButtonClick(View view) {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }


    public void saveData(Integer s1, String s2, String s3, String s4, String s5, int collectionSize) throws InterruptedException {
        questionId.add(s1);
        studentName.add(s2);
        question.add(s3);
        submission.add(s4);
        feedback.add(s5);

        Thread.sleep(100);

        if (questionId.size() == collectionSize) {
            printData();
        }
    }

    public void printData() {
        for (int i = 0; i < questionId.size(); i++) {
            userSubmissionText.append(
                    "Question ID: " + Integer.toString(questionId.get(i))
                            + "\n\n" +
                            "Student Name: " + studentName.get(i)
                            + "\n\n" +
                            "Question: " + question.get(i)
                            + "\n\n" +
                            "Submission: " + submission.get(i)
                            + "\n\n" +
                            "Admin Feedback: " + feedback.get(i)
                            + "\n\n");
            collectionSize = 0;
        }
    }


    ArrayList<Integer> questionId = new ArrayList<>();
    ArrayList<String> studentName = new ArrayList<>();
    ArrayList<String> question = new ArrayList<>();
    ArrayList<String> submission = new ArrayList<>();
    ArrayList<String> feedback = new ArrayList<>();
    int collectionSize;

}
