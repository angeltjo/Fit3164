package edu.monash.fit3164.womensafety.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.monash.fit3164.womensafety.R;

public class ChangePasswordActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {



    private Button btn_edit;
    private TextInputLayout tl_old, tl_new, tl_again;
    private TextInputEditText et_old, et_new, et_again;

    private ProgressBar pBar;

    private FirebaseAuth auth;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.change_password);

        tl_old = findViewById(R.id.tl_old_pass);
        tl_new = findViewById(R.id.tl_new_pass);
        tl_again = findViewById(R.id.tl_pass_again);

        et_old = findViewById(R.id.et_old_pass);
        et_new = findViewById(R.id.et_new_pass);
        et_again = findViewById(R.id.et_pass_again);

        pBar = findViewById(R.id.pBar_edit_pass);

        findViewById(R.id.btn_back).setOnClickListener(this);
        btn_edit = findViewById(R.id.btn_edit_pass);
        btn_edit.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();

        et_old.addTextChangedListener(this);
        et_new.addTextChangedListener(this);
        et_again.addTextChangedListener(this);

    }

    @Override
    public void onClick(View view) {
        String btn_text = btn_edit.getText().toString();
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_edit_pass:
                if (btn_text.equals("Verify")){
                    verifyPass();
                }else if (btn_text.equals("Save")){
                    SaveNewPass();
                }
                break;
        }
    }

    // Edit password
    private void SaveNewPass() {
        String old_pass = et_old.getText().toString();
        String new_pass = et_new.getText().toString();
        String pass_again = et_again.getText().toString();
        // Check old password input is not empty.
        if (old_pass.isEmpty()){
            tl_old.setError("Old password is required.");
            et_old.requestFocus();
            return;
        }
        // Check old password input is longer than 6 digits.
        if (old_pass.length() < 6){
            tl_old.setError("Old password should longer than 6.");
            et_old.requestFocus();
            return;
        }
        // Check new password input is not empty.
        if (new_pass.isEmpty()){
            tl_new.setError("New password is required.");
            et_new.requestFocus();
            return;
        }
        // Check new password input is longer than 6 digits.
        if (new_pass.length() < 6){
            tl_new.setError("New password should longer than 6.");
            et_new.requestFocus();
            return;
        }
        // Check repeat password input is not empty.
        if (pass_again.isEmpty()){
            tl_again.setError("Password is required.");
            et_again.requestFocus();
            return;
        }
        // Check repeat password input is longer than 6 digits.
        if (pass_again.length() < 6){
            tl_again.setError("Password should longer than 6.");
            et_again.requestFocus();
            return;
        }
        // Check new password and password again are same.
        if (!new_pass.equals(pass_again)){
            tl_again.setError("Password is not same as the new password.");
            et_again.requestFocus();
            return;
        }
        // Check new password is not same as old password.
        if (new_pass.equals(old_pass)){
            tl_new.setError("New password should not same as old password");
            et_new.requestFocus();
            tl_again.setError("New password should not same as old password");
            et_again.requestFocus();
            return;
        }

        // Update password in firebase.
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        fUser.updatePassword(new_pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ChangePasswordActivity.this, "Update password successful.", Toast.LENGTH_SHORT).show();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            pBar.setVisibility(View.GONE);
                            finish();
                        }else{
                            Toast.makeText(ChangePasswordActivity.this, "Password change failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void verifyPass() {
        String old_pass = et_old.getText().toString();
        String email = fUser.getEmail();
        // Check old password input is not empty.
        if (old_pass.isEmpty()){
            tl_old.setError("Old password is required.");
            et_old.requestFocus();
            return;
        }
        // Check old password input is longer than 6 digits.
        if (old_pass.length() < 6){
            tl_old.setError("Old password should longer than 6.");
            et_old.requestFocus();
            return;
        }
        if (email == null){
            return;
        }

        // Check user old password input is correct,
        // If password is correct, verified successful.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, old_pass);

        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        fUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // if verification successful, show the new password input textView
                        if (task.isSuccessful()){
                            tl_new.setVisibility(View.VISIBLE);
                            tl_again.setVisibility(View.VISIBLE);
                            btn_edit.setText(R.string.save);
                            Toast.makeText(ChangePasswordActivity.this, "verify successful.", Toast.LENGTH_SHORT).show();
                        }else{
                            tl_old.setError("Old password is wrong.");
                            et_old.requestFocus();
                            Toast.makeText(ChangePasswordActivity.this, "verification failed.", Toast.LENGTH_SHORT).show();

                        }
                        pBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tl_new.setError(null);
        tl_old.setError(null);
        tl_again.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}