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
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    /** Button for creating a new series of data. */
//    private Button mNewSeries;
    /** Button for adding entered data to the current series. */
    private Button mShowChart;
    /** Edit text field for entering the X value of the data to be added. */
//    private EditText mX;
    /** Edit text field for entering the Y value of the data to be added. */
//    private EditText mY;
    /** The chart view that displays the data. */
    private GraphicalView mChartView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
        outState.putSerializable("dataset", mDataset);
        Log.i("LATEST_DEBUGGING", "on save instance state reached");
        outState.putSerializable("renderer", mRenderer);
        Log.i("LATEST_DEBUGGING", "mrenderer serialized");
        outState.putSerializable("current_series", mCurrentSeries);
        Log.i("LATEST_DEBUGGING", "mcurrentseries serialized");
        outState.putSerializable("current_renderer", mCurrentRenderer);
        Log.i("LATEST_DEBUGGING", "mcurrentrenderer serialized");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        // restore the current data, for instance when changing the screen
        // orientation
        mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
        Log.i("LATEST_DEBUGGING", "on restore instance state reached");
        mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
        mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
        mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        // the top part of the UI components for adding new data points
//        mX = (EditText) findViewById(R.id.xValue);
//        mY = (EditText) findViewById(R.id.yValue);
        Log.i("LATEST_DEBUGGING", "on create reached");
        mShowChart = (Button) findViewById(R.id.show_chart);

        // set some properties on the main renderer
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setAxesColor(Color.rgb(130, 130, 230));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(5);
        mRenderer.setInScroll(true);
        Log.i("LATEST_DEBUGGING", "set properties for mRenderer");

        // the button that handles the new series of data creation
//        mNewSeries = (Button) findViewById(R.id.new_series);
//        mNewSeries.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
//                // create a new series of data
//                XYSeries series = new XYSeries(seriesTitle);
//                mDataset.addSeries(series);
//                mCurrentSeries = series;
//                // create a new renderer for the new series
//                XYSeriesRenderer renderer = new XYSeriesRenderer();
//                mRenderer.addSeriesRenderer(renderer);
//                // set some renderer properties
//                renderer.setPointStyle(PointStyle.CIRCLE);
//                renderer.setFillPoints(true);
//                renderer.setDisplayChartValues(true);
//                renderer.setDisplayChartValuesDistance(10);
//                mCurrentRenderer = renderer;
//                setSeriesWidgetsEnabled(true);
//                mChartView.repaint();
//            }
//        });

        // add points at x & y - replace later with adding points from database
        mShowChart.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Log.i("LATEST_DEBUGGING", "on set click listener started");
                    double x = 0;
                    double y = 0;
//                    XYSeries newSeries = new XYSeries("Example");
//                    mDataset.addSeries(newSeries);
//                    mCurrentSeries = newSeries;
//                    XYSeriesRenderer renderer = new XYSeriesRenderer();
//                    mRenderer.addSeriesRenderer(renderer);
                    // set some renderer properties
//                    renderer.setPointStyle(PointStyle.CIRCLE);
//                    renderer.setFillPoints(true);
//                    renderer.setDisplayChartValues(true);
//                    renderer.setDisplayChartValuesDistance(5);
//                    mCurrentRenderer = renderer;                    
                    String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
                    // create a new series of data
                    XYSeries series = new XYSeries(seriesTitle);
                    mDataset.addSeries(series);
                    mCurrentSeries = series;
                    // create a new renderer for the new series
                    XYSeriesRenderer renderer = new XYSeriesRenderer();
                    mRenderer.addSeriesRenderer(renderer);
                    // set some renderer properties
                    renderer.setPointStyle(PointStyle.CIRCLE);
                    renderer.setFillPoints(true);
                    renderer.setDisplayChartValues(true);
                    renderer.setDisplayChartValuesDistance(10);
                    mCurrentRenderer = renderer;
                    mChartView.repaint();
                    for (int i = 0; i < 25; i++) {
                	    x += .11;
                	    y += .11;
                    	mCurrentSeries.add(x, y);
                    	Log.i("LATEST_DEBUGGING", "" + x + " and " + y +" plotted");
                    }
                    
                // repaint the chart such as the newly added point to be visible
                    Log.i("LATEST_DEBUGGING", "Before repainting");
                    mChartView.repaint();
                    Log.i("LATEST_DEBUGGING", "After repainting");
				List<DataStorageClass> dsc = DataORM.getData(getBaseContext());
				int length = dsc.size();
				Toast toast = Toast.makeText(getBaseContext(), Integer.toString(length), Toast.LENGTH_SHORT);
				toast.show();
            }
        });
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i("LATEST_DEBUGGING", "resuming yay");
    	if (mChartView == null) {
    		Log.i("LATEST_DEBUGGING", "chartview is null"); 
    		LinearLayout layout = (LinearLayout) findViewById(R.id.chart_area);
    		mChartView = ChartFactory.getLineChartView(this.getBaseContext(), mDataset, mRenderer);
    		// enable the chart click events
    		mRenderer.setClickEnabled(true);
    		mRenderer.setSelectableBuffer(10);
    		mChartView.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				// handle the click event on the chart
    				SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                    	Toast.makeText(XYChartBuilder.this, "No chart element", Toast.LENGTH_SHORT).show();
                    } else {
                        // display information of the clicked point
                        Toast.makeText(
                        		XYChartBuilder.this,
                        		"Chart element in series index " + seriesSelection.getSeriesIndex()
                                + " data point index " + seriesSelection.getPointIndex() + " was clicked"
                                + " closest point value X=" + seriesSelection.getXValue() + ", Y="
                                + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
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