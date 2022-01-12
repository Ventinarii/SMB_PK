package com.example.s17149.Logic;

import android.content.Context;

import com.example.s17149.DataBase.MyRoomDatabase;
import com.example.s17149.DataBase.ShopDao;
import com.example.s17149.DataBase.ShopRepo;

public class WigetLogic {
    public static MyRoomDatabase db;
    public static ShopDao dao;
    public static ShopRepo repo;

    public static void init(Context context){
        if(db==null){
            db = MyRoomDatabase.Companion.getDatabase(context);
            dao = db.productDao();
            repo = new ShopRepo(dao);
        }
    }


}
