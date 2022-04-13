package com.reactnativecreatereactnativevideothumbnail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@ReactModule(name = CreateReactNativeVideoThumbnailModule.NAME)
public class CreateReactNativeVideoThumbnailModule extends ReactContextBaseJavaModule {
  public static final String NAME = "CreateReactNativeVideoThumbnail";
  public String _FileName = "";

  public CreateReactNativeVideoThumbnailModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(int a, int b, Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  public void getVideoThumbnail(String url, String fileName, Promise promise) {
    _FileName = fileName;
    ReactApplicationContext reactApplicationContext = getReactApplicationContext();
    RequestOptions myOptions = new RequestOptions().override(300, 300);
    Glide.with(reactApplicationContext).asBitmap().apply(myOptions).load(url).centerCrop().into(new CustomTarget<Bitmap>() {
      @Override
      public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        Uri uri = getImageUri(reactApplicationContext, resource, fileName);
        Log.d("uRL path", uri + "");
        promise.resolve(uri.toString());
      }

      @Override
      public void onLoadCleared(@Nullable Drawable placeholder) {

      }
    });
  }

  public Uri getImageUri(Context context, Bitmap inImage, String fileName) {
    Log.d("uRL fileName", fileName + "");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    File mFile =  createDirectoryAndSaveFile(inImage,fileName, context);
    Log.d("uRL path", mFile + "");
    return Uri.fromFile(mFile);
  }

  private File createDirectoryAndSaveFile(Bitmap imageToSave, String fileName,Context context) {

    File direct = new File(context.getExternalFilesDir(null), ".VideoThumbnails");

    if (!direct.exists()) {
      direct.mkdirs();
    }

    File imageFile = new File(direct,fileName+".jpg");

    if (imageFile.exists()) {
      return imageFile;
    }

    try {
      FileOutputStream out = new FileOutputStream(imageFile);
      imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
      out.flush();
      out.close();
    } catch (Exception e) {
      Log.d("ERROR---", e.toString());
      e.printStackTrace();
    }
    return imageFile;
  }

  public static native int nativeMultiply(int a, int b);
}
