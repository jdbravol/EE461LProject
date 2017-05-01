package com.example.ee461lproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    private Button backButton;
    private EditText emailField;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToLogin();
            }
        });

        emailField = (EditText) findViewById(R.id.resetEmail);
        resetButton = (Button) findViewById(R.id.resetButton);

        //functionality for sending reset email
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = (String) emailField.getText().toString();
                if(!email.equals("")){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassword.this,
                                                "Reset email sent succesfully",
                                                Toast.LENGTH_SHORT).show();
                                        goBackToLogin();
                                    }
                                    else{
                                        Toast.makeText(ForgotPassword.this,
                                                "Wrong email address.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(ForgotPassword.this,
                            "Please input an email address",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void goBackToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
