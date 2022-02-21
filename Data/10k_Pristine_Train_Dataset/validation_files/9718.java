/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片信息实例
 *
 * @author 咖枯
 * @version 1.0 2016/1/13
 */
public class ImageItem implements Parcelable {
    /**
     * 图片id
     */
    private String imageId;

    /**
     * 图片缩略图path
     */
    private String thumbnailPath;

    /**
     * 图片path
     */
    private String imagePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageId);
        dest.writeString(this.thumbnailPath);
        dest.writeString(this.imagePath);
    }

    public ImageItem() {
    }

    private ImageItem(Parcel in) {
        this.imageId = in.readString();
        this.thumbnailPath = in.readString();
        this.imagePath = in.readString();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
