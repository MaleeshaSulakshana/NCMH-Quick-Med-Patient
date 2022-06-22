package com.example.quickmedpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView fname, lname, email, qualifications;
    private Button btnChat;
    private String id = "", isCreated = "", doctorId = "", doctorName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isCreated = intent.getStringExtra("isCreated");

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        qualifications = findViewById(R.id.qualifications);

        btnChat = findViewById(R.id.btnChat);

        showDetails();

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DoctorDetailsActivity.this, ChatActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("isCreated", isCreated);
                intent.putExtra("doctorId", doctorId);
                intent.putExtra("doctorName", doctorName);
                startActivity(intent);

            }
        });

    }

    private void showDetails()
    {
        String URL = Api.DOCTORS_API + "/" +id;

        RequestQueue requestQueue = Volley.newRequestQueue(DoctorDetailsActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {

                            String Psychologist_ID = response.getString("psychologist_ID");
                            String fnameValue = response.getString("lastName");
                            String lnameValue = response.getString("firstName");
                            String emailValue = response.getString("email");
                            String qualificationsValue = response.getString("qualifications");

                            doctorId = Psychologist_ID;
                            doctorName = fnameValue + " " + lnameValue;

                            fname.setText(fnameValue);
                            lname.setText(lnameValue);
                            qualifications.setText(qualificationsValue);
                            email.setText(emailValue);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DoctorDetailsActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonObjectRequest);

    }

}