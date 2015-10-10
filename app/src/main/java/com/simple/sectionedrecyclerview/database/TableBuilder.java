package com.simple.sectionedrecyclerview.database;

import java.util.ArrayList;

public class TableBuilder {
    public static final String DB_INTEGER = "INTEGER";
    public static final String DB_TEXT = "TEXT";
    public static final String DB_BOOLEAN = "INTEGER";
    private final String SPACE = " ";
    private final String PERIOD = ".";
    private final String COMMA = ",";
    private StringBuilder createString;
    private String currentTable;
    private ArrayList<String> columns;
    private boolean closed = true;
    private static TableBuilder tableBuild;

    private TableBuilder() {
    }

    public static TableBuilder getInstance() {
        if(tableBuild == null) {
            tableBuild = new TableBuilder();
        }

        return tableBuild;
    }

    public String open(String tableName) {
        if(!this.closed) {
            throw new IllegalStateException("Table builder must be closed before opening a new one");
        } else {
            this.closed = false;
            this.createString = new StringBuilder();
            this.columns = new ArrayList();
            this.currentTable = tableName;
            this.createString.append("CREATE TABLE ");
            this.createString.append(this.currentTable);
            this.createString.append(" ( ");
            this.createString.append("_id");
            this.createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            this.createString.append("UUID");
            this.createString.append(" ");
            this.createString.append("TEXT");
            this.columns.add(this.currentTable + "." + "_id");
            this.columns.add(this.currentTable + "." + "UUID");
            return tableName;
        }
    }

    public String openWithUuidForeignKeyRestraint(String tableName, String referenceTable) {
        if(!this.closed) {
            throw new IllegalStateException("Table builder must be closed before opening a new one");
        } else {
            this.closed = false;
            this.createString = new StringBuilder();
            this.columns = new ArrayList();
            this.currentTable = tableName;
            this.createString.append("CREATE TABLE ");
            this.createString.append(this.currentTable);
            this.createString.append(" ( ");
            this.createString.append("_id");
            this.createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            this.createString.append("UUID");
            this.createString.append(" ");
            this.createString.append("TEXT");
            this.createString.append(" REFERENCES ");
            this.createString.append(referenceTable);
            this.createString.append("(");
            this.createString.append("UUID");
            this.createString.append(")");
            this.columns.add(this.currentTable + "." + "_id");
            this.columns.add(this.currentTable + "." + "UUID");
            return tableName;
        }
    }

    public String appendText(String columnName) {
        return this.append(columnName, "TEXT");
    }

    public String appendInt(String columnName) {
        return this.append(columnName, "INTEGER");
    }

    public String appendBoolean(String columnName) {
        return this.append(columnName, "INTEGER");
    }

    private String append(String columnName, String type) {
        this.createString.append(",");
        this.createString.append(columnName);
        this.createString.append(" ");
        this.createString.append(type);
        this.columns.add(this.currentTable + "." + columnName);
        return columnName;
    }

    public String appendTextWithConstraint(String columnName, String constraint) {
        return this.appendWithConstraint(columnName, "TEXT", constraint);
    }

    public String appendIntWithConstraint(String columnName, String constraint) {
        return this.appendWithConstraint(columnName, "INTEGER", constraint);
    }

    public String appendBooleanWithConstraint(String columnName, String constraint) {
        return this.appendWithConstraint(columnName, "INTEGER", constraint);
    }

    private String appendWithConstraint(String columnName, String type, String constraint) {
        this.createString.append(",");
        this.createString.append(columnName);
        this.createString.append(" ");
        this.createString.append(type);
        this.createString.append(" ");
        this.createString.append(constraint);
        this.columns.add(this.currentTable + "." + columnName);
        return columnName;
    }

    public void end() {
        this.createString.append(")");
        this.closed = true;
    }

    public String retrieveDropString() {
        if(!this.closed) {
            this.end();
        }

        return this.currentTable + " DROP TABLE IF EXISTS ";
    }

    public String retrieveCreateString() {
        if(!this.closed) {
            this.end();
        }

        return this.createString.toString();
    }

    public String[] retrieveAllColumnsArray() {
        if(!this.closed) {
            this.end();
        }

        return (String[])this.columns.toArray(new String[this.columns.size()]);
    }

    private void tableBuilderNotClose() {
        throw new IllegalStateException("Table builder must be closed before opening a new one");
    }
}
