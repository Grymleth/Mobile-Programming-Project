package com.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.R;
import com.project.builder.AppAlert;
import com.project.data.User;
import com.project.sqlite.DBHelper;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "DEBUG";
    private EditText usernameFld;
    private EditText passwordFld;

    private Button registerBtn;
    private Button loginBtn;

    public ClickHandler clickHandler;
    public KeyHandler keyHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initHandler();
        initViews();
    }

    private void initHandler() {
        // instantiate click handler class
        clickHandler = new ClickHandler();
        keyHandler = new KeyHandler();
    }

    private void initViews() {
        // create references for views
        usernameFld = findViewById(R.id.userInput);
        passwordFld = findViewById(R.id.passInput);

        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);

        // set handlers
        passwordFld.setOnKeyListener(keyHandler);

        registerBtn.setOnClickListener(clickHandler);
        loginBtn.setOnClickListener(clickHandler);
    }

    // Handler class
    private class ClickHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v == registerBtn){
                onRegisterBtnClick();
            }
            else if(v == loginBtn){
                onLoginBtnClick();
            }
        }
    }

    //clicked methods
    private void onRegisterBtnClick(){
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void onLoginBtnClick(){
        Resources resource = getResources();
        String username = usernameFld.getText().toString();
        String password = passwordFld.getText().toString();
        User user;
        try{
            DBHelper db = new DBHelper(getApplicationContext());
            db.openReadable();
            user = db.getUser(username, password);
            db.close();
        }
        catch(CursorIndexOutOfBoundsException ex){
            AppAlert.createLongToast(resource.getString(R.string.user_not_found),
                    getBaseContext()).show();
            return;
        }

        AppAlert.createMessageDialog(resource.getString(R.string.greeting_text)+ " " +user.getFirstName(),
                resource.getString(R.string.welcome_title),
                MainActivity.this).show();
    }

    private class KeyHandler implements View.OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(passwordFld.hasFocus()){
                        loginBtn.callOnClick();
                    }
                }
            }
            return false;
        }
    }
}
