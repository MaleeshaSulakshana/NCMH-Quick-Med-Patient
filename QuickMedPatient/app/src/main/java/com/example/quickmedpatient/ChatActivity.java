package com.example.quickmedpatient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ChatActivity extends AppCompatActivity {

    private ListView chatList;
    private EditText typeMsg;
    private LinearLayout btnSend;
    private SwipeRefreshLayout chatListPullToRefresh;

    private ArrayList<ChatItems> chatArrayList = new ArrayList<>();

    private String id = "", isCreated= "", doctorId = "", doctorName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isCreated = intent.getStringExtra("isCreated");
        doctorId = intent.getStringExtra("doctorId");
        doctorName = intent.getStringExtra("doctorName");

        chatList = (ListView) this.findViewById(R.id.chatList);
        typeMsg = (EditText) this.findViewById(R.id.typeMsg);
        btnSend = (LinearLayout) this.findViewById(R.id.btnSend);
        chatListPullToRefresh = (SwipeRefreshLayout) this.findViewById(R.id.chatListPullToRefresh);

        if (isCreated.equals("yes")) {
            showExistingChat(Preferences.LOGGED_USER_ID + id);
        } else {
            createChat(Preferences.LOGGED_USER_ID + id);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value = typeMsg.getText().toString();
                if (!value.equals("")) {
                    sendMessage(Preferences.LOGGED_USER_ID + id, value);
                }

            }
        });

    }

    private void createChat(String chatId) {

        try {

            String URL = Api.CONVOCATION_API;

            RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("patients_ID", Preferences.LOGGED_USER_ID);
            jsonBody.put("patient_FullName", "");
            jsonBody.put("psychologist_ID", doctorId);
            jsonBody.put("psychologist_FullName", doctorName);
            jsonBody.put("ConvoID", chatId);
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

                            showExistingChat(chatId);
                            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }else {

                            Intent intent = new Intent(ChatActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(ChatActivity.this, "Some error occur" + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void showExistingChat(String chatId)
    {
        chatArrayList.clear();
        chatList.setAdapter(null);

        ChatAdapter chatAdapter = new ChatAdapter(this, R.layout.row_chat, chatArrayList);
        chatList.setAdapter(chatAdapter);

//        chatArrayList.add(new ChatItems("You", "Hello Doctor"));
//        chatArrayList.add(new ChatItems("Doctor", "Hello"));
//        chatAdapter.notifyDataSetChanged();

        String URL = Api.MESSAGE_API + "/" + chatId;

        RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
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

                                String sender = jsonObject.getString("sender");
                                String msg = jsonObject.getString("msg");

                                chatArrayList.add(new ChatItems(sender, msg));
                            }

                            chatAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonArrayRequest);

    }

    private void sendMessage(String chatId, String msg) {

        try {

            String URL = Api.MESSAGE_API;

            RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
            JSONObject jsonBody = new JSONObject();

            String todayDateTime = new SimpleDateFormat("yyyyMMddHHMMss", Locale.getDefault()).format(new Date());

            System.out.println("************* " + todayDateTime);

            jsonBody.put("sender", Preferences.LOGGED_USER_ID);
            jsonBody.put("msg", msg);
            jsonBody.put("convoId", chatId);
            jsonBody.put("sentTime", todayDateTime);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("msg");

                        if (status.equals("success")) {

                            showExistingChat(chatId);
                            typeMsg.setText("");
                            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }else {

                            Intent intent = new Intent(ChatActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(ChatActivity.this, "Some error occur" + error.toString(), Toast.LENGTH_SHORT).show();
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

class ChatItems {

    String name, msg;

    public ChatItems(String name, String msg) {
        this.msg = msg;
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

}

class ChatAdapter extends ArrayAdapter<ChatItems> {

    private Context mContext;
    private int mResource;

    public ChatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatItems> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView msg = (TextView) convertView.findViewById(R.id.msg);

        name.setText(getItem(position).getName());
        msg.setText(getItem(position).getMsg());

        return convertView;
    }

}