package com.flowerfat.getworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.flowerfat.photolibrary.GetWorld;

/**
 * this is a demo for pic
 *
 * @author BigFlower
 * @Email heilongjiang333@126.com
 * <p>
 * if you find something wrong , please email me .
 */
public class MainActivity extends AppCompatActivity {

    private SimpleDraweeView photoImg;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        photoImg = (SimpleDraweeView) findViewById(R.id.photoImg);
    }

    public void buttonClick(View v) {
        if (v.getId() == R.id.photoTake) {
            imageUri = GetWorld.creatImageUri(this);
            Log.i("MainActivity", "uri " + imageUri.toString());
            GetWorld.TakePhoto(this, imageUri);
        } else if (v.getId() == R.id.photoFind) {
            GetWorld.FindPhoto(this);
        } else if (v.getId() == R.id.photoCrop) {
            // 这里传递一个有效的imageUri!!!
            imageUri = GetWorld.creatImageUri(this);
            GetWorld.CropPhoto(this, imageUri);
        }else if (v.getId() == R.id.photoFindCrop) {
            // 这里传递一个有效的imageUri!!!
            imageUri = GetWorld.creatImageUri(this);
            GetWorld.FindPhotoCrop(this, imageUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GetWorld.REQUEST_CODE_FROM_CAMERA) {
            if (requestCode == RESULT_CANCELED) {
                GetWorld.deleteUri(this, imageUri);
            } else if (imageUri != null) {
                photoImg.setImageURI(imageUri);
            } else {
                Toast.makeText(MainActivity.this, "图片uri为空？请联系作者", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == GetWorld.REQUEST_CODE_FROM_ALBUM) {
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data != null) {
                Uri uri = data.getData();
                photoImg.setImageURI(uri);
            }
        }  else if (requestCode == GetWorld.REQUEST_CODE_FROM_CROP) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = extras.getParcelable("data");
                    // iconUrl = saveToSdCard(bitmap);

                }
            }
        }
        if(requestCode == 100 && imageUri != null){
             photoImg.setImageURI(imageUri);
        }
    }

}
