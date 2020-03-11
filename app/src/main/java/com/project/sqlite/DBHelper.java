package com.project.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.project.data.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wallet.db";

    private SQLiteDatabase db;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public void openWritable(){
        db = this.getWritableDatabase();
    }

    public void openReadable(){
        db = this.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    // Contract Class for User Table
    public static class UserEntry implements BaseColumns {
        public static final String USER_TABLE = "user_t";
        public static final String _LOGIN_ID = "login_id";
        public static final String _PASSWORD = "password";
        public static final String _FIRST_NAME = "first_name";
        public static final String _LAST_NAME = "last_name";
    }
    // USER TABLE METHODS
    private static final String SQL_CREATE_USER_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                            "%s TEXT UNIQUE NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL);",
                    UserEntry.USER_TABLE,
                    UserEntry._ID,
                    UserEntry._LOGIN_ID,
                    UserEntry._PASSWORD,
                    UserEntry._FIRST_NAME,
                    UserEntry._LAST_NAME);

    private static final String SQL_DELETE_USER_ENTRIES =
            String.format("DROP TABLE IF EXISTS %s",
                    UserEntry.USER_TABLE);

    public long insertUser(User entry, String password){
        ContentValues values = new ContentValues();
        values.put(UserEntry._LOGIN_ID, entry.getLoginId());
        values.put(UserEntry._PASSWORD, password);
        values.put(UserEntry._FIRST_NAME, entry.getFirstName());
        values.put(UserEntry._LAST_NAME, entry.getLastName());

        return db.insert(UserEntry.USER_TABLE, null, values);
    }

    public User getUser(String username, String password){
        String[] projection = {
                UserEntry._ID,
                UserEntry._LOGIN_ID,
                UserEntry._FIRST_NAME,
                UserEntry._LAST_NAME
        };

        String selection = UserEntry._LOGIN_ID + " = ? AND "+ UserEntry._PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                UserEntry.USER_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();
        return new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(UserEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserEntry._LOGIN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserEntry._FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserEntry._LAST_NAME))
        );
    }

    public List<User> getUsers(){
        String tag = "SQLTAG";
        Cursor cursor = db.rawQuery(
                String.format("SELECT %s, %s, %s, %s FROM %s;",
                        UserEntry._LOGIN_ID,
                        UserEntry._ID,
                        UserEntry._FIRST_NAME,
                        UserEntry._LAST_NAME,
                        UserEntry.USER_TABLE), null);

        List<User> list = new ArrayList<>();

        while(cursor.moveToNext()){
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserEntry._LOGIN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserEntry._FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserEntry._LAST_NAME))
                    );

            Log.d(tag, user.toString());
            list.add(user);
        }

        return list;
    }

    public void updateUser(int id, String username, String firstName, String lastName){
        String where = UserEntry._ID + "=?";
        String[] condition = new String[] {""+id};
        ContentValues  contentValues = new ContentValues();
        contentValues.put(UserEntry._LOGIN_ID, username);
        contentValues.put(UserEntry._FIRST_NAME, firstName);
        contentValues.put(UserEntry._FIRST_NAME, lastName);
        db.update(UserEntry.USER_TABLE, contentValues, where, condition);

    }

    public long authUser(String username, String password){
        return 1;
    }
}
