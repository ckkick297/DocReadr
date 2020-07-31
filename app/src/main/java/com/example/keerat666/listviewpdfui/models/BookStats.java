package com.example.keerat666.listviewpdfui.models;

public class BookStats {

    private int stat_id;
    private int stat_book_id;
    private int stat_user_id;
    private String stat_date_created;
    private int stat_word_count;
    private int stat_minutes;

    public int getStat_id() {
        return stat_id;
    }

    public void setStat_id(int stat_id) {
        this.stat_id = stat_id;
    }

    public int getStat_book_id() {
        return stat_book_id;
    }

    public void setStat_book_id(int stat_book_id) {
        this.stat_book_id = stat_book_id;
    }

    public int getStat_user_id() {
        return stat_user_id;
    }

    public void setStat_user_id(int stat_user_id) {
        this.stat_user_id = stat_user_id;
    }

    public String getStat_date_created() {
        return stat_date_created;
    }

    public void setStat_date_created(String stat_date_created) {
        this.stat_date_created = stat_date_created;
    }

    public int getStat_word_count() {
        return stat_word_count;
    }

    public void setStat_word_count(int stat_word_count) {
        this.stat_word_count = stat_word_count;
    }

    public int getStat_minutes() {
        return stat_minutes;
    }

    public void setStat_minutes(int stat_minutes) {
        this.stat_minutes = stat_minutes;
    }
}
