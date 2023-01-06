package com.example.administrator.searchpicturetool.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.administrator.searchpicturetool.config.API;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jude.utils.JUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenhuaijun on 2016/1/22 0022.
 */
public class SaveBitmapModel {
    public static Observable<String> getSaveBitmapObservable(final Bitmap bitmap, Context context) {
        Observable<Bitmap> observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(bitmap);
            }
        });
        return observable
                .map(new Func1<Bitmap, String>() {
                    @Override
                    public String call(Bitmap bitmap) {
                        String name = "/" + System.currentTimeMillis() + ".png";
                        File file = new File(API.imgPath);
                        if (!file.exists()) {
                            JUtils.Log("!file.exists()");
                            file.mkdirs();
                        }
                        file = new File(API.imgPath + name);
                        try {
                            //创建需要保存的图片成功，若未成功则已经有该文件
                            if (file.createNewFile()) {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                //发送广播，让相册更新图片
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return API.status.error + "";
                        }

                        return file.toString();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static DataSource<CloseableReference<CloseableImage>> getFrescoDownloadBitmap(Context context, String url) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        return dataSource;
    }

    public static DataSource<CloseableReference<CloseableImage>> getFrescoCacheBitmap(Context context, String url) {

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline.isInBitmapMemoryCache(Uri.parse(url)) || imagePipeline.isInDiskCacheSync(Uri.parse(url))) {
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(url))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
            return dataSource;
        }
        JUtils.Log("getFrescoCacheBitmap ==null !!");
        return null;
    }
}
