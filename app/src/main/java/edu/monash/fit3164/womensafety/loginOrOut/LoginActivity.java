package edu.monash.fit3164.womensafety.loginOrOut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

import edu.monash.fit3164.womensafety.R;

public class LoginActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private TextInputEditText et_email, et_password;
    private TextInputLayout tl_email, tl_pass;
    private ProgressBar pBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.login);

        mAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_log_email);
        et_password = findViewById(R.id.et_log_pass);

        tl_email = findViewById(R.id.tl_log_email);
        tl_pass = findViewById(R.id.tl_log_pass);

        pBar = findViewById(R.id.progressBar_log);

        findViewById(R.id.btn_login_successful).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_forget_pass).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_email.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_successful:
                loginUser();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_pass:
                startActivity(new Intent(this, FindPassActivity.class));
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void loginUser() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        // check email is not empty.
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
        // Check password is not empty.
        if (password.isEmpty()){
            tl_pass.setError("Password is required.");
            et_password.requestFocus();
            return;
        }

        // Check user email and password, if correct login.
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert fUser != null;
                    // if user email has not verified, send verification email to user.
                    // if user email has already verified, login.
                    if (fUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                        pBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
                    }else{
                        fUser.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check you email to verify your account", Toast.LENGTH_SHORT).show();
                        pBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }else{
                    tl_email.setError("Email maybe wrong");
                    tl_pass.setError("Password maybe wrong");
                    Toast.makeText(LoginActivity.this,"Login failed", Toast.LENGTH_SHORT).show();
                    pBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tl_email.setError(null);
        tl_pass.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


}