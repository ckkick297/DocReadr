package com.example.keerat666.listviewpdfui.models;

public class LoginData {

    private int login_id;
    // login data
    private String USER_NAME;
    private String USER_EMAIL;
    private String login_user_picture;
    private String USER_ACCESS_TOKEN;
    private String USER_REFRESH_TOKEN;

    private String login_user_linked_in_token;

    public String getLogin_user_linked_in_token() {
        return login_user_linked_in_token;
    }

    public void setLogin_user_linked_in_token(String login_user_linked_in_token) {
        this.login_user_linked_in_token = login_user_linked_in_token;
    }

    //server data
    private String login_user_app_user_id;
    private String login_user_token_access;

    private Boolean status;
    private String message;
    private Data data;

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_EMAIL() {
        return USER_EMAIL;
    }

    public void setUSER_EMAIL(String USER_EMAIL) {
        this.USER_EMAIL = USER_EMAIL;
    }

    public String getUSER_ACCESS_TOKEN() {
        return USER_ACCESS_TOKEN;
    }

    public void setUSER_ACCESS_TOKEN(String USER_ACCESS_TOKEN) {
        this.USER_ACCESS_TOKEN = USER_ACCESS_TOKEN;
    }

    public String getUSER_REFRESH_TOKEN() {
        return USER_REFRESH_TOKEN;
    }

    public void setUSER_REFRESH_TOKEN(String USER_REFRESH_TOKEN) {
        this.USER_REFRESH_TOKEN = USER_REFRESH_TOKEN;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getLogin_user_picture() {
        return login_user_picture;
    }

    public void setLogin_user_picture(String login_user_picture) {
        this.login_user_picture = login_user_picture;
    }

    public String getLogin_user_app_user_id() {
        return login_user_app_user_id;
    }

    public void setLogin_user_app_user_id(String login_user_app_user_id) {
        this.login_user_app_user_id = login_user_app_user_id;
    }

    public String getLogin_user_token_access() {
        return login_user_token_access;
    }

    public void setLogin_user_token_access(String login_user_token_access) {
        this.login_user_token_access = login_user_token_access;
    }

    public class Data{
        private String user_id;
        private String signature_whatsapp;
        private String signature_text;
        private String token;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSignature_whatsapp() {
            return signature_whatsapp;
        }

        public void setSignature_whatsapp(String signature_whatsapp) {
            this.signature_whatsapp = signature_whatsapp;
        }

        public String getSignature_text() {
            return signature_text;
        }

        public void setSignature_text(String signature_text) {
            this.signature_text = signature_text;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
