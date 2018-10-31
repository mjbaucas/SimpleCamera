package com.mjbaucas.simplecamera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    Camera camObj;
    Camera.PictureCallback pictureCallback;

    public CameraPreview(Context context) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outputStream;
                try {
                    outputStream = getContext().openFileOutput(String.format("%d.jpg", System.currentTimeMillis()), Context.MODE_PRIVATE);
                    outputStream.write(data);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            camObj.startPreview();
                        }
                    }, 2000);

                }
            }
        };
    }

    public void takeImage() {
        camObj.takePicture(null, null, pictureCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camObj = Camera.open();
            camObj.setDisplayOrientation(90);
            camObj.setPreviewDisplay(holder);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Cannot set preview", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camObj.getParameters().setPreviewSize(width, height);
        camObj.setDisplayOrientation(90);
        camObj.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camObj.stopPreview();
        camObj = null;
    }
}
