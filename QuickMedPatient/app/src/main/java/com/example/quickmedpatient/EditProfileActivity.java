package com.example.quickmedpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnUpdate;
    private EditText fname, lname, email, dob;
    private ChipGroup genderType;

    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fname = (EditText) this.findViewById(R.id.fname);
        lname = (EditText) this.findViewById(R.id.lname);
        email = (EditText) this.findViewById(R.id.email);
        dob = (EditText) this.findViewById(R.id.dob);

        genderType = (ChipGroup) findViewById(R.id.genderType);

        btnUpdate = findViewById(R.id.btnUpdate);

        getProfileData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfile();

            }
        });

    }

    private void selectCategory(String category)
    {
        if (genderType.getChildCount() > 0) {

            for (int i = 0; i < genderType.getChildCount(); i++) {
                Chip chip = (Chip) genderType.getChildAt(i);
                String categoryName = chip.getText().toString();

                if (category.equals(categoryName)) {
                    chip.setChecked(true);
                }

            }

        }
    }

    private void editProfile() {

        String fnameValue = fname.getText().toString();
        String lnameValue = lname.getText().toString();
        String emailValue = email.getText().toString();
        String dobValue = dob.getText().toString();

        if (fnameValue.equals("") || lnameValue.equals("") || emailValue.equals("") ||
                dobValue.equals("") || selectedGender.equals("")) {
            Toast.makeText(EditProfileActivity.this, "Fields empty!",Toast.LENGTH_SHORT).show();

        } else {
            try {
                String URL = Api.PATIENT_API + "/" + Preferences.LOGGED_USER_ID;

                RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("firstName", fnameValue);
                jsonBody.put("lastName", lnameValue);
                jsonBody.put("dob", dobValue);
                jsonBody.put("gender", selectedGender);

                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");

                            if (status.equals("success")) {

                                Toast.makeText(EditProfileActivity.this, "Profile update successful.", Toast.LENGTH_SHORT).show();


                            }else {
                                Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(EditProfileActivity.this, "Some error occur" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            return null;
                        }
                    }
                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getProfileData() {

        String URL = Api.PATIENT_API + "/" + Preferences.LOGGED_USER_ID;

        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {

                            String fnameValue = response.getString("lastName");
                            String lnameValue = response.getString("firstName");
                            String dobValue = response.getString("dob");
                            String emailValue = response.getString("email");
                            String genderValue = response.getString("gender");

                            DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            try {
                                Date dd = df.parse(dobValue);
                                df=new SimpleDateFormat("yyyy-MM-dd");
                                dobValue = df.format(dd);

                            } catch (ParseException e) {}

                            fname.setText(fnameValue);
                            lname.setText(lnameValue);
                            dob.setText(dobValue);
                            email.setText(emailValue);
                            selectedGender = genderValue;

                            selectCategory(genderValue);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonObjectRequest);

    }

}