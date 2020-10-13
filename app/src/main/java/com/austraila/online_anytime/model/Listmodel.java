package com.austraila.online_anytime.model;

public class Listmodel {
    String ListText;
    String Item_id;
    public Listmodel(String ListText)
    {
        this.ListText=ListText;
//        this.Item_id = item_id;
    }

    public String getListText()
    {
        return ListText;
    }

//    public String getItem_id() {
//        return Item_id;
//    }
}
