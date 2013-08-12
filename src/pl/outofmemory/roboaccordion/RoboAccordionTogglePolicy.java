package pl.outofmemory.roboaccordion;

import android.view.View;

/**
 * The RoboAccordionTogglePolicy should be implemented to provide
 * custom policy that will tell the RoboAccordionView what view needs to be
 * expanded when the view loads and what view needs to be expanded next when
 * one of views collapse.
 * <p/>
 * Created by Marcin Palka on 12.08.2013.
 */
public interface RoboAccordionTogglePolicy {
    /**
     * This method should return id of a view that need to be
     * expanded when RoboAccordionView first loads
     *
     * @return index of a view to be expanded on load
     */
    public int getExpandedViewIndex();

    /**
     * This method should return a reference to a view that need to be expaned
     * when a view of index=collapsingIndex collapses
     *
     * @param collapsingIndex - index of collapsing viw
     * @return a reference to a view to be expanded
     */
    public View getContentViewToExpand(int collapsingIndex);
}
