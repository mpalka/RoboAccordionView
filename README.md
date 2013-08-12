RoboAccordionView for Android
==========================

RoboAccordionView is a component that presents a list of collapsible/expandable segments. It is compatible with Android 2.1 and newer.

Sample application
==================

Try out the [sample application ][2].

![Demo application screenshot][1]

Usage
=====

*Please note that this project has been created in Intellij Idea 12 and thus by default can be opened in Intellij Idea and/or Android Studio. To make it work with Eclipse and ADT you may need to use Eclipse's import funcion.*

*For a working implementation of this project see the `demo/` folder.*

  1. Include the widget in your view. 

      ```xml
      <pl.outofmemory.roboaccordion.RoboAccordionView
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:id="@+id/accordion"/>
      ```
        
  **Currently it's not supported to embed the RoboAccordionView inside a ScrollView**

  2. In your `onCreate` method (or `onCreateView` for a fragment), bind the
     accordion view to a class implementing the `RoboAccordionAdapter` interface.

    ```java
    public class DemoActivity extends Activity implements RoboAccordionAdapter {
      private RoboAccordionView accordionView;
      
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.main);
          accordionView = (RoboAccordionView) findViewById(R.id.accordion);
          accordionView.setAccordionAdapter(this);
          accordionView.setAnimDuration(300);
      }
      ```

  3. Implement methods of the `RoboAccordionAdapter` to provide accordion's segment count (`getSegmentCount`) as well as header (`getHeaderView`) and content view (`getContentView`) of each segment. Segments can be expanded or collapsed by clicking/tapping onto the header view.

      ```java
      private String[] capitals = new String[]{"Athens", "Berlin", "London",
            "Helsinki", "Copenhagen", "Warsaw",
            "Stockholm", "Oslo", "Prague",
            "Budapest", "Paris", "Moscow",
            "Kiev", "Bratislava", "Rome"};
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
                  TextView tv1 = (TextView)view;
                  tv1.setText(String.format("Content %d", index));
                  break;
              case 1:
                  view = new TextView(this);
                  view.setBackgroundResource(R.color.light_green);
                  TextView tv2 = (TextView)view;
                  tv2.setText(String.format("Content %d", index));
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
      ```
  4. Attach a listener to the `RoboAccordionView` to be notified about segments being expanded and collapsed

      ```java
        accordionView.setListener(new RoboAccordionStateListener() {
                  @Override
                  public void onAccordionStateWillChange(int expandSegmentIndex, int collapseSegmentIndex) {
                      //TODO empty method stub
                  }
      
                  @Override
                  public void onAccordionStateChanged(int expandSegmentIndex, int collapseSegmentIndex) {
                      //TODO empty method stub
                  }
              });
      ```
  5. You may override the behavior of the RoboAccordionView in terms of what view is being expanded when one view is collapsed and what view should be initially expanded when the RoboAccordionView loads by providing so called toggle policy. The toggle policy is simply implemementation of RoboAccordionTogglePolicy interface. You set it by calling the setTogglePolicy on the RoboAccordionView instance. That method should be called before the accordion view is rendered on screen.

        ```java
        public void onCreate(Bundle savedInstanceState) {
            ...
            accordionView.setTogglePolicy(new CustomAccordionTogglePolicy());
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
        ```

Developed By
============

 * Marcin Palka

License
=======

    Copyright 2013 Marcin Palka

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://raw.github.com/mpalka/RoboAccordionView/master/screenshots/demo-application.png
[2]: https://raw.github.com/mpalka/RoboAccordionView/master/demo/RoboAccordionViewDemo/RoboAccordionViewDemo.apk
