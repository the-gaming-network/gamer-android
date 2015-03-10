package co.johnnyli.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnnyli on 3/9/15.
 */
public class GroupFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private List<String> testArray = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        testArray.add("Avalon");
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, testArray
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String group_name = testArray.get(position);
        Intent group = new Intent(GroupFragment.this.getActivity(), Group.class);
        group.putExtra("group", group_name);
        startActivity(group);
    }

}
