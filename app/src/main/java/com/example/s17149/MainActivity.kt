package com.example.s17149

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser




class MainActivity : AppCompatActivity() {

    private lateinit var biding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(biding.root);

        if(AppLogic.sp==null){
            AppLogic.sp = getPreferences(MODE_PRIVATE);
            AppLogic.spEditor = AppLogic.sp.edit();
        }
        AppLogic.mainActivity = Intent(this,MainActivity::class.java);
        AppLogic.productListActivity = Intent(this,ProductListActivity::class.java);
        AppLogic.addOrEditActivity = Intent(this,AddOrEditActivity::class.java);
        //AppLogic.optionsActivity = Intent(this,OptionsActivity::class.java);

        AppLogic.fAuth = FirebaseAuth.getInstance();
        val currentUser: FirebaseUser? = AppLogic.fAuth.getCurrentUser();
        if(currentUser!=null)
            UpdateForUser(currentUser!!);
    }
    fun buAc01ProductList(view: android.view.View) {
        startActivity(AppLogic.productListActivity);
    }
    override fun onResume() {
        super.onResume();
    }
    fun LoginButton(view: android.view.View) {
        //1-change password;2-login;3-register
        AppLogic.login(biding,this);
    }

    /**
     * this function updates the values (text) of email and status text fields in UI
     * @param user user that is logged in. (or null if no user is logged in)
     */
    fun UpdateForUser(user:FirebaseUser?){
        if(user!=null){
            biding.UserNameTextField.setText(user.email);
            biding.editTextTextPassword.setText("");
            biding.UserIdTextView.setText("Logged in as User ${user.email} with uid: ${user.uid}");
        }else{
            biding.UserNameTextField.setText("");
            biding.editTextTextPassword.setText("");
            biding.UserIdTextView.setText("Not logged in");
        }

    }
}