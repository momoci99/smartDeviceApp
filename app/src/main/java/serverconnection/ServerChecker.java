package serverconnection;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Melchior_S on 2016-09-16.
 */
public class ServerChecker implements Runnable{


    private Handler mTargetHandler;

    private final String Token = "c94da62a7d8c4b4b0148b9738ede292b";
    private final String USER_AGENT = "Mozilla/5.0";

    private final String url = "http://hive.codefict.com/hive/data";



    @Override
    public void run() {
        if(sendPing());
        {
            sendMessageToConfigActivity();
        }
    }
    private void sendMessageToConfigActivity()
    {
        Message SignalMessage = Message.obtain();
        SignalMessage.what = 99;
        mTargetHandler.sendMessage(SignalMessage);

    }
    private boolean sendPing()
    {
        boolean serverStatus = false;
        int responseCode = -1;
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection ) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Token",Token);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes("ping");
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            String responseCodeSting = String.valueOf(responseCode);


            System.out.println("\n con :"+con);
            System.out.println("\nSending Ping : " + url);
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


            if(responseCodeSting.charAt(0)==4)
            {
                serverStatus= true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverStatus;
    }
}
