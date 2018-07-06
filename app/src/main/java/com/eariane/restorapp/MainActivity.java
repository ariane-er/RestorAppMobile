package com.eariane.restorapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Requests requests = new Requests(this);
    TextInputEditText userInput, passwordInput;
    Button btnLogin, btnSignup;
    Requests rq = new Requests(this);
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInput = findViewById(R.id.main_username);
        passwordInput = findViewById(R.id.main_password);
        btnLogin = findViewById(R.id.main_btn_login);
        btnSignup = findViewById(R.id.main_btn_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The actual input of the user
                final String inputUsername = userInput.getText().toString();
                final String inputPassword = passwordInput.getText().toString();

                // First verify if the credentials were even set
                if (!inputUsername.equals("") && !inputPassword.equals("")) {
                    // Verification of the credentials. This is usually done server-side
                    rq.login(inputUsername, inputPassword, new Requests.VolleyCallback() {
                        @Override
                        public void onSuccess(String resp) {
                            // TODO: Fix validation
//                            Toast.makeText(MainActivity.this, resp, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, MapActivity.class));
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(MainActivity.this, "Error del servidor", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, MapActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Uno o más de los campos está incompleto", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
