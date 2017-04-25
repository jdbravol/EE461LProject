package com.example.ee461lproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by juanbravo on 4/22/17.
 */

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button newUserButton;
    private Button forgotPasswordButton;
    private Button loginButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //1.0 link EditText and Buttons in layouts
        email = (EditText) findViewById(R.id.emailLine);
        password = (EditText) findViewById(R.id.passwordLine);
        newUserButton = (Button) findViewById(R.id.newUserButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPassButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        //2.0 Add onclick functionality to each button

        //2.1 newUserButton
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCreateUserPage();
            }
        });

        //2.2 forgotPasswordButton
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadForgotPasswordPage();
            }
        });

        //2.3 Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loadCreateUserPage() {
        Intent intent = new Intent(this, CreateUser.class);
        startActivity(intent);
    }

    private void loadForgotPasswordPage() {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }
}
