package com.example.bookblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity
{
    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccount, AlreadyHaveAccount;
    private ProgressBar registerBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        registerBar = (ProgressBar) findViewById(R.id.reg_progress);
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_cpassword);
        CreateAccount = (Button) findViewById(R.id.register_create_account);
        AlreadyHaveAccount = (Button) findViewById(R.id.already_account);
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             finish();
            }
        });
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount()
    {

        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirm_password = UserConfirmPassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password))
        {
            if (password.equals(confirm_password))
            {
                registerBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            SendUserToSetupActivity();
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();

                        }
                        registerBar.setVisibility(View.INVISIBLE);
                    }

                });
            }
            else
            {
                Toast.makeText(this, "Your Password do not match your Confirm password..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SendUserToSetupActivity()
    {
        Intent SetupIntent = new Intent(RegisterActivity.this, SetUpActivity.class);
        SetupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIntent);
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null)
        {
            SendUserToMainActivity();
        }
        else{

        }
    }

    private void SendUserToMainActivity()
    {
        Intent MainIntent = new Intent (RegisterActivity.this, MainActivity.class);
        startActivity(MainIntent);
        finish();
    }
}
