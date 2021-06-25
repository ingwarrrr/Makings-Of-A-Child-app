package com.example.themakingsofachild.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MyDBManager(private val context: Context) {
    val myDBHelper = MyDBHelper(context)
    var db: SQLiteDatabase? = null
    val sortOrder = "${MyDBName.COLUMN_NAME} ASC"
    val sortInclinationOrder = "${MyDBName.COLUMN_INC_NAME} ASC"

    fun openDatabase() {
        val dbFile = context.getDatabasePath(MyDBName.DATABASE_NAME)

        if (!dbFile.exists()) {
            try {
                val checkDB = context.openOrCreateDatabase(MyDBName.DATABASE_NAME, Context.MODE_PRIVATE, null)

                checkDB?.close()
                copyDatabase(dbFile)
            } catch (e: IOException) {
                throw RuntimeException("Error creating source database", e)
            }
        }
        db = SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY)
    }

    @SuppressLint("WrongConstant")
    private fun copyDatabase(dbFile: File) {
        val `is` = context.assets.open(MyDBName.DATABASE_NAME)
        val os = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)
        while (`is`.read(buffer) > 0) {
            os.write(buffer)
            Log.d("#DB", "writing>>")
        }

        os.flush()
        os.close()
        `is`.close()
        Log.d("#DB", "completed..")
    }

    fun readDataBase(tableName: String) : ArrayList<String>{
        val dataList = ArrayList<String>()

        val cursor = db?.query(tableName, null, null, null, null, null, sortOrder)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(MyDBName.COLUMN_NAME))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    fun readDataBaseInc(tableName: String) : ArrayList<String>{
        val dataList = ArrayList<String>()

        val cursor = db?.query(tableName, null, null, null, null, null, sortInclinationOrder)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(MyDBName.COLUMN_INC_NAME))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    fun closeDataBase() {
        myDBHelper.close()
    }

    fun readDataBaseForGender(tableName: String) : ArrayList<String>{
        val dataList = ArrayList<String>()

        val cursor = db?.query(tableName, null, null, null, null, null, sortOrder)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(MyDBName.COLUMN_GENDER))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }
}