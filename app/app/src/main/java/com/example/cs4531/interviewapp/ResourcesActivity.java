package com.example.cs4531.interviewapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;

public class ResourcesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.nav_drawer);
        mToggle = new ActionBarDrawerToggle(ResourcesActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    //clickable links that open the browser which navigates to them
    public void linkCareer(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://d.umn.edu/career-internship-services"));
        startActivity(browserIntent);
    }

    public void linkCS(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://scse.d.umn.edu/about/departments-and-programs/computer-science-department/faculty-staff"));
        startActivity(browserIntent);
    }

    public void linkHandbook(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://d.umn.edu/career-internship-services/career-handbook"));
        startActivity(browserIntent);
    }

    public void linkMeet(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://d.umn.edu/career-internship-services/about/events/drop-hours"));
        startActivity(browserIntent);
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
}
