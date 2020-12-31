package org.dailyfreshbasket.co.in.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.dailyfreshbasket.co.in.models.LocalCartModel;

import java.util.ArrayList;

public class MyLocalCartDataBase {
		public static final String KEY_ROWID="_id";
		public static final String KEY_PID="pid_key";
	public static final String KEY_QUANTITY="quantity_key";
		public static final	String DATABASE_NAME="cart_dataBase";
		public static String DATABASE_TABLE="cart_list";
		public static final int DATABASE_VERSION=1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	public MyLocalCartDataBase(Context c){
		ourContext=c;
	}
	public MyLocalCartDataBase open() throws SQLException{
		ourHelper = new	DbHelper(ourContext);
		ourDatabase= ourHelper.getWritableDatabase();
		return this;
		}
	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_TABLE, null,DATABASE_VERSION);
			// TODO Auto-generated constructor stub
			}
			@Override
			public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE "+DATABASE_TABLE+" ("+
						KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
						KEY_PID+ " TEXT NOT NULL UNIQUE,"+
						KEY_QUANTITY+ " INTEGER NOT NULL);"
					);
			}
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
			}
		
		}
		
		public void close(){
			ourHelper.close();	
		}

		public long createEntry(String pid,int q) {
			// TODO Auto-generated method stub
			ContentValues cv=new ContentValues();
			cv.put(KEY_PID, pid);
			cv.put(KEY_QUANTITY, q);
			return ourDatabase.insert(DATABASE_TABLE, null, cv);
		}
		public ArrayList<LocalCartModel> getData() {
			// TODO Auto-generated method stub
			String[] col=new String[]{KEY_ROWID,KEY_PID,KEY_QUANTITY};
			Cursor c=ourDatabase.query(DATABASE_TABLE, col, null, null, null, null, null);
			String result="";
			int iRow=c.getColumnIndex(KEY_ROWID);
			int iName=c.getColumnIndex(KEY_PID);
			int iQuantity=c.getColumnIndex(KEY_QUANTITY);
			ArrayList<LocalCartModel> list=new ArrayList();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				list.add(new LocalCartModel(c.getString(iName),c.getInt(iQuantity),c.getInt(iRow)));
			}
			return list;
		}
		public void rmvall() {
			ourDatabase.delete(DATABASE_TABLE, null, null);
			ourDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DATABASE_TABLE + "'");
		}
		public void createOrUpdate(String pid, int q){
			ContentValues values = new ContentValues();
			values.put(KEY_PID,pid);
			values.put(KEY_QUANTITY,q);
			int id = getID(pid);
			if(id==-1)
				ourDatabase.insert(DATABASE_TABLE, null, values);
			else
				ourDatabase.update(DATABASE_TABLE, values, KEY_PID+" =?", new String[]{pid});
		}
		private int getID(String pid){
			Cursor c = ourDatabase.query(DATABASE_TABLE,new String[]{KEY_ROWID}, KEY_PID +" =? ",new String[]{pid},null,null,null,null);
			if (c.moveToFirst()) //if the row exist then return the id
				return c.getInt(c.getColumnIndex(KEY_ROWID));
			return -1;
		}
		public ArrayList<String> getSearchsList(){
				ArrayList<String> names=new ArrayList<String>();
				String[] col=new String[]{KEY_ROWID,KEY_PID,KEY_QUANTITY};
				Cursor c=ourDatabase.query(DATABASE_TABLE, col, null, null, null, null,KEY_ROWID+ " DESC" );
				int ipid=c.getColumnIndex(KEY_PID);
				int iquanityt=c.getColumnIndex(KEY_QUANTITY);
				int i=0;
				for(c.moveToFirst();i<10 && !c.isAfterLast();c.moveToNext())
				{
					i++;
					names.add(c.getString(ipid));
				}
				return names;
			}
		}
