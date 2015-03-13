package co.johnnyli.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

/**
 * Created by johnnyli on 3/9/15.
 */
public class FeedFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    FeedJSONAdapter mJSONAdapter;
    private static final String URL = "http://10.12.6.28:8000/Feed.json";
//    private static final String URL = "http://10.12.6.28:8000/api/post/?ordering=-id";
//    private static final String URL = "http://10.12.5.41:8000/api/post/";


    ProgressDialog mDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        mDialog = new ProgressDialog(FeedFragment.this.getActivity());
        mDialog.setMessage("Loading");
        mDialog.setCancelable(true);
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
        String pk = jsonObject.optString("pk", "");
        String group = jsonObject.optString("group");
        String owner_name = jsonObject.optString("owner_name");
        String text = jsonObject.optString("text");
        String image_url = jsonObject.optString("image_url");
        Intent detailIntent = new Intent(FeedFragment.this.getActivity(), DetailView.class);
        detailIntent.putExtra("pk", pk);
        detailIntent.putExtra("group", group);
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
                Toast.makeText(FeedFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
