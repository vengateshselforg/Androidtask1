package nfnlabs.test.task1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nfnlabs.test.task1.model.Fields;

public class DBHelper {
    private static DBHelper mInstance;
    private Task1DbOpenHelper dbOpenHelper;

    public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    private DBHelper(Context context) {
        dbOpenHelper = new Task1DbOpenHelper(context);
    }

    public Task1DbOpenHelper geDBInstance() {
        return dbOpenHelper;
    }

    public boolean addFieldsToFavourite(Fields fields, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put("title", fields.getName());
        values.put("description", fields.getDescription());
        values.put("image_url", imageUrl);
        values.put("is_favourited", 1);

        SQLiteDatabase db = geDBInstance().getWritableDatabase();
        boolean createSuccessful = db.insert("fields", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public List<Fields> getFavouritedFields() {

        List<Fields> favouritesList = new ArrayList<>();

        String sql = "SELECT * FROM fields ORDER BY id ASC";

        SQLiteDatabase db = geDBInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("image_url"));
                Integer isFavourited = cursor.getInt(cursor.getColumnIndex("is_favourited"));

                Fields fields = new Fields();
                fields.setFieldId(id);
                fields.setmName(title);
                fields.setmDescription(description);
                fields.setmUrl(imageUrl);
                fields.setIsFavourited(isFavourited);
                favouritesList.add(fields);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return favouritesList;
    }

    public boolean deleteFromFavourite(Integer fieldId) {
        SQLiteDatabase db = geDBInstance().getWritableDatabase();
        return db.delete("fields", "id ='" + fieldId + "'", null) > 0;
    }
}
