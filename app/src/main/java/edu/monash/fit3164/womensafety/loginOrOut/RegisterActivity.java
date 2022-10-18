package edu.monash.fit3164.womensafety.loginOrOut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextInputEditText et_username, et_email, et_mobile, et_password;
    private TextInputLayout tl_username, tl_email, tl_mobile, tl_pass;
    private ProgressBar pBar;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.register);

        et_username = findViewById(R.id.et_reg_username);
        et_email = findViewById(R.id.et_reg_email);
        et_mobile = findViewById(R.id.et_reg_mobile);
        et_password = findViewById(R.id.et_reg_pass);

        tl_username = findViewById(R.id.tl_reg_username);
        tl_email = findViewById(R.id.tl_reg_email);
        tl_mobile = findViewById(R.id.tl_reg_mobile);
        tl_pass = findViewById(R.id.tl_reg_pass);

        pBar = findViewById(R.id.progressBar_reg);

        findViewById(R.id.btn_register_successful).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_username.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        et_mobile.addTextChangedListener(this);
        et_password.addTextChangedListener(this);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_successful:
                registerAccount();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    // User register a new account with email.
    private void registerAccount(){
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String mobile = et_mobile.getText().toString();
        String password = et_password.getText().toString();

        // Check username input is not empty.
        if (username.isEmpty()){
            tl_username.setError("Username is required.");
            et_username.requestFocus();
            return;
        }
        // Check email input is not empty.
        if (email.isEmpty()){
            tl_email.setError("Email is required.");
            et_email.requestFocus();
            return;
        }
        // Check email format is correct.
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tl_email.setError("Email is invalid");
            et_email.requestFocus();
            return;
        }
        // Check mobile input is not empty.
        if (mobile.isEmpty()){
            tl_mobile.setError("Mobile is required.");
            et_mobile.requestFocus();
        }
        // Australian mobile has at least 10 digits. Check mobile is valid or not
        if (mobile.length() < 10){
            tl_mobile.setError("Mobile is invalid.");
            et_mobile.requestFocus();
            return;
        }
        // Check password input is not empty.
        if (password.isEmpty()){
            tl_pass.setError("Password is required.");
            et_password.requestFocus();
            return;
        }
        // Check password input is longer than 6 digits.
        if (password.length() < 6){
            tl_pass.setError("Password should longer than 6.");
            et_password.requestFocus();
            return;
        }

        // Register account with email and password.
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if register successful, upload user information to firebase.
                        if (task.isSuccessful()){
                            String smsMessage = "I am in danger and I need help";
                            User user = new User(username, email, mobile, "None", smsMessage);
                            reference.child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user);
                            Toast.makeText(RegisterActivity.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                            pBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            finish();
                        }else{
                            // if email has already registered with an exist account, register failed.
                            tl_email.setError("This email has already been registered.");
                            et_email.requestFocus();
                            pBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tl_username.setError(null);
        tl_email.setError(null);
        tl_mobile.setError(null);
        tl_pass.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}