package com.example.quickmedpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText fname, lname, email, dob, psw, cpsw;
    private TextView textLogin;
    private Button btnSignUp;

    private ChipGroup genderType;

    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = (EditText) this.findViewById(R.id.fname);
        lname = (EditText) this.findViewById(R.id.lname);
        email = (EditText) this.findViewById(R.id.email);
        dob = (EditText) this.findViewById(R.id.dob);
        psw = (EditText) this.findViewById(R.id.psw);
        cpsw = (EditText) this.findViewById(R.id.cpsw);

        textLogin = (TextView) this.findViewById(R.id.textLogin);
        btnSignUp = (Button) this.findViewById(R.id.btnSignUp);

        genderType = (ChipGroup) findViewById(R.id.genderType);

        genderType.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    String value = chip.getText().toString();
                    selectedGender = value;
                }
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
//                finish();

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fnameValue = fname.getText().toString();
                String lnameValue = lname.getText().toString();
                String emailValue = email.getText().toString();
                String dobValue = dob.getText().toString();
                String pswValue = psw.getText().toString();
                String cpswValue = cpsw.getText().toString();

                if (fnameValue.equals("") || lnameValue.equals("") || emailValue.equals("") ||
                        dobValue.equals("") || pswValue.equals("") || cpswValue.equals("") || selectedGender.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Fields empty!",Toast.LENGTH_SHORT).show();

                } else if (!pswValue.equals(cpswValue)) {
                    Toast.makeText(RegisterActivity.this, "Password and confirm password not matched!",Toast.LENGTH_SHORT).show();

                } else {

                    try {

                        int min = 1000;
                        int max = 9999;
                        String random_number = String.valueOf(new Random().nextInt(max) + min);

                        String URL = Api.PATIENT_API;

                        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                        JSONObject jsonBody = new JSONObject();

                        jsonBody.put("patients_ID", random_number + fnameValue);
                        jsonBody.put("firstName", fnameValue);
                        jsonBody.put("lastName", lnameValue);
                        jsonBody.put("dob", dobValue);
                        jsonBody.put("gender", selectedGender);
                        jsonBody.put("email", emailValue);
                        jsonBody.put("password", pswValue);
                        jsonBody.put("account_Status", "Active");

                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status.equals("success")) {

                                        fname.setText("");
                                        lname.setText("");
                                        email.setText("");
                                        dob.setText("");
                                        psw.setText("");
                                        cpsw.setText("");
                                        selectedGender = "";

                                        Toast.makeText(RegisterActivity.this, "Registration successful. Please login!", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(RegisterActivity.this, "Some error occur" + error.toString(), Toast.LENGTH_SHORT).show();
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
        });


    }
}