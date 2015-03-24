package co.johnnyli.gamer;

import android.app.ProgressDialog;
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

public class GroupFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    GroupJSONAdapter mJSONAdapter;
    private static final String URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/myinfo";
    ProgressDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        mDialog = new ProgressDialog(GroupFragment.this.getActivity());
        mDialog.setMessage("Loading");
        mDialog.setCancelable(true);
        getGroup();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mJSONAdapter = new GroupJSONAdapter(getActivity(), getActivity().getLayoutInflater());
        listView.setAdapter(mJSONAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String name = jsonObject.optString("name");
        String pk = jsonObject.optString("pk");
        Intent groupIntent = new Intent(GroupFragment.this.getActivity(), Group.class);
        groupIntent.putExtra("name", name);
        groupIntent.putExtra("pk", pk);
        startActivity(groupIntent);
    }

    private void getGroup() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", Login.auth);
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    mJSONAdapter.updateData(jsonObject.optJSONArray("groups"));
                } catch (Exception e) {
                    Log.d("ERROR!", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                mDialog.dismiss();
                Toast.makeText(GroupFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
