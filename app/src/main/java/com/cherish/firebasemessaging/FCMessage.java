package com.cherish.firebasemessaging;

public class FCMessage {
    private String to;
    private Data data;
//    private Data notification;



    public FCMessage(String to, Data data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
