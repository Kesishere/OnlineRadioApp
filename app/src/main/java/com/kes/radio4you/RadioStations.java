package com.kes.radio4you;

        import android.app.Activity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;

public class RadioStations extends Activity {

    private DataBaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    ArrayList <HashMap<String, Object>> names = new ArrayList<HashMap<String, Object>>();
    HashMap<String,Object> station;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_stations);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        mDBHelper = new DataBaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        namesUpdate();
        String[] from = { "name", "link"};
        int[] to = { R.id.name, R.id.link};
        final SimpleAdapter adapter = new SimpleAdapter(this, names, R.layout.station_list,
                from, to);

        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(i);
                String result= (String) obj.get("link");
                Log.wtf("TAG",result);

                Intent intent = new Intent();
                intent.putExtra("station", result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    public void namesUpdate(){
        Cursor cursor = mDb.rawQuery("SELECT * FROM stations" ,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            station = new HashMap<String,Object>();
            station.put("name",  cursor.getString(1));
            station.put("link",  cursor.getString(2));
            names.add(station);
            cursor.moveToNext();
        }
        cursor.close();

    }
}
