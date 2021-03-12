package com.commax.ocf.server.app.listview;

import android.widget.CheckBox;

public class MainListviewItem {
    int image;
    String title;
    String exp;

    public int getImage() {
        return image;
    }
    public String getTitle() {
        return title;
    }

    public void setExp(String exp){
        this.exp = exp;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setExp(int pos) {
        if(pos==0)
            this.exp = "registered";
        else if(pos==1)
            this.exp = "unregistered";
        else if(pos==2)
            this.exp = "reset";
    }

    public String getExp() {
        return exp;
    }

    public MainListviewItem(int image, String title, String exp) {
        this.image = image;
        this.title = title;
        this.exp = exp;
    }


}
