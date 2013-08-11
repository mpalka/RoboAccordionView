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

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Marcin Palka on 11.08.2013.
 */
public class RoboAccordionView extends LinearLayout {

    private RoboAccordionAdapter mAccordionAdapter;
    private LinearLayout mRootLayout;
    private TextView fillerView;
    private View mPanelExpanded;
    private boolean mAccordionAnimating;
    private int ANIM_DURATION = 300;

    public RoboAccordionView(Context context) {
        super(context);
    }

    public RoboAccordionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootLayout = new LinearLayout(getContext());
        mRootLayout.setOrientation(VERTICAL);
        addView(mRootLayout, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    public void setAccordionAdapter(RoboAccordionAdapter mAdapter) {
        this.mAccordionAdapter = mAdapter;
        notifyDataSetChanged();
    }

    private void addSegment(int index) {
        if (mAccordionAdapter == null) {
            throw new IllegalStateException("No adapter has been set");
        }
        View headerView = mAccordionAdapter.getHeaderView(index);
        mRootLayout.addView(headerView, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 0));
        View contentView = mAccordionAdapter.getContentView(index);
        contentView.setVisibility(View.GONE);
        mRootLayout.addView(contentView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
        headerView.setOnClickListener(new AccordionHeaderOnClickListener(contentView));
        //last item, add filler view
        if (index == mAccordionAdapter.getSegmentCount() - 1) {
            fillerView = new TextView(getContext());
            fillerView.setBackgroundResource(R.color.background_light);
            mRootLayout.addView(fillerView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
            mPanelExpanded = fillerView;
        }
    }

    public void notifyDataSetChanged() {
        mRootLayout.removeAllViews();
        final int count = mAccordionAdapter.getSegmentCount();
        for (int i = 0; i < count; i++) {
            addSegment(i);
        }
        requestLayout();
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

        private View toggleView;

        private AccordionHeaderOnClickListener(View toggleView) {

            this.toggleView = toggleView;
        }

        @Override
        public void onClick(View v) {
            if (!mAccordionAnimating) {
                mAccordionAnimating = true;
                if (mPanelExpanded != toggleView) {
                    Animation a = new ExpandAnimation(0, mPanelExpanded
                            .getMeasuredHeight(), toggleView,
                            mPanelExpanded);
                    a.setDuration(ANIM_DURATION);
                    a.setAnimationListener(new AccordionAnimationListener(
                            toggleView, mPanelExpanded));
                    v.startAnimation(a);
                } else {
                    Animation a = new ExpandAnimation(0, mPanelExpanded
                            .getMeasuredHeight(), fillerView,
                            mPanelExpanded);
                    a.setDuration(ANIM_DURATION);
                    a.setAnimationListener(new AccordionAnimationListener(
                            fillerView, mPanelExpanded));
                    v.startAnimation(a);
                }
            }
        }

        private class AccordionAnimationListener implements Animation.AnimationListener {

            private View mExpandingView;
            private View mCollapsingView;

            public AccordionAnimationListener(View expandingView, View collapsingView) {
                super();
                this.mExpandingView = expandingView;
                this.mCollapsingView = collapsingView;
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPanelExpanded = mExpandingView;
                mAccordionAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
        }
    }
}
