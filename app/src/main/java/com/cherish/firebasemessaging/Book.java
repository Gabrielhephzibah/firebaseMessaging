package com.cherish.firebasemessaging;

public class Book {
    private String bookName;
    private String bookDescription;
    private  String bookCategory;
    private  String bookImage;

    public Book(String bookName, String bookDescription, String bookCategory, String bookImage) {
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.bookCategory = bookCategory;
        this.bookImage = bookImage;
    }


    public Book(){

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

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", bookDescription='" + bookDescription + '\'' +
                ", bookCategory='" + bookCategory + '\'' +
                ", bookImage='" + bookImage + '\'' +
                '}';
    }
}
