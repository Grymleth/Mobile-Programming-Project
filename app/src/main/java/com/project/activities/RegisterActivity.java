package com.project.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.project.builder.AppAlert;
import com.project.R;
import com.project.data.User;
import com.project.sqlite.DBHelper;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameFld;
    private EditText lastNameFld;
    private EditText usernameFld;
    private EditText passwordFld;
    private EditText confirmFld;

    private Button registerBtn;
    private Button cancelBtn;

    public ClickHandler clickHandler;
    public DialogHandler dialogHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initHandler();
        initViews();
    }

    private void initHandler() {
        // instantiate click handler class
        clickHandler = new ClickHandler();
        dialogHandler = new DialogHandler();
    }

    private void initViews() {
        // create references for views
        firstNameFld = findViewById(R.id.firstEdit);
        lastNameFld = findViewById(R.id.lastEdit);
        usernameFld = findViewById(R.id.userEdit);
        passwordFld = findViewById(R.id.passwordEdit);
        confirmFld = findViewById(R.id.confirmPassEdit);

        registerBtn = findViewById(R.id.registerBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        // set handlers

        registerBtn.setOnClickListener(clickHandler);
        cancelBtn.setOnClickListener(clickHandler);
    }



    // Handler class

    private class ClickHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v == registerBtn){
                registerBtnClicked();
            }
            else if(v == cancelBtn){
                cancelBtnClicked();
            }
        }
    }

    private class DialogHandler implements Dialog.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE){
                finish();
            }
        }
    }

    // procedures

    public void cancelBtnClicked(){
        Resources resource = getResources();
        AppAlert.createYesNoDialog(resource.getString(R.string.cancelAlertMessage),
                resource.getString(R.string.confirmString),
                dialogHandler,
                this).show();
    }

    public void registerBtnClicked(){
        Resources resource = getResources();
        // VALIDATION
        String message = "";
        if(!Auth.emptyFields(firstNameFld.getText().toString(),
                lastNameFld.getText().toString(),
                usernameFld.getText().toString(),
                passwordFld.getText().toString())){
            AppAlert.createMessageDialog(resource.getString(R.string.emptyfield),
                    resource.getString(R.string.invalid), this).show();

            return;
        }

        if(!Auth.validUsername(usernameFld.getText().toString(), getBaseContext())){
            if(!message.isEmpty())
                message += "\n";
            message += resource.getString(R.string.usernameValidation);
        }

        if(!passwordFld.getText().toString().equals(confirmFld.getText().toString())){
            if(!message.isEmpty())
                message += "\n";
            message += resource.getString(R.string.password_not_match);
        }

        if(!message.isEmpty()){
            AppAlert.createMessageDialog(message,
                    resource.getString(R.string.invalid),
                    this).show();
            return;
        }

        AppAlert.createYesNoDialog(resource.getString(R.string.registerAlertMessage),
                getResources().getString(R.string.password_not_match),
                dialogHandler,
                this).show();


        User regUser = new User(usernameFld.getText().toString(),
                firstNameFld.getText().toString(),
                lastNameFld.getText().toString());

        DBHelper db = new DBHelper(getBaseContext());

        db.openWritable();
        db.insertUser(regUser, passwordFld.getText().toString());
        db.close();
    }

    private static class Auth {

        private static boolean emptyFields(String...fields){
            for(String item: fields){
                if(item.isEmpty()){
                    return false;
                }
            }

            return true;
        }

        private static boolean validUsername(String username, Context context) {
            DBHelper db = new DBHelper(context);
            db.openReadable();

            List<User> users = db.getUsers();

            for(User item: users){
                if (item.getLoginId().equals(username)){
                    return false;
                }
            }
            return true;
        }

    }


}
