package com.flowerfat.photolibrary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by 明明大美女 on 2015/11/20.
 * <p/>
 * Thank for boredream ,
 * I made a Util before, but it is not good .
 * Yesterday I find some classes from boredream.
 * So I pick some useful to make this class.
 */
public class GetWorld {

    // Two mode
    public static final int REQUEST_CODE_FROM_CAMERA = 1;
    public static final int REQUEST_CODE_FROM_ALBUM = 2;
    public static final int REQUEST_CODE_FROM_CROP = 3;

    /**
     * make photo by camera
     * before use it , we usually creat a uri for the picture by function'creatImageUri'
     *
     * @param activity
     * @param uri      the pic's uri
     */
    public static void TakePhoto(Activity activity, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        activity.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
    }

    /**
     * we can get the photo by the data.getData() in the onActivityResult(...)
     * pick the photo from the album .
     *
     * @param activity
     */
    public static void FindPhoto(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);
    }

    /**
     * find the photo from the album, and the result is Uri witch we created before
     * it's different with FindPhoto,
     *
     * @param activity
     * @return the uri which the photo we pick
     */
    public static Uri FindPhotoUri(Activity activity) {
        Uri uri = creatImageUri(activity);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setType("image/*");

        activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);

        return uri;
    }


    /**
     * get the photo and crop it .
     * here I find an important thing.
     * we can use this( intent.putExtra("output", uri) ) to put the pic into the uri
     *
     * @param activity
     * @param uri
     */
    public static void FindPhotoCrop(Activity activity, Uri uri) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", uri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 180);// 输出图片大小
        intent.putExtra("outputY", 180);
        activity.startActivityForResult(intent, 100);
    }

    /**
     * here we detect the version, because the new version's way is different with the old .
     *
     * @param activity
     * @param uri
     */
    public static void CropPhoto(final Activity activity, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Uri selectedImage = uri;
            String imagePath = PictureHelper.getPath(activity, selectedImage); // 获取图片的绝对路径
            Uri newUri = Uri.parse("file:///" + imagePath); // 将绝对路径转换为URL
            intent.setDataAndType(newUri, "image/*");

            activity.startActivityForResult(intent, REQUEST_CODE_FROM_CROP);// 4.4版本
        } else {
            intent.setDataAndType(uri, "image/*");
            activity.startActivityForResult(intent, REQUEST_CODE_FROM_CROP);// 4.4以下版本
        }
    }

    /**
     * 将Bitmap保存为图片
     *
     * @param bitmap
     * @return
     */
//    public String saveToSdCard(Context context, Bitmap bitmap) {
//        String files = CacheUtils.getCacheDirectory(context, true, "icon")
//                + new Date() + "_12";
//        File file = new File(files);
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
//                out.flush();
//                out.close();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file.getAbsolutePath();
//    }

    /**
     * creat a uri for takePhoto
     * I don't know the name , what's the meaning of "GetWorld"?
     *
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
     *
     * @param context
     * @param uri     the old uri we created before
     */
    public static void deleteUri(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);
    }

}
