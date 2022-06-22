package com.example.quickmedpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtPsw;
    private TextView textSignUp;
    private Button btnLogin;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = (EditText) this.findViewById(R.id.txtEmail);
        txtPsw = (EditText) this.findViewById(R.id.txtPsw);
        textSignUp = (TextView) this.findViewById(R.id.textSignUp);
        btnLogin = (Button) this.findViewById(R.id.btnLogin);

//        For shared preferences
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                finish();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailValue = txtEmail.getText().toString();
                String pswValue = txtPsw.getText().toString();

                if (emailValue.equals("") || pswValue.equals("")) {
                    Toast.makeText(LoginActivity.this, "Fields empty!",Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        String URL = Api.PATIENT_API + "/login";

                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        JSONObject jsonBody = new JSONObject();

                        jsonBody.put("email", emailValue);
                        jsonBody.put("password", pswValue);

                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status.equals("success")) {

                                        txtEmail.setText("");
                                        txtPsw.setText("");

                                        String id = jsonObject.getString("id");

                                        Preferences.LOGGED_USER_ID = id;

                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();

                                        editor.putString("id", id);
                                        editor.commit();

                                    }

                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(LoginActivity.this, "Some error occur" + error.toString(), Toast.LENGTH_SHORT).show();
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