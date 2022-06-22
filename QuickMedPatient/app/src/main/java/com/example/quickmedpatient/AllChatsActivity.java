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

public class AllChatsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Chat> detailsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        listView = findViewById(R.id.listView);

        showDetails();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String selected = String.valueOf(detailsArrayList.get(i).getPsychologist_ID());

                Intent intent = new Intent(AllChatsActivity.this, DoctorDetailsActivity.class);
                intent.putExtra("id", selected);
                intent.putExtra("isCreated", "yes");
                startActivity(intent);

            }
        });

    }

    private void showDetails()
    {
        detailsArrayList.clear();
        listView.setAdapter(null);

        ChatsAdapter chatsAdapter = new ChatsAdapter(this, R.layout.row_doctor_item, detailsArrayList);
        listView.setAdapter(chatsAdapter);

        String URL = Api.CONVOCATION_API + "/" + Preferences.LOGGED_USER_ID;

        RequestQueue requestQueue = Volley.newRequestQueue(AllChatsActivity.this);
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

                                String convoID = jsonObject.getString("convoID");
                                String psychologist_FullName = jsonObject.getString("psychologist_FullName");
                                String psychologist_ID = jsonObject.getString("psychologist_ID");

                                detailsArrayList.add(new Chat(convoID, psychologist_FullName, psychologist_ID));
                            }

                            chatsAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AllChatsActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonArrayRequest);

    }

}

class Chat {

    String convoID, psychologist_FullName, psychologist_ID;

    public Chat(String convoID, String psychologist_FullName, String psychologist_ID) {
        this.convoID = convoID;
        this.psychologist_FullName = psychologist_FullName;
        this.psychologist_ID = psychologist_ID;
    }

    public String getConvoID() {
        return convoID;
    }

    public String getPsychologist_FullName() {
        return psychologist_FullName;
    }

    public String getPsychologist_ID() {
        return psychologist_ID;
    }
}

class ChatsAdapter extends ArrayAdapter<Chat> {

    private Context mContext;
    private int mResource;

    public ChatsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Chat> objects) {
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

        qualification.setVisibility(View.GONE);
        email.setVisibility(View.GONE);

        doctor.setText(getItem(position).getPsychologist_FullName());

        return convertView;
    }

}