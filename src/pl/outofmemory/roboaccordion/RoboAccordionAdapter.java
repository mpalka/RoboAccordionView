/*
 * Copyright (C) 2013 Marcin Palka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.outofmemory.roboaccordion;

import android.view.View;

/**
 * A custom adapter interface designed to provide header and content views for RoboAccordionView
 * <p/>
 * Created by Marcin Palka on 11.08.2013.
 */
public interface RoboAccordionAdapter {

    /**
     * Returns a number of segments in the accordion.
     * For each segment a header and content view need
     * need to be provided in
     *
     * @return a numer of segments in the accordion
     */
    public int getSegmentCount();

    /**
     * Returns a view that will be displayed as one of accordion's
     * segment header. By tapping onto the header the content view of the
     * segment will either expand or collapse.
     * The header view should not provide any
     * clickable elements as it may interfere with the OnClickListener
     * set on the header by the RoboAccordionView code.
     *
     * @return a view for a accordion segment header
     */
    public View getHeaderView(int index);

    /**
     * Returns a view that will be displayed as one of accordion's
     * segment content. The content will be either expanded or collapsed
     * by tapping onto respective header view.
     *
     * @return a view for a accordion segment content
     */
    public View getContentView(int index);
}
