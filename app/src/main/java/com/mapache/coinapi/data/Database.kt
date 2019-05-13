package com.mapache.coinapi.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${DatabaseContract.CoinEntry.TABLE_NAME} (" +
            "${DatabaseContract.CoinEntry.COLUMN_ID} TEXT PRIMARY KEY," +
            "${DatabaseContract.CoinEntry.COLUMN_NAME} TEXT," +
            "${DatabaseContract.CoinEntry.COLUMN_COUNTRY} TEXT," +
            "${DatabaseContract.CoinEntry.COLUMN_VALUE} INT," +
            "${DatabaseContract.CoinEntry.COLUMN_VALUE_US} DOUBLE,"+
            "${DatabaseContract.CoinEntry.COLUMN_YEAR} INT," +
            "${DatabaseContract.CoinEntry.COLUMN_REVIEW} TEXT," +
            "${DatabaseContract.CoinEntry.COLUMN_ISAVAILABLE} BOOLEAN," +
            "${DatabaseContract.CoinEntry.COLUMN_IMG} TEXT)"

private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${DatabaseContract.CoinEntry.TABLE_NAME}"

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES) // TODO (9) Ejecuta el DDL.
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES) // Como esta base de datos se utilizará como caché, no nos interesa conservar los datos almacenados en la versión anterior.
        onCreate(db) // Se vuelve a crear la base.
    }

    companion object {
        const val DATABASE_NAME = "miprimerabase.db"
        const val DATABASE_VERSION = 1
    }
}