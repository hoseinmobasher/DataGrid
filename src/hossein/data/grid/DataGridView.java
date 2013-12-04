package hossein.data.grid;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class DataGridView extends ScrollView {
	private DataGridViewAdapter adapter;
	private LinearLayout container;

	private boolean readOnly;
	private boolean hasHeader;
	private boolean coloryRows;
	private int oddColor;
	private int evenColor;

	public DataGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.DataGridView);

		readOnly = a.getBoolean(R.styleable.DataGridView_readOnly, false);
		hasHeader = a.getBoolean(R.styleable.DataGridView_hasHeader, false);
		coloryRows = a.getBoolean(R.styleable.DataGridView_coloryRows, false);

		oddColor = a.getColor(R.styleable.DataGridView_oddColor, Color.WHITE);
		evenColor = a.getColor(R.styleable.DataGridView_evenColor, Color.WHITE);

		a.recycle();

		container = (LinearLayout) ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.data_grid_view, null, false);
		addView(container, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		this.adapter = null;
	}

	public DataGridViewAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(DataGridViewAdapter adapter) {
		this.adapter = adapter;
		notifyDataSetChanged();
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public boolean isColoryRows() {
		return coloryRows;
	}

	public void setColoryRows(boolean coloryRows) {
		this.coloryRows = coloryRows;
	}

	public int getOddColor() {
		return oddColor;
	}

	public void setOddColor(int oddColor) {
		this.oddColor = oddColor;
	}

	public int getEvenColor() {
		return evenColor;
	}

	public void setEvenColor(int evenColor) {
		this.evenColor = evenColor;
	}

	public void notifyDataSetChanged() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		container.removeAllViews();

		if (adapter == null)
			return;

		int colSize = adapter.getHeadersCount();
		int rowSize = adapter.getRowsCount();

		if (hasHeader) {
			LinearLayout headerRow = new LinearLayout(getContext());
			headerRow.setOrientation(LinearLayout.HORIZONTAL);
			headerRow.setWeightSum(colSize);
			headerRow.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			for (int i = 0; i < colSize; i++) {
				String header = adapter.getHeader(i);

				Button headerTextView = (Button) inflater.inflate(
						R.layout.data_grid_view_header, null, false);
				headerTextView.setText(header);
				headerTextView.setLayoutParams(new LinearLayout.LayoutParams(0,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1));

				headerRow.addView(headerTextView);
			}

			container.addView(headerRow);
		}

		for (int i = 0; i < rowSize; i++) {
			LinearLayout dataRow = new LinearLayout(getContext());
			dataRow.setOrientation(LinearLayout.HORIZONTAL);
			dataRow.setWeightSum(colSize);
			dataRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));

			int color = coloryRows && i % 2 == 0 ? evenColor : oddColor;

			DataGridViewRow row = adapter.getRow(i);

			for (int j = 0; j < colSize; j++) {
				String rowField = row.getRow(j);
				if (rowField instanceof String) {
					EditText rowFieldEditText = (EditText) inflater.inflate(
							R.layout.data_grid_view_field, null, false);
					rowFieldEditText.setText((String) rowField);
					rowFieldEditText.setEnabled(!readOnly);
					rowFieldEditText.setBackgroundColor(color);
					rowFieldEditText
							.setLayoutParams(new LinearLayout.LayoutParams(0,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
					dataRow.addView(rowFieldEditText);
				}
			}

			container.addView(dataRow);
		}
	}

	public interface DataGridViewRow {
		void insertRow(String data);

		void replaceRow(int index, String data);

		String getRow(int index);
	}

	public abstract class DataGridViewAdapter {
		private List<DataGridViewRow> rows;
		private List<String> headers;

		public DataGridViewAdapter(List<String> headers,
				List<DataGridViewRow> rows) {
			this.rows = rows;
			this.headers = headers;
		}

		public String getHeader(int index) {
			return headers.get(index);
		}

		public DataGridViewRow getRow(int index) {
			return rows.get(index);
		}

		public int getHeadersCount() {
			return headers.size();
		}

		public int getRowsCount() {
			return rows.size();
		}
	}
}
