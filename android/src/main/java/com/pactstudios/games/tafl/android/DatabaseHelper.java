package com.pactstudios.games.tafl.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public DatabaseHelper(Context context, String databaseName,
            CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        // This will be done inside the DatabaseService
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
            ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // This will be done inside the DatabaseService
    }
}
