package com.simple.sectionedrecyclerview;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deserttowersprogramming.sectioned.CursorRecyclerViewSectionAdapter;
import com.simple.sectionedrecyclerview.database.PersonTable;

public class CursorListAdapter extends CursorRecyclerViewSectionAdapter<ViewHolder> {

    public CursorListAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        if (viewHolder instanceof PersonViewHolder) {
            PersonViewHolder personViewHolder = (PersonViewHolder) viewHolder;

            String firstName = cursor.getString(cursor.getColumnIndex(PersonTable.FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(PersonTable.LAST_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(PersonTable.AGE));

            personViewHolder.firstName.setText(firstName);
            personViewHolder.lastName.setText(lastName);
            personViewHolder.age.setText(Integer.toString(age));
        }
    }

    @Override
    public ViewHolder onCreateFooter(ViewGroup parent) {
        View footer = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_footer, parent, false);

        return new FooterViewHolder(footer);
    }

    @Override
    public void onBindFooter(ViewHolder viewHolder) {
        // Since the footer is invisible there is nothing to do here
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View personView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_view, parent, false);

        TextView firstNameView = (TextView) personView.findViewById(R.id.first_name);
        TextView lastNameView = (TextView) personView.findViewById(R.id.last_name);
        TextView ageView = (TextView) personView.findViewById(R.id.age);

        return new PersonViewHolder(personView, firstNameView, lastNameView, ageView);
    }

    @Override
    public ViewHolder onCreateSectionViewHolder(ViewGroup parent) {
        View sectionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_section, parent, false);

        TextView titleView = (TextView) sectionView.findViewById(R.id.title);

        return new SectionViewHolder(sectionView, titleView);
    }

    @Override
    public void onBindSectionViewHolder(ViewHolder viewHolder, Cursor cursor) {
        int age = cursor.getInt(cursor.getColumnIndex(PersonTable.AGE));

        if (viewHolder instanceof SectionViewHolder) {
            SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;

            sectionViewHolder.title.setText("Age: " + Integer.toString(age));
        }
    }

    int previousAge = 0;
    @Override
    public boolean needsSectionBefore(Cursor item) {
        boolean needsSection = false;
        int currentAge = item.getInt(item.getColumnIndex(PersonTable.AGE));

        if (currentAge > previousAge) {
            previousAge = currentAge;
            needsSection = true;
        }

        return needsSection;
    }

    public class PersonViewHolder extends ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView age;

        public PersonViewHolder(View itemView, TextView firstName, TextView lastName, TextView age) {
            super(itemView);
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
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
