package co.johnnyli.gamer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class Profile extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private Context mContext;
    private String baseURL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/users/";
    private GroupJSONAdapter mJSONAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        //ActionBar Color
        String color = MainActivity.color;
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        String username = this.getIntent().getExtras().getString("username");
        String bio = this.getIntent().getExtras().getString("bio");
        String picture = this.getIntent().getExtras().getString("picture");
        String gender = this.getIntent().getExtras().getString("gender");
        String dob = this.getIntent().getExtras().getString("dob");
        String location = this.getIntent().getExtras().getString("location");

        ImageView profilePicture = (ImageView) findViewById(R.id.profile_image);
        Picasso.with(mContext).load(picture).into(profilePicture);

        TextView usernameView = (TextView) findViewById(R.id.username);
        usernameView.setText(username);

        TextView profileInfoView = (TextView) findViewById(R.id.profile_info);
        profileInfoView.setText("Bio: " + bio + "\nGender: " + gender + "\nBirthday: " + dob
                                + "\nLocation: "+ location);

        ListView listView = (ListView) findViewById(R.id.group_list);
        mJSONAdapter = new GroupJSONAdapter(this, getLayoutInflater());
        listView.setAdapter(mJSONAdapter);
        listView.setOnItemClickListener(this);
        getGroups();
    }

    public void getGroups() {
        String pk = this.getIntent().getExtras().getString("pk");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", Login.auth);
        client.get(baseURL + pk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                mJSONAdapter.updateData(jsonObject.optJSONArray("groups"));
            }
            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String name = jsonObject.optString("name");
        String pk = jsonObject.optString("pk");
        Intent groupIntent = new Intent(this, Group.class);
        groupIntent.putExtra("name", name);
        groupIntent.putExtra("pk", pk);
        startActivity(groupIntent);
    }
}
