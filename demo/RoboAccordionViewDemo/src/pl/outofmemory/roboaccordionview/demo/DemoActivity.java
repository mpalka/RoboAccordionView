package pl.outofmemory.roboaccordionview.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import pl.outofmemory.roboaccordion.RoboAccordionAdapter;
import pl.outofmemory.roboaccordion.RoboAccordionStateListener;
import pl.outofmemory.roboaccordion.RoboAccordionTogglePolicy;
import pl.outofmemory.roboaccordion.RoboAccordionView;

/**
 * Created by Marcin Palka on 11.08.2013.
 */
public class DemoActivity extends Activity implements RoboAccordionAdapter, RoboAccordionStateListener {
    private RoboAccordionView accordionView;
    private View firstContentView;
    private View secondContentView;
    private View thirdContentView;

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
        accordionView.setListener(this);
        accordionView.setAnimDuration(300);
        //accordionView.setTogglePolicy(new CustomAccordionTogglePolicy());
    }


    @Override
    public int getSegmentCount() {
        return 3;
    }

    @Override
    public View getHeaderView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.accordion_header, null);
        TextView tv = (TextView) view.findViewById(R.id.header_text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        switch (index) {
            case 0:
                view.setBackgroundResource(R.color.dark_red);
                break;
            case 1:
                view.setBackgroundResource(R.color.dark_green);
                break;
            case 2:
                view.setBackgroundResource(R.color.dark_blue);
                break;
        }
        tv.setText(String.format("Header %d", index));
        return view;
    }

    @Override
    public View getContentView(int index) {
        View view = null;
        switch (index) {
            case 0:
                view = new TextView(this);
                TextView tv1 = (TextView) view;
                tv1.setText(String.format("Content %d", index));
                firstContentView = view;
                break;
            case 1:
                view = new TextView(this);
                view.setBackgroundResource(R.color.light_green);
                TextView tv2 = (TextView) view;
                tv2.setText(String.format("Content %d", index));
                secondContentView = view;
                break;
            case 2:
                view = new ListView(this);
                ListView lv = (ListView) view;
                lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_capital_row, capitals));
                lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                lv.setCacheColorHint(Color.TRANSPARENT);
                view.setBackgroundResource(R.color.light_blue);
                thirdContentView = view;
                break;
        }
        return view;
    }

    @Override
    public void onAccordionStateWillChange(int expandingSegmentIndex, int collapsingSegmentIndex) {
        Log.i("DemoActivity", String.format("onAccordionStateWillChange:%d,%d", expandingSegmentIndex, collapsingSegmentIndex));
    }

    @Override
    public void onAccordionStateChanged(int expandSegmentIndex, int collapseSegmentIndex) {
        Log.i("DemoActivity", String.format("onAccordionStateChanged:%d,%d", expandSegmentIndex, collapseSegmentIndex));
    }

    private class CustomAccordionTogglePolicy implements RoboAccordionTogglePolicy {

        @Override
        public int getExpandedViewIndex() {
            return 0;
        }

        @Override
        public View getContentViewToExpand(int collapsingIndex) {
            switch (collapsingIndex) {
                case 0:
                    return secondContentView;
                case 1:
                    return thirdContentView;
                case 2:
                    return secondContentView;
            }
            return null;
        }
    }
}