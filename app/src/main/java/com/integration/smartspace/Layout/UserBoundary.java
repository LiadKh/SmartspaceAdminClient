package com.integration.smartspace.Layout;

import java.util.Map;

/**
 * Created by liadkh on 5/22/19.
 */
public class UserBoundary {
    private Map<String, String> key;
    private UserRole role;
    private String username;
    private String avatar;
    private Long points;

    public UserBoundary() {
    }

    public Map<String, String> getKey() {
        return key;
    }

    public void setKey(Map<String, String> key) {
        this.key = key;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "UserBoundary [key=" + key + ", role=" + role + ", username=" + username + ", avatar=" + avatar
                + ", points=" + points + "]";
    }
}
