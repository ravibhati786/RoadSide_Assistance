package com.example.roadsideassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText registeredEmail;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final CardView cardView = findViewById(R.id.cv);
        final CardView cardView1 = findViewById(R.id.cv1);

        final Button button = findViewById(R.id.sendotp);
        final Button button1 = findViewById(R.id.verifyotp);

        firebaseAuth = FirebaseAuth.getInstance();

        registeredEmail = findViewById(R.id.email);
        newPassword = findViewById(R.id.newpassword);
        confirmPassword = findViewById(R.id.confirmpassword);
        resetPassword = findViewById(R.id.verifyotp);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Email sent successfully!",Toast.LENGTH_LONG);
                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT);

                                }
                            }
                        });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                button1.setVisibility(View.VISIBLE);
            }
        });
    }




}