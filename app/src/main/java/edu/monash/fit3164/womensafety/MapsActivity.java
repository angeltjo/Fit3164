package edu.monash.fit3164.womensafety;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.monash.fit3164.womensafety.contact.ContactListActivity;
import edu.monash.fit3164.womensafety.loginOrOut.LoginActivity;
import edu.monash.fit3164.womensafety.profile.UserDisplayActivity;
import edu.monash.fit3164.womensafety.provider.Emergency;
import edu.monash.fit3164.womensafety.provider.MelSuburbs;
import edu.monash.fit3164.womensafety.provider.PoliceStation;
import edu.monash.fit3164.womensafety.provider.TrainStation;
import edu.monash.fit3164.womensafety.provider.User;
import edu.monash.fit3164.womensafety.setting.SettingActivity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Geocoder geocoder;
    private LocationListener locationListener;
    private LocationManager locationManager;
    SupportMapFragment mapFragment;
    LatLng latLng;
    private final long MIN_TIME = 1000; // 1 second
    private final long MIN_DIST = 5; // 5 Meters\
    GoogleMap googleMap;
    TextView search;
    Button btn_search;
    private DrawerLayout drawerlayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    ArrayList<PoliceStation> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userId;

    private MenuItem item_log;

    private CircleImageView imageView;
    private TextView tv_title;

    private FloatingActionButton fBtn_sos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        search = findViewById(R.id.searchSuburb);
        btn_search = findViewById(R.id.btn_search);
        fBtn_sos = findViewById(R.id.fBtn_sos);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // to link toolbar with navigation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        //Set the navigation items listener to the navigation view
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // Get item loginOrOut in navigation view.
        item_log = navigationView.getMenu().findItem(R.id.loginOrOut);

        geocoder = new Geocoder(this, Locale.getDefault());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        imageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.image_profile);
        tv_title = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user);

        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        //Hook the drawer and the toolbar using ActionBarDrawerToggle class

        fBtn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    callContact();
                } else {
                    Toast.makeText(MapsActivity.this, "Please give phone call permissions to access.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

    }

    // Call to police with floatActionButton
    private void callContact() {
        String number = "000";
        // user alertdialog to make user can check that user want call to police or not
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call to Police" )
                .setIcon(R.drawable.ic_phone)
                .setMessage("Are you sure to contact with Police")
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
                        Toast.makeText(MapsActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
    }

    // When activity started, check user has already login or not
    @Override
    protected void onStart() {
        super.onStart();
        // If user has already login, load username and profile image from Firebase.
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser != null){
            readUsername();
        }else{
            tv_title.setText(R.string.please_login);
            item_log.setTitle(R.string.login);
        }
    }

    // Read user information from firebase, and set username and profile image.
    private void readUsername() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = fUser.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    String s = "Hi, " + user.username;
                    tv_title.setText(s);
                    item_log.setTitle(R.string.logout);
                    if (!Objects.equals(user.profileImageUri, "None")){
                        Picasso.get().load(user.profileImageUri).into(imageView);
                    }else{
                        Picasso.get().load(R.drawable.profile).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapsActivity.this, "Account has some error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        // set the location permission enable
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        // cast to view and find the location button first
        View locationBtn = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationBtn.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 50, 300);


        googleMap.clear();
        // add police station marker (red)
        DatabaseReference ref_suburb = database.getReference("police station data");
        if (ref_suburb != null){
            ref_suburb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            LatLng latlng = new LatLng(Float.parseFloat(ds.getValue(PoliceStation.class).getLatitude()), Float.parseFloat(ds.getValue(PoliceStation.class).getLongitude()));
                            googleMap.addMarker(new MarkerOptions().position(latlng).title(ds.getValue(PoliceStation.class).getName()+" police station")
                                    .snippet("Opening hour: "+ ds.getValue(PoliceStation.class).getOpening_Hours()+"\n" +"Phone: "+ ds.getValue(PoliceStation.class).getPhone()));

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        // add emergency data marker (green)
        ref_suburb = database.getReference("emergency data");
        if (ref_suburb != null){
            ref_suburb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            LatLng latlng = new LatLng(ds.getValue(Emergency.class).getLatitude(), ds.getValue(Emergency.class).getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(latlng).title(ds.getValue(Emergency.class).getName()+" emergency station")
                                    .snippet("Phone: "+ ds.getValue(Emergency.class).getPhone())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        // add train station marker (blue)
        ref_suburb = database.getReference("train station data");
        if (ref_suburb != null){
            ref_suburb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            LatLng latlng = new LatLng(ds.getValue(TrainStation.class).getLatitude(), ds.getValue(TrainStation.class).getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(latlng).title(ds.getValue(TrainStation.class).getName()+" Train Station")
                                    .snippet("Line: "+ ds.getValue(TrainStation.class).getLine())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        // get current location
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    // move camera
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //save current location
                String msg;
                boolean actionFlag;
                String selectedSuburb = "";


                List<Address> addresses = new ArrayList<>();
                try {
                    //The results of getFromLocation are a best guess and are not guaranteed to be meaningful or correct.
                    // It may be useful to call this method from a thread separate from your primary UI thread.
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); //last param means only return the first address object
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses.size() == 0) {
                    msg = "No information at this location!! Sorry";
                    actionFlag = false;
                }
                else {
                    android.location.Address address = addresses.get(0);
                    selectedSuburb = address.getLocality();

                    msg = "Safety information of " + address.getLocality();
                    actionFlag = true;
                }

                Snackbar.make(mapFragment.getView(), msg, Snackbar.LENGTH_LONG).setAction("Details", (actionFlag) ? (new ActionOnClickListener(selectedSuburb)) : null).show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = String.valueOf(search.getText());
                try {
                    int postcode_input = Integer.parseInt(string);
                    DatabaseReference ref_suburb = database.getReference("melbsuburbs");
                    Query query = ref_suburb.orderByChild("Postcode").equalTo(postcode_input);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    MelSuburbs location = data.getValue(MelSuburbs.class);
                                    latLng = new LatLng(location.getLattitude(), location.getLongtitude());
                                    // move camera
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                                    String msg = "Suburb Name: " + location.getSuburb()+ " , Postcode: "+ location.getPostcode();
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String msg = "Can't find the Suburb, please make sure is valid name/postcode";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }

                    });
                } catch (Exception e) {
                    DatabaseReference ref_suburb = database.getReference("melbsuburbs");
                    Query query = ref_suburb.orderByChild("Suburb_Cap").equalTo(string.toUpperCase());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    MelSuburbs location = data.getValue(MelSuburbs.class);
                                    latLng = new LatLng(location.getLattitude(), location.getLongtitude());
                                    // move camera
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                                    String msg = "Suburb Name: " + location.getSuburb()+ " , Postcode: "+ location.getPostcode();
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String msg = "Can't find the Suburb, please make sure is valid name/postcode";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }

                    });
                }

            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.account) {
                // if user has not login, user need to login first.
                if (fUser != null){
                    startActivity(new Intent(MapsActivity.this, UserDisplayActivity.class));
                }else {
                    Toast.makeText(MapsActivity.this,"Please login first.", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.contactList) {
                // if user has not login, user need to login first.
                if (fUser != null){
                    Intent intent = new Intent(MapsActivity.this, ContactListActivity.class);
                    intent.putExtra("address", latLng.toString());
                    startActivity(intent);
                }else {
                    Toast.makeText(MapsActivity.this,"Please login first.", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.MelCrimeData) {
                startActivity(new Intent(MapsActivity.this, MelbourneCrimeData.class));
            }  else if (id == R.id.setting) {
                startActivity(new Intent(MapsActivity.this, SettingActivity.class));
            } else if (id == R.id.loginOrOut) {
                loginOrNot();
            }
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }
    //Custom onclicklistener to accept 'selectedcountry' as parameter
    public class ActionOnClickListener implements View.OnClickListener {

        String suburb;

        public ActionOnClickListener(String suburb) {
            this.suburb = suburb; //this refers to the nested class's instance not the an instance of the enclosing class
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mapFragment.getContext(), SuburbDetails.class);
            intent.putExtra("suburb", suburb);
            startActivity(intent);
        }
    }

    // Login or Logout
    private void loginOrNot() {
        // User click item_log to logout,
        // if fUser != null, means user is already login.
        if (fUser != null){
            FirebaseAuth.getInstance().signOut();
            Picasso.get().load(R.drawable.profile).into(imageView);
            tv_title.setText(R.string.please_login);
            item_log.setTitle(R.string.login);
            fUser = null;
            Toast.makeText(MapsActivity.this,"Logout Successful.", Toast.LENGTH_SHORT).show();
            // User login
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}