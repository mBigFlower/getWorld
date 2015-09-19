package com.flowerfat.getworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flowerfat.photolibrary.GetWorldUtil;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private ImageView photoImg;
    private Uri imageUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        photoImg = (ImageView) findViewById(R.id.photoImg);
    }

    public void buttonClick(View v){
        if(v.getId() == R.id.photoTake){
            GetWorldUtil.TakePhoto(this, "take", GetWorldUtil.IMG_TPYE_JPG);
        } else if(v.getId() == R.id.photoFind){
            GetWorldUtil.FindPhoto(this, "find", GetWorldUtil.IMG_TPYE_JPG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult _ last", "requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
        // 进入相册、相机以后点击了返回，则不做处理
        if (data == null)
            return;
        String mData = data.getStringExtra("photoName");
        if (mData != null) {
            Log.i("onActivityResult", mData);
            if (requestCode == 1) {
                File outputImage = new File(Environment.getExternalStorageDirectory(), mData);
                imageUri = Uri.fromFile(outputImage);
                Glide.with(this).load(imageUri).into(photoImg);
            } else if (requestCode == 2) {
                imageUri = Uri.parse(mData);
                Glide.with(MainActivity.this).load(imageUri).into(photoImg);
            }
        }
    }

}
