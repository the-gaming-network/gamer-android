package co.johnnyli.gamer;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Search extends ActionBarActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    EditText searchField;
    Button searchButton;
    ListView searchListView;
    private static final String QUERY_URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/groups?search=";
    GroupListJSONAdapter mJSONAdapter;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        //ActionBar Color
        String color = "#00006B";
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        searchField = (EditText) findViewById(R.id.search_field);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        searchListView = (ListView) findViewById(R.id.search_list);
        mJSONAdapter = new GroupListJSONAdapter(this, getLayoutInflater());
        searchListView.setAdapter(mJSONAdapter);
        searchListView.setOnItemClickListener(this);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Searching");
        mDialog.setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_group:
                Intent search = new Intent(this, AddGroup.class);
                startActivity(search);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        queryGroups(searchField.getText().toString());
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        searchField.setText("");
    }

    private void queryGroups(String searchString) {
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        mDialog.dismiss();
                        mJSONAdapter.updateData(jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                                throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Gamer", statusCode + " " + throwable.getMessage());
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
