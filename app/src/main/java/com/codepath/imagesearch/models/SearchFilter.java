package com.codepath.imagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchFilter implements Parcelable{
    public String imageSize;
    public String imageType;
    public String colorFilter;
    public String siteFilter;

    public SearchFilter(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.imageSize = data[0];
        this.imageType = data[1];
        this.colorFilter = data[2];
        this.siteFilter = data[3];
    }

    public SearchFilter(String imageSize, String imageType, String colorFilter, String siteFilter) {
        this.imageSize = imageSize;
        this.imageType = imageType;
        this.colorFilter = colorFilter;
        this.siteFilter = siteFilter;
                
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeStringArray(new String[] {this.imageSize,
                this.imageType,
                this.colorFilter,
                this.siteFilter});

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
        }

        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };
}
