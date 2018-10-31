package com.mjbaucas.simplecamera;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

        fileList = getListFiles(getFilesDir());

        browseLayout = findViewById(R.id.browse_layout);
        for (int i = 0; i < fileList.size(); i+=2) {
            LinearLayout browseLayoutInner = new LinearLayout(this);
            browseLayoutInner.setOrientation(LinearLayout.HORIZONTAL);
            browseLayoutInner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            File tempFileLeft = fileList.get(i);
            Bitmap bitmapLeft = decodeFile(tempFileLeft.getAbsolutePath());

            if (bitmapLeft != null) {
                LinearLayout browseLayoutLeft = new LinearLayout(this);
                browseLayoutLeft.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                browseLayoutLeft.setLayoutParams(params);

                ImageView tempView = new ImageView(this);
                tempView.setImageBitmap(bitmapLeft);
                tempView.setId(i);
                tempView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView tempTextView = new TextView(this);
                tempTextView.setText(tempFileLeft.getName());
                tempTextView.setId(i);
                tempTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                browseLayoutLeft.addView(tempView);
                browseLayoutLeft.addView(tempTextView);
                browseLayoutInner.addView(browseLayoutLeft);
            }

            if (i < fileList.size() - 1){
                File tempFileRight = fileList.get(i+1);
                Bitmap bitmapRight = decodeFile(tempFileRight.getAbsolutePath());

                if (bitmapRight != null) {
                    LinearLayout browseLayoutRight = new LinearLayout(this);
                    browseLayoutRight.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,10,10,10);
                    browseLayoutRight.setLayoutParams(params);

                    ImageView tempView = new ImageView(this);
                    tempView.setImageBitmap(bitmapRight);
                    tempView.setId(i);
                    tempView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    TextView tempTextView = new TextView(this);
                    tempTextView.setText(tempFileRight.getName());
                    tempTextView.setId(i);
                    tempTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    browseLayoutRight.addView(tempView);
                    browseLayoutRight.addView(tempTextView);
                    browseLayoutInner.addView(browseLayoutRight);
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

            float maxWid = Resources.getSystem().getDisplayMetrics().widthPixels/2;
            float maxHei = Resources.getSystem().getDisplayMetrics().heightPixels/5;

            if (width > maxWid) {
                height = (maxWid/width) * height;
                width = maxWid;
            }

            if (height > maxHei) {
                width = (maxHei/height) * width;
                height = maxHei;
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        }

        return bitmap;
    }
}
