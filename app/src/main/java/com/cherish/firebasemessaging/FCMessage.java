package com.cherish.firebasemessaging;

public class FCMessage {
    private String to;
    private  Data data;


    public FCMessage(String to, Data data) {
        this.to = to;
        this.data = data;
    }


    public String getTitle() {
        return to;
    }

    public void setTitle(String title) {
        this.to = title;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
