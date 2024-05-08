package com.example.quizme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText email, password, password1;
    Button bregister;

    FirebaseAuth MyAuthentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.RegisterEmailEditText);
        password = findViewById(R.id.RegisterPasswordEditText);
        password1 = findViewById(R.id.ConfirmPasswordEditText);
        bregister = findViewById(R.id.RegisterButton);
        MyAuthentication = FirebaseAuth.getInstance();

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String pass1 = password1.getText().toString();
                if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(pass1)){
                    Toast.makeText(Register.this, "Veuillez insérer les champs obligatoires", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length()<6){
                    Toast.makeText(Register.this,"Nombre de caractères insuffisant", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(pass1)){
                    Toast.makeText(Register.this, "Vérification de password invalide", Toast.LENGTH_SHORT).show();
                    return;
                }
                SignUp(mail,pass);
            }
        });
    }

    public void SignUp(String mail, String password){
        MyAuthentication.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Enregistrement réussi",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(Register.this, "Enregistrement réussi" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}