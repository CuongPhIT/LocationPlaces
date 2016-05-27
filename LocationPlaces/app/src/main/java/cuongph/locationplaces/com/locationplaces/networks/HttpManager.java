package cuongph.locationplaces.com.locationplaces.networks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CuongPH on 31/03/2016.
 */
public class HttpManager {
    public static String getData(String stringUrl)throws IOException {

        InputStream is = null;
        StringBuilder sb = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);

            sb = new StringBuilder();
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line="";
            while ((line = reader.readLine())!=null){
                sb.append(line);

            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            }catch (Exception e) {
            }
            connection.disconnect();
        }
        return sb.toString();
    }


}
