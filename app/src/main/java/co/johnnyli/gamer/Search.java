package co.johnnyli.gamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by johnnyli on 3/9/15.
 */
public class Search extends ActionBarActivity implements View.OnClickListener {

    EditText searchField;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        searchField = (EditText) findViewById(R.id.search_field);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        searchField.setText("");
    }
}
