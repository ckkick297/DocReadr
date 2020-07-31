package com.example.keerat666.listviewpdfui.models;

public class BookNotesJoinBook {

    private int note_id;
    private int note_book_id;
    private int note_user_id;
    private String note_date_created;
    private String note_text;
    private int note_page_number;
    private String note_text_highlighted;
    private int note_is_deleted;

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getNote_book_id() {
        return note_book_id;
    }

    public void setNote_book_id(int note_book_id) {
        this.note_book_id = note_book_id;
    }

    public int getNote_user_id() {
        return note_user_id;
    }

    public void setNote_user_id(int note_user_id) {
        this.note_user_id = note_user_id;
    }

    public String getNote_date_created() {
        return note_date_created;
    }

    public void setNote_date_created(String note_date_created) {
        this.note_date_created = note_date_created;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public int getNote_page_number() {
        return note_page_number;
    }

    public void setNote_page_number(int note_page_number) {
        this.note_page_number = note_page_number;
    }

    public String getNote_text_highlighted() {
        return note_text_highlighted;
    }

    public void setNote_text_highlighted(String note_text_highlighted) {
        this.note_text_highlighted = note_text_highlighted;
    }

    public int getNote_is_deleted() {
        return note_is_deleted;
    }

    public void setNote_is_deleted(int note_is_deleted) {
        this.note_is_deleted = note_is_deleted;
    }


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
}
