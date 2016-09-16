package serverconnection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Melchior_S on 2016-09-16.
 */
public class HttpURLConnecter {
    private final String Token = "c94da62a7d8c4b4b0148b9738ede292b";
    private final String USER_AGENT = "Mozilla/5.0";
    public void sendPost(JSONObject parameters)  {

        String url = "http://hive.codefict.com/hive/TestPath";
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection ) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Token",Token);
            //String urlParameters = " {\"device\":\"C8:14:79:E3:65:33\",\"data\":[{\"nid\":\"mOmOcus\",\"oxy\":25.5,\"tempt\":29.7,\"hum\":40.66,\"time\":\"2016-09-16 10:24:00\"}]}";
            String urlParameters = parameters.toString();
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\n con :"+con);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
