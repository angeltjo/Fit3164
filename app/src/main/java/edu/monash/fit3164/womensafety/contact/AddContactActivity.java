package edu.monash.fit3164.womensafety.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.Contacts;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;

    private TextInputEditText et_firstName, et_secondName, et_mobile;
    private TextInputLayout tl_firstName, tl_secondName, tl_mobile;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.add_contact);

        et_firstName = findViewById(R.id.et_add_f);
        et_secondName = findViewById(R.id.et_add_l);
        et_mobile = findViewById(R.id.et_add_mobile);

        tl_firstName = findViewById(R.id.tl_add_f);
        tl_secondName = findViewById(R.id.tl_add_l);
        tl_mobile = findViewById(R.id.tl_add_mobile);

        pBar = findViewById(R.id.pBar_add);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("contacts");
        userID = fUser.getUid();

        findViewById(R.id.btn_add_save).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_firstName.addTextChangedListener(this);
        et_secondName.addTextChangedListener(this);
        et_mobile.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_save:
                saveContact();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void saveContact() {
        String firstName = et_firstName.getText().toString();
        String secondName = et_secondName.getText().toString();
        String mobile = et_mobile.getText().toString();

        // check first name input is not empty.
        if (firstName.isEmpty()){
            tl_firstName.setError("First name is required.");
            et_firstName.requestFocus();
            return;
        }
        // check last name input is not empty.
        if (secondName.isEmpty()){
            tl_secondName.setError("Second name is required.");
            et_secondName.requestFocus();
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

        // save contact in firebase
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Contacts contacts = new Contacts(firstName, secondName, mobile);
        reference.child(userID).child(mobile).setValue(contacts);

        // Give 2 seconds to upload contact in firebase, then finish this activity.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                pBar.setVisibility(View.GONE);
                Toast.makeText(AddContactActivity.this, "Contact added successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, 2000);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tl_firstName.setError(null);
        tl_secondName.setError(null);
        tl_mobile.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}