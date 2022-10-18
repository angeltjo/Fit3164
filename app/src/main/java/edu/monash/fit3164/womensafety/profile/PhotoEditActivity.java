package edu.monash.fit3164.womensafety.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.User;

public class PhotoEditActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;
    private StorageReference storageReference;
    private Uri imageUri;

    private CircleImageView imageView;
    private ProgressBar pBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.edit_profile_image);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = fUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("Profile image");

        imageView = findViewById(R.id.image_edit_pro);

        pBar = findViewById(R.id.pBar_pro_edit);

        findViewById(R.id.btn_choose).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);


        // Load user profile image from firebase if existed.
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    if (!Objects.equals(user.profileImageUri, "None")){
                        Picasso.get().load(user.profileImageUri).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhotoEditActivity.this, "Account has some error.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose:
                chooseImage();
                break;
            case R.id.btn_upload:
                if (imageUri != null){
                    uploadImage();
                    // Give 5 seconds to update user profile image to firebase.
                    // If finished this activity too fast, the UserDisplayActivity will load the old image from firebase.
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            pBar.setVisibility(View.GONE);
                            finish();
                        }
                    }, 5000);
                }else{
                    Toast.makeText(PhotoEditActivity.this, "You need to choose a new photo.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    // Start the gallery in phone.
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    // Set the choose image from gallery and load it in imageView.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }else {
            Toast.makeText(PhotoEditActivity.this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    // Upload image to firebase, image name is user id
    // to avoid save too much image in firebase. Each Account only has one profile image in firebase.
    private void uploadImage() {
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StorageReference sRef = storageReference.child(userID);
        sRef.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String fUri = task.getResult().toString();
                                        reference.child(userID).child("profileImageUri").setValue(fUri);
                                        Toast.makeText(PhotoEditActivity.this, "Image update successful", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(PhotoEditActivity.this, "Failed to update image", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}