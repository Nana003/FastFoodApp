package com.example.fastfoodapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastfoodapp.R;
import com.example.fastfoodapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {
    EditText registerEmail, registerUsername, registerPassword, verifyPwd;
    Button btnRegister;
    TextView goToLogin;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerEmail = findViewById(R.id.editTextRegisterEmail);
        registerUsername = findViewById(R.id.editTextRegisterUsername);
        registerPassword = findViewById(R.id.editTextRegisterPassword);
        verifyPwd = findViewById(R.id.editTextRegisterConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        goToLogin = findViewById(R.id.textViewBtnLogin);

        //ref = FirebaseDatabase.getInstance().getReference().child("User");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if there is an empty field
                if(registerEmail.getText().toString().isEmpty() || registerUsername.getText().toString().isEmpty() ||
                        registerPassword.getText().toString().isEmpty() || verifyPwd.getText().toString().isEmpty()){
                    //ask the user to fill out the blanks
                    Toast.makeText(RegisterActivity.this, "Please fill out the blanks", Toast.LENGTH_LONG).show();
                    //if the passwords doesn't matches
                }else if (!registerPassword.getText().toString().equals(verifyPwd.getText().toString())){
                    //ask the user to matches the password
                    Toast.makeText(RegisterActivity.this, "Password doesn't matches", Toast.LENGTH_LONG).show();
                    //if the password is smaller than 6
                }else if(registerPassword.getText().toString().length() < 6) {
                    //ask the user to make a longer password
                    Toast.makeText(RegisterActivity.this, "Your password must be above 6 letter", Toast.LENGTH_LONG).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()){
                    Toast.makeText(RegisterActivity.this, "Enter a correct email address", Toast.LENGTH_LONG).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "You are registered!", Toast.LENGTH_SHORT).show();
                                String bcryptHashString = BCrypt.withDefaults().hashToString(12, registerPassword.getText().toString().toCharArray());
                                String profilePic = "https://firebasestorage.googleapis.com/v0/b/fastfoodapp-3892c.appspot.com/o/default.png?alt=media&token=de05fbc9-373c-4ab3-94ba-af7ea2f49f4d";
                                User user = new User(registerUsername.getText().toString(), bcryptHashString, "Customer", registerEmail.getText().toString(), profilePic);
                                String userId = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(userId).setValue(user);
                                registerEmail.getText().clear();
                                registerUsername.getText().clear();
                                registerPassword.getText().clear();
                                verifyPwd.getText().clear();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}