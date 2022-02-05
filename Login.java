package com.example.tumainichurch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {
    private EditText mail,pass;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private DatabaseReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail=(EditText) findViewById(R.id.logmail);
        pass=(EditText) findViewById(R.id.logpass);
        login=(Button) findViewById(R.id.btn_login);

        mAuth= FirebaseAuth.getInstance();
        dialog= new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                AllowUserToLogIn();
            }
        });
    }




    private void AllowUserToLogIn()
    {
        String logmail = mail.getText().toString();
        String logpass= pass.getText().toString();

        if (TextUtils.isEmpty(logmail))
        {
            Toast.makeText(this, "Please Provide Your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(logpass))
        {
            Toast.makeText(this, "Please Provide A Password", Toast.LENGTH_SHORT).show();
        }

        else {
            dialog.setTitle("Confirming Details");
            dialog.setMessage("Wait While We Confirm Your Information ");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(logmail,logpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {

                    if (task.isSuccessful())
                    {
                        SendUserHome();
                        Toast.makeText(Login.this, "Welcome To Tumaini Church", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(Login.this, "Error Occurred"+message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserHome()
    {
        Intent logintent = new Intent(Login.this, Home.class);
        logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logintent);
        finish();

    }


}