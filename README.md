# Tech Prep

## App Demo Video
Click to watch a demo of the Tech Prep App on YouTube.

[![Alt text](https://img.youtube.com/vi/Oq0JdTPiH1w/0.jpg)](https://youtu.be/Oq0JdTPiH1w)

## Implementing UMD Fonts & Colors
Under the Attributes look for fontFamily, then hit the drop down button and all the imported UMD fonts will appear. __textbook.otf__ will be the default normal text. 

The UMD Maroon and Gold colors are implmented into the app. The id for the marroon and gold colors are:
```
@color/colorMaroon 
@color/colorGold
```
These can easily applied by adding it into the background section under the attributes of the button and or other item added.

## Changing A Blank Activity to Navigation Drawer Activity
1. Go into the activity layout xml file under the Res -> Layout folders.
2. Then in the second line of the xml file change the constrait layout to a drawer layout: 
	`android.support.v4.widget.DrawerLayout`
3. To add in the navigation drawer, then copy and paste this code right above the end tag for the DrawerLayout
```xml
    <android.support.design.widget.NavigationView
        android:id="@+id/navigation_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="@color/colorMaroon"
        app:menu="@menu/nav_menu"
        app:itemTextColor="@color/colorWhite"
        app:theme="@style/NavigationDrawerStyle">
    </android.support.design.widget.NavigationView>
```
4. Now move to the new activity's java file and add two private variables:
```java
private DrawerLayout mDrawerLayout;
private ActionBarDrawerToggle mToggle;
```
5. In the onCreate function add in this code:
```java
mDrawerLayout= (DrawerLayout) findViewById(R.id.nav_drawer);
mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
```
6. Have the public class also implement NavigationView.OnNavigationItemSelectedLister. Add this code to the defintion of the public class:
	`implements NavigationView.OnNavigationItemSelectedListener`
7. Then after the OnClick function add these additional functions:
```java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
        return false;
    }
```
8. Please check out the MainActivity's code to see where things are placed.

## Add Option/Link to the Menu
1. Go to nav_menu.xml in the Res -> Menu folder.
2. Add a new item with this code: 
```xml
<item android:id="@+id/id_name
	android:title="Page Name">
</item>
```
3. Add this code in the __onNaviagationItemSelected__ funtion in __ALL__ the activity pages. Make sure to use the new id that was declared in the nav_menu file. 
```java
if (id == R.id.id_name){
    Intent intent = new Intent(this, NEWACTIVITY.class);
    startActivity(intent);
}
```

__NOTE:__ That id's may not appear automatically. If this is occuring build and clean the project.

## Generating google-services.JSON Files
Everyone will have to generate their own google-services.JSON file in order to get the Google login working on their devicies (including emulators).

1. Go to https://console.developers.google.com

2. Go to Select a project > hit the + button to create a new project > name project and create

3. Go to https://console.firebase.google.com/u/0/
  ![Firebase](images/firebase.png)

4. Select "Add Project"

5. Select the project you just created

6. Name product (TechPrep)

7. From sidebar select "Authentication" -> Sign-in Method -> Google then enable. Once enabled select the hyperlink -> "Project Settings"

  1.  At the bottom select "Add Firebase To Android App"
    ![AddFirebase](images/firebase_add.png)

8. Package name: com.example.cs4531.interviewapp

9. For SHA-1 signing certificate: Open Android Studio > Select 'Gradle' from top right side bar > Select 'app(root)' > Select 'Android' > Select 'signingReport' > Copy and paste SHA1 key > Create > Done
  ![ ](https://i.stack.imgur.com/3QcBI.png)
  Image from: https://stackoverflow.com/questions/15727912/sha-1-fingerprint-of-keystore-certificate/15727931

10. In Firebase go to Settings > Project Settings > Add Firebase to you Adnroid App > Fill in fields > Register App > Download google-services.json 

11. Open Android > Project view > and delete existing google-services.json file from the app if it exists

12. Right click app[app-app] > New > File > name it google-services.json > copy and paste contents from downloaded file

## Useful Links
1. In-app video recording tutorial: https://www.youtube.com/watch?v=5sEeprYnHa8
2. Google login tutorial: https://www.youtube.com/watch?v=2PIaGpJMCNs
3. Navigation Drawer Tutorial: https://www.youtube.com/watch?v=NC3dM8qcpEM

## Future Improvements
1. Get video saving to account/database
2. Notification/option to change permissions on device for camera
3. Daily general interview tips (not CS related ex. don't wear jeans to an interview)
4. Continuously add questions to database via website
5. Method of detecting duplicate questions 

## Database Changes

If you are getting this project in the future, chances are the database you want to work with will not be active. This is what you will do in order to create a new one 

Here is a [handy dandy guide](https://code.tutsplus.com/tutorials/how-to-use-mongodb-stitch-in-android-apps--cms-31877) that will teach you how to set up a Mongodb Atlas account (That's what we used) and set it up with Stitch, which is an API to connect the cloud to the app


### Changing out database information in Android Studio
In line 15 of `strings.xml`, there is a string name called `my_app_id`, and you can change that out for the connection string that Atlas gives you