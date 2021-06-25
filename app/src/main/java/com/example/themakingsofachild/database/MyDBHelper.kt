package com.example.themakingsofachild.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.themakingsofachild.database.MyDBName.Companion.SQL_DELETE_ENTRIES

class MyDBHelper(contex: Context) : SQLiteOpenHelper(contex, MyDBName.DATABASE_NAME,
    null, MyDBName.DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }


}