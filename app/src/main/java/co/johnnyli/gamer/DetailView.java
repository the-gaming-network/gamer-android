package co.johnnyli.gamer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class DetailView extends ActionBarActivity implements View.OnClickListener{

    Context mContext;
    private static final String base_URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/posts/";
    private static final String postURL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/comments";
    FeedJSONAdapter mJSONAdapter;
    TextView parent_user;
    TextView parent_post;
    EditText add_comment;
    Button post_comment;
    ImageView avatarView;
    private String pk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        //ActionBar color
        String color = MainActivity.color;
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        String label = this.getIntent().getExtras().getString("label");
        setTitle(label);
        pk = this.getIntent().getExtras().getString("pk");
        String owner_name;
        if (this.getIntent().getExtras().getString("group") != null) {
            owner_name = this.getIntent().getExtras().getString("owner_name") + " in "
                    + this.getIntent().getExtras().getString("group");
        } else {
            owner_name = this.getIntent().getExtras().getString("owner_name");
        }
        String text = this.getIntent().getExtras().getString("text");
        String image_url = this.getIntent().getExtras().getString("image_url");
        Log.d("this is image_url", " " + image_url);
        parent_user = (TextView) findViewById(R.id.author);
        parent_post = (TextView) findViewById(R.id.post_content);
        parent_post.setOnClickListener(this);
        parent_user.setText(owner_name);
        parent_post.setText(text);
        ListView listView;
        listView = (ListView) findViewById(R.id.comment);
        avatarView = (ImageView) findViewById(R.id.user_img);
        if (image_url != null) {
            Picasso.with(mContext).load(image_url).into(avatarView);
        } else {
            avatarView.setVisibility(View.GONE);
        }
        mJSONAdapter = new FeedJSONAdapter(this, getLayoutInflater());
        listView.setAdapter(mJSONAdapter);
        getComment(pk);
        add_comment = (EditText) findViewById(R.id.post_comment);
        post_comment = (Button) findViewById(R.id.post_button);
        post_comment.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getComment(String pk) {
        String urlString = "";
        try {
            urlString = URLEncoder.encode(pk, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(base_URL + urlString, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(JSONObject jsonObject) {
                mJSONAdapter.updateData(jsonObject.optJSONArray("comments"));
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.post_button) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(add_comment.getWindowToken(), 0);
            RequestParams params = new RequestParams();
            params.put("text", add_comment.getText().toString());
            params.put("post", pk);
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("X-CSRFToken", Info.csrftoken);
            client.addHeader("Authorization", Login.auth);
            client.post(postURL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    Log.d("testing", "it worked!!!");
                    getComment(pk);
                }

                @Override
                public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                    Log.d("Error", error.toString());
                }
            });
            add_comment.setText("");
        } else if (v.getId() == R.id.post_content) {
            RequestParams params = new RequestParams();
            params.put("post", pk);
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("X-CSRFToken", Info.csrftoken);
            client.addHeader("Authorization", Login.auth);
            client.post("http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/like",
                    params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Toast.makeText(getBaseContext(), "Liked post", Toast.LENGTH_LONG).show();                        }
                        @Override
                        public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                            Log.d("Error", error.toString());
                        }
                    });
        }
    }
}
