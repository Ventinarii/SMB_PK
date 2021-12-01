package com.example.s17149.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    public static FirebaseUser fUser;
    public static FirebaseDatabase fDatabase;
    public static DatabaseReference rDatabase;
    /**
     * this function tries to do following things in order:
     * 1) user logged in->update password
     * 2) ->login
     * 3) ->register
     *
     * @param biding biding of main activity - used for extracting strings -> should be reaplaced with explicit arguments (enail, password)
     * @param activity used as source of context (can use any other) used in creation of on complete listeners
     */
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
                            fUser = user;
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
                                            fUser = user;
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(activity, "Registration failed.",Toast.LENGTH_SHORT).show();
                                            activity.UpdateForUser(null);
                                            fUser = null;
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
    private static LiveData<List<Product>> visibleProducts;
    private static List<Product> allProducts;
    private static void chkDb(){
        if(rDatabase==null){
            rDatabase = fDatabase.getReference("productsDb");
            rDatabase.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
            rDatabase.child("products").setValue(Arrays.asList(new Product(-1,"-1",false,"-1",-1f,-1f,false)));

            allProducts = new ArrayList<Product>();
            visibleProducts = new MutableLiveData<List<Product>>(new ArrayList<Product>());

            rDatabase.child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    visibleProducts.getValue().clear();
                    List<Product> ob = ((ArrayList<HashMap<String,Object>>)snapshot.getValue()).stream()
                            .map(h->Product(h))
                            .collect(Collectors.toList());
                    allProducts = ob;
                    getAllVisibleProducts();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    int x = 2;
                }
            });
        }
    }
    private static Product Product(HashMap<String,Object> h){
        int id = ((Long)h.get("id")).intValue();
        String userId = (String)h.get("userId");
        boolean isPublic = (Boolean) h.get("public");
        String name = (String)h.get("name");
        float qty =  ((Long)h.get("qty")).floatValue();
        float price =  ((Long)h.get("price")).floatValue();
        boolean click = (Boolean)h.get("click");

        return new Product(id,userId,isPublic,name,qty,price,click);
    }
    public static void getAllVisibleProducts(){
        visibleProducts.getValue().clear();
        String userUid = fUser.getUid();
        List<Product> productsProcess = allProducts.stream().filter(p->
                p.getUserId().equals(userUid)||//belongs to us OR
                p.getPublic())//is public
                .collect(Collectors.toList());
        int x = 2;
        visibleProducts.getValue().addAll(productsProcess);
    }
    public static LiveData<List<Product>> getAllProducts(){
        chkDb();

        return visibleProducts;
    }
    public static void insert(Product product){
        chkDb();
        List<Product> products = allProducts;
        product.setId(products.size());
        products.add(product);
        rDatabase.child("products").setValue(products);
    }
    public static void  delete(Product product){
        chkDb();
        List<Product> products = allProducts;
        products.removeIf(p->p.getId()==product.getId());
        rDatabase.child("products").setValue(products);
    }
    public static void  update(Product product){
        chkDb();
        List<Product> products = allProducts;
        products.removeIf(p->p.getId()==product.getId());
        products.add(product);
        rDatabase.child("products").setValue(products);
    }
    public static void  deleteAll(){
        chkDb();
        rDatabase.child("products").setValue(new ArrayList<Product>());
    }
    //============================================================================================== DATA EXCHANGE
    public static Product product;
    public static ProductViewModel productViewModel;
}
