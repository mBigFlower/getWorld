package com.flowerfat.photolibrary;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Bigflower on 2015/11/21.
 *
 * tools
 */
public class Util {

    /**
     * find the filePath by uri
     * @param context
     * @param uri
     * @return
     */
    public static String Uri2FilePath(Context context, Uri uri) {
        String filePath = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(column_index);
        }
        cursor.close();
        return filePath;
    }

}
