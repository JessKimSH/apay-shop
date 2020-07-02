package com.autoever.apay.activities;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.autoever.apay.models.Store;
import com.autoever.apay.models.VirtualAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/* 앱 전체에서 사용하는 Store, Store의 Virtual Account Object 생성 */
public class CommonApplication extends Application {
    private static Store store;
    private static VirtualAccount virtualAccount;

    // 해당 객체의 데이터 불러오기
    public void onCreate() {
        super.onCreate();

        store = new Store();
        virtualAccount = new VirtualAccount();

        SyncData orderData = new SyncData();
        try {
            orderData.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Async Task (but 동기화해서 데이터 불러 옴)
    private static class SyncData extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            Log.d("debug", "Getting Common Object Data ");
            URL url = null;
            String str, receiveMsg = null;

            try {

                /*
                 Store 데이터 가져오기
                 */

                int storeId = 2;
                url = new URL("http://api.apay.ga:8080/store/" + storeId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    while (((str = bufferedReader.readLine()) != null)) {
                        stringBuffer.append(str);
                    }
                    receiveMsg = stringBuffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    store = getStore(stringBuffer.toString());

                    bufferedReader.close();
                } else {
                    Log.i("Connection", "Error");
                }

                /*
                 Virtual Account 데이터 가져오기
                 */

                // 변수 초기화
                url = null;
                str = null;
                receiveMsg = null;

                int tokenSystemId = 1;
                url = new URL("http://api.apay.ga:8080/subscriber/" + storeId
                                        + "/balance?tokenSystemId=" + tokenSystemId);

                HttpURLConnection connV = (HttpURLConnection) url.openConnection();

                if (connV.getResponseCode() == connV.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connV.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    while (((str = bufferedReader.readLine()) != null)) {
                        stringBuffer.append(str);
                    }
                    receiveMsg = stringBuffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    virtualAccount = getVirtualAccount(stringBuffer.toString());

                    bufferedReader.close();
                } else {
                    Log.i("Connection", "Error");
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        // Json to Store
        public Store getStore(String jsonString) {
            Store returnObject = new Store();

            try {
                JSONObject json = new JSONObject(jsonString).getJSONObject("data");

                returnObject.setId(json.optInt("storeId"));
                returnObject.setLoginId(json.optString("loginId"));
                returnObject.setBusinessRegistrationNumber(json.optString("businessRegistrationNumber"));
                returnObject.setStoreName(json.optString("storeName"));
                returnObject.setTokenSystemId(json.optString("tokenSystemId"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return returnObject;
        }

        // Json to Virtual Account
        public VirtualAccount getVirtualAccount(String jsonString) {
            VirtualAccount returnObject = new VirtualAccount();

            try {
                JSONObject json = new JSONObject(jsonString).getJSONObject("data");

                Log.i("Account", "value: " + json.optInt("balance"));
                returnObject.setValue(json.optInt("balance"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return returnObject;
        }
    }

    // store, virtual account 업데이트 함수
    public static void reload() throws ExecutionException, InterruptedException {
        SyncData orderData = new SyncData();

        // .get()을 붙여 데이터를 동기화 상태로 가져온다.
        orderData.execute().get();
    }

    public static Store getStore() {
        return store;
    }

    public static VirtualAccount getVirtualAccount() { return virtualAccount; }
}


