package com.example.quickmedpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class DashboardActivity extends AppCompatActivity {

    private LinearLayout btnChat, btnForm, btnSearch, btnProfile, btnLogout;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnChat = (LinearLayout) this.findViewById(R.id.btnChat);
        btnForm = (LinearLayout) this.findViewById(R.id.btnForm);
        btnSearch = (LinearLayout) this.findViewById(R.id.btnSearch);
        btnProfile = (LinearLayout) this.findViewById(R.id.btnProfile);
        btnLogout = (LinearLayout) this.findViewById(R.id.btnLogout);

//        For shared preferences
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, AllChatsActivity.class);
                startActivity(intent);
//                finish();

            }
        });

        btnForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, FormActivity.class);
                startActivity(intent);
//                finish();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(intent);
//                finish();

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                startActivity(intent);
//                finish();

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Preferences.LOGGED_USER_ID = "";

                editor.clear();
                editor.apply();

                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });

    }
}