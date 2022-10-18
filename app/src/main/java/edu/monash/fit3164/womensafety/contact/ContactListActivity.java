package edu.monash.fit3164.womensafety.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.adapter.ContactAdapter;
import edu.monash.fit3164.womensafety.adapter.RecyclerViewInterface;
import edu.monash.fit3164.womensafety.provider.Contacts;
import edu.monash.fit3164.womensafety.provider.User;

public class ContactListActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewInterface {

    RecyclerView recyclerView;
    ContactAdapter cAdapter;
    ArrayList<Contacts> list;

    private DatabaseReference reference;
    private String userID, sendM;

    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Use my own toolbar
        getSupportActionBar().hide();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.contact_list);

        ImageView iv_add = findViewById(R.id.iv_right);
        iv_add.setImageResource(R.drawable.ic_add);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        cAdapter = new ContactAdapter(this, list, this);

        recyclerView.setAdapter(cAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        userID = fUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        iv_add.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        location = getIntent().getStringExtra("address");
    }

    // Load contacts list of current user and show in recyclerView.
    @Override
    protected void onStart() {
        super.onStart();
        if (reference == null) {
            return;
        }
        reference.child("contacts").child(userID).orderByChild("firstName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Each time start this activity, clear the list and load new contact list.
                        // Avoid RecyclerView show Repeated contacts.
                        list.clear();
                        for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                            Contacts contacts = datasnapshot.getValue(Contacts.class);
                            list.add(contacts);
                        }
                        cAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ContactListActivity.this, "Contact list failed to load, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

        // load short massage detail from firebase.
        reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    sendM = user.smsMessage;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sendM = "I am in danger and I need help.";
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                startActivity(new Intent(this, AddContactActivity.class));
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    // If user click one contact, start new activity that user can edit that contact information.
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ContactListActivity.this, EditContactActivity.class);
        intent.putExtra("mobile", list.get(position).mobile);
        startActivity(intent);
    }

    // If user click sms image, check that app has sms permission or not.
    // If app has the permission, send short message.
    @Override
    public void onSmsClick(int position) {
        if (sendM == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendMessage(position);
        } else {
            Toast.makeText(ContactListActivity.this, "Please give sms permissions to access.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }

    // show alertdialog, Users can confirm whether to send a text message here to avoid accidental touches.
    private void sendMessage(int position) {
        String number = list.get(position).mobile;
        String name = list.get(position).firstName + " " + list.get(position).secondName;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send message to " + name)
                .setIcon(R.drawable.ic_sms)
                .setMessage("Are you sure to send message to " +
                        "\nName: " + name +
                        "\nMobile: " + number +
                        "\n" + sendM)
                .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (location.contains("lat/lng:")){
                            location = location.replace("lat/lng:", "");
                        }
                        String finalSms = sendM + "\n\nmy location:\n\n" + location;
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, finalSms, null, null);
                        Toast.makeText(ContactListActivity.this, "Message is sent.", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ContactListActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
    }

    @Override
    public void onPhoneCallClick(int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            callContact(position);
        } else {
            Toast.makeText(ContactListActivity.this, "Please give phone call permissions to access.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    // show alertdialog, Users can confirm whether to make phone call here to avoid accidental touches.
    private void callContact(int pos) {
        String number = list.get(pos).mobile;
        String name = list.get(pos).firstName + " " + list.get(pos).secondName;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call to " + name)
                .setIcon(R.drawable.ic_phone)
                .setMessage("Are you sure to contact with " +
                        "\nName: " + name +
                        "\nMobile: " + number)
                .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String dial = "tel:" + number;
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                        startActivity(intent);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ContactListActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
    }

}