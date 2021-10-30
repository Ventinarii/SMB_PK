package com.example.s17149.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.example.s17149.AddOrEditActivity;
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

    //app version ----------------------------------------------------------------------------------
    private static final String spAppVer = "AppVer";
    private static final float ver = 0.8f;
    //=-

    //colors ---------------------------------------------------------------------------------------
    public static final String[]
            colorMC = {"colorMCr","colorMCg","colorMCb"},//mainColor
            colorTC = {"colorTCr","colorTCg","colorTCb"},//trimColor
            colorMT = {"colorMTr","colorMTg","colorMTb"},//textMainColor
            colorTT = {"colorTTr","colorTTg","colorTTb"};//textTrimColor
    public static final String
            colorACC = "colorACC";//autoContrastColor
    public static Color
            mainColor = Color.valueOf(1,1,1),
            trimColor = Color.valueOf(0,0,0),
            textMainColor = Color.valueOf(0,0,0),
            textTrimColor = Color.valueOf(1,1,1);
    public static boolean autoContrastColor = false;
    //=-

    //size of text ---------------------------------------------------------------------------------
    public static final String
            textMainSize = "textMainSize",//mainTextSize
            textLocale = "textLocale";//isPolishLocale
    public static int
            mainTextSize = 24;
    public static boolean
            isPolishLocale = false;
    //=-

    //============================================================================================== CODE

    /**
     * This function is responsible for creating default preferences on app first start or ver change
     *
     * it is responsible for following:
     * -check if settings exist -> if yes then load them
     * --if not then create them
     *
     */
    public static void onAppStart(Context intent){
        //app version
        float version = sp.getFloat(spAppVer,-1);
        if(version!=ver){
            spEditor.putFloat(spAppVer,ver);
            //=-

            //colors
            {
                spEditor.putFloat(colorMC[0],mainColor.red());
                spEditor.putFloat(colorMC[1],mainColor.green());
                spEditor.putFloat(colorMC[2],mainColor.blue());

                spEditor.putFloat(colorTC[0],trimColor.red());
                spEditor.putFloat(colorTC[1],trimColor.green());
                spEditor.putFloat(colorTC[2],trimColor.blue());

                spEditor.putFloat(colorMT[0],textMainColor.red());
                spEditor.putFloat(colorMT[1],textMainColor.green());
                spEditor.putFloat(colorMT[2],textMainColor.blue());

                spEditor.putFloat(colorTT[0],textTrimColor.red());
                spEditor.putFloat(colorTT[1],textTrimColor.green());
                spEditor.putFloat(colorTT[2],textTrimColor.blue());

                spEditor.putBoolean(colorACC,autoContrastColor);
            }
            //=-

            //size of text
            {
                spEditor.putInt(textMainSize,mainTextSize);
                spEditor.putBoolean(textLocale,isPolishLocale);
            }
            //=-

            spEditor.commit();
        }
        reLoadSettings();
    }

    /**
     * this function reloads settings form shared preferences
     */
    public static void reLoadSettings(){
        //colors
        {
            float[] rgb;

            rgb = loadSettingsArray(colorMC);
            mainColor = Color.valueOf(rgb[0],rgb[1],rgb[2]);

            rgb = loadSettingsArray(colorTC);
            trimColor = Color.valueOf(rgb[0],rgb[1],rgb[2]);

            rgb = loadSettingsArray(colorMT);
            textMainColor = Color.valueOf(rgb[0],rgb[1],rgb[2]);

            rgb = loadSettingsArray(colorTT);
            textTrimColor = Color.valueOf(rgb[0],rgb[1],rgb[2]);

            autoContrastColor = sp.getBoolean(colorACC,false);
        }
        //=-

        //size of text
        {
            mainTextSize = sp.getInt(textMainSize,-1);
            isPolishLocale = sp.getBoolean(textLocale,false);
        }
        //=-
    }

    /**
     * this function loads arrays of vales from shared preferences
     * @param keys array of strings that are going to be loaded
     * @return array of floats that were found. if no value is found -1 is returned
     */
    public static float[] loadSettingsArray(String[] keys){
        List<Float> list = Arrays.stream(keys).map(key->sp.getFloat(key,-1)).collect(Collectors.toList());

        float[] values = new float[list.size()];
        for(int x = 0; x<values.length; x++)//not the most elegant but there is no direct way to get float array from stream / list
            values[x]=list.get(x);

        return values;
    }


}
/*  apparently this the only way to change locale in runtime.
    -it works.
    -it breaks cache.
    --don't use it.
    ---android is dumb.
    -to apply change then restart app and it should work. - how? WHO KNOWS. I DON'T.
    -also- little riddle: what language code is used for default locale? does it depend on system language?

    fix: also apparently it doesn't like new API. for newer version use variant with createConfigurationContext.
    */
    /**
     * call this function in each activity onStart(). if you change locale and use cache (don't call onStart()) then it WON'T translate.
     * if you open NEW activity you will end up with app in TWO or MORE languages. GOOD JOB.
     * and no - it CAN'T be fixed
    */
    /*
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
 */