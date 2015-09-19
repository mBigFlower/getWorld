package com.flowerfat.getworld;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by 明明大美女 on 2015/9/18.
 *
 * 使用说明：详见github： https://github.com/mBigFlower/getWorld
 *
 */
public class GetWorldUtil extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int FIND_PHOTO = 2;
    public static final int CROP_PHOTO = 3;

    public static final String IMG_TPYE_JPG = ".jpg";
    public static final String IMG_TPYE_PNG = ".png";

    private String photoName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.i("TakePhotoUtil", "onCreate");

        // 败家三星手机，胡乱旋转。  故而有了这个判断
        // 只有在竖屏的时候才能用 PORTRAIT:竖屏   ； LANDSCAPE:横屏
        Configuration mConfiguration = this.getResources().getConfiguration();
        if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            configTakePhoto();
        } else {
            this.photoName = savedInstanceState.getString("name");
        }
    }

    /**
     * 照相前的配置
     * <p/>
     * 这里原谅我太菜，为啥在名字那里，加了个随机数呢？
     * 因为你重复照相的时候，不同的照片名字一样，而如果用三方的图片加载，名字一样她就用缓存了，
     * 所以如果名字不变，重复照相后图片不变
     * <p/>
     * 以后再来完善
     */
    private void configTakePhoto() {
        int intentWay = getIntent().getIntExtra("way", 0);
        this.photoName = getIntent().getStringExtra("photoName") + new Random().nextInt(10000) + getIntent().getStringExtra("photoType");

        if (intentWay == TAKE_PHOTO) {
            takePhoto(getUriPath());
        } else if (intentWay == FIND_PHOTO) {
            findPhoto(getUriPath());
        }
    }

    /**
     * 拍照
     * <p/>
     * uri 文件的uri
     */
    private void takePhoto(Uri uri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void findPhoto(Uri uri) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("scale", true);
        startActivityForResult(intent, FIND_PHOTO);
    }

    private Uri getUriPath() {
        File outputImage = new File(Environment.getExternalStorageDirectory(), photoName);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ;
        }
        return Uri.fromFile(outputImage);
    }


    /**
     * 三星手机拍照屏幕会自动旋转，所以这里把文件名存一下，返回值根据文件名来定
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", photoName);
    }

    /**
     * 进入该activity的方法
     * 该功能为【拍照】获取相片
     *
     * @param startingActivity 正在运行的activity（由startingActivity跳转到本Activity）
     * @param photoName        图片文件的名字
     * @param photoType        图片文件的类型
     */
    public static void TakePhoto(Activity startingActivity, String photoName, String photoType) {
        Intent intent = new Intent(startingActivity, GetWorldUtil.class);
        intent.putExtra("way", TAKE_PHOTO);
        intent.putExtra("photoName", photoName);
        intent.putExtra("photoType", photoType);
        try {
            startingActivity.startActivityForResult(intent, TAKE_PHOTO);
        } catch (Exception e) {
            Log.e("GetWorldUtil", e.toString());
            Log.e("GetWorldUtil", "请将本Activity加入到Manifest中");
        }
    }

    /**
     * 进入该activity的方法
     * 该功能为【相册】获取相片
     *
     * @param startingActivity 正在运行的activity（由startingActivity跳转到本Activity）
     * @param photoName        图片文件的名字
     * @param photoType        图片文件的类型
     */
    public static void FindPhoto(Activity startingActivity, String photoName, String photoType) {
        Intent intent = new Intent(startingActivity, GetWorldUtil.class);
        intent.putExtra("way", FIND_PHOTO);
        intent.putExtra("photoName", photoName);
        intent.putExtra("photoType", photoType);
        try {
            startingActivity.startActivityForResult(intent, FIND_PHOTO);
        } catch (Exception e) {
            Log.e("GetWorldUtil", e.toString());
            Log.e("GetWorldUtil", "请将本Activity加入到Manifest中");
        }
    }

    /**
     * 拍照后的返回
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", "requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    onResult(TAKE_PHOTO, photoName);
                    break;
                case FIND_PHOTO:
                    // 这个第一个参数有点乱，要重新捋捋
                    onResult(requestCode, data.getData().toString());
                    break;
                default:
                    break;
            }
        } else {
            finish();
        }
    }

    private void onResult(int resultCode, String photoName) {
        Intent mIntent = new Intent();
        mIntent.putExtra("photoName", photoName);
        // 设置结果，并进行传送
        this.setResult(resultCode, mIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TakePhotoUtil", "onDestroy");
    }
}
