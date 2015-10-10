package com.simple.sectionedrecyclerview;

import java.util.List;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.simple.sectioned.ListRecyclerViewSectionAdapter;

public class ListAdapter extends ListRecyclerViewSectionAdapter<ViewHolder, Integer> {

    public ListAdapter(List<Integer> items) {
        super(items);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Integer item) {
        if (viewHolder instanceof IntegerViewHolder) {
            IntegerViewHolder integerViewHolder = (IntegerViewHolder) viewHolder;

            integerViewHolder.value.setText(item.toString());
        }
    }

    @Override
    public ViewHolder onCreateFooter(ViewGroup parent) {
        View footer = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_footer, parent, false);

        return new FooterViewHolder(footer);
    }

    @Override
    public void onBindFooter(ViewHolder viewHolder) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.integer_view, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.value);

        return new IntegerViewHolder(view, textView);
    }

    int previousValue = 0;
    @Override
    public boolean needsSectionBefore(Integer item) {
        boolean needsSection = false;

        if (item > previousValue) {
            previousValue = item;
            needsSection = true;
        }

        return needsSection;
    }

    @Override
    public ViewHolder onCreateSectionViewHolder(ViewGroup parent) {
        View sectionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_section, parent, false);

        TextView titleView = (TextView) sectionView.findViewById(R.id.title);

        return new SectionViewHolder(sectionView, titleView);
    }

    @Override
    public void onBindSectionViewHolder(ViewHolder viewHolder, Integer item) {
        if (viewHolder instanceof SectionViewHolder) {
            SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;

            sectionViewHolder.title.setText(item.toString());
        }
    }

    public class IntegerViewHolder extends ViewHolder {
        TextView value;

        public IntegerViewHolder(View itemView, TextView value) {
            super(itemView);
            this.value = value;
        }
    }

    public class SectionViewHolder extends ViewHolder {
        TextView title;


        public SectionViewHolder(View itemView, TextView title) {
            super(itemView);
            this.title = title;
        }
    }

    public class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
