package com.rakantao.pcg.lacostazamboanga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterSubStation extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_register_sub_station);


        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.tvEmailSubStationAdmin);
        etPassword = findViewById(R.id.tvPasswordSubStationAdmin);
        etStation = findViewById(R.id.ETSelectSubStation);
        btnBack = findViewById(R.id.btnBackRegSubStationAdmin);
        btnRegister = findViewById(R.id.btnRegisterSubStationAdmin);
        progressBar = findViewById(R.id.progressBarRegSubStation);

        etStation.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ETSelectSubStation:
                final CharSequence[] items2 = {
                        "MANDAUE",
                        "HAGNAYA",
                        "NAGA",
                        "TOLEDO",
                        "CAMOTES",
                        "DANAO",
                        "BANTAYAN",
                        "ADUANA",
                        "TABUELAN",
                        "TINAGO",
                        "BATO",
                        "ARGAO",
                        "TANGIL",
                        "JAGNA",
                        "UBAY",
                        "TALIBON",
                        "TUBIGON",
                        "PANGLAO",
                        "LOAY",
                        "GETAFE",
                        "TAGBILARAN",
                        "PRESIDENT CARLOS P GARCIA"
                };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(RegisterSubStation.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int item) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                        databaseReference.orderByChild("SubStation").equalTo(String.valueOf(items2[item])).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    new AlertDialog.Builder(RegisterSubStation.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setMessage("Sorry, but this sub station is already registered. Please contact the district HQ for more info.")
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
                });////////////////////////////////////////
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
                case R.id.btnBackRegSubStationAdmin:
                new AlertDialog.Builder(RegisterSubStation.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(RegisterSubStation.this, LoginActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        case R.id.btnRegisterSubStationAdmin:

                final String getStation = etStation.getText().toString().trim();
                final String getEmail = etEmail.getText().toString().trim();
                String getPassword = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(getStation)||
                        TextUtils.isEmpty(getEmail) ||
                        TextUtils.isEmpty(getPassword)){
                    new AlertDialog.Builder(RegisterSubStation.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Please don't leave any blank field(s).")
                            .setNeutralButton("Ok", null)
                            .show();
                }else {

                    String MyStation = null;
                    if (getStation.equals("MANDAUE")
                            || getStation.equals("HAGNAYA")
                            || getStation.equals("NAGA")
                            || getStation.equals("TOLEDO")
                            || getStation.equals("CAMOTES")
                            || getStation.equals("DANAO")
                            || getStation.equals("BANTAYAN")
                            || getStation.equals("ADUANA")
                            || getStation.equals("TABUELAN")
                            || getStation.equals("TINAGO")
                            || getStation.equals("BATO")
                            || getStation.equals("ARGAO")
                            || getStation.equals("TANGIL")){

                        MyStation = "CGS CEBU";
                    }else if (getStation.equals("JAGNA")
                            || getStation.equals("UBAY")
                            || getStation.equals("TALIBON")
                            || getStation.equals("TUBIGON")
                            || getStation.equals("PANGLAO")
                            || getStation.equals("LOAY")
                            || getStation.equals("GETAFE")
                            || getStation.equals("TAGBILARAN")
                            || getStation.equals("PRESIDENT CARLOS P GARCIA")) {

                        MyStation = "CGS BOHOL";
                    }


                    progressBar.setVisibility(View.VISIBLE);

                    final String finalMyStation = MyStation;
                    mAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                assert currentUser != null;
                                String user_id = currentUser.getUid();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                HashMap<String, String> User = new HashMap<>();
                                User.put("SubStation", "CGSS "+getStation);
                                User.put("Email", getEmail);
                                User.put("Usertype", "pcgsubstation");
                                User.put("MyStation", finalMyStation);

                                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user_id);
                                databaseReference.setValue(User);

                                Toast.makeText(RegisterSubStation.this, "You've Successfully Registered", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                                mAuth.signOut();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                startActivity(new Intent(RegisterSubStation.this, LoginActivity.class));
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(RegisterSubStation.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;

        }
    }
}
