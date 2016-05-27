package cuongph.locationplaces.com.locationplaces.JSON;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cuongph.locationplaces.com.locationplaces.models.Place;

/**
 * Created by CuongPH on 14/05/2016.
 */
public class DirectionJSONParser {

    public List<List<Place>> parse(JSONObject jsonObject){
        List<List<Place>> routes = new ArrayList<List<Place>>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        try {
            jRoutes = jsonObject.getJSONArray("routes");
            for (int i=0; i<jRoutes.length(); i++){
                jLegs = jRoutes.getJSONObject(i).getJSONArray("legs");
                List<Place> path = new ArrayList<Place>();
                for (int j=0; j<jLegs.length(); j++){
                    jSteps =jLegs.getJSONObject(j).getJSONArray("steps");
                    for (int k = 0; k<jSteps.length(); k++){
                        String polyline = "";
                        polyline=jSteps.getJSONObject(k).getJSONObject("polyline").getString("points");
                        List<LatLng> list = decodePoly(polyline);
                        for (int l=0; l<list.size(); l++){
                            Place place = new Place(Double.toString(((LatLng)list.get(l)).latitude),Double.toString(((LatLng)list.get(l)).longitude));
                            path.add(place);
                        }
                        routes.add(path);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){

        }
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
