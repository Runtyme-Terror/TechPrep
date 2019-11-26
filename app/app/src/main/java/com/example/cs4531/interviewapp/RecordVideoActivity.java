package com.example.cs4531.interviewapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class RecordVideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Button recordView, playView;
    private VideoView viewOfVideo;
    private TextView questionView;
    private int ACTIVITY_START_CAMERA_APP = 0;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public RestRequests requests; //our RestRequests class


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        recordView = findViewById(R.id.recordButton);
        playView =  findViewById(R.id.playbackButton);
        viewOfVideo = findViewById(R.id.videoView);
        questionView = findViewById(R.id.questionView);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.nav_drawer);
        mToggle = new ActionBarDrawerToggle(RecordVideoActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        requests = RestRequests.getInstance(getApplicationContext());
                recordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent playBackIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(playBackIntent, ACTIVITY_START_CAMERA_APP);
            }
        });

                playView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOfVideo.start();
            }
        });
        getQuestion(questionView);

    }

    public void getQuestion(View v)
    {
        String targetURL = getString(R.string.serverURL) + "/getTechnical";
        StringRequest sr = new StringRequest(Request.Method.GET, targetURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GET", response.toString());
                        questionView.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        questionView.setText(error.toString());
                    }
                });
        requests.addToRequestQueue(sr);
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

    //displays toast saying whether video was saved or not
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            viewOfVideo.setVideoURI(videoUri);
            Toast.makeText(getApplicationContext(), "Video has been saved", Toast.LENGTH_LONG).show();
        }

       if(resultCode != RESULT_OK) {
           Toast.makeText(getApplicationContext(), "ERROR video not saved", Toast.LENGTH_LONG).show();
       }
    }
}