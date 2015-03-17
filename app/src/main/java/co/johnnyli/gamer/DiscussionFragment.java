package co.johnnyli.gamer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiscussionFragment extends ListFragment implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private ListView listView;
    DiscussionJSONAdapter mJSONAdapter;
    private static final String URL = "http://10.12.6.28:8000/Feed.json";
    private EditText newPost;
    private Button postButton;
    ProgressDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        newPost = (EditText) view.findViewById(R.id.post);
        postButton = (Button) view.findViewById(R.id.post_button);
        postButton.setOnClickListener(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog = new ProgressDialog(DiscussionFragment.this.getActivity());
        mDialog.setMessage("Loading");
        mDialog.setCancelable(true);
        getFeed();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mJSONAdapter = new DiscussionJSONAdapter(getActivity(), getActivity().getLayoutInflater());
        listView.setAdapter(mJSONAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String owner_name = jsonObject.optString("owner_name");
        String text = jsonObject.optString("text");
        String image_url = jsonObject.optString("image_url");
        Intent detailIntent = new Intent(DiscussionFragment.this.getActivity(), DetailView.class);
        detailIntent.putExtra("owner_name", owner_name);
        detailIntent.putExtra("text", text);
        detailIntent.putExtra("image_url", image_url);
        startActivity(detailIntent);
    }

    private void getFeed() {
        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                mDialog.dismiss();
                mJSONAdapter.updateData(jsonArray);
            }
            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                mDialog.dismiss();
                Toast.makeText(DiscussionFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newPost.getWindowToken(), 0);
        RequestParams params = new RequestParams();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(cal.getTime());
        params.put("text", newPost.getText().toString());
        params.put("timestamp", strDate);
        params.put("owner_name", ProfileFragment.userFacebookName);
        Log.d("this is a test", params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d("testing", "it worked!!!");
            }
        });
        newPost.setText("");
    }
}
