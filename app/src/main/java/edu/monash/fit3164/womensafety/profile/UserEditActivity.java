package edu.monash.fit3164.womensafety.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.User;

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID, old_username, old_mobile;

    private TextInputEditText et_mobile, et_username;
    private TextInputLayout tl_username, tl_mobile;
    private ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.edit_profile);

        et_username = findViewById(R.id.et_edit_username);
        et_mobile = findViewById(R.id.et_edit_mobile);

        tl_username = findViewById(R.id.tl_edit_username);
        tl_mobile = findViewById(R.id.tl_edit_mobile);

        pBar = findViewById(R.id.pBar_edit);


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = fUser.getUid();

        findViewById(R.id.btn_edit_finish).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_username.addTextChangedListener(this);
        et_mobile.addTextChangedListener(this);
    }

    // load user information from firebase and show.
    @Override
    protected void onStart() {
        super.onStart();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    old_username = user.username;
                    old_mobile = user.mobile;

                    et_username.setText(old_username);
                    et_mobile.setText(old_mobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserEditActivity.this, "Account has some error.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_edit_finish:
                editUser();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    // Edit user information
    private void editUser() {
        String username = et_username.getText().toString();
        String mobile = et_mobile.getText().toString();

        // check username input is not empty.
        if (username.isEmpty()){
            tl_username.setError("Username is required.");
            et_username.requestFocus();
            return;
        }
        // check mobile input is not empty.
        if (mobile.isEmpty()){
            tl_mobile.setError("Mobile is required.");
            et_mobile.requestFocus();
            return;
        }
        // Australian mobile has at least 10 digits. Check mobile is valid or not
        if (mobile.length() < 10) {
            tl_mobile.setError("Mobile is invalid.");
            et_mobile.requestFocus();
            return;
        }

        // if user has not change any information, user cannot upload it to firebase.
        pBar.setVisibility(View.VISIBLE);
        if (username.equals(old_username) && mobile.equals(old_mobile)) {
            Toast.makeText(UserEditActivity.this, "All information has not changed.", Toast.LENGTH_SHORT).show();
            pBar.setVisibility(View.GONE);
            return;
        }

        // update user information.
        if (!username.equals(old_username)){
            reference.child(userID).child("username").setValue(username);
        }
        if (!mobile.equals(old_mobile)){
            reference.child(userID).child("mobile").setValue(mobile);
        }
        pBar.setVisibility(View.GONE);
        Toast.makeText(UserEditActivity.this, "Account update successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tl_username.setError(null);
        tl_mobile.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}