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

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This class provides a widget to create accordion views
 * with expandable/collapsible segments.
 * Views to be displayed as segment's headers and content
 * are provided by a class implementing RoboAccordionAdapter interface
 * <p/>
 * Created by Marcin Palka on 11.08.2013.
 */
public class RoboAccordionView extends LinearLayout {

    private RoboAccordionAdapter mAccordionAdapter;
    private RoboAccordionStateListener mListener;
    private final LinearLayout mRootLayout;
    private TextView mFillerView;
    private View mPanelExpanded;
    private boolean mAccordionAnimating;
    private static final int DEFAULT_ANIM_DURATION = 300;
    private int mAnimDuration = DEFAULT_ANIM_DURATION;
    private List<View> mContentViews;
    private RoboAccordionTogglePolicy mCurrentTogglePolicy;
    private int mPreviouslyExpandedIndex = -1;
    private final RoboAccordionTogglePolicy HISTORY_TOGGLE_POLICY = new HistoryTogglePolicy();
    private final RoboAccordionTogglePolicy FILLER_TOGGLE_POLICY = new FillerTogglePolicy();
    private final RoboAccordionTogglePolicy NEXTPREV_TOGGLE_POLICY = new NextPreviousTogglePolicy();
    private static final int currentApiVersion = Build.VERSION.SDK_INT;


    /**
     * Default constructor
     *
     * @param context context
     * @param attrs   attributes
     */
    public RoboAccordionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCurrentTogglePolicy = HISTORY_TOGGLE_POLICY;
        mRootLayout = new LinearLayout(getContext());
        mRootLayout.setOrientation(VERTICAL);
        addView(mRootLayout, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mRootLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    /**
     * Method to set RoboAccordionView toggle policy. The policy definition is implementation
     * of the RoboAccordionTogglePolicy interface and it provides RoboAccordionView with information on
     * what is the view that should be expanded when RoboAccordionView loads and what is the next
     * content view to be expanded when the current view is collapsed.
     * This method need to be called just after the AccordionView has been constructed and before it
     * is rendered on screen.
     *
     * @param mCurrentTogglePolicy implementation of RoboAccordionTogglePolicy
     */
    public void setTogglePolicy(RoboAccordionTogglePolicy mCurrentTogglePolicy) {
        this.mCurrentTogglePolicy = mCurrentTogglePolicy;
    }

    /**
     * Method to set the adapter that will provide views for segment headers and content
     *
     * @param adapter an instance of RoboAccordionAdapter
     */
    public void setAccordionAdapter(RoboAccordionAdapter adapter) {
        this.mAccordionAdapter = adapter;
        notifyDataSetChanged();
    }

    private void addSegment(int index) {
        Log.i("RoboAccordionView", "mRootLayout.getMeasuredHeight()=" + mRootLayout.getMeasuredHeight());
        if (mAccordionAdapter == null) {
            throw new IllegalStateException("No adapter has been set");
        }
        View headerView = mAccordionAdapter.getHeaderView(index);
        mRootLayout.addView(headerView, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 0));
        View contentView = mAccordionAdapter.getContentView(index);
        if (mCurrentTogglePolicy.getFirstSegmentToExpandIndex() == index) {
            contentView.setVisibility(View.VISIBLE);
            mPanelExpanded = contentView;
        } else {
            contentView.setVisibility(View.GONE);
        }
        contentView.setTag(index);
        mRootLayout.addView(contentView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
        headerView.setOnClickListener(new AccordionHeaderOnClickListener(contentView, index));

        mContentViews.add(contentView);

        //last item, add filler view
        if (index == mAccordionAdapter.getSegmentCount() - 1) {
            mFillerView = new TextView(getContext());
            mFillerView.setTag(-1);
            mFillerView.setBackgroundResource(android.R.color.transparent);
            mRootLayout.addView(mFillerView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
            if (mCurrentTogglePolicy.getFirstSegmentToExpandIndex() == -1) {
                mFillerView.setVisibility(View.VISIBLE);
                mPanelExpanded = mFillerView;
            } else {
                mFillerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Notifies the view that the backing data has been changed
     * and the view needs to be refreshed
     */
    public void notifyDataSetChanged() {
        mRootLayout.removeAllViews();
        mContentViews = new ArrayList<View>();
        final int count = mAccordionAdapter.getSegmentCount();
        for (int i = 0; i < count; i++) {
            addSegment(i);
        }
        requestLayout();
    }

    /**
     * Set the listener that will be notified about the changes
     * in the RoboAccordionView. The listener will be notified
     * prior and after any segment is expanded and collapsed.
     * Only a single listener can be supported. Call this method
     * with null parameter to remove the listener.
     *
     * @param listener an instance of RoboAccordionStateListener
     */
    public void setListener(RoboAccordionStateListener listener) {
        this.mListener = listener;
    }

    /**
     * Returns animation duration in miliseconds
     *
     * @return animation duration in miliseconds
     */
    public int getAnimDuration() {
        return mAnimDuration;
    }

    /**
     * Sets duration of a single expand/collapse animation
     *
     * @param animDuration animation duration in miliseconds
     */
    public void setAnimDuration(int animDuration) {
        this.mAnimDuration = animDuration;
    }

    private class ExpandAnimation extends Animation {
        private final int mStartHeight;
        private final int mEndHeight;
        private final int mDeltaHeight;
        private final View mPanelToExpand;
        private final View mPaneToCollapse;
        private final android.view.ViewGroup.LayoutParams mCollapseLp;
        private final android.view.ViewGroup.LayoutParams mExpandLp;

        public ExpandAnimation(int startHeight, int endHeight,
                               View expandContent, View collapseContent1) {
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            mDeltaHeight = endHeight - startHeight;
            mPanelToExpand = expandContent;
            mPaneToCollapse = collapseContent1;
            mCollapseLp = mPaneToCollapse.getLayoutParams();
            mExpandLp = mPanelToExpand.getLayoutParams();
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            mCollapseLp.height = (int) (mEndHeight - mDeltaHeight
                    * interpolatedTime);
            mPaneToCollapse.setLayoutParams(mCollapseLp);
            mExpandLp.height = (int) (mStartHeight + mDeltaHeight
                    * interpolatedTime);
            mPanelToExpand.setLayoutParams(mExpandLp);

            if (mExpandLp.height < 5) {
                mPanelToExpand.setVisibility(View.GONE);
            } else {
                mPanelToExpand.setVisibility(View.VISIBLE);
            }

            if (mExpandLp.height > mEndHeight - 5) {
                mExpandLp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                mPanelToExpand.setLayoutParams(mExpandLp);
                mPaneToCollapse.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private class AccordionHeaderOnClickListener implements View.OnClickListener {

        private final View mToggleView;
        private final int mClickedSegmentIndex;

        private AccordionHeaderOnClickListener(View toggleView, int clickedSegmentIndex) {

            this.mToggleView = toggleView;
            this.mClickedSegmentIndex = clickedSegmentIndex;
        }

        @Override
        public void onClick(View v) {
            if (!mAccordionAnimating) {
                mAccordionAnimating = true;
                View viewToBeExpanded = null;
                if (mPanelExpanded != mToggleView) {
                    viewToBeExpanded = mToggleView;
                } else {
                    //check next view to be expanded based on the toggle policy
                    int nextToExpand = mCurrentTogglePolicy.getNextSegmentToExpandIndex(mClickedSegmentIndex);
                    //if next index equals -1 then use the filler view
                    if (nextToExpand != -1) {
                        viewToBeExpanded = mContentViews.get(nextToExpand);
                    } else {
                        viewToBeExpanded = mFillerView;
                    }
                }
                Animation a = new ExpandAnimation(0, mPanelExpanded
                        .getMeasuredHeight(), viewToBeExpanded,
                        mPanelExpanded);
                a.setDuration(mAnimDuration);
                a.setAnimationListener(new AccordionAnimationListener(
                        viewToBeExpanded, mPanelExpanded));
                v.startAnimation(a);
            }
        }

        private class AccordionAnimationListener implements Animation.AnimationListener {

            private final View mExpandingView;
            private final View mCollapsingView;
            private int mCollapsingSegmentIndex = -1;
            private int mExpandingSegmentIndex = -1;

            public AccordionAnimationListener(View expandingView, View collapsingView) {
                super();
                this.mExpandingView = expandingView;
                this.mCollapsingView = collapsingView;
                this.mExpandingSegmentIndex = (Integer) mExpandingView.getTag();
                this.mCollapsingSegmentIndex = (Integer) mCollapsingView.getTag();
            }

            @Override
            public void onAnimationStart(Animation animation) {
                if (mListener != null) {
                    mListener.onAccordionStateWillChange(mExpandingSegmentIndex, mCollapsingSegmentIndex);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mListener != null) {
                    mListener.onAccordionStateChanged(mExpandingSegmentIndex, mCollapsingSegmentIndex);
                }
                mPanelExpanded = mExpandingView;
                mPreviouslyExpandedIndex = this.mCollapsingSegmentIndex;
                mAccordionAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
        }
    }

    private class HistoryTogglePolicy implements RoboAccordionTogglePolicy {

        @Override
        public int getFirstSegmentToExpandIndex() {
            return 0;
        }

        @Override
        public int getNextSegmentToExpandIndex(int collapsingIndex) {
            if (mPreviouslyExpandedIndex == -1) {
                return NEXTPREV_TOGGLE_POLICY.getNextSegmentToExpandIndex(collapsingIndex);
            }
            return mPreviouslyExpandedIndex;
        }
    }

    private class FillerTogglePolicy implements RoboAccordionTogglePolicy {

        @Override
        public int getFirstSegmentToExpandIndex() {
            return 0;
        }

        @Override
        public int getNextSegmentToExpandIndex(int collapsingIndex) {
            return -1;
        }
    }

    private class NextPreviousTogglePolicy implements RoboAccordionTogglePolicy {

        @Override
        public int getFirstSegmentToExpandIndex() {
            return 0;
        }

        @Override
        public int getNextSegmentToExpandIndex(int collapsingSegmentIndex) {
            if (collapsingSegmentIndex == mAccordionAdapter.getSegmentCount() - 1) {
                return collapsingSegmentIndex - 1;
            } else {
                return collapsingSegmentIndex + 1;
            }
        }
    }
}
