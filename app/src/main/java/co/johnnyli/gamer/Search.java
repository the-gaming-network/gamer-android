package co.johnnyli.gamer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by johnnyli on 3/9/15.
 */
public class Search extends ActionBarActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    EditText searchField;
    Button searchButton;
    ListView searchListView;
    private static final String QUERY_URL = "http://10.12.6.28:8000/user_group.json";
    GroupListJSONAdapter mJSONAdapter;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
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
        client.get(QUERY_URL /*+ urlString*/,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        mDialog.dismiss();
                        mJSONAdapter.updateData(jsonObject.optJSONArray("groups"));
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                                throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        Intent groupIntent = new Intent(this, Group.class);
        startActivity(groupIntent);
    }
}
