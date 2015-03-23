package co.johnnyli.gamer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class AddGroup extends ActionBarActivity implements View.OnClickListener{

    private String createURL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/groups/";
    private TextView groupName;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        setTitle("Add Group");
        String color = MainActivity.color;
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        groupName = (TextView) findViewById(R.id.group_name);
        description = (TextView) findViewById(R.id.group_description);
        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RequestParams params = new RequestParams();
        params.put("name", groupName.getText().toString());
        params.put("description", description.getText().toString());
        final Intent group = new Intent(this, Group.class);
        group.putExtra("name", groupName.getText().toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-CSRFToken", Info.csrftoken);
        client.addHeader("Authorization", MainActivity.auth);

        client.post(createURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                String newGroupKey = jsonObject.optString("pk");
                AsyncHttpClient joinClient = new AsyncHttpClient();
                RequestParams joinParams = new RequestParams();
                joinParams.put("group", newGroupKey);
                joinParams.put("owner", "2");
                joinClient.addHeader("Authorization", MainActivity.auth);
                joinClient.post(createURL + "join", joinParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode1, Throwable throwable1, JSONObject error1) {
                        Log.d("JOIN Error", error1.toString());
                    }
                });
                group.putExtra("pk", newGroupKey);
                finish();
                startActivity(group);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.d("Error", error.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent search = new Intent(this, Search.class);
        finish();
        startActivity(search);
    }
}
