/*
 * Copyright (C) 2015 thewaronryan@gmail.com
 * Added code to handle sections
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
 *
 */


package com.deserttowersprogramming.sectioned;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class ListRecyclerViewSectionAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerViewSectionAdapter<VH, T> implements SectionHandler<VH, T>, FooterHandler<VH> {

    protected List<T> items;
    protected OnListItemClickedListener<T> itemClickedListener;
    protected GestureDetector gestureDetector;

    public ListRecyclerViewSectionAdapter(Context context, List<T> items) {
        this.items = items;

        gestureDetector = new GestureDetector(context, new SimpleTouchListener());

        setViewTypes();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position < 0 || position > viewTypes.length)
            throw new IllegalStateException("No view type found for position " + position);

        if (isFooter(position)) {
            onBindFooter(holder);
        } else if (viewTypes[position] == DEFAULT_VIEW_TYPE) {
            onBindViewHolder(holder, items.get(getAdjustedPositionForSections(position)));
        } else if (viewTypes[position] == SECTION_VIEW_TYPE) {
            onBindSectionViewHolder(holder, items.get(getAdjustedPositionForSections(position)));
        } else {
            throw new IllegalStateException("No view type found for position " + position);
        }
    }

    @Override
    public void setViewTypes() {
        List<Integer> sectionPositions = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (needsSectionBefore(items.get(i)))
                sectionPositions.add(i);
        }

        viewTypes = new int[items.size() + sectionPositions.size()];
        Arrays.fill(viewTypes, DEFAULT_VIEW_TYPE);

        int sectionOffset = 0;
        for (int i = 0; i < sectionPositions.size(); i++) {
            viewTypes[sectionPositions.get(i) + sectionOffset] = SECTION_VIEW_TYPE;
            sectionOffset++;
        }
    }

    public void updateList(List<T> items) {
        this.items = items;
        setViewTypes();
        notifyDataSetChanged();
    }

    public abstract void onBindViewHolder(VH viewHolder, T item);

    public void setOnListItemClickListener(OnListItemClickedListener<T> itemClickListener) {
        this.itemClickedListener = itemClickListener;
    }

    public interface OnListItemClickedListener<T> {
        void onItemClicked(T item);
    }

    private class SimpleTouchListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        if (itemClickedListener != null && childView != null && gestureDetector.onTouchEvent(e)) {
            int position = rv.getChildLayoutPosition(childView);

            if (getItemViewType(position) == DEFAULT_VIEW_TYPE)
                itemClickedListener.onItemClicked(items.get(getAdjustedPositionForSections(position)));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
