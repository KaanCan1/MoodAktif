package com.kaancankurt.moodaktif_1;

import android.os.Parcel;
import android.os.Parcelable;

public class Recommendation implements Parcelable {
    private String text;
    private String description;

    public Recommendation(String text, String description) {
        this.text = text;
        this.description = description;
    }

    protected Recommendation(Parcel in) {
        text = in.readString();
        description = in.readString();
    }

    public static final Creator<Recommendation> CREATOR = new Creator<Recommendation>() {
        @Override
        public Recommendation createFromParcel(Parcel in) {
            return new Recommendation(in);
        }

        @Override
        public Recommendation[] newArray(int size) {
            return new Recommendation[size];
        }
    };

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(description);
    }
}