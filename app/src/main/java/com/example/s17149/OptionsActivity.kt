package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityMainBinding
import com.example.s17149.databinding.ActivityOptionsBinding

class OptionsActivity : AppCompatActivity() {

    private lateinit var biding: ActivityOptionsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityOptionsBinding.inflate(layoutInflater);
        setContentView(biding.root);

    }
    override fun onResume() {
        super.onResume();
        loadValuesFromAppLogic();
    }

    fun buAcOptionsBack(view: android.view.View) {
        startActivity(AppLogic.mainActivity);
    }

    //============================================================================================== CODE

    /**
     * this is simple clamp for purpose of setting value of seek bar progress.
     * @i input - any int.
     * @return value between 0 and 10.
     */
    private fun clamp(i: Int): Int {
        return if (i < 0) 0 else if (10 < i) 10 else i
    }

    private var v = 0;
    /**
     * this function is using AppLogic.sp directly. this is done in order to prevents unexpected bugs (sync lag).
     * it is expensive.
     */
    fun loadValuesFromAppLogic(){
        //colors
        v = (AppLogic.sp.getFloat(AppLogic.colorMC[0], 0f) / 25.5f).toInt();
        biding.sK01.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorMC[1], 0f) / 25.5f).toInt();
        biding.sK02.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorMC[2], 0f) / 25.5f).toInt();
        biding.sK03.progress = clamp(v);

        v = (AppLogic.sp.getFloat(AppLogic.colorTC[0], 0f) / 25.5f).toInt();
        biding.sK04.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorTC[1], 0f) / 25.5f).toInt();
        biding.sK05.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorTC[2], 0f) / 25.5f).toInt();
        biding.sK06.progress = clamp(v);

        v = (AppLogic.sp.getFloat(AppLogic.colorMT[0], 0f) / 25.5f).toInt();
        biding.sK07.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorMT[1], 0f) / 25.5f).toInt();
        biding.sK08.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorMT[2], 0f) / 25.5f).toInt();
        biding.sK09.progress = clamp(v);

        v = (AppLogic.sp.getFloat(AppLogic.colorTT[0], 0f) / 25.5f).toInt();
        biding.sK10.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorTT[1], 0f) / 25.5f).toInt();
        biding.sK11.progress = clamp(v);
        v = (AppLogic.sp.getFloat(AppLogic.colorTT[2], 0f) / 25.5f).toInt();
        biding.sK12.progress = clamp(v);

        biding.aCCswitch.isChecked = AppLogic.sp.getBoolean(AppLogic.colorACC,false);
        //=-

        //text
        v = (AppLogic.sp.getInt(AppLogic.textMainSize, 0)).toInt();
        v = (v-4)/2;
        biding.textSizeSeekBar.progress = clamp(v);

        biding.plLocaleSwitch.isChecked = AppLogic.sp.getBoolean(AppLogic.textLocale,false);
        //=-
    }

    /**
     * this function saves data directly to AppLogic.spEditor and commits them. this is break of hermetical rules.
     * this is done so as to avoid writing and maintaining convoluted and uncalled for interface to change those vars.
     * it MAY cause problems. direct reference to sp and spEditor should be restricted to AppLogic and OptionsActivity -=ONLY=-.
     */
    fun buAcOptionsSave(view: android.view.View) {
        //=-

        //colors
        //{
            AppLogic.spEditor.putFloat(AppLogic.colorMC[0], biding.sK01.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorMC[1], biding.sK02.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorMC[2], biding.sK03.progress*25.5f);

            AppLogic.spEditor.putFloat(AppLogic.colorTC[0], biding.sK04.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorTC[1], biding.sK05.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorTC[2], biding.sK06.progress*25.5f);

            AppLogic.spEditor.putFloat(AppLogic.colorMT[0], biding.sK07.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorMT[1], biding.sK08.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorMT[2], biding.sK09.progress*25.5f);

            AppLogic.spEditor.putFloat(AppLogic.colorTT[0], biding.sK10.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorTT[1], biding.sK11.progress*25.5f);
            AppLogic.spEditor.putFloat(AppLogic.colorTT[2], biding.sK12.progress*25.5f);

            AppLogic.spEditor.putBoolean(AppLogic.colorACC, biding.aCCswitch.isChecked);
        //}
        //=-

        //size of text
        //{
            AppLogic.spEditor.putInt(AppLogic.textMainSize, biding.textSizeSeekBar.progress*2+4);
            AppLogic.spEditor.putBoolean(AppLogic.textLocale, biding.plLocaleSwitch.isChecked);
        //}
        //=-

        AppLogic.spEditor.commit();

        AppLogic.reLoadSettings();

        loadValuesFromAppLogic();
    }

}