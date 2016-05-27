package cuongph.locationplaces.com.locationplaces;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cuongph.locationplaces.com.locationplaces.JSON.DirectionJSONParser;
import cuongph.locationplaces.com.locationplaces.JSON.PlaceJSONParser;
import cuongph.locationplaces.com.locationplaces.adapter.MarkersAdapter;
import cuongph.locationplaces.com.locationplaces.adapter.PlacesAdapter;
import cuongph.locationplaces.com.locationplaces.models.Place;
import cuongph.locationplaces.com.locationplaces.networks.HttpManager;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private GoogleMap myMap;
    private ProgressDialog myProgress;

    private double latitude=0;
    private double longtitude=0;

    private ListView placesList;

    private FloatingActionButton fab_bank, fab_bus, fab_restaurant, fab_atm, fab_coffee, fab_hospital;
    private FloatingActionMenu fab_menu;

    private Hashtable<String, String> markers;

    private Toolbar toolbar;

    private TextView tv_result;

    BottomSheetBehavior bts_placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        View bts = findViewById(R.id.bts_place);
//        bts_placesList = BottomSheetBehavior.from(bts);

        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Map Loading ...");
        myProgress.setMessage("Please Wait ...");
        myProgress.setCancelable(true);

        myProgress.show();
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);

                showMyLocation();

            }
        });


        fab_bank = (FloatingActionButton)findViewById(R.id.fab_bank);
        fab_bank.setLabelText("Bank");
        fab_bank.setImageResource(R.drawable.ic_bank);
        showPlaceType(fab_bank, "bank");


        fab_atm = (FloatingActionButton)findViewById(R.id.fab_atm);
        fab_atm.setLabelText("ATM");
        fab_atm.setImageResource(R.drawable.ic_atm);
        showPlaceType(fab_atm, "atm");

        fab_bus = (FloatingActionButton)findViewById(R.id.fab_bus);
        fab_bus.setLabelText("BUS");
        fab_bus.setImageResource(R.drawable.ic_bus);
        showPlaceType(fab_bus, "bus_station");

        fab_coffee = (FloatingActionButton)findViewById(R.id.fab_coffee);
        fab_coffee.setLabelText("Cafe");
        fab_coffee.setImageResource(R.drawable.ic_coffee);
        showPlaceType(fab_coffee, "cafe");

        fab_hospital = (FloatingActionButton)findViewById(R.id.fab_hospital);
        fab_hospital.setLabelText("Hospital");
        fab_hospital.setImageResource(R.drawable.ic_hospital);
        showPlaceType(fab_hospital, "hospital");

        fab_restaurant = (FloatingActionButton)findViewById(R.id.fab_restaurant);
        fab_restaurant.setLabelText("Restaurant");
        fab_restaurant.setImageResource(R.drawable.ic_restaurant);
        showPlaceType(fab_restaurant, "restaurant");

    }

    public void showPlaceType(FloatingActionButton btn_place, final String type){
        tv_result = (TextView)findViewById(R.id.tv_result);
        fab_menu=(FloatingActionMenu)findViewById(R.id.fab_menu);
        btn_place.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                showMyLocation();
                StringBuilder sb;
                sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + latitude + "," + longtitude);
                sb.append("&radius=5000");
                sb.append("&types=" + type);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyDuhTpj9BIfvsheMyzLRXt5lWoZc6aWCNY");
                tv_result.setText("Kết quả tìm kiếm cho: " + type);
//                bts_placesList.setState(BottomSheetBehavior.STATE_EXPANDED);
                Log.e("TAG", sb.toString());
                AsyncTaskCompat.executeParallel(new PlacesTask(), sb.toString());
            }
        });
    }



    public void onMyMapReady(GoogleMap googleMap){
        myMap=googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
            }
        });
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.setMyLocationEnabled(true);
    }

    public LatLng showMyLocation() {
        LatLng latLng = null;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String locationProvider = locationManager.getBestProvider(criteria, false);
        Location myLocation = null;
        if (locationManager == null) {
            return null;
        }
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return null;
            }
            myLocation = locationManager.getLastKnownLocation(locationProvider);


        } catch (SecurityException e) {
            Toast.makeText(this, " Error ...", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }

        if (myLocation!=null){
            latitude = myLocation.getLatitude();
            longtitude = myLocation.getLongitude();
            latLng = new LatLng(latitude, longtitude);
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
//                    .bearing(90)
//                    .tilt(90)
                    .build();
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            onLocationChanged(myLocation);
            drawCircle(latLng);

        }else {
            Toast.makeText(this,"error", Toast.LENGTH_SHORT);
        }
//        locationManager.requestLocationUpdates(locationProvider, 2000, 0, this);

        return latLng;
    }



    private class PlacesTask extends AsyncTask<String, Integer, List<Place>>{
        JSONObject jObject;

        @Override
        protected List<Place> doInBackground(String...arg) {
            List<Place> places = new ArrayList<Place>();


            PlaceJSONParser placeJSONParser = new PlaceJSONParser();
            try {
                String data = HttpManager.getData(arg[0]);
                jObject = new JSONObject(data);
                places= placeJSONParser.parser(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return places;
        }

        @Override
        protected void onPostExecute(final List<Place> list){
            myMap.clear();



            if (list != null && !list.isEmpty()) {
                final LatLng latLng = showMyLocation();
                findViewById(R.id.layout_list).setVisibility(View.VISIBLE);
                placesList = (ListView)findViewById(R.id.places_list);
                placesList.setAdapter(new PlacesAdapter(getApplicationContext(),list));

                //put data to listview
                placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        myMap.clear();
                        LatLng origin = new LatLng(latLng.latitude, latLng.longitude);
                        LatLng dest = new LatLng(Double.parseDouble(list.get(position).getLat()), Double.parseDouble(list.get(position).getLng()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(dest);
                        markerOptions.title(list.get(position).getPlaceName());
                        myMap.addMarker(markerOptions);
                        String url = getDirectionUrl(origin, dest);
                        Log.e("URL", url);
                        findViewById(R.id.layout_list).setVisibility(View.GONE);
                        AsyncTaskCompat.executeParallel(new ParerTask(), url);
                    }
                });
            }
            else {
                findViewById(R.id.layout_list).setVisibility(View.GONE);
            }
            pointMarker(list);
        }
    }

    private void pointMarker(List<Place> placesMarker){

        markers = new Hashtable<String, String>();
        for (int i = 0; i < placesMarker.size(); i++) {
            Place place = placesMarker.get(i);
            if (place == null) {
                continue;
            }
            LatLng latLng = new LatLng(Double.parseDouble(place.getLat()), Double.parseDouble(place.getLng()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng)
                    .title(place.getPlaceName())
                    .snippet(place.getVicinity());
            final Marker markerPlace= myMap.addMarker(markerOptions);
            if(place.getPhotoReference() != ""){
                String photoReference = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                        +place.getPhotoReference()
                        +"&key=AIzaSyDuhTpj9BIfvsheMyzLRXt5lWoZc6aWCNY";
                markers.put(markerPlace.getId(), photoReference);
            }else {
                markers.put(markerPlace.getId(), place.getIconPlace());
            }
        }
        myMap.setInfoWindowAdapter(new MarkersAdapter(getApplicationContext(), markers));



    }

    private String getDirectionUrl(LatLng origin, LatLng dest){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }


    private class ParerTask extends AsyncTask<String, Integer, List<List<Place>>>{
        JSONObject object;
        List<List<Place>> routes = null;

        @Override
        protected List<List<Place>> doInBackground(String... url) {
            try {
                String data = HttpManager.getData(url[0]);
                object = new JSONObject(data);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(object);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<Place>> result) {
            ArrayList<LatLng> points;
            PolylineOptions polylineOptions = null;
            List<Place> path = null;
            for (int i=0; i<result.size(); i++){
                points = new ArrayList<>();
                polylineOptions = new PolylineOptions();

                path = result.get(i);
                for (int j=0; j<path.size(); j++){
                    Place point = path.get(j);
                    LatLng position = new LatLng(Double.parseDouble(point.getLat()), Double.parseDouble(point.getLng()));

                    points.add(position);
                }
                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.GREEN);
            }
            myMap.addPolyline(polylineOptions);
        }
    }

    private void drawCircle(LatLng loccation){
        CircleOptions options = new CircleOptions();
        options.center(loccation);
        options.radius(100);
        options.fillColor(0x55CEE3F6);
        options.strokeColor(getResources().getColor(R.color.colorPrimaryDark));
        options.strokeWidth(5);

        myMap.addCircle(options);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (findViewById(R.id.layout_list).getVisibility() == View.VISIBLE){
            findViewById(R.id.layout_list).setVisibility(View.GONE);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longtitude=location.getLongitude();
        LatLng latLng=new LatLng(latitude,longtitude);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
