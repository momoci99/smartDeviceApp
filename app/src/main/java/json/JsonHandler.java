package json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import format.DBResultForm;

/**
 * Created by Melchior_S on 2016-09-16.
 */
public class JsonHandler {



    public JSONObject createJSONObjectForServer(String WifiMAC , CopyOnWriteArrayList<JSONObject> dbResultList)
    {
        JSONObject jsonTransaction = new JSONObject();
        JSONArray sensorDataJSONArray = new JSONArray();
        try{

            jsonTransaction.put("device",WifiMAC);

            for(int i =0; i<dbResultList.size(); i++)
            {
                sensorDataJSONArray.put(dbResultList.get(i));
            }


            jsonTransaction.put("data",sensorDataJSONArray);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonTransaction;
    }

    public JSONObject createJSONObjectForServer(String WifiMAC , DBResultForm dbResult)
    {
        JSONObject jsonTransaction = new JSONObject();
        JSONArray sensorDataJSONArray = new JSONArray();
        try{

            jsonTransaction.put("device",WifiMAC);


            sensorDataJSONArray.put(createJSONObjectSensorData(dbResult));

            jsonTransaction.put("data",sensorDataJSONArray);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonTransaction;
    }

    public JSONObject createJSONObjectSensorData(DBResultForm dbResult)
    {
        JSONObject sensorData = new JSONObject();
        try {
            sensorData.put("nid",dbResult.getDeviceName());

            String sensorName_1 = dbResult.getSensorName_1();
            if(sensorName_1!=null && !sensorName_1.isEmpty()) {
                sensorData.put(sensorName_1, changeFormatTo2f(dbResult.getSensorData_1()));

                String sensorName_2 = dbResult.getSensorName_2();
                if (sensorName_2 != null && !sensorName_2.isEmpty()) {
                    sensorData.put(sensorName_2, changeFormatTo2f(dbResult.getSensorData_2()));
                }

                String sensorName_3 = dbResult.getSensorName_3();
                if (sensorName_3 != null && !sensorName_3.isEmpty()) {
                    sensorData.put(sensorName_3, changeFormatTo2f(dbResult.getSensorData_3()));
                }

                String sensorName_4 = dbResult.getSensorName_4();
                if (sensorName_4 != null && !sensorName_4.isEmpty()) {
                    sensorData.put(sensorName_4, changeFormatTo2f(dbResult.getSensorData_4()));
                }

                sensorData.put("time", dbResult.getTime());

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return sensorData;
    }
    public double changeFormatTo2f(double value)
    {
        String d3_format = String.format(Locale.US,"%.2f",value);
        return Double.valueOf(d3_format);
    }

}
