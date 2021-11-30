package com.example.s17149.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.example.s17149.AddOrEditActivity;
import com.example.s17149.DataBase.Product;
import com.example.s17149.DataBase.ProductViewModel;
import com.example.s17149.MainActivity;
import com.example.s17149.OptionsActivity;
import com.example.s17149.ProductListActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppLogic {
    //app activities & preferences -----------------------------------------------------------------
    public static Intent mainActivity;
    public static Intent productListActivity;
    public static Intent addOrEditActivity;

    public static Intent optionsActivity;

    public static SharedPreferences sp;
    public static SharedPreferences.Editor spEditor;
    //=-
    //user settings
    public static final String
            UserIdString = "UserId",
            UserNameString = "UserName",
            UserIsPublishingString = "UserIsPublishing";
    public static int
            UserId = -1;
    public static String
            UserName = "No Data";
    public static boolean
            UserIsPublishing = false;
    //=-
    //============================================================================================== CODE
    public static void onAppStart(Context intent) {

    }
    public static void checkUserData(){

    }
    //============================================================================================== DATA EXCHANGE
    public static Product product;
    public static ProductViewModel productViewModel;
}
