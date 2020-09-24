package com.cherish.firebasemessaging;

public class BookItem {

    private  String bookName;
    private  String bookDescription;
    private  String bookImage;
    private  String bookCategory;


    public BookItem(String bookName, String bookDescription, String bookImage, String bookCategory) {
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.bookImage = bookImage;
        this.bookCategory = bookCategory;
    }

    public  BookItem(){

    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }
}
