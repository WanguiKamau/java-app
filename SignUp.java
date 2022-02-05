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

public class SignUp extends AppCompatActivity {
    private Button btncraete;
    private EditText signmail, signpass, signconfirm;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signmail= findViewById(R.id.signmail);
        signpass= findViewById(R.id.signpass);
        signconfirm= findViewById(R.id.signconfirm);
        btncraete= findViewById(R.id.btnCreate);

        mAuth= FirebaseAuth.getInstance();
        dialog= new ProgressDialog(this);

        btncraete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {

        String mail = signmail.getText().toString();
        String pass= signpass.getText().toString();
        String confirm= signconfirm.getText().toString();

        if (TextUtils.isEmpty(mail))
        {
            Toast.makeText(this, "Please Provide Your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please Provide A Password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirm))
        {
            Toast.makeText(this, "Please Provide Confirm Password", Toast.LENGTH_SHORT).show();
        }
        else if (!pass.equals(confirm))
        {
            Toast.makeText(this, "Ensure Your Passwords Match", Toast.LENGTH_SHORT).show();
        }
        else {
            dialog.setTitle("Validating Details");
            dialog.setMessage("Wait While Your Information Is Validated");
            dialog.show();

            dialog.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        SendUserToHomeActivity();

                        Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SignUp.this, "Error Occurred" +message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });
        }
    }

    private void SendUserToHomeActivity()
    {

        Intent setup= new Intent(SignUp.this, Home.class);
        setup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setup);
        finish();
    }
}
