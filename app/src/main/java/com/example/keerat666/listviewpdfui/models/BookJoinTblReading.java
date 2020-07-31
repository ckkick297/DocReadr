package com.example.keerat666.listviewpdfui.models;

public class BookJoinTblReading {

    private int book_id;
    private String book_name;
    private int book_folder_id;
    private int book_user_id;

    private String book_date_created;
    private String book_date_accessed;
    private String book_filepath;
    private String book_lang;

    private int book_page_count;
    private int book_length;        // in seconds
    private int book_is_active;
    private int book_is_deleted;

    private String book_type;
    private int book_id_global;

    private String book_cover;
    private int book_book_id;

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public int getBook_folder_id() {
        return book_folder_id;
    }

    public void setBook_folder_id(int book_folder_id) {
        this.book_folder_id = book_folder_id;
    }

    public int getBook_user_id() {
        return book_user_id;
    }

    public void setBook_user_id(int book_user_id) {
        this.book_user_id = book_user_id;
    }

    public String getBook_date_created() {
        return book_date_created;
    }

    public void setBook_date_created(String book_date_created) {
        this.book_date_created = book_date_created;
    }

    public String getBook_date_accessed() {
        return book_date_accessed;
    }

    public void setBook_date_accessed(String book_date_accessed) {
        this.book_date_accessed = book_date_accessed;
    }

    public String getBook_filepath() {
        return book_filepath;
    }

    public void setBook_filepath(String book_filepath) {
        this.book_filepath = book_filepath;
    }

    public String getBook_lang() {
        return book_lang;
    }

    public void setBook_lang(String book_lang) {
        this.book_lang = book_lang;
    }

    public int getBook_page_count() {
        return book_page_count;
    }

    public void setBook_page_count(int book_page_count) {
        this.book_page_count = book_page_count;
    }

    public int getBook_length() {
        return book_length;
    }

    public void setBook_length(int book_length) {
        this.book_length = book_length;
    }

    public int getBook_is_active() {
        return book_is_active;
    }

    public void setBook_is_active(int book_is_active) {
        this.book_is_active = book_is_active;
    }

    public int getBook_is_deleted() {
        return book_is_deleted;
    }

    public void setBook_is_deleted(int book_is_deleted) {
        this.book_is_deleted = book_is_deleted;
    }

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    public int getBook_id_global() {
        return book_id_global;
    }

    public void setBook_id_global(int book_id_global) {
        this.book_id_global = book_id_global;
    }

    public String getBook_cover() {
        return book_cover;
    }

    public void setBook_cover(String book_cover) {
        this.book_cover = book_cover;
    }

    public int getBook_book_id() {
        return book_book_id;
    }

    public void setBook_book_id(int book_book_id) {
        this.book_book_id = book_book_id;
    }


    private int reading_id;
    private int reading_book_id;
    private String reading_date_created;
    private int reading_user_id;
    private int reading_page_number;
    private int reading_word_number;

    public int getReading_id() {
        return reading_id;
    }

    public void setReading_id(int reading_id) {
        this.reading_id = reading_id;
    }

    public int getReading_book_id() {
        return reading_book_id;
    }

    public void setReading_book_id(int reading_book_id) {
        this.reading_book_id = reading_book_id;
    }

    public String getReading_date_created() {
        return reading_date_created;
    }

    public void setReading_date_created(String reading_date_created) {
        this.reading_date_created = reading_date_created;
    }

    public int getReading_user_id() {
        return reading_user_id;
    }

    public void setReading_user_id(int reading_user_id) {
        this.reading_user_id = reading_user_id;
    }

    public int getReading_page_number() {
        return reading_page_number;
    }

    public void setReading_page_number(int reading_page_number) {
        this.reading_page_number = reading_page_number;
    }

    public int getReading_word_number() {
        return reading_word_number;
    }

    public void setReading_word_number(int reading_word_number) {
        this.reading_word_number = reading_word_number;
    }
}
