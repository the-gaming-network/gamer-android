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

import org.json.JSONObject;

/**
 * Created by johnnyli on 3/9/15.
 */
public class GroupFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    GroupListJSONAdapter mJSONAdapter;
    private static final String URL = "http://10.12.6.28:8000/user_group.json";
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
        mJSONAdapter = new GroupListJSONAdapter(getActivity(), getActivity().getLayoutInflater());
        listView.setAdapter(mJSONAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        Intent groupIntent = new Intent(GroupFragment.this.getActivity(), Group.class);
        startActivity(groupIntent);
    }

    private void getGroup() {
        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                mDialog.dismiss();
                mJSONAdapter.updateData(jsonObject.optJSONArray("groups"));

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
