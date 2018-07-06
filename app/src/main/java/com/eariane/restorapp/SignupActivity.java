package com.eariane.restorapp;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {
    TextInputEditText username, password, confirmPassword;
    Button btnSignup;
    Requests rq = new Requests(this);
    private static final String TAG = "SignupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm);
        btnSignup = findViewById(R.id.signup_btn);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString();
                String pwd = password.getText().toString();
                String cPwd = confirmPassword.getText().toString();

                if (!usr.equals("") && !pwd.equals("") && !cPwd.equals("")) {
                    rq.signup(usr, pwd, cPwd, new Requests.VolleyCallback() {
                        @Override
                        public void onSuccess(String resp) {
                            Toast.makeText(SignupActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject response = new JSONObject(resp);
                                Log.d(TAG, "onSuccess: " + resp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(String error) {
                            Log.d(TAG, "onFailure: " + error);Toast.makeText(SignupActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Uno o más de los campos está incompleto", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
