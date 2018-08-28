package gssi.aq.it.afplibrary;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by gianlucascoccia on 15/06/16.
 */
public class AFPRecorder {
    private static boolean hasStarted = false;

    public static int getAudioSourceMax() {
        return MediaRecorder.getAudioSourceMax();
    }

    public static int getMaxAmplitude(MediaRecorder m) throws IllegalStateException {
        return m.getMaxAmplitude();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Surface getSurface(MediaRecorder m) throws IllegalStateException {
        return m.getSurface();
    }

    public static void prepare(MediaRecorder m) throws IllegalStateException, IOException {
        m.prepare();
    }

    public static void release(MediaRecorder m) {
        m.release();
    }

    public static void reset(MediaRecorder m) {
        m.reset();
    }

    public static void setAudioChannels(MediaRecorder m, int numChannels) {
        m.setAudioChannels(numChannels);
    }

    public static void setAudioEncoder (MediaRecorder m, int audio_encoder) throws IllegalStateException {
        m.setAudioEncoder(audio_encoder);
    }

    public static void setAudioEncodingBitRate (MediaRecorder m, int bitRate) {
        m.setAudioEncodingBitRate(bitRate);
    }

    public static void setAudioSamplingRate (MediaRecorder m, int samplingRate) {
        m.setAudioSamplingRate(samplingRate);
    }

    public static void setAudioSource (MediaRecorder m, int audio_source) throws IllegalStateException {
        m.setAudioSource(audio_source);
    }

    public static void setCamera (MediaRecorder m, Camera c) {
        m.setCamera(c);
    }

    public static void setCaptureRate (MediaRecorder m, double fps) {
        m.setCaptureRate(fps);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setInputSurface (MediaRecorder m, Surface surface) throws IllegalStateException, IllegalAccessException {
        m.setInputSurface(surface);
    }

    public static void setLocation (MediaRecorder m, float latitude, float longitude) throws IllegalArgumentException {
        m.setLocation(latitude, longitude);
    }

    public static void setMaxDuration (MediaRecorder m, int max_duration_ms) throws IllegalArgumentException {
        m.setMaxDuration(max_duration_ms);
    }

    public static void setMaxFileSize (MediaRecorder m, long max_filesize_bytes) throws IllegalArgumentException {
        m.setMaxFileSize(max_filesize_bytes);
    }

    public static void setOnErrorListener (MediaRecorder m, MediaRecorder.OnErrorListener l) {
        m.setOnErrorListener(l);
    }

    public static void setOnInfoListener (MediaRecorder m, MediaRecorder.OnInfoListener listener) {
        m.setOnInfoListener(listener);
    }

    public static void setOrientationHint (MediaRecorder m, int degrees) throws IllegalArgumentException {
        m.setOrientationHint(degrees);
    }

    public static void setOutputFile (MediaRecorder m, String path) throws IllegalStateException {
        m.setOutputFile(path);
    }

    public static void setOutputFile (MediaRecorder m, FileDescriptor fd) throws IllegalStateException {
        m.setOutputFile(fd);
    }

    public static void setOutputFormat (MediaRecorder m, int output_format) throws IllegalStateException {
        m.setOutputFormat(output_format);
    }

    public static void setPreviewDisplay (MediaRecorder m, Surface sv) {
        m.setPreviewDisplay(sv);
    }

    public static void setProfile (MediaRecorder m, CamcorderProfile profile){
        m.setProfile(profile);
    }

    public static void setVideoEncoder (MediaRecorder m, int video_encoder) throws IllegalStateException {
        m.setVideoEncoder(video_encoder);
    }

    public static void setVideoEncodingBitRate (MediaRecorder m, int bitRate){
        m.setVideoEncodingBitRate(bitRate);
    }

    public static void setVideoFrameRate (MediaRecorder m, int rate) throws IllegalStateException {
        m.setVideoFrameRate(rate);
    }

    public static void setVideoSize (MediaRecorder m, int width, int height) throws IllegalStateException {
        m.setVideoSize(width, height);
    }

    public static void setVideoSource (MediaRecorder m, int video_source) throws IllegalStateException {
        m.setVideoSource(video_source);
    }

    public static void start(MediaRecorder m, String [] featureNames) throws IllegalStateException {
        Log.w("AFPLib", " *** AFPRECORDER START ***");
        if(AFPModel.canAccessMicrophone(featureNames)) {
            m.start();
            hasStarted = true;
        }
    }

    public static void stop (MediaRecorder m) throws IllegalStateException {
        if(hasStarted){
            m.stop();
        }
        Log.w("AFPLib", "*** AFPRECORDER STOP ***");
    }
}
