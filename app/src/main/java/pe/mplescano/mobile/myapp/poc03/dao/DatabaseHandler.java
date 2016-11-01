package pe.mplescano.mobile.myapp.poc03.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mplescano on 03/10/2016.
 *
 * @see http://stackoverflow.com/questions/24095211/replacing-sqlite-net-bigint-with-integer-for-autoincrement-primary-key-constra
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    protected static final String DATABASE_NAME = "db_location";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //_id see https://developer.android.com/reference/android/widget/CursorAdapter.html
        //AUTOINCREMENT
        String sql = "CREATE TABLE point_location\n" +
                "(\n" +
                "  _id integer NOT NULL\n" +
                "        CONSTRAINT Key1 PRIMARY KEY,\n" +
                "  address character varying(200),\n" +
                "  description TEXT,\n" +
                "  longitude decimal(9, 6) NOT NULL,\n" +
                "  latitude decimal(9, 6) NOT NULL,\n" +
                "  dateCreation timestamp with time zone NOT NULL,\n" +
                "  dateModification timestamp with time zone\n" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {

        }
    }
}
