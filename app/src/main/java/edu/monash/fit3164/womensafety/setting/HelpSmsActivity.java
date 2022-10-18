package edu.monash.fit3164.womensafety.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

public class HelpSmsActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout tl_sms;
    private TextInputEditText et_sms;
    private TextView tv_sms;
    private Button btn_sms;

    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_sms);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.edit_help);


        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        assert fUser != null;
        userID = fUser.getUid();

        tl_sms = findViewById(R.id.tl_sms_text);
        et_sms = findViewById(R.id.et_sms_text);
        tv_sms = findViewById(R.id.tv_sms_text);

        btn_sms = findViewById(R.id.btn_sms_save);
        btn_sms.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        et_sms.addTextChangedListener(this);

        // Load user sms text from firebase and show.
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    String sms = user.smsMessage + "\n\nMy location:\n\n<latitude, longitude>";
                    tv_sms.setText(sms);
                    et_sms.setText(user.smsMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HelpSmsActivity.this, "Load text failed, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_sms_save:
                editSms();
                break;
        }
    }


    private void editSms() {
        String smsText = et_sms.getText().toString();
        String btnText = btn_sms.getText().toString();
        // If button text is Edit, when user click it, show the TextInputEditText, user can edit text.
        // If button text is sAVE, when user click it, Update user sms text in firebase.
        if (btnText.equals("Edit")){
            btn_sms.setText(R.string.save);
            tv_sms.setVisibility(View.GONE);
            tl_sms.setVisibility(View.VISIBLE);
        }else if(btnText.equals("Save")){
            // Check sms text input is not empty.
            if (smsText.isEmpty()){
                tl_sms.setError("Text is required.");
                return;
            }
            // update user sms text, and show the new text in textview.
            reference.child(userID).child("smsMessage").setValue(smsText);
            btn_sms.setText(R.string.edit);
            String newText = smsText + "\n\nMy location:\n\n<latitude, longitude>";
            tv_sms.setText(newText);
            tv_sms.setVisibility(View.VISIBLE);
            tl_sms.setVisibility(View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tl_sms.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}