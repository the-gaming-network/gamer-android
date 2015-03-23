package co.johnnyli.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class AboutFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private TextView name;
    private TextView description;
//    private Button join;
    private String pk;
    private String groupURL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/groups/";
    private String link;
    private MemberJSONAdapter mJSONAdapter;
    private ListView memberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        name = (TextView) view.findViewById(R.id.group_name);
        name.setOnClickListener(this);
        description = (TextView) view.findViewById(R.id.group_description);
        memberList = (ListView) view.findViewById(R.id.member_list);
        memberList.setOnItemClickListener(this);
        pk = getArguments().getString("pk");
        ImageView banner = (ImageView) view.findViewById(R.id.img_group);
        banner.setVisibility(View.GONE);
        getAbout();
//        join = (Button) view.findViewById(R.id.join_group);
//        join.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mJSONAdapter = new MemberJSONAdapter(getActivity(), getActivity().getLayoutInflater());
        memberList.setAdapter(mJSONAdapter);

    }

    public void getAbout() {
        link = groupURL + pk;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", Login.auth);
        client.get(link, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                name.setText(jsonObject.optString("name"));
                description.setText(jsonObject.optString("description"));
                mJSONAdapter.updateData(jsonObject.optJSONArray("members"));
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(AboutFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("group", pk);
        params.put("owner", "2");
        client.addHeader("Authorization", Login.auth);
        client.post(groupURL+"join" ,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(AboutFragment.this.getActivity(), "You joined this group.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.d("ERROR!", error.toString());
                Toast.makeText(AboutFragment.this.getActivity(), "You're already a member of the group!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        JSONObject profile = jsonObject.optJSONObject("profile");
        String username = jsonObject.optString("username");
        String pk = jsonObject.optString("pk");
        String bio = profile.optString("bio");
        String picture = profile.optString("picture");
        String gender = profile.optString("gender");
        String dob = profile.optString("dob");
        String location = profile.optString("location");
        Intent memberProfile = new Intent(this.getActivity(), Profile.class);
        memberProfile.putExtra("username", username);
        memberProfile.putExtra("pk", pk);
        memberProfile.putExtra("bio", bio);
        memberProfile.putExtra("picture", picture);
        memberProfile.putExtra("gender", gender);
        memberProfile.putExtra("dob", dob);
        memberProfile.putExtra("location", location);
        startActivity(memberProfile);
    }
}

