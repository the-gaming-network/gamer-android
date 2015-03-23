package co.johnnyli.gamer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Profile extends ActionBarActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        //ActionBar Color
        String color = MainActivity.color;
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        String username = this.getIntent().getExtras().getString("username");
        String bio = this.getIntent().getExtras().getString("bio");
        String picture = this.getIntent().getExtras().getString("picture");
        String gender = this.getIntent().getExtras().getString("gender");
        String dob = this.getIntent().getExtras().getString("dob");
        String location = this.getIntent().getExtras().getString("location");

        ImageView profilePicture = (ImageView) findViewById(R.id.profile_image);
        Picasso.with(mContext).load(picture).into(profilePicture);

        TextView usernameView = (TextView) findViewById(R.id.username);
        usernameView.setText(username);

        TextView profileInfoView = (TextView) findViewById(R.id.profile_info);
        profileInfoView.setText("Bio: " + bio + "\nGender: " + gender + "\nBirthday: " + dob
        + "\nLocation: "+ location);
    }
}
