package com.flowerfat.photolibrary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by 明明大美女 on 2015/11/20.
 *
 * Thank for boredream ,
 * I made a Util before, but it is not good .
 * Yesterday I find some classes from boredream.
 * So I pick some useful to make this class.
 *
 */
public class GetWorld {

    // Two mode
    public static final int REQUEST_CODE_FROM_CAMERA = 1;
    public static final int REQUEST_CODE_FROM_ALBUM = 2;

    /**
     * make photo by camera
     * before use it , we usually creat a uri for the picture by function'creatImageUri'
     * @param activity
     * @param uri the pic's uri
     */
    public static void TakePhoto(Activity activity, Uri uri){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        activity.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
    }

    public static void FindPhoto(Activity activity){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);
    }

    /**
     * creat a uri for takePhoto
     *
     * I don't know the name , what's the meaning of "GetWorld"?
     * @param context
     * @return
     */
    public static Uri creatImageUri(Context context) {
        String name = "GetWorld" + System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;
    }

    /**
     * if we choose the cancel rather than the save picture. wo should delete the uri, which we created before.
     * @param context
     * @param uri the old uri we created before
     */
    public static void deleteUri(Context context, Uri uri){
        context.getContentResolver().delete(uri, null, null);
    }

}
