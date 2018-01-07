package com.windyice.sahomework;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 32699 on 2018/1/7.
 */

public class JsonReader {
    private JSONObject jsonObject;

    public JsonReader(InputStream inputStream) throws IOException, JSONException {
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        jsonObject=new JSONObject(stringBuilder.toString());
    }

    public JSONObject getJsonObject(){
        return jsonObject;
    }
}
