package as.hn.com.hnprojectapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import database.AppDBSchema.AppDBSchema;
import database.AppDBSchema.AppDBSchema.ChatTable;

public class DbBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "chatEntities.db";

    //在当前context下面创建database
    public DbBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "create table " + ChatTable.NAME + "(" +
                " _id integer primary key autoincrement " + ", " +
                ChatTable.Cols.UUID + ", " +
                ChatTable.Cols.CHATNAME + ", " +
                ChatTable.Cols.CHATFACE + ", " +
                ChatTable.Cols.CHATMEMBERS + ", " +
                ChatTable.Cols.NOTIFY + ", " +
                ChatTable.Cols.CHATMESSAGES + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
