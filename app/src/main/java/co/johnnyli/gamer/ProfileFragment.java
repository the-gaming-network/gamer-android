package co.johnnyli.gamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private ImageView profilePictureView;
    private TextView name;
    private TextView description;
    Context mContext;
    private static final String URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/myinfo";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        profilePictureView = (ImageView) view.findViewById(R.id.profile_image);
        name = (TextView) view.findViewById(R.id.username);
        description = (TextView) view.findViewById(R.id.profile_info);
        getInfo();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getInfo() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", Login.auth);
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.optJSONArray("results");
                    JSONObject jsonData = jsonArray.getJSONObject(0);
                    String username = jsonData.optString("username");
                    name.setText(username);
                    JSONObject profile = jsonData.optJSONObject("profile");
                    String bio = profile.optString("bio");
                    String picture = profile.optString("picture");
                    Picasso.with(mContext).load(picture).into(profilePictureView);

                    String gender = profile.optString("gender");
                    String dob = profile.optString("dob");
                    String location = profile.optString("location");
                    description.setText("Bio: " + bio + "\nGender: " + gender + "\nBirthday: " + dob
                            + "\nLocation: " + location);

                } catch (Exception e) {
                    Log.d("ERROR!!!", e.toString());
                }
            }

        });

    }

}
