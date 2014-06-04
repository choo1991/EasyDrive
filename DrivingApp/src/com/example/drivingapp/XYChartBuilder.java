package com.example.drivingapp;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
//import org.achartengine.chartdemo.demo.R;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class XYChartBuilder extends Activity {
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private XYSeries mCurrentSeries;
    
    private XYSeries xSeries;
    private XYSeries ySeries;
    private XYSeries zSeries;
    
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    /** Button for adding entered data to the current series. */
    private Button mShowChart;
    private Button mDeleteData;
    /** The chart view that displays the data. */
    private GraphicalView mChartView;
    private List<DataStorageClass> accelData;
    private final String LOG_TAG = "XYChartBuilder";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dataset", mDataset);
        outState.putSerializable("renderer", mRenderer);
        outState.putSerializable("current_series", mCurrentSeries);
        outState.putSerializable("current_renderer", mCurrentRenderer);
//        Log.i(LOG_TAG, "Finished onSaveInstanceState");
        outState.putSerializable("x_series", xSeries);
        outState.putSerializable("y_series", ySeries);
        outState.putSerializable("z_series", zSeries);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        // restore the current data, for instance when changing the screen
        // orientation
        mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
        mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
        mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
        mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
//        Log.i(LOG_TAG, "Finished onRestoreInstanceState");
        xSeries = (XYSeries) savedState.getSerializable("x_series");
        ySeries = (XYSeries) savedState.getSerializable("y_series");
        zSeries = (XYSeries) savedState.getSerializable("z_series");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo);
        setContentView(R.layout.chart);
//        DataORM.deleteAllData(this);
        // the top part of the UI components for adding new data points
        mShowChart = (Button) findViewById(R.id.show_chart);
        mDeleteData = (Button) findViewById(R.id.clear_data);

        // set some properties on the main renderer
        mRenderer.setApplyBackgroundColor(true);
//        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
//        mRenderer.setAxesColor(Color.rgb(130, 130, 230));
        mRenderer.setBackgroundColor(Color.rgb(255, 250, 250));
        mRenderer.setAxesColor(Color.rgb(0, 0, 0));
        mRenderer.setAxisTitleTextSize(26);
//        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setShowGridX(true);
        mRenderer.setXLabelsColor(Color.rgb(0, 0, 0));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(20);
        mRenderer.setLegendTextSize(25);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(5);
        mRenderer.setInScroll(true);
//        Log.i(LOG_TAG, "Before getting the database content");
//        accelData = DataORM.getData(this);
//        Log.i(LOG_TAG, "Number of content in the database: " + accelData.size());
//        for (int i = 1; i < accelData.size(); i++) {
//        	Log.i(LOG_TAG, "Time: " + accelData.get(i).returnTime() + " x: " + accelData.get(i).returnX() + ", y: " + accelData.get(i).returnY() + ", z: " 
//        			+ accelData.get(i).returnZ());
//        }

        // add points from database
        mShowChart.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//				int size = mDataset.getSeriesCount();
//				for (int i = 0; i < size; i++) {
//				  // always remove the first element because once you remove one element,
//				  // the size of the list becomes size - 1 and so on
//					mDataset.removeSeries(0);
//				}
        		mRenderer.removeAllRenderers();
				mDataset.removeSeries(xSeries);
				mDataset.removeSeries(ySeries);
				mDataset.removeSeries(zSeries);
				
				accelData = DataORM.getData(getApplicationContext());
				
				// create a new renderer for the new series
				XYSeriesRenderer xRenderer = new XYSeriesRenderer();
				mRenderer.addSeriesRenderer(xRenderer);
//				Log.i(LOG_TAG, "Before X Series");
				xSeries = new XYSeries("X");
				mCurrentSeries = xSeries;
				for (int i = 0; i < accelData.size(); i++) {
					long time = accelData.get(i).returnTime();
					float accelX = accelData.get(i).returnX();
					mCurrentSeries.add(time, (double)accelX);
				}
//				Log.i(LOG_TAG, "After adding all X points to the series");
				mDataset.addSeries(xSeries);
				// set some renderer properties
//				xRenderer.setPointStyle(PointStyle.CIRCLE);
				xRenderer.setFillPoints(true);
				xRenderer.setColor(Color.BLUE);
				xRenderer.setDisplayChartValues(false);
				xRenderer.setDisplayChartValuesDistance(11);
				mCurrentRenderer = xRenderer;
				mChartView.repaint();
//				Log.i(LOG_TAG, "After painting the X series");
				
				// create a new renderer for the new series
				XYSeriesRenderer yRenderer = new XYSeriesRenderer();
				mRenderer.addSeriesRenderer(yRenderer);
//				Log.i(LOG_TAG, "Before Y Series");
				ySeries = new XYSeries("Y");
				mCurrentSeries = ySeries;
				for (int i = 0; i < accelData.size(); i++) {
					long time = accelData.get(i).returnTime();
					float accelY = accelData.get(i).returnY();
					mCurrentSeries.add(time, (double)accelY);
				}
//				Log.i(LOG_TAG, "After adding all Y points to the series");
				mDataset.addSeries(ySeries);
				// set some renderer properties
//				yRenderer.setPointStyle(PointStyle.TRIANGLE);
				yRenderer.setFillPoints(true);
				yRenderer.setColor(Color.RED);
				yRenderer.setDisplayChartValues(false);
				yRenderer.setDisplayChartValuesDistance(11);
				mCurrentRenderer = yRenderer;
				mChartView.repaint();
//				Log.i(LOG_TAG, "After painting the Y series");
				
				// create a new renderer for the new series
				XYSeriesRenderer zRenderer = new XYSeriesRenderer();
				mRenderer.addSeriesRenderer(zRenderer);
//				Log.i(LOG_TAG, "Before Z Series");
				zSeries = new XYSeries("Z");
				mDataset.addSeries(zSeries);
				mCurrentSeries = zSeries;
				for (int i = 0; i < accelData.size(); i++) {
					long time = accelData.get(i).returnTime();
					float accelZ = accelData.get(i).returnZ();
					mCurrentSeries.add(time, (double)accelZ);
				}
//				Log.i(LOG_TAG, "After adding all Z points to the series");
				// set some renderer properties
//				zRenderer.setPointStyle(PointStyle.SQUARE);
				zRenderer.setFillPoints(true);
				zRenderer.setColor(Color.GREEN);
				zRenderer.setDisplayChartValues(false);
				zRenderer.setDisplayChartValuesDistance(11);
				mCurrentRenderer = zRenderer;
				mChartView.repaint();
//				Log.i(LOG_TAG, "After painting the Z series");

                // repaint the chart such as the newly added point to be visible
				mChartView.repaint();

            }
        });
        
        mDeleteData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i(LOG_TAG, "Before deleting: " + DataORM.getData(getApplicationContext()).size());
				DataORM.deleteAllData(getApplicationContext());
				Log.i(LOG_TAG, "After deleting: " + DataORM.getData(getApplicationContext()).size());
			}
        });
    }

    @Override
    protected void onResume() {
    	super.onResume();
//    	Log.i("LATEST_DEBUGGING", "resuming yay");
    	if (mChartView == null) {
//    		Log.i("LATEST_DEBUGGING", "chartview is null"); 
    		LinearLayout layout = (LinearLayout) findViewById(R.id.chart_area);
    		mChartView = ChartFactory.getLineChartView(this.getBaseContext(), mDataset, mRenderer);
    		// enable the chart click events
//    		mRenderer.setClickEnabled(true);
//    		mRenderer.setSelectableBuffer(10);
//    		mChartView.setOnClickListener(new View.OnClickListener() {
//    			public void onClick(View v) {
//    				// handle the click event on the chart
//    				SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
//                    if (seriesSelection == null) {
//                    	Toast.makeText(XYChartBuilder.this, "No chart element", Toast.LENGTH_SHORT).show();
//                    } else {
                        // display information of the clicked point
//                        Toast.makeText(
//                        		XYChartBuilder.this,
//                        		"Chart element in series index " + seriesSelection.getSeriesIndex()
//                                + " data point index " + seriesSelection.getPointIndex() + " was clicked"
//                                + " closest point value X=" + seriesSelection.getXValue() + ", Y="
//                                + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
//            boolean enabled = mDataset.getSeriesCount() > 0;
//            setSeriesWidgetsEnabled(enabled);
        } else {
            mChartView.repaint();
        }
    }

    /**
     * Enable or disable the add data to series widgets
     * 
     * @param enabled the enabled state
     */
    // the refresh button is enabled by default via the XML now
//    private void setSeriesWidgetsEnabled(boolean enabled) {
//        mX.setEnabled(enabled);
//        mY.setEnabled(enabled);
//        mAdd.setEnabled(enabled);
//    }
}