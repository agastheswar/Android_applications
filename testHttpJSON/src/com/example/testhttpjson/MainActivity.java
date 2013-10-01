package com.example.testhttpjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	ListView videoList;
	ArrayAdapter<String> videoAdapter;
	Context context;
	String FeedURL = "https://gdata.youtube.com/feeds/api/videos?q=surfing&v=2&alt=jsonc&start-index=1&max-results=30";
	ArrayList<String> videoArrayList = new ArrayList<String>();
	ArrayList<Bitmap> thumbnail = new ArrayList<Bitmap>();
	ArrayList<String> player_url = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		videoList = (ListView) findViewById(R.id.videoList);
		
		
		VideoListTask task = new VideoListTask();
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	private class VideoListTask extends AsyncTask<Void, Void, Void>{

		ProgressDialog dialog;
		
		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(FeedURL);
			
			try {
				HttpResponse response = client.execute(getRequest);
				StatusLine status = response.getStatusLine();
				if(status.getStatusCode() != 200) {
					return null;
				}
				
				InputStream jsonStream = response.getEntity().getContent();
				BufferedReader bfr = new BufferedReader(new InputStreamReader(jsonStream));
				StringBuilder builder = new StringBuilder();
				String line;
				
				while((line = bfr.readLine())!= null) {
					builder.append(line);
				}
				
				String jsonData = builder.toString();
				
				JSONObject json = new JSONObject(jsonData);
				JSONObject data = json.getJSONObject("data");
				JSONArray videos = data.getJSONArray("items");
				
				for( int i=0; i<10; i++) {
					JSONObject video = videos.getJSONObject(i);
					String title = video.getString("title");
					String thumbnail_string = video.getJSONObject("thumbnail").getString("sqDefault");
					URL thumbnail_url = new URL(thumbnail_string);
					Bitmap bmp = BitmapFactory.decodeStream(thumbnail_url.openConnection().getInputStream());
					thumbnail.add(bmp);
					videoArrayList.add(title);
	                String url = video.getJSONObject("player").getString("default");
	                player_url.add(url);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("Loading Videos");
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			videoAdapter vadapter = new videoAdapter(MainActivity.this, player_url, videoArrayList, thumbnail);
			videoList.setAdapter(vadapter);
			super.onPostExecute(result);
		}

	}
}
