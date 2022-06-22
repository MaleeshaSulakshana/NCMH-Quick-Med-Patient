package com.example.quickmedpatient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText txtSearch;
    private ListView listView;

    private ArrayList<Search> detailsArrayList = new ArrayList<>();
    private static ArrayList<Search> detailsArrayList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = findViewById(R.id.listView);
        txtSearch = findViewById(R.id.txtSearch);

        showDetails();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String selected = String.valueOf(detailsArrayList.get(i).getPsychologist_ID());

                Intent intent = new Intent(SearchActivity.this, DoctorDetailsActivity.class);
                intent.putExtra("id", selected);
                intent.putExtra("isCreated", "no");
                startActivity(intent);

            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchDetails(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void showDetails()
    {
        detailsArrayList.clear();
        listView.setAdapter(null);

        SearchAdapter searchAdapter = new SearchAdapter(this, R.layout.row_doctor_item, detailsArrayList);
        listView.setAdapter(searchAdapter);

        String URL = Api.DOCTORS_API;

        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
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

                                String psychologist_ID = jsonObject.getString("psychologist_ID");
                                String firstName = jsonObject.getString("firstName");
                                String lastName = jsonObject.getString("lastName");
                                String qualifications = jsonObject.getString("qualifications");
                                String email = jsonObject.getString("email");

                                detailsArrayList.add(new Search(psychologist_ID, firstName, lastName, qualifications, email));
                                detailsArrayList2.add(new Search(psychologist_ID, firstName, lastName, qualifications, email));
                            }

                            searchAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonArrayRequest);

    }

    private void searchDetails(String value)
    {

        detailsArrayList.clear();
        listView.setAdapter(null);

        SearchAdapter searchAdapter = new SearchAdapter(this, R.layout.row_doctor_item, detailsArrayList);
        listView.setAdapter(searchAdapter);

        if (!value.equals("")) {
            for (int i = 0; i < detailsArrayList2.size(); i++) {
                if (detailsArrayList2.get(i).getFirstName().toLowerCase().contains(value) || detailsArrayList2.get(i).getLastName().toLowerCase().contains(value)) {
                    detailsArrayList.add(detailsArrayList2.get(i));
                }
            }
        } else {
            for (int i = 0; i < detailsArrayList2.size(); i++) {
                detailsArrayList.add(detailsArrayList2.get(i));
            }
        }

        searchAdapter.notifyDataSetChanged();

    }

}

class Search {

    String Psychologist_ID, FirstName, LastName, Qualifications, Email;

    public Search(String Psychologist_ID, String FirstName, String LastName, String Qualifications, String Email) {
        this.Psychologist_ID = Psychologist_ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Qualifications = Qualifications;
        this.Email = Email;
    }

    public String getPsychologist_ID() {
        return Psychologist_ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getQualifications() {
        return Qualifications;
    }

    public String getEmail() {
        return Email;
    }
}

class SearchAdapter extends ArrayAdapter<Search> {

    private Context mContext;
    private int mResource;

    public SearchAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Search> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView doctor = (TextView) convertView.findViewById(R.id.doctor);
        TextView qualification = (TextView) convertView.findViewById(R.id.qualification);
        TextView email = (TextView) convertView.findViewById(R.id.email);

        doctor.setText(getItem(position).getFirstName() + " " + getItem(position).getLastName());
        qualification.setText(getItem(position).getQualifications());
        email.setText(getItem(position).getEmail());

        return convertView;
    }

}