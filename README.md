# getWorld

Just a Util .

# 前言

感谢 Boredream [http://www.jikexueyuan.com/course/1642.html](http://www.jikexueyuan.com/course/1642.html)

# 使用方法

### 第一步 调用

	// 拍照
	Uri imageUri = GetWorld.creatImageUri(this); // 拍照需要自定义一个地址
    GetWorld.TakePhoto(this, imageUri);

	// 相册
	GetWorld.FindPhoto(this);

### 第二步  回调

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
                return ;
            }
            if(data != null ){
                Uri uri = data.getData() ;
                photoImg.setImageURI(uri);
            }
        }
    }

图片都是以 Uri地址的形式返回的。 （拍照是我们自己创建的Uri 而相册是系统的Uri）
这里我们使用了Fresco来加载图片，方便。大家也可以自行把Uri转换成Bitmap或者File或者其他的，来加载图片。

### 第三步 Manifest
**
权限**

	<!-- 相机权限 -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

----------

**三星注意** 三星手机拍照屏幕会旋转，所以这里要在Activity加上这句话，不然之前的全局变量imageUri会变成null


	<activity
        android:name=".MainActivity"
        android:configChanges="orientation|screenSize"/>