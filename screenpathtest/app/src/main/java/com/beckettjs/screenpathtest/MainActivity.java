package com.beckettjs.screenpathtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.net.PasswordAuthentication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
    }
    protected boolean handleLogin(String usrName, PasswordAuthentication pass){
        //factory generate authenticathion
        //pass tokens to authenticator and wait on result
        //on fail, reset and increment fail count
        //on success. login and continue to next string
        return true;
    }
    protected void handleForgetPass(){
        //for android they need a google account right, can w us that
    }
    protected void tooManyFails(){
        //lockout time x, or call forgot pass dialog?
    }
    protected void callSelectionScreen(View view){
System.out.println("onclick");
        Intent myIntent = new Intent(this, MainScreen.class);
        startActivity(myIntent);
    }
}
