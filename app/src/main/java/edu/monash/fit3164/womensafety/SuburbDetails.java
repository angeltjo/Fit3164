package edu.monash.fit3164.womensafety;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.monash.fit3164.womensafety.provider.CameraInfo;
import edu.monash.fit3164.womensafety.provider.CrimeStats;

public class SuburbDetails extends AppCompatActivity {

    private TextView name;
    private TextView camera;
    private TextView crime_2021;
    private TextView crime_2020;
    private TextView postcode;
    private BarChart barChart;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suburb_details);

        getSupportActionBar().setTitle(R.string.title_activity_suburb_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String selectedSuburb = getIntent().getStringExtra("suburb");

        name = findViewById(R.id.suburb_name);
        camera = findViewById(R.id.camera);
        crime_2021 = findViewById(R.id.crime_2021);
        crime_2020 = findViewById(R.id.crime_2020);
        postcode = findViewById(R.id.postcode);
        barChart = findViewById(R.id.barChart);


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("fixed camera location");
        Query query = databaseRef.orderByChild("Suburb").equalTo(selectedSuburb);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(selectedSuburb);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CameraInfo cameraInfo = data.getValue(CameraInfo.class);
                        camera.setText(cameraInfo.getLocation());
                    }
                } else{
                    camera.setText("No information provide");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArrayList barArrayList;
        barArrayList = new ArrayList();
        databaseRef = FirebaseDatabase.getInstance().getReference("suburb crime stats 2021");
        query = databaseRef.orderByChild("Suburb").equalTo(selectedSuburb);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CrimeStats crimeStats = data.getValue(CrimeStats.class);
                        crime_2021.setText(Integer.toString(crimeStats.getTotal()));
                        postcode.setText(Integer.toString(crimeStats.getPostcode()));

                        barArrayList.add(new BarEntry(1f, crimeStats.getCrimes_Against_Person()));
                        barArrayList.add(new BarEntry(2f, crimeStats.getDrug_Off()));
                        barArrayList.add(new BarEntry(3f, crimeStats.getJusticeProc_Off()));
                        barArrayList.add(new BarEntry(4f, crimeStats.getPublicOrder_Security_Off()));
                        barArrayList.add(new BarEntry(5f, crimeStats.getProp_Deception_Off()));

                        // creating a new bar data set.
                        BarDataSet barDataSet;
                        barDataSet = new BarDataSet(barArrayList, "Crime Data for 2021");
                        barDataSet.setColor(Color.YELLOW);
                        barDataSet.setValueTextSize(13f);
                        getBarEntries(barDataSet, selectedSuburb);

                    }
                } else{
                    crime_2021.setText("No information provide");
                    postcode.setText("No information provide");
//                    found = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        String msg = "data is : "+list;
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


        databaseRef = FirebaseDatabase.getInstance().getReference("suburb crime stats 2020");
        query = databaseRef.orderByChild("Suburb").equalTo(selectedSuburb);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CrimeStats crimeStats = data.getValue(CrimeStats.class);
                        crime_2020.setText(Integer.toString(crimeStats.getTotal()));
//                        barArrayList.add(new BarEntry(2f, crimeStats.getCrimes_Against_Person()));
                    }
                } else{
                    crime_2020.setText("No information provide");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//
//        String msg = "data: "+barArrayList.toString();
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

        // calling method to get bar entries.
//        getBarEntries();
//
//        String msg = "data is : "+barArrayList.toString();
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

        // creating a new bar data set.
//        barDataSet = new BarDataSet(barArrayList, "Crime Data");
//
//        // creating a new bar data and
//        // passing our bar data set.
//        barData = new BarData(barDataSet);
//
//        // below line is to set data
//        // to our bar chart.
//        barChart.setData(barData);
//
//        // adding color to our bar data set.
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        // setting text color.
//        barDataSet.setValueTextColor(Color.BLACK);
//
//        // setting text size
//        barDataSet.setValueTextSize(16f);
//        barChart.getDescription().setEnabled(false);
//        if (databaseRef != null){
//            databaseRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()){
//                        list = new ArrayList<>();
//                        for (DataSnapshot ds : snapshot.getChildren()){
//                            list.add(ds.getValue(Suburb.class));
//                        }
//                    }
//                    name.setText(list.get(0).getName());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        Query query = databaseRef.orderByChild("postcode").equalTo(string);
//        DatabaseReference cities = databaseRef.child("postcode");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    name.setText(postSnapshot.toString());
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        //Executor handler = ContextCompat.getMainExecutor(this);
//        Handler uiHandler=new Handler(Looper.getMainLooper()); // just a way to say Ui thread
//        // handler is an reference. so here the uiHandler is a reference of Ui thread
//
//
//
//        executor.execute(() -> { // execute take care of many thing and schedule the execution on a appropriate thread
//            //Background work here
//            CountryInfo countryInfo = new CountryInfo();
//
//            try {
//                // Create URL
//                URL webServiceEndPoint = new URL("https://restcountries.com/v2/name/" + selectedCountry); //
//
//                // Create connection
//                HttpsURLConnection myConnection = (HttpsURLConnection) webServiceEndPoint.openConnection();
//
//                if (myConnection.getResponseCode() == 200) {
//                    //JSON data has arrived successfully, now we need to open a stream to it and get a reader
//                    InputStream responseBody = myConnection.getInputStream();
//                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
//
//                    //now use a JSON parser to decode data
//                    JsonReader jsonReader = new JsonReader(responseBodyReader);
//                    jsonReader.beginArray(); //consume arrays's opening JSON brace
//                    String keyName;
//                    // countryInfo = new CountryInfo(); //nested class (see below) to carry Country Data around in
//                    boolean countryFound = false;
//                    while (jsonReader.hasNext() && !countryFound) { //process array of objects
//                        jsonReader.beginObject(); //consume object's opening JSON brace
//                        while (jsonReader.hasNext()) {// process key/value pairs inside the current object
//                            keyName = jsonReader.nextName();
//                            if (keyName.equals("name")) {
//                                countryInfo.setName(jsonReader.nextString());
//                                if (countryInfo.getName().equalsIgnoreCase(selectedCountry)) {
//                                    countryFound = true;
//                                }
//                            } else if (keyName.equals("alpha2Code")) {
//                                countryInfo.setAlpha2Code(jsonReader.nextString());
//                            } else if (keyName.equals("alpha3Code")) {
//                                countryInfo.setAlpha3Code(jsonReader.nextString());
//                            } else if (keyName.equals("capital")) {
//                                countryInfo.setCapital(jsonReader.nextString());
//                            } else if (keyName.equals("population")) {
//                                countryInfo.setPopulation(jsonReader.nextInt());
//                            } else if (keyName.equals("area")) {
//                                countryInfo.setArea(jsonReader.nextDouble());
//                            } else if (keyName.equals("currencies")) {
//                                jsonReader.beginArray();
//                                while (jsonReader.hasNext()) {
//                                    jsonReader.beginObject();
//                                    while (jsonReader.hasNext()) {
//                                        keyName = jsonReader.nextName();
//                                        if (keyName.equals("name")) {
//                                            countryInfo.setCurrencies(jsonReader.nextString());
//                                        }else {
//                                            jsonReader.skipValue();
//                                        }
//                                    }
//                                    jsonReader.endObject();
//                                }
//                                jsonReader.endArray();
//                            } else if (keyName.equals("languages")) {
//                                jsonReader.beginArray(); // []
//                                while (jsonReader.hasNext()) {
//                                    jsonReader.beginObject(); //{}
//                                    while (jsonReader.hasNext()) {
//                                        keyName = jsonReader.nextName();
//                                        if (keyName.equals("name")) {
//                                            countryInfo.setLanguages(jsonReader.nextString());
//                                        }else {
//                                            jsonReader.skipValue();
//                                        }
//                                    }
//                                    jsonReader.endObject();
//                                }
//                                jsonReader.endArray();
//                            } else if (keyName.equals("borders")) {
//                                jsonReader.beginArray(); // []
//                                while (jsonReader.hasNext()) {
//                                    countryInfo.setBorders(jsonReader.nextString());
//                                }
//                                jsonReader.endArray();
//                            } else {
//                                jsonReader.skipValue();
//                            }
//                        }
//                        jsonReader.endObject();
//                    }
//                    jsonReader.close();
//                    uiHandler.post(()->{ //uihandler are allow to update ui where as background method does not
//                        // the post is use to schedule the execution on a appropriate thread
//                        // post is not direct executing, it's scheduling
//                        name.setText(countryInfo.getName());
//                        capital.setText(countryInfo.getCapital());
//                        code.setText(countryInfo.getAlpha3Code());
//                        population.setText(Integer.toString(countryInfo.getPopulation()));
//                        area.setText(Double.toString(countryInfo.getArea()));
//                        currencies.setText(countryInfo.getCurrencies());
//                        languages.setText(countryInfo.getLanguages());
//                        wiki.setText("WIKI " + countryInfo.getName());
//                        borders.setText(countryInfo.getBorders());
//                    });
//                } else {
//                    Log.i("INFO", "Error:  No response");
//                    uiHandler.post(()-> { //uihandler are allow to update ui where as background method does not
//                        name.setText(selectedCountry);
//                        capital.setText("No messages");
//                        code.setText("No messages");
//                        population.setText("No messages");
//                        area.setText("No messages");
//                        currencies.setText("No messages");
//                        languages.setText("No messages");
//                        wiki.setText("WIKI " + selectedCountry);
//                        borders.setText("No messages");
//                    });
//                }
//
//                // All your networking logic should be here
//                // Create URL
//                String flagWeb = "https://countryflagsapi.com/png/" + countryInfo.getAlpha2Code().toLowerCase(); //
//
//                java.net.URL url = new java.net.URL(flagWeb);
//                HttpURLConnection connection = (HttpURLConnection) url
//                        .openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//
//                InputStream input = connection.getInputStream(); //gets the input stream of the subprocess.
//                // The stream obtains data piped from the standard output stream of the process represented by this Process object.
//
//                // Creates Bitmap objects from various sources, including files, streams, and byte-arrays
//                // Decode an input stream into a bitmap.
//                Bitmap myBitmap = BitmapFactory.decodeStream(input); // The input stream that holds the raw data to be decoded into a bitmap.
//                // The decoded bitmap, or null if the image data could not be decoded.
//
//                // now lets update the UI
//                uiHandler.post(() -> {
//                    flag.setImageBitmap(myBitmap);
//                });
//            } catch (Exception e) {
//                Log.i("INFO", "Error " + e.toString());
//            }
//        });
//
//        wiki.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String country = getIntent().getStringExtra("country");
//                Intent intent = new Intent(getApplicationContext(), WebWiki.class);
//                intent.putExtra("country", country);
//                startActivity(intent);
//            }
//        });
//    }
//
////    private void  searchSuburb (String string){
////        FirebaseRecyclerOptions<model> options =
////    }
//
//
    }

    private void getBarEntries(BarDataSet barDataSet1, String selectedSuburb) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("suburb crime stats 2020");
        Query query = databaseRef.orderByChild("Suburb").equalTo(selectedSuburb);
        ArrayList barArrayList_2020;
        barArrayList_2020 = new ArrayList();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CrimeStats crimeStats = data.getValue(CrimeStats.class);

                        barArrayList_2020.add(new BarEntry(1f, crimeStats.getCrimes_Against_Person()));
                        barArrayList_2020.add(new BarEntry(2f, crimeStats.getDrug_Off()));
                        barArrayList_2020.add(new BarEntry(3f, crimeStats.getJusticeProc_Off()));
                        barArrayList_2020.add(new BarEntry(4f, crimeStats.getPublicOrder_Security_Off()));
                        barArrayList_2020.add(new BarEntry(5f, crimeStats.getProp_Deception_Off()));
                        // creating a new bar data set.
                        BarDataSet barDataSet2 = new BarDataSet(barArrayList_2020, "Crime Data for 2020");
                        barDataSet2.setColor(Color.BLUE);
                        barDataSet2.setValueTextSize(13f);

                        // below line is to add bar data set to our bar data.
                        BarData barData = new BarData(barDataSet1, barDataSet2);


                        String[] days = new String[]{"Crimes Against Person",
                                "Drug Offences", "Justice Procedures Offences",
                                "Public Order and Security Offences",
                        "Property and Deception Offences"};
                        // after adding data to our bar data we
                        // are setting that data to our bar chart.
                        barChart.setData(barData);

                        // below line is to remove description
                        // label of our bar chart.
                        barChart.getDescription().setEnabled(false);

                        // below line is to get x axis
                        // of our bar chart.
                        XAxis xAxis = barChart.getXAxis();

                        // below line is to set value formatter to our x-axis and
                        // we are adding our days to our x axis.
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

                        // below line is to set center axis
                        // labels to our bar chart.
                        xAxis.setCenterAxisLabels(true);

                        // below line is to set position
                        // to our x-axis to bottom.
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // below line is to set granularity
                        // to our x axis labels.
                        xAxis.setGranularity(1);

                        // below line is to enable
                        // granularity to our x axis.
                        xAxis.setGranularityEnabled(true);

                        // below line is to make our
                        // bar chart as draggable.
                        barChart.setDragEnabled(true);

                        // below line is to make visible
                        // range for our bar chart.
                        barChart.setVisibleXRangeMaximum(1);

                        // below line is to add bar
                        // space to our chart.
                        float barSpace = 0.1f;

                        // below line is use to add group
                        // spacing to our bar chart.
                        float groupSpace = 0.5f;

                        // we are setting width of
                        // bar in below line.
                        barData.setBarWidth(0.15f);

                        // below line is to set minimum
                        // axis to our chart.
                        barChart.getXAxis().setAxisMinimum(0);

                        // below line is to
                        // animate our chart.
                        barChart.animate();

                        // below line is to group bars
                        // and add spacing to it.
                        barChart.groupBars(0, groupSpace, barSpace);

                        // below line is to invalidate
                        // our bar chart.
                        barChart.invalidate();




                    }
                } else {
                    crime_2020.setText("No information provide");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}