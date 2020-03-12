package com.example.myscanlotto;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenLottoNumber {
    private final static String TAG = "OpenLottoNumber";

    final static String openLottoAPIUri = "https://www.nlotto.co.kr/common.do?method=getLottoNumber";
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int DATARETRIEVAL_TIMEOUT = 5000;


    public Lotto getLotto(String num) {
        Lotto lotto = new Lotto();
        String urlString = openLottoAPIUri + "&drwNo=" + num;

        Log.d(TAG, urlString);

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

//            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = new JSONObject(getStringFromInputStream(in));

            lotto = parseJSON(json);

        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }catch (JSONException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }catch (IOException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return lotto;
    }

    private String getStringFromInputStream(InputStream is) {
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            while ((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d(TAG, sb.toString());

        return sb.toString();
    }

    private Lotto parseJSON(JSONObject json) throws JSONException {
        Lotto lotto = new Lotto();
        lotto.setDrwNoDate(json.getString("drwNoDate"));
        lotto.setDrwtNo1(json.getString("drwtNo1"));
        lotto.setDrwtNo2(json.getString("drwtNo2"));
        lotto.setDrwtNo3(json.getString("drwtNo3"));
        lotto.setDrwtNo4(json.getString("drwtNo4"));
        lotto.setDrwtNo5(json.getString("drwtNo5"));
        lotto.setDrwtNo6(json.getString("drwtNo6"));
        lotto.setBnusNo(json.getString("bnusNo"));

        return lotto;
    }

}
