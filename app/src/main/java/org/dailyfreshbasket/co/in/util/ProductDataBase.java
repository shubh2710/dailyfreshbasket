package org.dailyfreshbasket.co.shop.util;

/**
 * Created by shubham on 2/22/2017.
 */
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDataBase {
    public static final String KEY_ROWID="_id";
    public static final String KEY_PRODUCTID="product_id";
    public static final String KEY_PRODUCTNAME="product_name";
    public static final String KEY_PRODUCTIMAGEPATH="product_image_path";
    public static final	String DATABASE_NAME="product_dataBase";
    public static String DATABASE_TABLE="product_list";
    public static final int DATABASE_VERSION=1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;
    private String new_table;
    public ProductDataBase(Context c,String table_name){
        ourContext=c;
        new_table=table_name;
    }
    public ProductDataBase open() throws SQLException{
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
                    KEY_PRODUCTNAME+ " TEXT NOT NULL, "+
                    KEY_PRODUCTIMAGEPATH +" TEXT NOT NULL, "+
                    KEY_PRODUCTID +" LONG NOT NULL);"
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
    public long createEntry(String songname, String song_path,long song_id) {
        // TODO Auto-generated method stub
        ContentValues cv=new ContentValues();
        cv.put(KEY_PRODUCTNAME, songname);
        cv.put(KEY_PRODUCTIMAGEPATH, song_path);
        cv.put(KEY_PRODUCTID, song_id);
        return ourDatabase.insert(new_table, null, cv);

    }
    public String getData() {
        // TODO Auto-generated method stub
        String[] col=new String[]{KEY_ROWID,KEY_PRODUCTNAME,KEY_PRODUCTIMAGEPATH,KEY_PRODUCTID};
        Cursor c=ourDatabase.query(new_table, col, null, null, null, null, null);
        String result="";
        int iRow=c.getColumnIndex(KEY_ROWID);
        int iName=c.getColumnIndex(KEY_PRODUCTNAME);
        int iHotness=c.getColumnIndex(KEY_PRODUCTIMAGEPATH);
        int iHotnessid=c.getColumnIndex(KEY_PRODUCTID);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result=result+c.getString(iRow)+" "+c.getString(iName)+ " "+c.getString(iHotness)+" "+c.getString(iHotnessid)+ "\n";
        }
        return result;
    }
    public String getName(String id)throws SQLException {
        // TODO Auto-generated method stub
        String[] col=new String[]{KEY_ROWID,KEY_PRODUCTNAME,KEY_PRODUCTIMAGEPATH,KEY_PRODUCTID};
        Cursor c=ourDatabase.query(new_table, col,KEY_PRODUCTID +"="+id, null, null, null, null);
        if(c!=null){
            c.moveToFirst();
            String name=c.getString(1);
            return name;
        }
        return null;
    }
    public String getImagepath(String id)throws SQLException {
        // TODO Auto-generated method stub
        String[] col=new String[]{KEY_ROWID,KEY_PRODUCTNAME,KEY_PRODUCTIMAGEPATH,KEY_PRODUCTID};
        Cursor c=ourDatabase.query(new_table, col,KEY_PRODUCTID +"="+id, null, null, null, null);
        if(c!=null){
            c.moveToFirst();
            String hot=c.getString(2);
            return hot;
        }
        return null;
    }
    public void rmvall() {
        ourDatabase.delete(new_table, null, null);
        ourDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + new_table + "'");
    }

    public ArrayList<String> getPathsImageList(){
        ArrayList<String> paths=new ArrayList<String>();
        String[] col=new String[]{KEY_ROWID,KEY_PRODUCTNAME,KEY_PRODUCTIMAGEPATH,KEY_PRODUCTID};
        Cursor c=ourDatabase.query(new_table, col, null, null, null, null,KEY_PRODUCTID+ " DESC" );
        int iHotness=c.getColumnIndex(KEY_PRODUCTIMAGEPATH);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            paths.add(c.getString(iHotness));
        }
        return paths;

    }
    public ArrayList<String> getNamesList(){
        ArrayList<String> names=new ArrayList<String>();
        String[] col=new String[]{KEY_ROWID,KEY_PRODUCTNAME,KEY_PRODUCTIMAGEPATH,KEY_PRODUCTID};
        Cursor c=ourDatabase.query(new_table, col, null, null, null, null,KEY_PRODUCTID+ " DESC" );
        int iHotness=c.getColumnIndex(KEY_PRODUCTNAME);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            names.add(c.getString(iHotness));
        }
        return names;

    }


}