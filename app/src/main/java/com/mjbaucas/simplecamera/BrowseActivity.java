package com.mjbaucas.simplecamera;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {
    List<File> fileList;
    LinearLayout browseLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        populateImages();
    }

    public void populateImages() {
        fileList = getListFiles(getFilesDir());
        browseLayout = findViewById(R.id.browse_layout);
        for (int i = 0; i < fileList.size(); i+=2) {
            LinearLayout browseLayoutInner = new LinearLayout(this);
            browseLayoutInner.setOrientation(LinearLayout.HORIZONTAL);
            browseLayoutInner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 2; j++){
                if(fileList.size() > i+j) {
                    LinearLayout browseLayoutColumn = new LinearLayout(this);
                    browseLayoutColumn.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(20,10,20,10);
                    browseLayoutColumn.setLayoutParams(params);


                    final File tempFile = fileList.get(i + j);
                    Bitmap bitmap = decodeFile(tempFile.getAbsolutePath());

                    if (bitmap != null) {
                        ImageView tempView = new ImageView(this);
                        final String tempName = tempFile.getName();
                        tempView.setImageBitmap(bitmap);
                        tempView.setId(i);
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageParams.gravity = Gravity.CENTER;
                        tempView.setLayoutParams(imageParams);
                        tempView.setClickable(true);

                        final AlertDialog.Builder deleteAlert = new AlertDialog.Builder(BrowseActivity.this);
                        deleteAlert.setTitle("Delete");
                        deleteAlert.setMessage("Are you sure you want to delete this image? (" + tempName + ")");
                        deleteAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                browseLayout.removeAllViews();
                                tempFile.delete();
                                populateImages();
                            }
                        });
                        deleteAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        tempView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteAlert.show();
                            }
                        });

                        TextView tempTextView = new TextView(this);
                        tempTextView.setText(tempName);
                        tempTextView.setId(i);
                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                        browseLayoutColumn.addView(tempView);
                        browseLayoutColumn.addView(tempTextView);
                        browseLayoutInner.addView(browseLayoutColumn);
                    }
                }
            }
            browseLayout.addView(browseLayoutInner);
        }
    }

    public List<File> getListFiles(File parentDir) {
        ArrayList<File> fileArray = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()){
                fileArray.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".jpg")){
                    fileArray.add(file);
                }
            }
        }
        return fileArray;
    }

    public Bitmap decodeFile(String pathname){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++){
            try {
                bitmap = BitmapFactory.decodeFile(pathname, options);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {

            }
        }

        if (bitmap != null){
            float width = bitmap.getWidth();
            float height = bitmap.getHeight();

            float maxWid = Resources.getSystem().getDisplayMetrics().widthPixels;
            float maxHei = Resources.getSystem().getDisplayMetrics().heightPixels;

            if (width > maxWid) {
                height = (maxWid/width) * height;
                width = maxWid;
            }

            if (height > maxHei) {
                width = (maxHei/height) * width;
                height = maxHei;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }
}
