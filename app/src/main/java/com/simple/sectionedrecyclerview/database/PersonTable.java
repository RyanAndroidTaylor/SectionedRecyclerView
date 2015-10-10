package com.simple.sectionedrecyclerview.database;

public class PersonTable {
    private static TableBuilder mTableBuilder = TableBuilder.getInstance();

    public static final String TABLE_NAME = mTableBuilder.open("PersonTable");

    public static final String FIRST_NAME = mTableBuilder.appendText("FirstName");
    public static final String LAST_NAME = mTableBuilder.appendText("LastName");
    public static final String AGE = mTableBuilder.appendInt("Age");

    public static final String CREATE = mTableBuilder.retrieveCreateString();

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sectionadapter.PersonTable";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sectionadapter.PersonTable";
}
