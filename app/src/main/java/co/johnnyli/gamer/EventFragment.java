package co.johnnyli.gamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnnyli on 3/6/15.
 */
public class EventFragment extends Fragment {

    private CalendarView calendarView;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private List<String> testArray = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setShowWeekNumber(false);
        listView = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<String> testArray = new ArrayList<String>();
        testArray.add("Board games are super awesome don't you agree?");
        testArray.add("It's fun playing board games all day long!");
        testArray.add("We should meet up to play!!!");
        testArray.add("One two three four five six seven");
        testArray.add("Crack is wack");
        testArray.add("Smokers are jokers");
        testArray.add("Dogs are better than cats");
        testArray.add("Board games are super awesome don't you agree?");
        testArray.add("It's fun playing board games all day long!");
        testArray.add("We should meet up to play!!!");
        testArray.add("One two three four five six seven");
        testArray.add("Crack is wack");
        testArray.add("Smokers are jokers");
        testArray.add("Dogs are better than cats");testArray.add("Board games are super awesome don't you agree?");
        testArray.add("It's fun playing board games all day long!");
        testArray.add("We should meet up to play!!!");
        testArray.add("One two three four five six seven");
        testArray.add("Crack is wack");
        testArray.add("Smokers are jokers");
        testArray.add("Dogs are better than cats");
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, testArray
        );
        listView.setAdapter(arrayAdapter);

    }
}

