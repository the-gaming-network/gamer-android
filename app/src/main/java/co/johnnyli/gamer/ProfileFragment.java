package co.johnnyli.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ProfilePictureView profilePictureView;
    private TextView name;
    private TextView description;
    private UiLifecycleHelper uiHelper;
    private static final int REAUTH_ACTIVITY_CODE = 100;
    public static String userFacebookName;
    public static String userFacebookEmail;
    private ListView listView;
    GroupListJSONAdapter mJSONAdapter;
    ProgressDialog mDialog;
    private static final String URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/api/groups";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.profile_pic);
        profilePictureView.setCropped(true);
        name = (TextView) view.findViewById(R.id.user_name);
        description = (TextView) view.findViewById(R.id.user_description);
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        }
        listView = (ListView) view.findViewById(R.id.user_groups);
        mDialog = new ProgressDialog(ProfileFragment.this.getActivity());
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

    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                profilePictureView.setProfileId(user.getId());
                                userFacebookName = user.getName();
                                userFacebookEmail = user.asMap().get("email").toString();
                                name.setText(userFacebookName);
                                if (user.getBirthday() != null) {
                                    description.setText("Email: " + userFacebookEmail + "\n"
                                        + "Gender: " + user.asMap().get("gender").toString()
                                        +"\n" + "Birthday:" + user.getBirthday());
                                } else {
                                    description.setText("Email: " + userFacebookEmail + "\n"
                                            + "Gender: " + user.asMap().get("gender").toString());
                                }
                            }
                        }
                        if (response.getError() != null) {
                        }
                    }
                });
        request.executeAsync();
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void getGroup() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", MainActivity.auth);
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                mDialog.dismiss();
                mJSONAdapter.updateData(jsonArray);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                mDialog.dismiss();
                Toast.makeText(ProfileFragment.this.getActivity(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String name = jsonObject.optString("name");
        String pk = jsonObject.optString("pk");
        Intent groupIntent = new Intent(ProfileFragment.this.getActivity(), Group.class);
        groupIntent.putExtra("name", name);
        groupIntent.putExtra("pk", pk);
        startActivity(groupIntent);
    }
}
