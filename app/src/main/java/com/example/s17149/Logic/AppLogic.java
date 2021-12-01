package com.example.s17149.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.s17149.AddOrEditActivity;
import com.example.s17149.DataBase.Product;
import com.example.s17149.DataBase.ProductViewModel;
import com.example.s17149.MainActivity;
import com.example.s17149.OptionsActivity;
import com.example.s17149.ProductListActivity;
import com.example.s17149.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    public static FirebaseAuth fAuth;

    public static void login(ActivityMainBinding biding, MainActivity activity) {
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser!=null&&
                currentUser.getEmail().equals(biding.UserNameTextField.getText().toString())){
            currentUser
                    .updatePassword(
                            biding
                                    .editTextTextPassword
                                    .getText()
                                    .toString());
            Toast.makeText(activity, "Password updated to: "+
                    biding
                            .editTextTextPassword
                            .getText()
                            .toString(), Toast.LENGTH_SHORT).show();
        }else{
            fAuth
                .signInWithEmailAndPassword(biding.UserNameTextField.getText().toString(), biding.editTextTextPassword.getText().toString())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fAuth.getCurrentUser();
                            activity.UpdateForUser(user);
                            Toast.makeText(activity, "Authentication success. user"+user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(activity, "Authentication failed. Trying to register", Toast.LENGTH_SHORT).show();

                            fAuth.createUserWithEmailAndPassword(biding.UserNameTextField.getText().toString(), biding.editTextTextPassword.getText().toString())
                                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = fAuth.getCurrentUser();
                                            activity.UpdateForUser(user);
                                            Toast.makeText(activity, "Registration success. user"+user.getEmail(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(activity, "Registration failed.",Toast.LENGTH_SHORT).show();
                                            activity.UpdateForUser(null);
                                        }
                                    }
                                });
                            }
                        }
                    });
        }
    }
    //=-
    //============================================================================================== CODE
    public static void onAppStart() {

    }
    public static void checkUserData(){

    }
    //============================================================================================== DATA EXCHANGE
    public static Product product;
    public static ProductViewModel productViewModel;
}
