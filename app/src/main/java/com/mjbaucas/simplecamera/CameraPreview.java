package com.mjbaucas.simplecamera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    Camera camObj;
    Camera.PictureCallback pictureCallback;
    private boolean safeToTakePhoto = false;
    int cameraDirection = 0;

    public CameraPreview(final Context context) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, final Camera camera) {
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
                    if (camera != null) {
                        camera.startPreview();
                        Toast.makeText(getContext(), "Photo Taken", Toast.LENGTH_SHORT).show();
                    }
                }
                safeToTakePhoto = true;
            }
        };
    }

    public void switchCamera(){
        cameraDirection = (cameraDirection + 1) % 2;
        startCamera(surfaceHolder);
    }

    private void startCamera(SurfaceHolder holder){
        try {
            if (camObj != null) {
                camObj.stopPreview();
                camObj.release();
                camObj = null;
            }

            if (cameraDirection == 0) {
                camObj = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                camObj = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }

            camObj.setDisplayOrientation(90);
            camObj.setPreviewDisplay(holder);
            Camera.Parameters params = camObj.getParameters();
            params.setPictureSize(640, 480);
            camObj.setParameters(params);
            camObj.startPreview();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Cannot set preview", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void takeImage() {
        if (camObj != null && safeToTakePhoto) {
            camObj.takePicture(null, null, pictureCallback);
            safeToTakePhoto = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.startCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camObj.getParameters().setPreviewSize(width, height);
        camObj.setDisplayOrientation(90);
        camObj.startPreview();
        safeToTakePhoto = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camObj.stopPreview();
        camObj.release();
        camObj = null;
    }
}
