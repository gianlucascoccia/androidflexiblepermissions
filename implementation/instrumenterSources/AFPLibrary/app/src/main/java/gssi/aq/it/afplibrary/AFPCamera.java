package gssi.aq.it.afplibrary;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by gianlucascoccia on 21/06/16.
 */


public class AFPCamera {

    public static void getCameraInfo(int cameraId, Camera.CameraInfo cameraInfo){
        Camera.getCameraInfo(cameraId, cameraInfo);
    }

    public static int getNumberOfCameras(){
        return Camera.getNumberOfCameras();
    }

    public static Camera open(){
        return Camera.open();
    }

    public static Camera open(int cameraId){
        return Camera.open(cameraId);
    }

    public static void addCallbackBuffer (Camera c, byte[] callbackBuffer){
        c.addCallbackBuffer(callbackBuffer);
    }

    public static void autoFocus (Camera c, Camera.AutoFocusCallback cb){
        c.autoFocus(cb);
    }

    public static void cancelAutoFocus (Camera c) {
        c.cancelAutoFocus();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean enableShutterSound (Camera c, boolean enabled) {
        return c.enableShutterSound(enabled);
    }

    public static Camera.Parameters getParameters (Camera c) {
        return c.getParameters();
    }

    public static void lock(Camera c) throws RuntimeException {
        c.lock();
    }

    public static void reconnect(Camera c) throws IOException {
        c.reconnect();
    }

    public static void release(Camera c) {
        c.release();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setAutoFocusMoveCallback (Camera c, Camera.AutoFocusMoveCallback cb) {
        c.setAutoFocusMoveCallback(cb);
    }

    public static void setDisplayOrientation (Camera c, int degrees) {
        c.setDisplayOrientation(degrees);
    }

    public static void setErrorCallback (Camera c, Camera.ErrorCallback cb){
        c.setErrorCallback(cb);
    }

    public static void setFaceDetectionListener (Camera c, Camera.FaceDetectionListener listener){
        c.setFaceDetectionListener(listener);
    }

    public static void setOneShotPreviewCallback (Camera c, Camera.PreviewCallback cb){
        c.setOneShotPreviewCallback(cb);
    }

    public static void setParameters (Camera c, Camera.Parameters params) throws RuntimeException {
        c.setParameters(params);
    }

    public static void setPreviewCallback (Camera c, Camera.PreviewCallback cb){
        c.setPreviewCallback(cb);
    }

    public static void setPreviewCallbackWithBuffer (Camera c, Camera.PreviewCallback cb){
        c.setPreviewCallbackWithBuffer(cb);
    }

    public static void setPreviewDisplay (Camera c, SurfaceHolder holder) throws IOException {
        c.setPreviewDisplay(holder);
    }

    public static void setPreviewTexture (Camera c, SurfaceTexture surfaceTexture) throws IOException {
        c.setPreviewTexture(surfaceTexture);
    }

    public static void setZoomChangeListener (Camera c, Camera.OnZoomChangeListener listener){
        c.setZoomChangeListener(listener);
    }

    public static void startFaceDetection (Camera c) throws IllegalArgumentException, RuntimeException {
        c.startFaceDetection();
    }

    public static void startPreview (Camera c) {
        c.startPreview();
    }

    public static void startSmoothZoom (Camera c, int value) throws IllegalArgumentException, RuntimeException {
        c.startSmoothZoom(value);
    }

    public static void stopFaceDetection (Camera c) {
        c.stopFaceDetection();
    }

    public static void stopPreview (Camera c) {
        c.stopPreview();
    }

    public static void stopSmoothZoom (Camera c) throws RuntimeException {
        c.stopSmoothZoom();
    }

    public static void takePicture (Camera c, Camera.ShutterCallback shutter, Camera.PictureCallback raw,
                             Camera.PictureCallback postview, Camera.PictureCallback jpeg, String[] features) {
        Log.d("AFPLib", "** AFPCAMERA TAKE PICTURE 1 ***");
        if(AFPModel.canAccessCamera(features)){
            c.takePicture(shutter,raw,postview,jpeg);
        }
    }

    public static void takePicture (Camera c, Camera.ShutterCallback shutter, Camera.PictureCallback raw,
                                  Camera.PictureCallback jpeg, String[] features) {
        Log.d("AFPLib", "** AFPCAMERA TAKE PICTURE 2 ***");
        if(AFPModel.canAccessCamera(features)){
            c.takePicture(shutter,raw,jpeg);
        }
    }

    public static void unlock (Camera c) throws RuntimeException {
        c.unlock();
    }
}
