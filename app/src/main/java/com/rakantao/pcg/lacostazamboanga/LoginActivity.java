package com.rakantao.pcg.lacostazamboanga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.PCGAdminHome;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities.PCGHomeActivity;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Activities.PcgStationAdminHome;
import com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Activities.SubStationAdminHome;
import com.rakantao.pcg.lacostazamboanga.PublicUser.Activities.UserHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBar;
    DatabaseReference databaseReference;
    public String  userID;
    ImageButton btnQr;
    private IntentIntegrator qrScan;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        qrScan = new IntentIntegrator(this);

        btnQr = findViewById(R.id.btnQr);
        etEmail = findViewById(R.id.et_username);
        etPassword =  findViewById(R.id.et_password);
        btnLogin =  findViewById(R.id.btn_login);
       /* tvNoAccount = findViewById(R.id.tvNoAccount);*/
        mProgressBar =  findViewById(R.id.progressBarLogin);

        btnQr.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }

    boolean twice;
    @Override
    public void onBackPressed() {
        if (twice == true){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);
        twice = true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnQr:
                qrScan.initiateScan();
                break;
            /*case R.id.tvNoAccount:
                startActivity(new Intent(LoginActivity.this, RegisterUser.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;*/
            case R.id.btn_login:
                String getEmail = etEmail.getText().toString();
                String getPass = etPassword.getText().toString();

                boolean cancel = false;
                View focusView = null;
                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(getPass) && !isPasswordValid(getPass)) {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
                    focusView = etEmail;
                    cancel = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(getEmail)) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
                    focusView = etEmail;
                    cancel = true;
                } else if (!isEmailValid(getEmail)) {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show();
                    focusView = etEmail;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(getEmail,getPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        getUserData();

                                    }else {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    String container;
                    container = (obj.getString(""));

                    if (!container.equals("login")){
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        finish();
                    }else if (!container.equals("pcgstation")){
                        startActivity(new Intent(LoginActivity.this, RegisterStationAdmin.class));
                        finish();
                    }else if (!container.equals("substation")){
                        startActivity(new Intent(LoginActivity.this, RegisterSubStation.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }else {
                        Toast.makeText(this, "Invalid QR Code" , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    String var = result.getContents();

                    if (var.equals("login")){
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }else if (var.equals("pcgstation")){
                        startActivity(new Intent(LoginActivity.this, RegisterStationAdmin.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }else if (var.equals("substation")){
                        startActivity(new Intent(LoginActivity.this, RegisterSubStation.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }else {
                        Toast.makeText(this, "Invalid QR Code" , Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void getUserData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID =  currentUser.getUid();

        databaseReference.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataUser user = dataSnapshot.getValue(DataUser.class);
                if(dataSnapshot.exists()){

                    String usertype = (user.Usertype);

                    if (firebaseAuth.getCurrentUser() != null) {
                            if (usertype.equals("admin")){
                                startActivity(new Intent(LoginActivity.this, PCGAdminHome.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }else if (usertype.equals("personnel")){
                                startActivity(new Intent(LoginActivity.this, PCGHomeActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }else if (usertype.equals("user")){
                                startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }else if (usertype.equals("pcgstation")){
                                startActivity(new Intent(LoginActivity.this, PcgStationAdminHome.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }else if (usertype.equals("pcgsubstation")){
                                startActivity(new Intent(LoginActivity.this, SubStationAdminHome.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                            }
                    }else {
                        Toast.makeText(LoginActivity.this, "Please, check your internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
