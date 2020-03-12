package com.example.myscanlotto;

import android.os.AsyncTask;

public class BackgroundTask extends AsyncTask<String, Void, Lotto> {

    @Override
    protected Lotto doInBackground(String... params) {
        OpenLottoNumber client = new OpenLottoNumber();

        String aFewNum = params[0];

        Lotto lotto = client.getLotto(aFewNum);

        return lotto;
    }
}
