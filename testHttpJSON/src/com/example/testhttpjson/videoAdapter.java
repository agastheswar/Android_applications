package com.example.testhttpjson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class videoAdapter extends BaseAdapter {

	Context video_context;
	ArrayList<String> video_player_url;
    ArrayList<String> video_title;
    ArrayList<Bitmap> video_thumbnail;
    LayoutInflater video_Inflater;
	
	public videoAdapter(Context context, ArrayList<String>player_url, ArrayList<String>title, ArrayList<Bitmap>thumbnail) {
		video_context = context;
		video_player_url = player_url;
		video_title = title;
		video_thumbnail = thumbnail;
		video_Inflater = (LayoutInflater) video_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return video_player_url.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View videoView, ViewGroup parent) {
		View itemView = videoView;
		if(itemView == null) {
			itemView = video_Inflater.inflate(R.layout.item_view, null);
		}
		
		ImageView Image_view = (ImageView)itemView.findViewById(R.id.Icon_video);
		TextView title_text = (TextView)itemView.findViewById(R.id.title);
		StringBuilder builder = new StringBuilder();
		builder.append("<a href=");
		builder.append("\"");
		builder.append(video_player_url.get(position));
		builder.append("\">");
		builder.append(video_title.get(position));
		builder.append("</a>");
		String hello = builder.toString();
		
		title_text.setText(Html.fromHtml(hello));
		title_text.setMovementMethod(LinkMovementMethod.getInstance());
		Image_view.setImageBitmap(Bitmap.createScaledBitmap(video_thumbnail.get(position), 250, 250, false));		
		return itemView;
		
	}
}
