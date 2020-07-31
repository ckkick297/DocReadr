package com.example.keerat666.listviewpdfui.models;

public class BookFolder {
    private int folder_id;
    private String folder_name;
    private int folder_is_active;
    private int folder_is_deleted;
    private String folder_date_created;
    private int folder_user_id;
    private int folder_is_global;

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public int getFolder_is_active() {
        return folder_is_active;
    }

    public void setFolder_is_active(int folder_is_active) {
        this.folder_is_active = folder_is_active;
    }

    public int getFolder_is_deleted() {
        return folder_is_deleted;
    }

    public void setFolder_is_deleted(int folder_is_deleted) {
        this.folder_is_deleted = folder_is_deleted;
    }

    public String getFolder_date_created() {
        return folder_date_created;
    }

    public void setFolder_date_created(String folder_date_created) {
        this.folder_date_created = folder_date_created;
    }

    public int getFolder_user_id() {
        return folder_user_id;
    }

    public void setFolder_user_id(int folder_user_id) {
        this.folder_user_id = folder_user_id;
    }

    public int getFolder_is_global() {
        return folder_is_global;
    }

    public void setFolder_is_global(int folder_is_global) {
        this.folder_is_global = folder_is_global;
    }
}
