package cuongph.locationplaces.com.locationplaces.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;
import java.util.List;

import cuongph.locationplaces.com.locationplaces.R;

/**
 * Created by CuongPH on 23/04/2016.
 */
public class MarkersAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private LayoutInflater inflate;
    private Hashtable<String, String> markers;
    public MarkersAdapter(Context context, Hashtable<String, String> markers){
        this.context = context;
        inflate=LayoutInflater.from(context);
        this.markers=markers;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = inflate.inflate(R.layout.custom_info_window, null);
        ImageView image = (ImageView)v.findViewById(R.id.marker_photo);
        TextView name = (TextView)v.findViewById(R.id.marker_name);
        TextView vicinity = (TextView)v.findViewById(R.id.marker_vinicity);

        String url = markers.get(marker.getId());
        Picasso.with(context).load(url).into(image);
        name.setText(marker.getTitle());
        vicinity.setText(marker.getSnippet());
        return v;
    }
}
