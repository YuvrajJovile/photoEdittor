package com.photoedittor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ahmedadeltito.photoeditorsdk.BrushDrawingView;
import com.ahmedadeltito.photoeditorsdk.OnPhotoEditorSDKListener;
import com.ahmedadeltito.photoeditorsdk.PhotoEditorSDK;
import com.ahmedadeltito.photoeditorsdk.ViewType;

public class MainActivity extends MediaActivity {


    private boolean mIsGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openUserGallery(View view) {
        openGallery();
        mIsGallery = true;
    }

    public void openUserCamera(View view) {
        startCameraActivity();
    }

    @Override
    protected void onPhotoTaken() {
        Intent intent = new Intent(MainActivity.this, PhotoEditorActivity.class);
        intent.putExtra("selectedImagePath", selectedImagePath);
        intent.putExtra("is_gallery", mIsGallery);
        startActivity(intent);
    }


}
