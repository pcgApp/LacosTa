package com.rakantao.pcg.lacostazamboanga;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterStationAdmin extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etStation;
     Button btnRegister;
     ImageButton btnBack;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_station_admin);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.tvEmailStationAdmin);
        etPassword = findViewById(R.id.tvPasswordStationAdmin);
        etStation = findViewById(R.id.ETSelectStation);
        btnBack = findViewById(R.id.btnBackRegStationAdmin);
        btnRegister = findViewById(R.id.btnRegisterStationAdmin);
        progressBar = findViewById(R.id.progressBarRegStation);

        etStation.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ETSelectStation:
                final CharSequence[] items2 = {
                        "CGS CEBU",
                        "CGS BOHOL",
                        "CGS CAMOTES"
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(RegisterStationAdmin.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int item) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                        databaseReference.orderByChild("Station").equalTo(String.valueOf(items2[item])).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    new AlertDialog.Builder(RegisterStationAdmin.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setMessage("Sorry, but this station's admin already registered. Please contact the district HQ for more info.")
                                            .setNeutralButton("Ok", null)
                                            .show();
                                    etStation.setText("");
                                }else {
                                    etStation.setText(items2[item]);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
            case R.id.btnBackRegStationAdmin:
                new AlertDialog.Builder(RegisterStationAdmin.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(RegisterStationAdmin.this, LoginActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case R.id.btnRegisterStationAdmin:

                final String getStation = etStation.getText().toString().trim();
                final String getEmail = etEmail.getText().toString().trim();
                String getPassword = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(getStation)||
                        TextUtils.isEmpty(getEmail) ||
                        TextUtils.isEmpty(getPassword)){
                    new AlertDialog.Builder(RegisterStationAdmin.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Please don't leave any blank field(s).")
                            .setNeutralButton("Ok", null)
                            .show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                assert currentUser != null;
                                String user_id = currentUser.getUid();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                HashMap<String, String> User = new HashMap<>();
                                User.put("Station", getStation);
                                User.put("Email", getEmail);
                                User.put("Usertype", "pcgstation");


                                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user_id);
                                databaseReference.setValue(User);

                                Toast.makeText(RegisterStationAdmin.this, "You've Successfully Registered", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                                mAuth.signOut();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                startActivity(new Intent(RegisterStationAdmin.this, LoginActivity.class));
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(RegisterStationAdmin.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;

        }
    }
}
