package com.photoedittor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahmedadeltito.photoeditorsdk.BrushDrawingView;
import com.ahmedadeltito.photoeditorsdk.OnPhotoEditorSDKListener;
import com.ahmedadeltito.photoeditorsdk.PhotoEditorSDK;
import com.ahmedadeltito.photoeditorsdk.ViewType;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.photoedittor.MediaActivity.stringIsNotEmpty;

public class PhotoEditorActivity extends MediaActivity implements OnPhotoEditorSDKListener {

    private final String TAG = "PhotoEditorActivity";
    private RelativeLayout mParentLay, mScribbleLay, mTopLay, mBottomlay, mDeleteLay, mCropLayoutTop, mBottomSheetlay;
    private View mCropLayoutBottom;
    private ImageView mIvParent;
    //mIvClose, mIvAddImage, mIvScribble, mIvAddText, mIvClearAll, mIvSave, mIvErraseScribble, mIvScribbleDone;
    private PhotoEditorSDK mPhotoEditorSDK;
    private BrushDrawingView mBrushDrawingView;
    private RecyclerView mRvColorRecycler;
    private ArrayList<Integer> colorPickerColors;
    private int mColorCodeAddText = -1;
    private ColorPickerAdapter mColorPickerAdapter;
    private CropImageView mIvCropImageView;
    private Bitmap mImageDataBitmap = null;
    private boolean mIsGallery, mIsCroppingOnProcess, mIsScribbleOnProcess;
    private int mNoOfViewsAppended = -1;
    //private BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor);

        mRvColorRecycler = findViewById(R.id.rv_color_recycler);
        mBrushDrawingView = findViewById(R.id.dv_drawing_view);
        mParentLay = findViewById(R.id.v_parent);
        mScribbleLay = findViewById(R.id.v_scribble_lay);
        mTopLay = findViewById(R.id.v_top_lay);
        mBottomlay = findViewById(R.id.v_bottom_lay);
        mDeleteLay = findViewById(R.id.v_delete_lay);
        mIvParent = findViewById(R.id.iv_parent);
        mIvCropImageView = findViewById(R.id.iv_crop_image_view);
        mCropLayoutTop = findViewById(R.id.v_crop_lay_top);
        mCropLayoutBottom = findViewById(R.id.v_crop_lay_bottom);
        mBottomSheetlay = findViewById(R.id.v_bottom_sheet_lay);
        mIvCropImageView.setVisibility(View.GONE);
        mCropLayoutTop.setVisibility(View.GONE);
        mCropLayoutBottom.setVisibility(View.GONE);

       /* mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetlay);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int pNewState) {
                switch (pNewState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });*/


        /*mIvClose = findViewById(R.id.iv_close);
        mIvAddImage = findViewById(R.id.iv_add_image);
        mIvScribble = findViewById(R.id.iv_scribble);
        mIvAddText = findViewById(R.id.iv_add_text);
        mIvClearAll = findViewById(R.id.iv_clear_all);
        mIvScribbleDone = findViewById(R.id.iv_scribble_done);
        mIvErraseScribble = findViewById(R.id.iv_erase_scribble);
        mIvSave = findViewById(R.id.iv_save);*/


        mRvColorRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        String selectedImagePath = getIntent().getExtras().getString("selectedImagePath");
        mIsGallery = getIntent().getExtras().getBoolean("is_gallery");
        mImageDataBitmap = BitmapFactory.decodeFile(selectedImagePath, options);


        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(getResources().getColor(R.color.black));
        colorPickerColors.add(getResources().getColor(R.color.blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.brown_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.green_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.sky_blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.violet_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.white));
        colorPickerColors.add(getResources().getColor(R.color.yellow_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.yellow_green_color_picker));

        mColorPickerAdapter = new ColorPickerAdapter(PhotoEditorActivity.this, colorPickerColors);
        mRvColorRecycler.setAdapter(mColorPickerAdapter);


        mIvParent.setImageBitmap(mImageDataBitmap);

        mPhotoEditorSDK = new PhotoEditorSDK.PhotoEditorSDKBuilder(PhotoEditorActivity.this)
                .parentView(mParentLay) // add parent image view
                .childView(mIvParent) // add the desired image view
                .deleteView(mDeleteLay) // add the deleted view that will appear during the movement of the views
                .brushDrawingView(mBrushDrawingView) // add the brush drawing view that is responsible for drawing on the image view
                .buildPhotoEditorSDK(); // build photo editor sdk
        mPhotoEditorSDK.setOnPhotoEditorSDKListener(this);
    }

    @Override
    protected void onPhotoTaken() {
        mNoOfViewsAppended = -1;
        mImageDataBitmap.recycle();
        mImageDataBitmap = null;
        mImageDataBitmap = BitmapFactory.decodeFile(selectedImagePath);
        mIvParent.setImageBitmap(mImageDataBitmap);
    }


    public void toggleBottomSheet() {
        /*if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }*/
    }

    public void save(View view) {
        //save

       /* RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mParentLay.setLayoutParams(layoutParams);*/

        new CountDownTimer(1000, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageName = "IMG_" + timeStamp + ".jpg";
                Intent returnIntent = new Intent();
                returnIntent.putExtra("imagePath", mPhotoEditorSDK.saveImage("PhotoEditorSDK", imageName));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }.start();


    }

    public void clearAll(View view) {
        //clear all
        if (mNoOfViewsAppended > 0) {
            try {
                mPhotoEditorSDK.clearAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addText(View view) {
        //add text

        openAddTextPopupWindow("", getResources().getColor(R.color.white_mixed));
    }


    private void openAddTextPopupWindow(String text, int colorCode) {

        mColorCodeAddText = colorCode;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
        final EditText addTextEditText = addTextPopupWindowRootView.findViewById(R.id.et_add_text);
        TextView addTextDoneTextView = addTextPopupWindowRootView.findViewById(R.id.tv_done);
        RecyclerView addTextColorPickerRecyclerView = addTextPopupWindowRootView.findViewById(R.id.rv_color_picker);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PhotoEditorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(PhotoEditorActivity.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                addTextEditText.setTextColor(colorCode);
                mColorCodeAddText = colorCode;
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);


        addTextEditText.setTextColor(mColorCodeAddText);

        if (stringIsNotEmpty(text)) {
            addTextEditText.setText(text);
            addTextEditText.requestFocus(addTextEditText.getText().length());
        }

        final PopupWindow pop = new PopupWindow(PhotoEditorActivity.this);
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditorSDK.addText(addTextEditText.getText().toString(), mColorCodeAddText);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                pop.dismiss();
            }
        });
    }


    public void addScribble(View view) {
        //add scribble

        mIsScribbleOnProcess = true;

        mTopLay.setVisibility(View.GONE);
        mBottomlay.setVisibility(View.GONE);
        mScribbleLay.setVisibility(View.VISIBLE);
        mRvColorRecycler.setVisibility(View.VISIBLE);

        mPhotoEditorSDK.setBrushDrawingMode(true);

        mColorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mPhotoEditorSDK.setBrushColor(colorCode);
            }
        });

    }

    public void addImage(View view) {
        //add image

        toggleBottomSheet();
    }

    public void cropImage(View view) {
        //crop image

        if (mImageDataBitmap != null) {

            mIsCroppingOnProcess = true;

            mIvParent.setVisibility(View.GONE);
            mIvCropImageView.setVisibility(View.VISIBLE);
            mCropLayoutTop.setVisibility(View.VISIBLE);
            mCropLayoutBottom.setVisibility(View.VISIBLE);

            mIvCropImageView.setImageBitmap(mImageDataBitmap);
            mIvCropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
                @Override
                public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

                    finishCrop(view.getCroppedImage());

                }
            });
        }
    }

    private void finishCrop(Bitmap pBitmap) {
        mIsCroppingOnProcess = false;

        mIvCropImageView.setVisibility(View.GONE);
        mIvParent.setVisibility(View.VISIBLE);

        mCropLayoutTop.setVisibility(View.GONE);
        mCropLayoutBottom.setVisibility(View.GONE);
        mIvParent.setImageBitmap(pBitmap);
    }

    @Override
    public void onEditTextChangeListener(String pText, int pColorCode) {
        openAddTextPopupWindow(pText, pColorCode);
    }

    @Override
    public void onAddViewListener(ViewType viewType, int i) {
        mNoOfViewsAppended = i;
    }

    @Override
    public void onRemoveViewListener(int i) {
        mNoOfViewsAppended = i;
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        if (viewType.equals(ViewType.IMAGE) || viewType.equals(ViewType.TEXT) || viewType.equals(ViewType.EMOJI)) {
            mBottomlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        if (viewType.equals(ViewType.IMAGE) || viewType.equals(ViewType.TEXT) || viewType.equals(ViewType.EMOJI)) {
            mBottomlay.setVisibility(View.VISIBLE);
        }
    }

    public void eraseScribble(View view) {
        //errase scribble brush
        mIsScribbleOnProcess = true;
        mPhotoEditorSDK.brushEraser();
    }

    public void scribbleDone(View view) {
        //done Scribble

        finishScribble();
    }

    private void finishScribble() {
        mIsScribbleOnProcess = false;
        mScribbleLay.setVisibility(View.GONE);
        mTopLay.setVisibility(View.VISIBLE);
        mBottomlay.setVisibility(View.VISIBLE);
        mRvColorRecycler.setVisibility(View.GONE);
        mPhotoEditorSDK.setBrushDrawingMode(false);
    }

    public void onCropDone(View view) {
        //on crop completed
        mIvCropImageView.getCroppedImageAsync();
    }

    public void onClickClose(View view) {
        //on selecting close option

        if (mIsGallery)
            openGallery();
        else
            startCameraActivity();
    }

    @Override
    public void onBackPressed() {

        mIsGallery = false;
        if (mIsCroppingOnProcess) {
            finishCrop(mImageDataBitmap);
        } else if (mIsScribbleOnProcess) {
            finishScribble();
        } else
            super.onBackPressed();
    }

    public void onCropBack(View view) {
        //on select back icon while cropping
        finishCrop(mImageDataBitmap);
    }
}
