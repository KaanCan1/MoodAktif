package com.kaancankurt.moodaktif_1;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private String passwordHash;
    private long createdAt;
    private String moodProfile; // Ruh hali profili
    private int completedSurveys; // Tamamlanan anket sayısı

    public User() {
        this.createdAt = System.currentTimeMillis();
        this.completedSurveys = 0;
        this.moodProfile = "Yeni Kullanıcı";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getMoodProfile() {
        return moodProfile;
    }

    public void setMoodProfile(String moodProfile) {
        this.moodProfile = moodProfile;
    }

    public int getCompletedSurveys() {
        return completedSurveys;
    }

    public void setCompletedSurveys(int completedSurveys) {
        this.completedSurveys = completedSurveys;
    }
}