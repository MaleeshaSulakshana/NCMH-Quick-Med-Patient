package com.example.quickmedpatient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class FormActivity extends AppCompatActivity {

    private EditText txtReport;
    private Button btnAdd;
    private ListView listView;

    private ArrayList<Report> detailsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        listView = findViewById(R.id.listView);
        txtReport = findViewById(R.id.txtReport);
        btnAdd = findViewById(R.id.btnAdd);

        showDetails();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addForum();

            }
        });
    }

    private void showDetails()
    {
        detailsArrayList.clear();
        listView.setAdapter(null);

        ReportAdapter reportAdapter = new ReportAdapter(this, R.layout.row_report_item, detailsArrayList);
        listView.setAdapter(reportAdapter);

        String URL = Api.REPORTS_API + "/" + Preferences.LOGGED_USER_ID;

        RequestQueue requestQueue = Volley.newRequestQueue(FormActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Integer idValue = Integer.valueOf(jsonObject.getString("id"));
                                String dateValue = jsonObject.getString("date");
                                String howIFeelValue = jsonObject.getString("howIFeel");

                                detailsArrayList.add(new Report(idValue, dateValue, howIFeelValue));
                            }

                            reportAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonArrayRequest);

    }

    private void addForum() {

        String report = txtReport.getText().toString();

        if (report.equals("")) {
            Toast.makeText(FormActivity.this, "Fields empty!",Toast.LENGTH_SHORT).show();

        } else {

            try {

                String URL = Api.REPORTS_API;
                String todayDate = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS", Locale.getDefault()).format(new Date());

                RequestQueue requestQueue = Volley.newRequestQueue(FormActivity.this);
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("patients_ID", Preferences.LOGGED_USER_ID);
                jsonBody.put("fullName", Preferences.LOGGED_USER_NAME);
                jsonBody.put("date", todayDate);
                jsonBody.put("howIFeel", report);

                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");

                            if (status.equals("success")) {

                                txtReport.setText("");

                                Toast.makeText(FormActivity.this, "Daily report added!", Toast.LENGTH_SHORT).show();
                                showDetails();
                            }else {
                                Toast.makeText(FormActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("*************** " + error.toString());
                        Toast.makeText(FormActivity.this, "Some error occur" + error.toString(), Toast.LENGTH_SHORT).show();
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

}


class Report {

    int id;
    String date, howFeel;

    public Report(int id, String date, String howFeel) {
        this.id = id;
        this.date = date;
        this.howFeel = howFeel;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getHowFeel() {
        return howFeel;
    }
}

class ReportAdapter extends ArrayAdapter<Report> {

    private Context mContext;
    private int mResource;

    public ReportAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Report> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView report = (TextView) convertView.findViewById(R.id.report);

        date.setText(getItem(position).getDate());
        report.setText(getItem(position).getHowFeel());

        return convertView;
    }

}