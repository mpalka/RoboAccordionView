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

/**
 * Created by Marcin Palka on 11.08.2013.
 *
 */
public interface RoboAccordionStateListener {
    /**
     * Called prior the change in the accordion to be applied
     * @param expandSegmentIndex index of an accordion segment to be expanded
     * @param collapseSegmentIndex index of an accordion segment to be collapsed
     */
    public void onAccordionStateWillChange(int expandSegmentIndex, int collapseSegmentIndex);
    /**
     * Called after the change in the accordion has been applied
     * @param expandSegmentIndex index of an accordion segment that has been expanded
     * @param collapseSegmentIndex index of an accordion segment that has been collapsed
     */
    public void onAccordionStateChanged(int expandSegmentIndex, int collapseSegmentIndex);
}
