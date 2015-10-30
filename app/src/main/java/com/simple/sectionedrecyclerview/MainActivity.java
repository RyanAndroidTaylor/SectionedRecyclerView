package com.simple.sectionedrecyclerview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.simple.sectionedrecyclerview.database.PersonTable;
import com.simple.sectionedrecyclerview.database.Provider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText editFirstName;
    EditText editLastName;
    EditText editAge;

    Button add;

    CursorListAdapter cursorListAdapter;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editFirstName = (EditText) findViewById(R.id.first_name);
        editLastName = (EditText) findViewById(R.id.last_name);
        editAge = (EditText) findViewById(R.id.age);

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList();
//                savePerson();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        cursorListAdapter = new CursorListAdapter(null);
//        cursorListAdapter.enableFooter();
//        mRecyclerView.setAdapter(cursorListAdapter);

        listAdapter = new ListAdapter(this, getIntegerList());
        listAdapter.enableFooter();
        mRecyclerView.setAdapter(listAdapter);

//        getSupportLoaderManager().initLoader(1, null, this);
    }


    private void changeList() {
        listAdapter.updateList(getNewIntList());
    }

    private List<Integer> getIntegerList() {
        List<Integer> integers = new ArrayList<>();

        integers.add(5);
        integers.add(2);
        integers.add(9);
        integers.add(12);
        integers.add(5);
        integers.add(9);
        integers.add(9);

        Collections.sort(integers);

        return integers;
    }

    private List<Integer> getNewIntList() {
        List<Integer> integers = new ArrayList<>();

        integers.add(100);
        integers.add(95);
        integers.add(95);
        integers.add(37);
        integers.add(37);
        integers.add(37);
        integers.add(76);

        Collections.sort(integers);

        return integers;
    }

    private void savePerson() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonTable.FIRST_NAME, editFirstName.getText().toString());
        contentValues.put(PersonTable.LAST_NAME, editLastName.getText().toString());
        contentValues.put(PersonTable.AGE, editAge.getText().toString());

        getContentResolver().insert(Provider.PERSON_URI, contentValues);

        editFirstName.setText("");
        editLastName.setText("");
        editAge.setText("");
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Provider.PERSON_URI, null, null, null, PersonTable.AGE + " ASC");
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        cursorListAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        cursorListAdapter.changeCursor(null);
    }
}
