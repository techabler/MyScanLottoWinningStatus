package com.example.myscanlotto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    //view Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress, textViewResult, txtFewNumber, txtNumber01;

    //qr code scanner object
    private IntentIntegrator qrScan;

    private String aFewTime = "0000";

    private int value;

    private MyBackgroundTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Objects
        buttonScan =  findViewById(R.id.buttonScan);
        textViewName =  findViewById(R.id.textViewName);
        textViewResult =   findViewById(R.id.textViewResult);

        txtFewNumber = findViewById(R.id.txtFewNumber);
        txtNumber01 = findViewById(R.id.txtNumber01);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //qrScan.setOrientationLocked(false);
        //qrScan.initiateScan();

        //button onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                //qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
//                qrScan.initiateScan();

                qrScan.setBeepEnabled(false);//바코드 인식시 소리
                qrScan.initiateScan();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                textViewResult.setText(result.getContents());

                setNumber(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setNumber(String scanText){
        String mIndexChar = "v=";
        String mNumbers = "";
        int oneNumberSize = 13;
        String[] arrNumber = new String[5];
        String mNumberList = "";

        aFewTime = scanText.substring(scanText.indexOf(mIndexChar)+2);
        mNumbers = aFewTime.substring(4);
        aFewTime = aFewTime.substring(0,4);
        mNumbers = mNumbers.substring(0, 65);

        //arrNumber
        for(int i=0; i<5;i++){
            arrNumber[i] = ChangeNumber(mNumbers.substring(i*oneNumberSize, (i+1)*oneNumberSize));
        }

        if(aFewTime.length() > 0){
            //task = new MyBackgroundTask();
            //task.execute(100);
            BackgroundTask task = new BackgroundTask();
            try {

                Lotto lotto = task.execute(aFewTime).get();
                if(lotto == null){
                    Log.d(TAG, "Lotto is null");
                }else{
                    StringBuilder mySb = new StringBuilder();
                    mySb.append(lotto.drwtNo1);
                    mySb.append(" ");
                    mySb.append(lotto.drwtNo2);
                    mySb.append(" ");
                    mySb.append(lotto.drwtNo3);
                    mySb.append(" ");
                    mySb.append(lotto.drwtNo4);
                    mySb.append(" ");
                    mySb.append(lotto.drwtNo5);
                    mySb.append(" ");
                    mySb.append(lotto.drwtNo6);
                    mySb.append(" ");

                    txtFewNumber.setText(lotto.getDrwNoDate() + ":" + aFewTime);
                    txtNumber01.setText(mySb.toString());
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        mNumberList = TextUtils.join("\n", arrNumber);

        textViewName.setText(mNumberList);

    }

    public String ChangeNumber(String orgNumber){
        String result = "";
        String mHeader = "";
        String mBody = "";

        if(orgNumber.length() == 13){
            mHeader = orgNumber.substring(0,1);
            mBody = orgNumber.substring(1);

            switch (mHeader.toUpperCase()){
                case "M":
                    result += "Maul:";
                    break;
                case "Q":
                    result += "Auto:";
                    break;
                case "N":
                    return result;
//                    result += "None:";
//                    break;
            }

            for(int i=0; i<6; i++){
                result += mBody.substring(i*2, (i+1)*2) + "_";
            }
        }


        return result;
    }


    //########## Inner AsyncTask Class #############333
    // Deprecated
    public class MyBackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            value = 0;
//            progress.setProgress(value);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            while (isCancelled() == false){
                value++;

                if(value >= 100){
                    break;
                }else{
                    publishProgress();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return value;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progress.setProgress(values[0].intValue());
//            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
//            progress.setProgress(0);
        }

        @Override
        protected void onCancelled() {
//            progress.setProgress(0);
        }
    }

}
