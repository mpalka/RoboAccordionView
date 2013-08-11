package pl.outofmemory.roboaccordionview.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import pl.outofmemory.roboaccordion.RoboAccordionAdapter;
import pl.outofmemory.roboaccordion.RoboAccordionView;

/**
 * Created by Marcin Palka on 11.08.2013.
 */
public class DemoActivity extends Activity implements RoboAccordionAdapter {
    private RoboAccordionView accordionView;
    private String[] capitals = new String[]{"Athens", "Berlin", "London",
            "Helsinki", "Copenhagen", "Warsaw",
            "Stockholm", "Oslo", "Prague",
            "Budapest", "Paris", "Moscow",
            "Kiev", "Bratislava", "Rome"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        accordionView = (RoboAccordionView) findViewById(R.id.accordion);
        accordionView.setAccordionAdapter(this);
    }


    @Override
    public int getSegmentCount() {
        return 3;
    }

    @Override
    public View getHeaderView(int position) {
        TextView view = null;
        view = new TextView(this);
        switch (position) {
            case 0:
                view.setBackgroundResource(R.color.dark_red);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                break;
            case 1:
                view.setBackgroundResource(R.color.dark_green);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                break;
            case 2:
                view.setBackgroundResource(R.color.dark_blue);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                break;
        }
        view.setText(String.format("Header %d", position));
        return view;
    }

    @Override
    public View getContentView(int position) {
        View view = null;
        switch (position) {
            case 0:
                view = new TextView(this);
                view.setBackgroundResource(R.color.light_red);
                ((TextView) view).setText(String.format("Content %d", position));
                break;
            case 1:
                view = new TextView(this);
                view.setBackgroundResource(R.color.light_green);
                ((TextView) view).setText(String.format("Content %d", position));
                break;
            case 2:
                view = new ListView(this);
                ListView lv = (ListView)view;
                lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_capital_row, capitals));
                lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                view.setBackgroundResource(R.color.light_blue);
                break;
        }
        return view;
    }
}