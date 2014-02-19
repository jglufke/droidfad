/**
 * 
 */
package com.droidfad.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 *
 * @author John
 * copyright Jens Glufke, Germany mailto:jglufke@gmx.de
 * implementation of a table view with fixed cols and rows 
 * 
 */
public class TableViewExtra extends LinearLayout implements IOnScrollListener {

	public class TableCell extends TextView {
		private Object addInfo;
		int row, col;
		public TableCell(Context pContext, int row, int col) {
			super(pContext);
			this.row = row;
			this.col = col;
			setLayoutParams(new LayoutParams(colWidth, rowHeight));
			// setDrawBorder(false);
		}
		public int getCol() {
			return col;
		}
		public int getRow() {
			return row;
		}
		public Object getAddInfo() {
			return addInfo;
		}
		public void setAddInfo(Object pAddInfo) {
			addInfo = pAddInfo;
		}
	}
	private Activity activity;
	private HorizontalScrollView colHeaderScroll;
	private int displayHeight;
	private int displayWidth;
	private ScrollView rowHeaderScroll;

	private LinearLayout tableContent;
	private LinearLayout rowHeader;
	private LinearLayout colHeader;
	private int rowHeight;
	private int colWidth;
	private TableCell rowColHead;
	private CoupledVertScroller tableContentVertScroll;
	private CoupledHorizScroller tableContentHorScroll;

	/**
	 *
	 * @param pActivity
	 *
	 */
	public TableViewExtra(Activity pActivity, int pRowCount, int pColCount) {
		super(pActivity);
		activity = pActivity;
		init(pRowCount, pColCount);
	}

	public synchronized int getColumnCount() {
		return colHeader.getChildCount();
	}

	public synchronized int getRowCount() {
		return rowHeader.getChildCount();
	}

	public synchronized void appendColumn() {
		/**
		 * append a new entry in the column header 
		 */
		int lColCount = getColumnCount();
		colHeader.addView(new TableCell(activity, -1, lColCount));
		colHeader.invalidate();
		/**
		 * append a new entry to every row in tableContent
		 */
		for(int row=0; row<getRowCount();row++) {
			LinearLayout lRowView = (LinearLayout) tableContent.getChildAt(row);
			lRowView.addView(new TableCell(activity, row, lColCount));
			lRowView.invalidate();
		}

	}
	public synchronized void appendRow() {
		/**
		 * append a new entry to the row header
		 */
		int lRowCount = getRowCount();
		rowHeader.addView(new TableCell(activity, lRowCount, -1));
		rowHeader.invalidate();
		/**
		 * append a new row to the tablecontent
		 */
		LinearLayout lNewRow = new LinearLayout(activity);
		tableContent.addView(lNewRow);
		lNewRow.setOrientation(LinearLayout.HORIZONTAL);

		for(int  col=0; col<getColumnCount(); col++) {
			lNewRow.addView(new TableCell(activity, lRowCount, col));
		}
		tableContent.invalidate();
	}
	/**
	 * 
	 *
	 * @param row, if == -1 col header is returned
	 * @param col, if == -1 row header is returned 
	 * @return
	 *
	 */
	public synchronized TableCell getCellAt(int row, int col) {
		TableCell lTableCell = null;
		if(row == -1 && col == -1) {
			lTableCell = rowColHead;
		} else if(row == -1) {
			lTableCell = (TableCell) colHeader.getChildAt(col);
		} else if(col == -1) {
			lTableCell = (TableCell) rowHeader.getChildAt(row);
		} else {
			lTableCell = (TableCell) ((LinearLayout) tableContent.getChildAt(row)).getChildAt(col);
		}
		return lTableCell;
	}
	private void init(int pRowCount, int pColCount) {

		Display        lDisplay = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics lMetrics = new DisplayMetrics();
		lDisplay.getMetrics(lMetrics);
		displayWidth            = lMetrics.widthPixels;
		displayHeight           = lMetrics.heightPixels;

		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(Color.rgb(120, 120, 120));
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		/**
		 * create the column header
		 */
		colWidth  = Math.min(100, displayWidth/4);
		rowHeight = Math.min(100, displayWidth/8);

		LinearLayout lColHeaderRow = new LinearLayout(activity);
		addView(lColHeaderRow);
		lColHeaderRow.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, rowHeight));

		rowColHead = new TableCell(activity, -1, -1);
		lColHeaderRow.addView(rowColHead);
		rowColHead.setLayoutParams(new LayoutParams(colWidth, rowHeight));

		LinearLayout lColScrollWrapper = new LinearLayout(activity);
		lColHeaderRow.addView(lColScrollWrapper);
		lColScrollWrapper.setLayoutParams(new LinearLayout.LayoutParams(displayWidth-colWidth, rowHeight));

		colHeaderScroll = new CoupledHorizScroller(activity, this);
		lColScrollWrapper.addView(colHeaderScroll);
		colHeaderScroll.setHorizontalScrollBarEnabled(true);

		colHeader = new LinearLayout(activity);
		colHeaderScroll.addView(colHeader);
		colHeader.setOrientation(LinearLayout.HORIZONTAL);

		for(int col=0; col<pColCount; col++) {
			TableCell lColHead = new TableCell(activity, -1, col);
			lColHead.setText("col:" + col);
			colHeader.addView(lColHead);
		}

		LinearLayout lRowsView = new LinearLayout(activity);
		addView(lRowsView);
		lRowsView.setOrientation(LinearLayout.HORIZONTAL);
		lRowsView.setLayoutParams(new LinearLayout.LayoutParams(displayWidth, displayHeight-rowHeight));

		LinearLayout lRowHeaderWrapper = new LinearLayout(activity);
		lRowsView.addView(lRowHeaderWrapper);
		lRowHeaderWrapper.setLayoutParams(new LinearLayout.LayoutParams(colWidth, displayHeight - rowHeight));

		tableContentVertScroll = new CoupledVertScroller(activity, this);
		rowHeaderScroll        = new CoupledVertScroller(activity, this);

		lRowHeaderWrapper.addView(rowHeaderScroll);
		lRowHeaderWrapper.setHorizontalScrollBarEnabled(false);
		lRowHeaderWrapper.setVerticalScrollBarEnabled(true);

		rowHeader = new LinearLayout(activity);
		rowHeaderScroll.addView(rowHeader);
		rowHeader.setOrientation(LinearLayout.VERTICAL);
		rowHeader.setBackgroundColor(Color.rgb(100, 100, 100));

		for(int row=0; row<pRowCount; row++) {
			TableCell lRowHead = new TableCell(activity, row, -1);
			rowHeader.addView(lRowHead);
			lRowHead.setText("row:" + row);
		}

		LinearLayout lTableContentWrapper = new LinearLayout(activity);
		lRowsView.addView(lTableContentWrapper);
		lTableContentWrapper.setLayoutParams(new LinearLayout.LayoutParams(displayWidth-colWidth, displayHeight-rowHeight));

		lTableContentWrapper.addView(tableContentVertScroll);
		tableContentVertScroll.setHorizontalScrollBarEnabled(true);
		tableContentVertScroll.setVerticalScrollBarEnabled(true);
		tableContentVertScroll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		tableContentHorScroll = new CoupledHorizScroller(activity, this);

		tableContentVertScroll.addView(tableContentHorScroll);
		tableContentHorScroll.setHorizontalScrollBarEnabled(true);

		tableContent = new LinearLayout(activity);
		tableContentHorScroll.addView(tableContent);

		tableContent.setBackgroundColor(Color.rgb(150, 100, 100));
		tableContent.setOrientation(LinearLayout.VERTICAL);

		for(int row=0; row<pRowCount;row++) {
			LinearLayout lRowView = new LinearLayout(activity);
			tableContent.addView(lRowView);
			lRowView.setOrientation(LinearLayout.HORIZONTAL);
			for(int col=0;col<pColCount; col++) {
				TableCell lTableCell = new TableCell(activity, row, col); 
				lRowView.addView(lTableCell);
				lTableCell.setText("c:" + col + " r:" + row);
			}
		}
	}

	public synchronized void removeColumn(int col) {
		/**
		 * remove child at col in colHeader
		 */
		colHeader.removeViewAt(col);
		colHeader.invalidate();
		/**
		 * remove entry from every table row
		 */
		for(int row=0; row<getRowCount();row++) {
			LinearLayout lRowView = (LinearLayout) tableContent.getChildAt(row);
			lRowView.removeViewAt(col);
			lRowView.invalidate();
		}
	}

	public synchronized void removeRow(int row) {

		rowHeader.removeViewAt(row);
		rowHeader.invalidate();
		/**
		 * remove row from tablecontent
		 */
		tableContent.removeViewAt(row);
		tableContent.invalidate();

	}

	public synchronized void setBackgroundColor(int row, int col, int color) {
		TableCell lCell = getCellAt(row, col);
		if(lCell != null) {
			lCell.setBackgroundColor(color);
			lCell.invalidate();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.droidfad.view.IOnScrollListener#onScrollChanged(android.view.View, int, int)
	 */
	@Override
	public void onScrollChanged(View pSource, int pX, int pY) {

		if(pSource == rowHeaderScroll) {
			tableContentVertScroll.scrollTo(0, pY);
		} else if(pSource == tableContentVertScroll) {
			rowHeaderScroll.scrollTo(0, pY);
		} else if(pSource == colHeaderScroll) {
			tableContentHorScroll.scrollTo(pX, 0);
		} else if(pSource == tableContentHorScroll) {
			colHeaderScroll.scrollTo(pX, 0);
		}

	}
}

interface IOnScrollListener {
	void onScrollChanged(View pSource, int x, int y);
}

class CoupledVertScroller extends ScrollView {
	IOnScrollListener        scrollListener = null;

	public CoupledVertScroller(Context pContext, IOnScrollListener pScrollListener) {
		super(pContext);
		scrollListener = pScrollListener;
	}
	@Override
	protected void onScrollChanged(final int pL, final int pT, final int pOldl, final int pOldt) {
		if(scrollListener != null) {

			scrollListener.onScrollChanged(CoupledVertScroller.this, pL, pT);
		}
	}
}

class CoupledHorizScroller extends HorizontalScrollView {
	IOnScrollListener        scrollListener;
	public CoupledHorizScroller(Context pContext, IOnScrollListener pScrollListener) {
		super(pContext);
		scrollListener = pScrollListener;
	}
	@Override
	protected void onScrollChanged(final int pL, final int pT, final int pOldl, int pOldt) {
		if(scrollListener!= null) {

			scrollListener.onScrollChanged(CoupledHorizScroller.this, pL, pT);
		}
	}
}
