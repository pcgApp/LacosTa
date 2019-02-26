package com.rakantao.pcg.lacostazamboanga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterUser extends AppCompatActivity {

     EditText etLastName;
     EditText etMiddleName;
     EditText etFirstName;
     EditText etAdress;
     EditText etEmail;
     EditText etPassword;
     Button btnRegister;
     ProgressBar mProgressBar;
     RadioGroup rgGender;
     String mGender = null;
     DatabaseReference databaseReference;
     FirebaseAuth mAuth;
     ImageButton btnBack;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        btnBack = findViewById(R.id.btnBackRegUser);
        etLastName =  findViewById(R.id.tvLastNameUser);
        etMiddleName =  findViewById(R.id.tvMiddleNameUser);
        etFirstName =  findViewById(R.id.tvFirstNameUser);
        etAdress = findViewById(R.id.tvAddressUser);
        etEmail = findViewById(R.id.tvEmailUser);
        etPassword = findViewById(R.id.tvPasswordUser);
        btnRegister =  findViewById(R.id.btnRegisterUser);
        mProgressBar =  findViewById(R.id.progressBarRegUser);
        rgGender =  findViewById(R.id.rgGenderUser);

        selectGender();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(RegisterUser.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(RegisterUser.this, LoginActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
    private void selectGender() {

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rbMaleUser:
                        mGender = "Male";
                        break;
                    case R.id.rbFemaleUser:
                        mGender = "Female";
                        break;
                }
            }
        });
    }

    private void registerUser() {

        final String lastname = etLastName.getText().toString().trim();
        final String firstname = etFirstName.getText().toString().trim();
        final String middlename = etMiddleName.getText().toString().trim();
        final String address = etAdress.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(lastname) ||
                TextUtils.isEmpty(firstname) ||
                TextUtils.isEmpty(middlename) ||
                TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password)){

            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();

        } else if (mGender.isEmpty()){
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String user_id = currentUser.getUid();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                HashMap<String, String> User = new HashMap<String, String>();
                                User.put("FirstName", firstname);
                                User.put("LastName", lastname);
                                User.put("MiddleName", middlename);
                                User.put("Address", address);
                                User.put("Gender", mGender);
                                User.put("Email", email);
                                User.put("Password", password);
                                User.put("Usertype", "user");


                                databaseReference = firebaseDatabase.getReference("Users").child(user_id);
                                databaseReference.setValue(User);

                                Toast.makeText(RegisterUser.this, "You've Successfully Registered", Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.INVISIBLE);

                                mAuth.signOut();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                startActivity(new Intent(RegisterUser.this, LoginActivity.class));
                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(RegisterUser.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(RegisterUser.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(RegisterUser.this, LoginActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
