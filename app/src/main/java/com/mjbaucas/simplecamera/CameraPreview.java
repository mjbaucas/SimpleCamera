package com.mjbaucas.simplecamera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    Camera camObj;

    public CameraPreview(Context context) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camObj = Camera.open();
            camObj.setPreviewDisplay(holder);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Cannot set preview", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camObj.getParameters().setPreviewSize(width, height);
        camObj.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camObj.stopPreview();
        camObj = null;
    }
}
