package com.example.keerat666.listviewpdfui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keerat666.listviewpdfui.models.Book;
import com.example.keerat666.listviewpdfui.models.BookFolder;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.models.BookMeta;
import com.example.keerat666.listviewpdfui.models.BookNotesJoinBook;
import com.example.keerat666.listviewpdfui.models.BookReadingCurrentStatus;
import com.example.keerat666.listviewpdfui.models.BookStatsGetDateHours;
import com.example.keerat666.listviewpdfui.models.LoginData;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {

    BookFolder bookFolder;

    public static final String DATABASE_NAME = "DocReader.db";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    ////////////////////////   CREATE TABLE   /////////////////////

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /////////////// Login ///////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_users ( " +
                        "login_id INTEGER PRIMARY KEY," +
                        "login_user_name TEXT DEFAULT NULL," +
                        "login_user_email TEXT DEFAULT NULL," +
                        "login_user_picture TEXT DEFAULT NULL," +
                        "login_user_token_google_access TEXT DEFAULT NULL," +
                        "login_user_token_google_refresh TEXT DEFAULT NULL," +
                        "login_user_token_linkedin_access TEXT DEFAULT NULL," +
                        "login_user_app_user_id TEXT DEFAULT NULL," +
                        "login_user_token_access TEXT DEFAULT NULL)");

        ////////////   Books  ////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_books("+
                        "book_id INTEGER PRIMARY KEY,"+

                        "book_name TEXT DEFAULT NULL,"+

                        "book_folder_id INTEGER DEFAULT 0,"+
                        "book_user_id INTEGER DEFAULT 0,"+

                        "book_date_created DATETIME DEFAULT (datetime('now','localtime')),"+
                        "book_date_accessed DATETIME DEFAULT (datetime('now','localtime')),"+

                        "book_filepath TEXT DEFAULT NULL,"+
                        "book_lang TEXT DEFAULT NULL,"+

                        "book_page_count INTEGER DEFAULT 0,"+
                        "book_length INTEGER DEFAULT 0,"+       // IN seconds
                        "book_is_active INTEGER DEFAULT 0,"+
                        "book_is_deleted INTEGER DEFAULT 0,"+

                        "book_type TEXT DEFAULT NULL,"+

                        "book_id_global INTEGER DEFAULT 0,"+

                        "book_cover TEXT DEFAULT NULL,"+

                        "book_book_id INTEGER DEFAULT 0)");

        ////////////   Reading  ////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_reading("+
                        "reading_id INTEGER PRIMARY KEY,"+
                        "reading_book_id INTEGER DEFAULT 0,"+
                        "reading_date_created DATETIME DEFAULT (datetime('now','localtime')),"+
                        "reading_user_id INTEGER DEFAULT 0,"+
                        "reading_page_number INTEGER DEFAULT 0,"+
                        "reading_word_number INTEGER DEFAULT 0)");

        ////////////   Meta  ////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_meta("+
                        "meta_id INTEGER PRIMARY KEY,"+
                        "meta_book_id INTEGER DEFAULT 0,"+
                        "meta_user_id INTEGER DEFAULT 0,"+
                        "meta_date_created DATETIME DEFAULT (datetime('now','localtime')),"+
                        "meta_is_deleted INTEGER DEFAULT 0,"+
                        "meta_lang_reading TEXT DEFAULT NULL,"+
                        "meta_speech_engine TEXT DEFAULT NULL,"+
                        "meta_rate INTEGER DEFAULT 0,"+
                        "meta_pitch INTEGER DEFAULT 0)");

        ////////////   Folder  ////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_folders("+
                        "folder_id INTEGER PRIMARY KEY,"+
                        "folder_name TEXT DEFAULT NULL,"+
                        "folder_is_active INTEGER DEFAULT 0,"+
                        "folder_date_created DATETIME DEFAULT (datetime('now','localtime')),"+
                        "folder_is_deleted INTEGER DEFAULT 0,"+
                        "folder_user_id INTEGER DEFAULT 0,"+
                        "folder_is_global INTEGER DEFAULT 0)");

        ////////////   Notes  ////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_notes("+
                        "note_id INTEGER PRIMARY KEY,"+
                        "note_book_id INTEGER DEFAULT 0,"+
                        "note_user_id INTEGER DEFAULT 0,"+
                        "note_date_created DATETIME DEFAULT (datetime('now','localtime')),"+
                        "note_text TEXT DEFAULT NULL,"+
                        "note_page_number INTEGER DEFAULT 0,"+
                        "note_text_highlighted TEXT DEFAULT NULL,"+
                        "note_is_deleted INTEGER DEFAULT 0)");

        ////////////   Stats  ////////
        sqLiteDatabase.execSQL(
                "CREATE TABLE tbl_stats("+
                        "stat_id INTEGER PRIMARY KEY,"+
                        "stat_book_id INTEGER DEFAULT 0,"+
                        "stat_user_id INTEGER DEFAULT 0,"+
                        "stat_date_created DATETIME DEFAULT (datetime('now','localtime')),"+
                        "stat_word_count INTEGER DEFAULT 0,"+
                        "stat_minutes INTEGER DEFAULT 0)");

        bookFolder = new BookFolder();
        bookFolder.setFolder_name("Recent");
        insertBookDefaultFolder(sqLiteDatabase, bookFolder);

        bookFolder = new BookFolder();
        bookFolder.setFolder_name("Books");
        insertBookDefaultFolder(sqLiteDatabase, bookFolder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_books");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_reading");
        onCreate(sqLiteDatabase);
    }


    //////////////////  INSERT  //////////////////////////

    public void insertUser(LoginData loginData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("login_user_name", loginData.getUSER_NAME());
        values.put("login_user_email", loginData.getUSER_EMAIL());
        values.put("login_user_picture", loginData.getLogin_user_picture());
        if (loginData.getUSER_REFRESH_TOKEN() != null){
            values.put("login_user_token_google_access", loginData.getUSER_ACCESS_TOKEN());
            values.put("login_user_token_google_refresh", loginData.getUSER_REFRESH_TOKEN());
        }else {
            values.put("login_user_token_linkedin_access", loginData.getUSER_ACCESS_TOKEN());
        }
        values.put("login_user_app_user_id", loginData.getLogin_user_app_user_id());
        values.put("login_user_token_access", loginData.getLogin_user_token_access());

        db.insert("tbl_users", null, values);
        db.close();
    }

    public void insertBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put("book_id", book.getBook_id());
        values.put("book_name", book.getBook_name());
        values.put("book_folder_id", book.getBook_folder_id());
        values.put("book_user_id", book.getBook_user_id());

        //values.put("book_date_created", book.getBook_date_created());
        //values.put("book_date_accessed", book.getBook_date_accessed());
        values.put("book_filepath", book.getBook_filepath());
        values.put("book_lang", book.getBook_lang());

        values.put("book_page_count", book.getBook_page_count());
        values.put("book_length", book.getBook_length());
        values.put("book_is_active", book.getBook_is_active());
        values.put("book_is_deleted", book.getBook_is_deleted());

        values.put("book_type", book.getBook_type());
        values.put("book_id_global", book.getBook_id_global());
        values.put("book_cover", book.getBook_cover());
        values.put("book_book_id", book.getBook_book_id());

        db.insert("tbl_books", null, values);
        db.close(); // Closing database connection
    }

    public void insertBookReadingCurrentStatus(BookReadingCurrentStatus bookReadingCurrentStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

       // values.put("reading_id", bookReadingCurrentStatus.getReading_id());
        values.put("reading_book_id", bookReadingCurrentStatus.getReading_book_id());
       // values.put("reading_date_created", bookReadingCurrentStatus.getReading_date_created());
        values.put("reading_user_id", bookReadingCurrentStatus.getReading_user_id());
        values.put("reading_page_number", bookReadingCurrentStatus.getReading_page_number());
        values.put("reading_word_number", bookReadingCurrentStatus.getReading_word_number());

        db.insert("tbl_reading", null, values);
        db.close(); // Closing database connection
    }

    public void insertBookMeta(BookMeta bookMeta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("meta_book_id", bookMeta.getMeta_book_id());
        values.put("meta_user_id", bookMeta.getMeta_user_id());
        values.put("meta_is_deleted", bookMeta.getMeta_is_deleted());
        values.put("meta_lang_reading", bookMeta.getMeta_lang_reading());
        values.put("meta_speech_engine", bookMeta.getMeta_speech_engine());
        values.put("meta_rate", bookMeta.getMeta_rate());
        values.put("meta_pitch", bookMeta.getMeta_pitch());

        db.insert("tbl_meta", null, values);
        db.close(); // Closing database connection
    }

    /** Book foler Insertion **/
    public void insertBookDefaultFolder(SQLiteDatabase db, BookFolder bookFolder) {
        ContentValues values = new ContentValues();
        // values.put("folder_id", bookMeta.getMeta_book_id());
        values.put("folder_name", bookFolder.getFolder_name());
        values.put("folder_is_active", bookFolder.getFolder_is_active());
        //values.put("folder_date_created", bookFolder.getFolder_date_created());

        values.put("folder_is_deleted", bookFolder.getFolder_is_deleted());
        values.put("folder_user_id", bookFolder.getFolder_user_id());
        values.put("folder_is_global", bookFolder.getFolder_is_global());

        db.insert("tbl_folders", null, values);
    }

    public void insertBookFolder(BookFolder bookFolder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

       // values.put("folder_id", bookMeta.getMeta_book_id());
        values.put("folder_name", bookFolder.getFolder_name());
        values.put("folder_is_active", bookFolder.getFolder_is_active());
        //values.put("folder_date_created", bookFolder.getFolder_date_created());

        values.put("folder_is_deleted", bookFolder.getFolder_is_deleted());
        values.put("folder_user_id", bookFolder.getFolder_user_id());
        values.put("folder_is_global", bookFolder.getFolder_is_global());

        db.insert("tbl_folders", null, values);
        db.close(); // Closing database connection
    }
    /*************/

    ////////////   Notes  ////////

    public void insertBookNotes(BookNotesJoinBook bookNotes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("note_book_id", bookNotes.getNote_book_id());
        values.put("note_user_id", bookNotes.getNote_user_id());
       // values.put("note_date_created", bookNotes.getNote_date_created());

        values.put("note_text", bookNotes.getNote_text());
        values.put("note_page_number", bookNotes.getNote_page_number());
        values.put("note_text_highlighted", bookNotes.getNote_text_highlighted());
        values.put("note_is_deleted", bookNotes.getNote_is_deleted());

        db.insert("tbl_notes", null, values);
        db.close(); // Closing database connection
    }

    public void insertBookStats(int book_id, int user_id, int word_count, int minutes){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into tbl_stats(stat_book_id,stat_user_id,stat_word_count,stat_minutes) values("+book_id+","+user_id+","+word_count+","+minutes+")");
    }

  /**  public void insertBookStats(BookStats bookStats){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("stat_book_id", bookStats.getStat_book_id());
        values.put("stat_user_id", bookStats.getStat_user_id());
        values.put("stat_word_count", bookStats.getStat_word_count());
        values.put("stat_minutes", bookStats.getStat_minutes());

        db.insert("tbl_stats", null, values);
        db.close();
    }*/

        /////////////////////  SELECT  ////////////

    public LoginData getAllUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<LoginData> usr_list = new ArrayList<>();
        Cursor res = db.rawQuery( "select * from tbl_users", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            LoginData loginData = new LoginData();

            loginData.setLogin_id(res.getInt(res.getColumnIndex("login_id")));
            loginData.setUSER_NAME(res.getString(res.getColumnIndex("login_user_name")));
            loginData.setUSER_EMAIL(res.getString(res.getColumnIndex("login_user_email")));
            loginData.setLogin_user_picture(res.getString(res.getColumnIndex("login_user_picture")));
            loginData.setUSER_ACCESS_TOKEN(res.getString(res.getColumnIndex("login_user_token_google_access")));
            loginData.setUSER_REFRESH_TOKEN(res.getString(res.getColumnIndex("login_user_token_google_refresh")));
            loginData.setLogin_user_linked_in_token(res.getString(res.getColumnIndex("login_user_token_linkedin_access")));

            loginData.setLogin_user_app_user_id(res.getString(res.getColumnIndex("login_user_app_user_id")));
            loginData.setLogin_user_token_access(res.getString(res.getColumnIndex("login_user_token_access")));
            usr_list.add(loginData);
            res.moveToNext();
        }
        return usr_list.get(usr_list.size()-1);
    }

    public List<Book> getAllBooks() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<Book> books_list = new ArrayList<>();
        Cursor res = db.rawQuery( "select * from tbl_books where book_is_deleted=0", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            Book book = new Book();

            book.setBook_id(res.getInt(res.getColumnIndex("book_id")));
            book.setBook_name(res.getString(res.getColumnIndex("book_name")));
            book.setBook_folder_id(res.getInt(res.getColumnIndex("book_folder_id")));
            book.setBook_user_id(res.getInt(res.getColumnIndex("book_user_id")));

            book.setBook_date_created(res.getString(res.getColumnIndex("book_date_created")));
            book.setBook_date_accessed(res.getString(res.getColumnIndex("book_date_accessed")));
            book.setBook_filepath(res.getString(res.getColumnIndex("book_filepath")));
            book.setBook_lang(res.getString(res.getColumnIndex("book_lang")));

            book.setBook_page_count(res.getInt(res.getColumnIndex("book_page_count")));
            book.setBook_length(res.getInt(res.getColumnIndex("book_length")));
            book.setBook_is_active(res.getInt(res.getColumnIndex("book_is_active")));
            book.setBook_is_deleted(res.getInt(res.getColumnIndex("book_is_deleted")));

            book.setBook_type(res.getString(res.getColumnIndex("book_type")));
            book.setBook_id_global(res.getInt(res.getColumnIndex("book_id_global")));
            book.setBook_cover(res.getString(res.getColumnIndex("book_cover")));
            book.setBook_book_id(res.getInt(res.getColumnIndex("book_book_id")));

            books_list.add(book);
            res.moveToNext();
        }
        return books_list;
    }

    public List<BookJoinTblReading> getAllBookWithJoinTblReading() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookJoinTblReading> books_list = new ArrayList<>();

        Cursor res = db.rawQuery( "select b.*, r.* from tbl_books b inner join tbl_reading r on b.book_id=r.reading_book_id\n" +
                "                where b.book_is_active=0 and b.book_is_deleted=0\n" +
                "                order by b.book_date_created DESC", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookJoinTblReading book = new BookJoinTblReading();

            book.setBook_id(res.getInt(res.getColumnIndex("book_id")));
            book.setBook_name(res.getString(res.getColumnIndex("book_name")));
            book.setBook_folder_id(res.getInt(res.getColumnIndex("book_folder_id")));
            book.setBook_user_id(res.getInt(res.getColumnIndex("book_user_id")));

            book.setBook_date_created(res.getString(res.getColumnIndex("book_date_created")));
            book.setBook_date_accessed(res.getString(res.getColumnIndex("book_date_accessed")));
            book.setBook_filepath(res.getString(res.getColumnIndex("book_filepath")));
            book.setBook_lang(res.getString(res.getColumnIndex("book_lang")));

            book.setBook_page_count(res.getInt(res.getColumnIndex("book_page_count")));
            book.setBook_length(res.getInt(res.getColumnIndex("book_length")));
            book.setBook_is_active(res.getInt(res.getColumnIndex("book_is_active")));
            book.setBook_is_deleted(res.getInt(res.getColumnIndex("book_is_deleted")));

            book.setBook_type(res.getString(res.getColumnIndex("book_type")));
            book.setBook_id_global(res.getInt(res.getColumnIndex("book_id_global")));
            book.setBook_cover(res.getString(res.getColumnIndex("book_cover")));
            book.setBook_book_id(res.getInt(res.getColumnIndex("book_book_id")));

            book.setReading_id(res.getInt(res.getColumnIndex("reading_id")));
            book.setReading_book_id(res.getInt(res.getColumnIndex("reading_book_id")));
            book.setReading_date_created(res.getString(res.getColumnIndex("reading_date_created")));
            book.setReading_user_id(res.getInt(res.getColumnIndex("reading_user_id")));
            book.setReading_page_number(res.getInt(res.getColumnIndex("reading_page_number")));
            book.setReading_word_number(res.getInt(res.getColumnIndex("reading_word_number")));

            books_list.add(book);
            res.moveToNext();
        }
        return books_list;
    }

    public List<BookJoinTblReading> getAllBookWithJoinTblReading(int folder_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookJoinTblReading> books_list = new ArrayList<>();

        Cursor res = db.rawQuery( "select b.*, r.* from tbl_books b inner join tbl_reading r on b.book_id=r.reading_book_id\n" +
                "                where b.book_folder_id="+folder_id+" and b.book_is_active=0 and b.book_is_deleted=0\n" +
                "                order by b.book_date_created DESC", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookJoinTblReading book = new BookJoinTblReading();

            book.setBook_id(res.getInt(res.getColumnIndex("book_id")));
            book.setBook_name(res.getString(res.getColumnIndex("book_name")));
            book.setBook_folder_id(res.getInt(res.getColumnIndex("book_folder_id")));
            book.setBook_user_id(res.getInt(res.getColumnIndex("book_user_id")));

            book.setBook_date_created(res.getString(res.getColumnIndex("book_date_created")));
            book.setBook_date_accessed(res.getString(res.getColumnIndex("book_date_accessed")));
            book.setBook_filepath(res.getString(res.getColumnIndex("book_filepath")));
            book.setBook_lang(res.getString(res.getColumnIndex("book_lang")));

            book.setBook_page_count(res.getInt(res.getColumnIndex("book_page_count")));
            book.setBook_length(res.getInt(res.getColumnIndex("book_length")));
            book.setBook_is_active(res.getInt(res.getColumnIndex("book_is_active")));
            book.setBook_is_deleted(res.getInt(res.getColumnIndex("book_is_deleted")));

            book.setBook_type(res.getString(res.getColumnIndex("book_type")));
            book.setBook_id_global(res.getInt(res.getColumnIndex("book_id_global")));
            book.setBook_cover(res.getString(res.getColumnIndex("book_cover")));
            book.setBook_book_id(res.getInt(res.getColumnIndex("book_book_id")));

            book.setReading_id(res.getInt(res.getColumnIndex("reading_id")));
            book.setReading_book_id(res.getInt(res.getColumnIndex("reading_book_id")));
            book.setReading_date_created(res.getString(res.getColumnIndex("reading_date_created")));
            book.setReading_user_id(res.getInt(res.getColumnIndex("reading_user_id")));
            book.setReading_page_number(res.getInt(res.getColumnIndex("reading_page_number")));
            book.setReading_word_number(res.getInt(res.getColumnIndex("reading_word_number")));

            books_list.add(book);
            res.moveToNext();
        }
        return books_list;
    }


    public List<BookReadingCurrentStatus> getAllBookReadingCurrentStatus() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookReadingCurrentStatus> books_list = new ArrayList<>();
        Cursor res = db.rawQuery( "select * from tbl_reading", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookReadingCurrentStatus book = new BookReadingCurrentStatus();

            book.setReading_id(res.getInt(res.getColumnIndex("reading_id")));
            book.setReading_book_id(res.getInt(res.getColumnIndex("reading_book_id")));
            book.setReading_date_created(res.getString(res.getColumnIndex("reading_date_created")));
            book.setReading_user_id(res.getInt(res.getColumnIndex("reading_user_id")));
            book.setReading_page_number(res.getInt(res.getColumnIndex("reading_page_number")));
            book.setReading_word_number(res.getInt(res.getColumnIndex("reading_word_number")));
            books_list.add(book);

            res.moveToNext();
        }
        return books_list;
    }

    public List<BookMeta> getAllBookMeta() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookMeta> bookMetaList = new ArrayList<>();
        Cursor res = db.rawQuery( "select * from tbl_meta", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookMeta book = new BookMeta();

            book.setMeta_id(res.getInt(res.getColumnIndex("meta_id")));
            book.setMeta_book_id(res.getInt(res.getColumnIndex("meta_book_id")));
            book.setMeta_user_id(res.getInt(res.getColumnIndex("meta_user_id")));
            book.setMeta_date_created(res.getString(res.getColumnIndex("meta_date_created")));

            book.setMeta_is_deleted(res.getInt(res.getColumnIndex("meta_is_deleted")));
            book.setMeta_lang_reading(res.getString(res.getColumnIndex("meta_lang_reading")));
            book.setMeta_speech_engine(res.getString(res.getColumnIndex("meta_speech_engine")));
            book.setMeta_rate(res.getInt(res.getColumnIndex("meta_rate")));
            book.setMeta_pitch(res.getInt(res.getColumnIndex("meta_pitch")));

            bookMetaList.add(book);

            res.moveToNext();
        }
        return bookMetaList;
    }

    public String getFolderName(int id) {
        String folder_name ="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tbl_folders where folder_is_deleted =" + 0 + " AND folder_id =" + id, null);

        res.moveToFirst();
        while (res.isAfterLast() == false) {
            folder_name = res.getString(res.getColumnIndex("folder_name"));
            res.moveToNext();
        }
        return folder_name;
    }
    public List<BookFolder> getAllBookFolder() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookFolder> bookFolderList = new ArrayList<>();
        Cursor res = db.rawQuery( "select * from tbl_folders where folder_is_deleted ="+0, null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookFolder book = new BookFolder();

            book.setFolder_id(res.getInt(res.getColumnIndex("folder_id")));
            book.setFolder_name(res.getString(res.getColumnIndex("folder_name")));
            book.setFolder_is_active(res.getInt(res.getColumnIndex("folder_is_active")));
            book.setFolder_date_created(res.getString(res.getColumnIndex("folder_date_created")));

            book.setFolder_is_deleted(res.getInt(res.getColumnIndex("folder_is_deleted")));
            book.setFolder_user_id(res.getInt(res.getColumnIndex("folder_user_id")));
            book.setFolder_is_global(res.getInt(res.getColumnIndex("folder_is_global")));

            bookFolderList.add(book);

            res.moveToNext();
        }
        return bookFolderList;
    }

    public List<BookNotesJoinBook> getAllBookNotesJoinBook() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookNotesJoinBook> bookNotesJoinBooks = new ArrayList<>();
        Cursor res = db.rawQuery( "select n.*, b.* from tbl_notes n inner join tbl_books b on n.note_book_id=b.book_id \n" +
                "where b.book_is_active=0 and b.book_is_deleted=0 and n.note_is_deleted=0 \n" +
                "order by n.note_date_created DESC", null );

        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookNotesJoinBook bookNotesJoinBook = new BookNotesJoinBook();

            bookNotesJoinBook.setNote_id(res.getInt(res.getColumnIndex("note_id")));
            bookNotesJoinBook.setNote_book_id(res.getInt(res.getColumnIndex("note_book_id")));
            bookNotesJoinBook.setNote_user_id(res.getInt(res.getColumnIndex("note_user_id")));
            bookNotesJoinBook.setNote_date_created(res.getString(res.getColumnIndex("note_date_created")));

            bookNotesJoinBook.setNote_text(res.getString(res.getColumnIndex("note_text")));
            bookNotesJoinBook.setNote_page_number(res.getInt(res.getColumnIndex("note_page_number")));
            bookNotesJoinBook.setNote_text_highlighted(res.getString(res.getColumnIndex("note_text_highlighted")));
            bookNotesJoinBook.setNote_is_deleted(res.getInt(res.getColumnIndex("note_is_deleted")));

            //////////////// Book
            bookNotesJoinBook.setBook_id(res.getInt(res.getColumnIndex("book_id")));
            bookNotesJoinBook.setBook_name(res.getString(res.getColumnIndex("book_name")));
            bookNotesJoinBook.setBook_folder_id(res.getInt(res.getColumnIndex("book_folder_id")));
            bookNotesJoinBook.setBook_user_id(res.getInt(res.getColumnIndex("book_user_id")));

            bookNotesJoinBook.setBook_date_created(res.getString(res.getColumnIndex("book_date_created")));
            bookNotesJoinBook.setBook_date_accessed(res.getString(res.getColumnIndex("book_date_accessed")));
            bookNotesJoinBook.setBook_filepath(res.getString(res.getColumnIndex("book_filepath")));
            bookNotesJoinBook.setBook_lang(res.getString(res.getColumnIndex("book_lang")));

            bookNotesJoinBook.setBook_page_count(res.getInt(res.getColumnIndex("book_page_count")));
            bookNotesJoinBook.setBook_length(res.getInt(res.getColumnIndex("book_length")));
            bookNotesJoinBook.setBook_is_active(res.getInt(res.getColumnIndex("book_is_active")));
            bookNotesJoinBook.setBook_is_deleted(res.getInt(res.getColumnIndex("book_is_deleted")));

            bookNotesJoinBook.setBook_type(res.getString(res.getColumnIndex("book_type")));
            bookNotesJoinBook.setBook_id_global(res.getInt(res.getColumnIndex("book_id_global")));
            bookNotesJoinBook.setBook_cover(res.getString(res.getColumnIndex("book_cover")));
            bookNotesJoinBook.setBook_book_id(res.getInt(res.getColumnIndex("book_book_id")));


            bookNotesJoinBooks.add(bookNotesJoinBook);

            res.moveToNext();
        }
        return bookNotesJoinBooks;
    }

    public List<BookStatsGetDateHours> getAllBookStats() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<BookStatsGetDateHours> bookStats = new ArrayList<>();
        Cursor res = db.rawQuery( "SELECT SUM(`stat_minutes`) as totalTime, date(stat_date_created) as dates from tbl_stats GROUP BY date(stat_date_created)", null );
        //Cursor res = db.rawQuery( "SELECT SUM(`stat_minutes`) as totalTime, date(stat_date_created) as dates from tbl_stats WHERE stat_user_id = 0 GROUP BY date(stat_date_created)", null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {

            BookStatsGetDateHours book = new BookStatsGetDateHours();
            book.setTotalTime(res.getInt(res.getColumnIndex("totalTime")));
            book.setDate(res.getString(res.getColumnIndex("dates")));

            bookStats.add(book);
            res.moveToNext();
        }
        return bookStats;
    }

    ///////////////////  UPDATE   //////////////////
    public void updateBookLang(String lang, int book_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put("book_lang", lang);
        db.update("tbl_books", data, "book_id=" + book_id, null);

        //String strSQL = "UPDATE tbl_books SET book_lang = "+lang+" WHERE book_id = "+ book_id;
        //db.execSQL(strSQL);
    }

    public boolean updateBookReadingCurrentStatus(BookReadingCurrentStatus bookReadingCurrentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues.put("reading_id", bookReadingCurrentStatus.getReading_id());
        //contentValues.put("reading_book_id", bookReadingCurrentStatus.getReading_book_id());
       // contentValues.put("reading_date_created", bookReadingCurrentStatus.getReading_date_created());
       // contentValues.put("reading_user_id", bookReadingCurrentStatus.getReading_user_id());
        contentValues.put("reading_page_number", bookReadingCurrentStatus.getReading_page_number());
        contentValues.put("reading_word_number", bookReadingCurrentStatus.getReading_word_number());

        db.update("tbl_reading", contentValues, "reading_id = ? ", new String[] { ""+bookReadingCurrentStatus.getReading_id() } );
        return true;
    }

    public boolean updateBookFolder(BookFolder bookFolder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("folder_name", bookFolder.getFolder_name());

        db.update("tbl_folders", contentValues, "folder_id = ? ", new String[] { ""+bookFolder.getFolder_id() } );
        return true;
    }

    public boolean updateNotes(int id, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_text", note);
        db.update("tbl_notes", contentValues, "note_id = ? ", new String[] { ""+id } );
        return true;
    }
    ///////////////////   DELETE  //////////

    public boolean deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("book_is_deleted", 1);
        db.update("tbl_books", contentValues, "book_id = ? ", new String[] { ""+id } );
        return true;
    }

    public boolean deleteNotes(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_is_deleted", 1);
        db.update("tbl_notes", contentValues, "note_id = ? ", new String[] { ""+id } );
        return true;
    }

    public boolean deleteBookFolder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("folder_is_deleted", 1);
        db.update("tbl_folders", contentValues, "folder_id = ? ", new String[] { ""+id } );
        return true;
    }
}
