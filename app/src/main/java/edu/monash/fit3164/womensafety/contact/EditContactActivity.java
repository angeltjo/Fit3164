package edu.monash.fit3164.womensafety.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.Contacts;

public class EditContactActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    TextView tv_fLetter;
    private TextInputEditText et_firstName, et_secondName, et_mobile;
    private TextInputLayout tl_firstName, tl_secondName, tl_mobile;
    private DatabaseReference reference;
    private ProgressBar pBar;

    private String oldLastName, oldFirstName, oldMobile, mMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.edit_contact);

        tv_fLetter = findViewById(R.id.tv_edit_fLetter);
        et_firstName = findViewById(R.id.et_edit_con_f);
        et_secondName = findViewById(R.id.et_edit_con_l);
        et_mobile = findViewById(R.id.et_edit_con_mobile);

        tl_firstName = findViewById(R.id.tl_edit_con_f);
        tl_secondName = findViewById(R.id.tl_edit_con_l);
        tl_mobile = findViewById(R.id.tl_edit_con_mobile);

        pBar = findViewById(R.id.pBar_edit_contact);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        String userID = fUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("contacts").child(userID);

        findViewById(R.id.btn_edit_con_del).setOnClickListener(this);
        findViewById(R.id.btn_edit_con_save).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_firstName.addTextChangedListener(this);
        et_secondName.addTextChangedListener(this);
        et_mobile.addTextChangedListener(this);

        mMobile = getIntent().getStringExtra("mobile");

        // Load contact information and show.
        reference.child(mMobile).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Contacts contacts = snapshot.getValue(Contacts.class);
                if (contacts != null){
                    oldFirstName = contacts.firstName;
                    oldLastName = contacts.secondName;
                    oldMobile = contacts.mobile;

                    tv_fLetter.setText(oldFirstName.substring(0,1));
                    et_firstName.setText(oldFirstName);
                    et_secondName.setText(oldLastName);
                    et_mobile.setText(oldMobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditContactActivity.this, "Failed to load contact, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_edit_con_del:
                String delMessage = "Are you sure to delete the contact?";
                dialogShow(delMessage, "delete");
                break;
            case R.id.btn_edit_con_save:
                inputCheck();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    // Show the dialog that user can confirm whether edit or delete the contact or not
    // Avoid accidental touches.
    private void dialogShow(String message, String item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (Objects.equals(item, "delete")){deleteContact();}
                        else if (Objects.equals(item, "update")){updateContact();}
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Check input
    private void inputCheck(){
        String fName = et_firstName.getText().toString();
        String lName = et_secondName.getText().toString();
        String mobile = et_mobile.getText().toString();

        // check first name input is not empty.
        if (fName.isEmpty()){
            tl_firstName.setError("First name is required.");
            et_firstName.requestFocus();
            return;
        }

        // check last name input is not empty.
        if (lName.isEmpty()){
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

        // If user has not changed anything about contact, User cannot upload it to firebase.
        if (fName.equals(oldFirstName) && lName.equals(oldLastName) && mobile.equals(oldMobile)){
            Toast.makeText(EditContactActivity.this, "All information has not changed.", Toast.LENGTH_SHORT).show();
            return;
        }

        String sureMessage = "Are you sure to update the contact?";
        dialogShow(sureMessage, "update");
    }

    // delete contacts
    private void deleteContact() {
        pBar.setVisibility(View.VISIBLE);
        reference.child(oldMobile).setValue(null);
        updateFinish("Delete");
    }

    // update contact.
    private void updateContact() {
        String fName = et_firstName.getText().toString();
        String lName = et_secondName.getText().toString();
        String mobile = et_mobile.getText().toString();

        // update contacts
        pBar.setVisibility(View.VISIBLE);
        if (!mobile.equals(oldMobile)){
            Contacts contacts = new Contacts(fName, lName, mobile);
            reference.child(mobile).setValue(contacts);
            reference.child(mMobile).setValue(null);
        }
        if (!fName.equals(oldFirstName)){
            reference.child(mobile).child("firstName").setValue(fName);
        }
        if (!lName.equals(oldLastName)){
            reference.child(mobile).child("secondName").setValue(lName);
        }
        updateFinish("Update");
    }

    // Give 2 seconds to upload contact in firebase, then finish this activity.
    private void updateFinish(String item) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                pBar.setVisibility(View.GONE);
                Toast.makeText(EditContactActivity.this, item + " contact successful", Toast.LENGTH_SHORT).show();
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