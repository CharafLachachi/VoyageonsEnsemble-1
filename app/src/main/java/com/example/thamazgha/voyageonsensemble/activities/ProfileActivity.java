package com.example.thamazgha.voyageonsensemble.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.thamazgha.voyageonsensemble.activities.MainActivity.SHARED_PREFS;

public class ProfileActivity extends AppCompatActivity {
    private JWT token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

      //  Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = new JWT(sharedPreferences.getString("token", ""));
       // token = (JWT) intent.getExtras().get(EXTRA_MESSAGE);
        //  Toast.makeText(ProfileActivity.this, ""+(token == null), Toast.LENGTH_SHORT).show();
        BottomNavigationView bottomNav = findViewById(R.id.prof_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            if(ProfileFragment.token == null)
                ProfileFragment.set_token(token);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ProfileFragment.getInstance()).commit();

        }

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_profile_info:
                            if(ProfileFragment.token == null)
                                ProfileFragment.set_token(token);
                            selectedFragment = ProfileFragment.getInstance();
                            break;
                        case R.id.nav_personnal_posts:
                            if(PersonnalPostsFragment.token == null)
                                PersonnalPostsFragment.set_token(token);
                            selectedFragment = PersonnalPostsFragment.getInstance();
                            break;
                        case R.id.nav_posts_of_interest:
                            if(PostsOfInterestFragment.token == null)
                                PostsOfInterestFragment.set_token(token);
                            selectedFragment = new PostsOfInterestFragment();

                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
