package com.nidoe.firstapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;


public class DisplayMessageActivity extends ActionBarActivity {

    private TextView textView;
    ListView listView;
    String origUrl;
    JSONArray follows;
    ArrayList<Stream> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        setContentView(R.layout.activity_display_message);
        textView = (TextView)findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listView);
        

        origUrl = "https://api.twitch.tv/kraken/users/" + message + "/follows/channels";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(origUrl);
        } else {
            textView.setText("No network connection available.");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {

                String result = downloadUrl(urls[0]);
                JSONObject jsonObj = new JSONObject(result);
                int total = Integer.parseInt(jsonObj.get("_total").toString());
//                JSONObject resultJSON = new JSONObject(result);
                follows = jsonObj.getJSONArray("follows");

                for(int i=25; i <= total; i+=25){

                    String next = downloadUrl(origUrl + "?direction=DESC&limit=25&offset=" + i + "&sortby=created_at");
                    JSONArray follow = new JSONObject(next).getJSONArray("follows");
                    for(int j=0;j < follow.length();j++){
                        follows.put(follow.get(j));
                    }
                }

                return result;
            } catch (Exception e) {
                return "Unable to download URL properly";

            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                list = new ArrayList<Stream>(follows.length());
                for(int i=0;i < follows.length();i++) {
                    JSONObject channel = follows.getJSONObject(i).getJSONObject("channel");
                    Stream chan = new Stream(channel);
                    textView.setText(channel.getString("followers"));
                    list.add(chan);
                }
                //String res = downloadUrl("https://api.twitch.tv/kraken/streams?limit=100&channel=" + Stream.names);
                //textView.setText(res);
                MyAdapter adapter = new MyAdapter(DisplayMessageActivity.this,list);
                listView.setAdapter(adapter);
                AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Stream clicked = (Stream)parent.getItemAtPosition(position);
                        Intent click = new Intent(getApplicationContext(),StreamInfo.class);
                        String name = clicked.getDisplayName();
                        click.putExtra("Display Name",name);
                        startActivity(click);
                    }
                };
                listView.setOnItemClickListener(mMessageClickedHandler);
                // textView.setText(jsonObj.toString(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            is.close();
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(stream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        return result;
    }


}
