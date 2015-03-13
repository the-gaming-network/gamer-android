package co.johnnyli.gamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by johnnyli on 3/9/15.
 */
public class DetailView extends ActionBarActivity implements View.OnClickListener{

    Context mContext;
//    private static final String URL = "http://10.12.6.28:8000/api/post/";
    private static final String URL = "http://10.12.6.28:8000/post.json";
    private ListView listView;
    FeedJSONAdapter mJSONAdapter;
    TextView parent_user;
    TextView parent_post;
    EditText add_comment;
    Button post_comment;
    ImageView avatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        String pk = this.getIntent().getExtras().getString("pk");
        String owner_name = this.getIntent().getExtras().getString("owner_name") + " in "
                + this.getIntent().getExtras().getString("group");
        String text = this.getIntent().getExtras().getString("text");
        String image_url = this.getIntent().getExtras().getString("image_url");
        parent_user = (TextView) findViewById(R.id.author);
        parent_post = (TextView) findViewById(R.id.post_content);
        parent_user.setText(owner_name);
        parent_post.setText(text);
        listView = (ListView) findViewById(R.id.comment);
        avatarView = (ImageView) findViewById(R.id.user_img);
        Picasso.with(mContext).load(image_url).into(avatarView);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getComment(String pk) {
//        String urlString = "";
//        try {
//            urlString = URLEncoder.encode(pk, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL /*+ urlString*/, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d("helloooooooooooo", "worked");
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
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(add_comment.getWindowToken(), 0);
        add_comment.setText("");
    }
}
