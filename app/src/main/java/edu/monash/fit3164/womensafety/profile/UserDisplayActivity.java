package edu.monash.fit3164.womensafety.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.User;

public class UserDisplayActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private String userID;

    private CircleImageView imageView;
    private TextView tv_username, tv_email, tv_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.profile);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        assert fUser != null;
        userID = fUser.getUid();

        tv_username = findViewById(R.id.tv_dis_username);
        tv_email = findViewById(R.id.tv_dis_email);
        tv_mobile = findViewById(R.id.tv_dis_mobile);

        imageView = findViewById(R.id.image_dis_profile);

        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.image_dis_profile).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

    }

    // Load new user information and image from firebase and show.
    @Override
    protected void onStart() {
        super.onStart();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    tv_username.setText(user.username);
                    tv_email.setText(user.email);
                    tv_mobile.setText(user.mobile);
                    if (!Objects.equals(user.profileImageUri, "None")){
                        Picasso.get().load(user.profileImageUri).into(imageView);
                    }else{
                        Toast.makeText(UserDisplayActivity.this,"Fail to load profile Image.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDisplayActivity.this, "Account has some error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.btn_edit:
                startActivity(new Intent(UserDisplayActivity.this, UserEditActivity.class));
                break;
            case R.id.image_dis_profile:
                startActivity(new Intent(UserDisplayActivity.this, PhotoEditActivity.class));
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}