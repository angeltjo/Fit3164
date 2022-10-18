package edu.monash.fit3164.womensafety.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.monash.fit3164.womensafety.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userId;
    private StorageReference storageReference;

    private TextView tv_sms, tv_call;
    private TextInputEditText et_pass;
    private ProgressBar pBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.setting);

        tv_sms = findViewById(R.id.tv_sms_p);
        tv_call = findViewById(R.id.tv_call_p);

        pBar =findViewById(R.id.pBar_set);

        checkAppPermission();

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.tv_set_del).setOnClickListener(this);
        findViewById(R.id.tv_set_pass).setOnClickListener(this);
        findViewById(R.id.tv_set_sms).setOnClickListener(this);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Profile image");
    }

    private void checkAppPermission() {
        // if user give the sms permission, show the permission status in setting page.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            tv_sms.setText(R.string.allow);
            tv_sms.setTextColor(Color.BLUE);
        }else {
            tv_sms.setText(R.string.denied);
            tv_sms.setTextColor(Color.RED);
        }
        // if user give the sms permission, show the permission status in setting page.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            tv_call.setText(R.string.allow);
            tv_call.setTextColor(Color.BLUE);
        }else {
            tv_call.setText(R.string.denied);
            tv_call.setTextColor(Color.RED);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_set_del:
                // Check user is login or not.
                if (fUser != null){
                    diaForDel();
                }else {
                    Toast.makeText(SettingActivity.this,"Please login first.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_set_pass:
                // Check user is login or not.
                if (fUser != null){
                    startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
                }else {
                    Toast.makeText(SettingActivity.this,"Please login first.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_set_sms:
                // Check user is login or not.
                if (fUser != null){
                    startActivity(new Intent(SettingActivity.this, HelpSmsActivity.class));
                }else {
                    Toast.makeText(SettingActivity.this,"Please login first.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // User alert dialog to show the edittext of the password.
    private void diaForDel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        et_pass = view.findViewById(R.id.et_del_pass);

        String email = fUser.getEmail();

        builder.setTitle("Delete Account")
                .setIcon(R.drawable.ic_set_delete)
                .setView(view)
                .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password = et_pass.getText().toString();
                        if(password.isEmpty()){
                            Toast.makeText(SettingActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                        }else {
                            verifyPass(email, password);
                        }

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SettingActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Use user input password to do verification.
    private void verifyPass(String email, String password) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        // if user password input is correct, delete user account.
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        fUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            deleteAccount();
                        }else{
                            Toast.makeText(SettingActivity.this, "Password is wrong", Toast.LENGTH_SHORT).show();

                        }
                        pBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
    }

    // delete the user account and clear the data of the user in realtime database.
    private void deleteAccount() {
        userId = fUser.getUid();
        reference.child("users").child(userId).setValue(null);
        reference.child("contacts").child(userId).setValue(null);
        storageReference.child(userId).delete();
        fUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SettingActivity.this, "Delete Account successful.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SettingActivity.this, "Failed to delete Account.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}