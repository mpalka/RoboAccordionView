package pl.outofmemory.roboaccordion;

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
    public int getFirstViewToExpandIndex();

    /**
     * This method should return an index of a segment that need to be expaned
     * when a view of index=collapsingIndex collapses. Return -1 if you want to
     * the remaining space be filled with a filler/spacer view
     *
     * @param collapsingIndex - index of collapsing viw
     * @return an index of a segment to be expanded
     */
    public int getNextViewToExpandIndex(int collapsingIndex);
}
