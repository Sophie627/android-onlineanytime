package com.austraila.online_anytime.Common;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.FormActivity;
import com.austraila.online_anytime.activitys.cameraActivity.CameraActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.austraila.online_anytime.activitys.cameraActivity.CameraActivity.Image_Capture_Code;

public class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment{
    TextView photoIcon, localIcon;
    String strtext,formDes,formtitle, elementId, checkpage;

    public static AddPhotoBottomDialogFragment newInstance() {
        return new AddPhotoBottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        strtext = getArguments().getString("id");
        formDes = getArguments().getString("formDes");
        formtitle = getArguments().getString("formtitle");
        checkpage = getArguments().getString("checkpage");
        View view = inflater.inflate(R.layout.layout_photo_bottom_sheet, container,false);
        photoIcon = view.findViewById(R.id.tv_btn_add_photo_camera);
        localIcon = view.findViewById(R.id.tv_btn_add_photo_gallery);

        photoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("id", strtext);
                intent.putExtra("des", formDes);
                intent.putExtra("title", formtitle);
                intent.putExtra("checkpage", checkpage);
                startActivity(intent);
                dismiss();
            }


        });

        localIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                Intent chooseFile = new Intent(Intent.ACTION_PICK);
                chooseFile.setType("image/*");
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 7);
            }
        });

        // get the views and attach the listener
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub

        switch(requestCode){
            case 7:
                if(resultCode==RESULT_OK){
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Uri uri = data.getData();
                    String src = uri.getPath();

                    Uri PathHolder = data.getData();
                    File file = new File(PathHolder.getPath());
                    String path = file.getAbsolutePath();
                    path = getRealPathFromURI(this.getContext(), PathHolder);
                    Intent intent = new Intent(getActivity(), FormActivity.class);
                    intent.putExtra("filestr", src);
                    intent.putExtra("filepath", path);
                    intent.putExtra("id", strtext);
                    intent.putExtra("des", formDes);
                    intent.putExtra("title", formtitle);
                    intent.putExtra("checkpage", checkpage);
                    startActivity(intent);
                }
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}