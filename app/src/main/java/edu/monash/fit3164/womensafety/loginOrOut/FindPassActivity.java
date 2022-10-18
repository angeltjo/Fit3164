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
import com.google.firebase.auth.FirebaseAuth;

import edu.monash.fit3164.womensafety.R;

public class FindPassActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private TextInputEditText et_email;
    private TextInputLayout tl_email;
    private ProgressBar pBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.find_password);

        et_email = findViewById(R.id.et_find_email);

        tl_email = findViewById(R.id.tl_find_email);

        pBar = findViewById(R.id.progressBar_find);

        findViewById(R.id.btn_verify_find).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_email.addTextChangedListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_verify_find:
                findPass();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }


    private void findPass(){
        String email = et_email.getText().toString();
        // Check email input is not empty
        if (email.isEmpty()){
            tl_email.setError("Email is required.");
            et_email.requestFocus();
            return;
        }
        // check email format is correct.
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tl_email.setError("Email is invalid");
            et_email.requestFocus();
            return;
        }

        // Send reset password email to user's email, user can reset password with link in the email.
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(FindPassActivity.this, "Check your email and reset your password.", Toast.LENGTH_SHORT).show();
                    pBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finish();
                }else{
                    Toast.makeText(FindPassActivity.this, "Reset password wrong, please try again", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


}