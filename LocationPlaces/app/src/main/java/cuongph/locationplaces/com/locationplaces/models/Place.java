package cuongph.locationplaces.com.locationplaces.models;

/**
 * Created by CuongPH on 31/03/2016.
 */
public class Place {

    private String placeName="";
    private String lat="";
    private String lng="";
    private String vicinity="";
    private String photoReference="";
    private String iconPlace="";

    public Place(String lat, String lng){
        this.lat = lat;
        this.lng = lng;
    }

    public Place(String placeName, String lat, String lng, String vicinity, String photoReference, String iconPlace){
        this.placeName=placeName;
        this.lat=lat;
        this.lng=lng;
        this.vicinity=vicinity;
        this.photoReference=photoReference;
        this.iconPlace=iconPlace;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public void setIconPlace(String iconPlace) {
        this.iconPlace = iconPlace;
    }

    public String getIconPlace() {
        return iconPlace;
    }
}
