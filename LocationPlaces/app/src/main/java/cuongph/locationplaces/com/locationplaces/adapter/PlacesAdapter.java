package cuongph.locationplaces.com.locationplaces.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cuongph.locationplaces.com.locationplaces.models.Place;
import cuongph.locationplaces.com.locationplaces.R;

/**
 * Created by CuongPH on 03/04/2016.
 */
public class PlacesAdapter extends BaseAdapter {
    private List<Place> places;
    private LayoutInflater inflater;
    private Context context;
    public PlacesAdapter(Context context, List<Place> list){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.places = list;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_layout, null);
            viewHolder.img_place = (ImageView)convertView.findViewById(R.id.ic_place);
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_vicinity = (TextView)convertView.findViewById(R.id.tv_vinicity);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Place place = this.places.get(position);
        viewHolder.tv_name.setText(place.getPlaceName());
        viewHolder.tv_vicinity.setText(place.getVicinity());
        String photo_reference = place.getPhotoReference();
        String iconPlace = place.getIconPlace();

        if (photo_reference != ""){
            String photoReference = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + photo_reference
                    +"&key=AIzaSyDuhTpj9BIfvsheMyzLRXt5lWoZc6aWCNY";
            Picasso.with(context).load(photoReference).into(viewHolder.img_place);
        }else {
            Picasso.with(context).load(iconPlace).into(viewHolder.img_place);
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView img_place;
        TextView tv_name;
        TextView tv_vicinity;
    }
}
