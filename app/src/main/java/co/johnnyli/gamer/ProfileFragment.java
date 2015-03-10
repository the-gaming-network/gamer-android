package co.johnnyli.gamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by johnnyli on 3/9/15.
 */
public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        name = (TextView) view.findViewById(R.id.user_name);
        description = (TextView) view.findViewById(R.id.user_description);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name.setText("Mr. Awesome");
        description.setText("I'm Mr. Awesome and I'm super awesome. If you disagree with me I'll" +
                " be so awesome your head will explode.");

    }
}
