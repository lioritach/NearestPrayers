package com.example.group21.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group21.MainActivity;
import com.example.group21.MainGabayActivity;
import com.example.group21.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GabayLogin extends AppCompatActivity {

    private EditText emailGabay, passwordGabay;
    private TextView forgotPasswordGabay, noAccountGabay;
    private ImageView backLoginGabay;
    private Button loginGabay;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gabay_login);

        //init
        emailGabay = (EditText) findViewById(R.id.EmailGabayID);
        passwordGabay = (EditText) findViewById(R.id.passwordGabayID);
        forgotPasswordGabay = (TextView) findViewById(R.id.forgotPasswordGabayID);
        noAccountGabay = (TextView) findViewById(R.id.noAccountGabayID);
        loginGabay = (Button) findViewById(R.id.loginGabayButtonID);
        backLoginGabay = (ImageView) findViewById(R.id.backLoginGabayID);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן ...");
        progressDialog.setCanceledOnTouchOutside(false);

        noAccountGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GabayLogin.this, GabayRegister.class));
            }
        });

        forgotPasswordGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GabayLogin.this, ForgotPassowrdGabay.class));
            }
        });

        loginGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginGabbay();
            }
        });

        backLoginGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GabayLogin.this, MainActivity.class));
            }
        });
    }


    private String Email, Password;

    private void loginGabbay(){
        Email = emailGabay.getText().toString().trim();
        Password = passwordGabay.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            //Toast.makeText(this, "כתובת מייל אינה חוקית", Toast.LENGTH_SHORT).show();
            emailGabay.setError("כתובת מייל אינה חוקית");
            return;
        }
        if(TextUtils.isEmpty(Password)){
            //Toast.makeText(this, "שדה סיסמא הינו חובה", Toast.LENGTH_SHORT).show();
            passwordGabay.setError("שדה סיסמא הינו חובה");
            return;
        }

        progressDialog.setMessage("מתחבר ...");
        progressDialog.show();
        progressDialog.dismiss();

        firebaseAuth.signInWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //logged in successful
                        makeMeOnline();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed logged in
                        progressDialog.dismiss();
                        Toast.makeText(GabayLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void makeMeOnline() {
        //after logging in make user online
        progressDialog.setMessage("בודק פרטי משתמש ...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online","true");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update successfully
                        startActivity(new Intent(GabayLogin.this, MainGabayActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss();
                        Toast.makeText(GabayLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
