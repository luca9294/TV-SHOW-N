package adapters;

import info.androidhive.slidingmenu.R;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import engine.ImageViewFromURL;
import engine.TvShow_result;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<TvShow_result> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;

	public ExpandableListAdapter(Context context,
			List<TvShow_result> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		if (this._listDataChild.get(this._listDataHeader.get(groupPosition).id)
				.get(childPosititon).length() == 0) {
			return "";

		} else {

			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition).id).get(
					childPosititon);
		}
		// return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.activity_main_activity3, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.ses);
		txtListChild.setText(Html.fromHtml(childText));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(_listDataHeader.get(groupPosition).id)
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TvShow_result headerTitle = (TvShow_result) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.result_item2, null);
		}

		TextView title = (TextView) convertView.findViewById(R.id.title);

		title.setTypeface(null, Typeface.BOLD);
		title.setText(headerTitle.title);

		TextView year = (TextView) convertView.findViewById(R.id.year);

		year.setText(headerTitle.year);

		TextView nation = (TextView) convertView.findViewById(R.id.nation);

		nation.setText(headerTitle.nation);

		TextView genres = (TextView) convertView.findViewById(R.id.genres);

		String varText = headerTitle.genres.toString().replace("[", "")
				.replace("]", "").replace("\"", " ").replace(" ,", ",");
		genres.setText(varText.substring(1));

		ImageViewFromURL url = new ImageViewFromURL(headerTitle.image_link);
		try {
			ImageView p = (ImageView) convertView.findViewById(R.id.web);
			p.setImageBitmap(url.getImage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
