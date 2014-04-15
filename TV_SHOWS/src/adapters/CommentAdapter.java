package adapters;

import android.R.color;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Comment;
import engine.Episode;

/**
 * Created with IntelliJ IDEA. User: Shahab Date: 8/22/12 Time: 11:37 AM To
 * change this template use File | Settings | File Templates.
 */
public class CommentAdapter extends BaseAdapter {

	private static final String TAG = CommentAdapter.class.getSimpleName();

	Vector<Comment> comments;
	Context context;
	FragmentManager fm;

	public CommentAdapter(Vector<Comment> comments, Context context,
			FragmentManager fm) {
		this.comments = comments;
		this.context = context;
		this.fm = fm;

	}

	@Override
	public int getCount() {
		return comments.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int i) {
		return comments.get(i); // single item in the list
	}

	@Override
	public long getItemId(int i) {
		return i; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.comment, parent, false);

		}
		
		TextView user = (TextView) view.findViewById(R.id.user);
		user.setText(comments.get(index).id);

		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(comments.get(index).date);

		TextView text = (TextView) view.findViewById(R.id.comment);
		text.setText(Html.fromHtml(comments.get(index).text.replaceAll("[\n\r]", "")));

		return view;

	}

	
	

	
}