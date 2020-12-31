package org.dailyfreshbasket.co.in.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecentSearchDataBase {
		public static final String KEY_ROWID="_id";
		public static final String KEY_SEARCH="search_key";
		public static final	String DATABASE_NAME="search_dataBase";
		public static String DATABASE_TABLE="search_list";
		public static final int DATABASE_VERSION=1;
		
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	private String new_table;
	public RecentSearchDataBase (Context c,String table_name){
		ourContext=c;
		new_table=table_name;
	}
	public RecentSearchDataBase open() throws SQLException{
		ourHelper = new	DbHelper(ourContext);
		ourDatabase= ourHelper.getWritableDatabase();
		return this;
		}
	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, new_table, null,DATABASE_VERSION);
			// TODO Auto-generated constructor stub
			}
			@Override
			public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE "+new_table+" ("+
						KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
						KEY_SEARCH+ " TEXT NOT NULL UNIQUE);"
					);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+new_table);
			onCreate(db);
			}
		
		}
		
		public void close(){
			ourHelper.close();	
		}

		public long createEntry(String key) {
			// TODO Auto-generated method stub
			ContentValues cv=new ContentValues();
			cv.put(KEY_SEARCH, key);
			return ourDatabase.insert(new_table, null, cv);
		}
		public String getData() {
			// TODO Auto-generated method stub
			String[] col=new String[]{KEY_ROWID,KEY_SEARCH};
			Cursor c=ourDatabase.query(new_table, col, null, null, null, null, null);
			String result="";
			int iRow=c.getColumnIndex(KEY_ROWID);
			int iName=c.getColumnIndex(KEY_SEARCH);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				result=result+c.getString(iRow)+" "+c.getString(iName);
			}
			return result;
		}

		public void rmvall() {
			ourDatabase.delete(new_table, null, null);
			ourDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + new_table + "'");
		}

		public ArrayList<String> getSearchsList(){
			ArrayList<String> names=new ArrayList<String>();
			String[] col=new String[]{KEY_ROWID,KEY_SEARCH};
			Cursor c=ourDatabase.query(new_table, col, null, null, null, null,KEY_ROWID+ " DESC" );
			int iHotness=c.getColumnIndex(KEY_SEARCH);
			int i=0;
			for(c.moveToFirst();i<10 && !c.isAfterLast();c.moveToNext())
			{
				i++;
				names.add(c.getString(iHotness));
			}
			return names;

		}
		
	}
