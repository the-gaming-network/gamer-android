package co.johnnyli.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private FeedJSONAdapter mJSONAdapter;
    private static final String URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/groups/feed";
    private static final String infoURL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/myinfo";
    public static String userpk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        getFeed();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mJSONAdapter = new FeedJSONAdapter(getActivity(), getActivity().getLayoutInflater());
        listView.setAdapter(mJSONAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String pk = jsonObject.optString("pk");
        String group = jsonObject.optString("group_name");
        String owner_name = jsonObject.optString("owner_name");
        String text = jsonObject.optString("text");
        String image_url = jsonObject.optString("owner_profile_image");
        Intent detailIntent = new Intent(FeedFragment.this.getActivity(), DetailView.class);
        detailIntent.putExtra("pk", pk);
        detailIntent.putExtra("group", group);
        detailIntent.putExtra("owner_name", owner_name);
        detailIntent.putExtra("text", text);
        detailIntent.putExtra("label", group);
        detailIntent.putExtra("image_url", image_url);
        startActivity(detailIntent);
    }

    private void getFeed() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", Login.auth);
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                mJSONAdapter.updateData(jsonObject.optJSONArray("results"));
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(FeedFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AsyncHttpClient pkClient = new AsyncHttpClient();
        pkClient.addHeader("Authorization", Login.auth);
        pkClient.get(infoURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    JSONObject jsonData = jsonArray.getJSONObject(0);
                    userpk = jsonData.optString("pk");
                } catch (Exception e) {
                    Log.d("Error!", e.toString());
                }
            }
        });
    }

}
