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

import org.json.JSONObject;

public class DiscussionFragment extends ListFragment implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private ListView listView;
    DiscussionJSONAdapter mJSONAdapter;
    private static final String URL =
            "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/groups/";
    private static final String postURL =
            "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/posts";
    private EditText newPost;
    private Button postButton;
    ProgressDialog mDialog;
    private String pk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        newPost = (EditText) view.findViewById(R.id.post);
        postButton = (Button) view.findViewById(R.id.post_button);
        postButton.setOnClickListener(this);
        pk = getArguments().getString("pk");
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
        String image_url = jsonObject.optString("owner_profile_image");
        String pk = jsonObject.optString("pk");
        Intent detailIntent = new Intent(DiscussionFragment.this.getActivity(), DetailView.class);
        detailIntent.putExtra("owner_name", owner_name);
        detailIntent.putExtra("text", text);
        detailIntent.putExtra("image_url", image_url);
        detailIntent.putExtra("label", Group.nameOfGroup);
        detailIntent.putExtra("pk", pk);
        startActivity(detailIntent);
    }

    private void getFeed() {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("this is the url", URL);
        mDialog.show();
        client.get(URL + pk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                mDialog.dismiss();
                mJSONAdapter.updateData(jsonObject.optJSONArray("posts"));
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
        params.put("text", newPost.getText().toString());
        params.put("group", pk);
        params.put("owner", "4");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-CSRFToken", Info.csrftoken);
        client.addHeader("Authorization", Info.auth);
        client.post(postURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d("testing", "it worked!!!");
                getFeed();
            }
            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(DiscussionFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error", error.toString());
            }
        });
//
        newPost.setText("");
    }
}
