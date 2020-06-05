package com.example.group21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassowrdGabay extends AppCompatActivity {

    private ImageButton backForgotPassGabay;
    private EditText emailGabay;
    private Button recoverPassButtonGabay;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passowrd_gabay);

        backForgotPassGabay = (ImageButton) findViewById(R.id.backForgotPassGabayID);
        emailGabay = (EditText) findViewById(R.id.EmailGabayID);
        recoverPassButtonGabay = (Button) findViewById(R.id.recoverPassGabayID);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן ...");
        progressDialog.setCanceledOnTouchOutside(false);

        backForgotPassGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recoverPassButtonGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPassword();
            }
        });
    }

    private String Email;

    private void recoverPassword() {
        Email = emailGabay.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Toast.makeText(this, "כתובת מייל אינה חוקית", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("שולח הוראות לאיפוס הסיסמא ...");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(Email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //instructions send
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassowrdGabay.this, "הוראות לאיפוס הסיסמא נשלחו למייל", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to send
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassowrdGabay.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
