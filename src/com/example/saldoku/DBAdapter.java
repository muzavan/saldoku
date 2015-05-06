package com.example.saldoku;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "saldo";
	public static final String ID = "id";
	public static final String JENIS = "jenis";
	public static final String NOMINAL = "nominal";
	public static final String KETERANGAN = "keterangan";

	// Constructor DataKamus untuk initiate database
	public DBAdapter(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	public void createTable(SQLiteDatabase db) {
		//db.execSQL("DROP TABLE IF EXISTS SALDO");
		db.execSQL("CREATE  TABLE if not exists SALDO"
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,WAKTU TEXT,JENIS TEXT, NOMINAL INTEGER,KETERANGAN TEXT);");
	} // nama tipe sifat?

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
