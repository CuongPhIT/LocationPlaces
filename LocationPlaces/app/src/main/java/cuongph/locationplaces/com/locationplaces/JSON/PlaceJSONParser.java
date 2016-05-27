package cuongph.locationplaces.com.locationplaces.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cuongph.locationplaces.com.locationplaces.models.Place;

/**
 * Created by CuongPH on 30/03/2016.
 */
public class PlaceJSONParser {

    public List<Place> parser(JSONObject jObject){
        JSONArray jPlaces = null;

        try {
            jPlaces = jObject.getJSONArray("results");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return getPlaces(jPlaces);
    }

    public  List<Place> getPlaces(JSONArray jPlaces){
        List<Place> placeList = new ArrayList<Place>();
        Place place = null;
        for (int i=0; i<jPlaces.length(); i++){
            try{
                place = getPlace(jPlaces.getJSONObject(i));
                placeList.add(place);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return placeList;
    }

    public Place getPlace(JSONObject jplace){
        Place place=null;

        String placeName;
        String vicinity;
        String latitude;
        String longitude;
        String photoReference="";
        String iconPlace="";

        try {
            placeName=jplace.optString("name", "");
            vicinity=jplace.optString("vicinity", "");

            latitude=jplace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=jplace.getJSONObject("geometry").getJSONObject("location").getString("lng");

            JSONArray jPhoto;
            jPhoto = jplace.optJSONArray("photos");
            if (jPhoto!=null){
                for (int i=0; i<jPhoto.length(); i++){
                    photoReference = jPhoto.getJSONObject(i).getString("photo_reference");
                }
            }
            else {
                iconPlace = jplace.optString("icon", "");
            }
            place = new Place(placeName, latitude, longitude, vicinity, photoReference, iconPlace);
        }catch (Exception e){
            e.printStackTrace();
        }

        return place;
    }


}
