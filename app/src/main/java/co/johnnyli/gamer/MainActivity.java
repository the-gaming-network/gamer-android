package co.johnnyli.gamer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    private static final int SPLASH = 0;
    private static final int SETTINGS = 1;
    private static final int FRAGMENT_COUNT = SETTINGS +1;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    public static String facebookKey;
    public static String facebookToken;
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private String URL = "http://ec2-52-11-124-82.us-west-2.compute.amazonaws.com/rest-auth/facebook/";
    public static String color = "#2f69ab";
    public static String auth = "Basic " +
            Base64.encodeToString((Info.username + ":" + Info.password).getBytes(), Base64.NO_WRAP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton authButton = (LoginButton) findViewById(R.id.login_button);
        authButton.setReadPermissions(Arrays.asList("email"));

        FragmentManager fm = getSupportFragmentManager();
        fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();

    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            if (state.isOpened()) {
                facebookToken = session.getAccessToken();
                RequestParams params = new RequestParams();
                params.put("access_token", facebookToken);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            facebookKey = jsonObject.getString("key");
                            Log.d("KEY!!!!!!!!", facebookKey);
                            Log.d("TOKEN!!!!!!", facebookToken);
                        } catch (Exception e) {
                            Log.d("Error", e.toString());
                        }
                    }
                });
                startActivity(new Intent(MainActivity.this, Feed.class));
                showFragment(SETTINGS,false);
            } else if (state.isClosed()) {
                showFragment(SPLASH, false);
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            showFragment(SETTINGS, false);
        } else {
            showFragment(SPLASH, false);
        }
    }

    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


}
