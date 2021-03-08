package com.ygha.bargraph;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;

    //Typeface tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
    //Typeface tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setTitle("BarChartActivityMultiDataset");



        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setMax(10);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        //mv.setChartView(chart); // For bounds control
        //chart.setMarker(mv); // Set the marker to the chart

        seekBarX.setProgress(4);
        seekBarY.setProgress(100);

        Legend l = chart.getLegend();
        //l.setTextSize(50);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        //l.setTypeface(tfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setXOffset(0f);
        l.setXOffset(20f);
        l.setYEntrySpace(20f);
        l.setXEntrySpace(20f);
        l.setTextSize(30f);
        l.setFormSize(30f);

        XAxis xAxis = chart.getXAxis();
        //xAxis.setTypeface(null);
        xAxis.setGranularity(3f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        xAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setEnabled(false);


        chart.getAxisRight().setEnabled(false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = seekBarX.getProgress() + 1;
        int startYear = 1980;
        int endYear = startYear + groupCount;

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();

        float randomMultiplier = seekBarY.getProgress() * 100000f;

        double[] dataimsi_1 = new double[] {76.5, 600.8, 500.12, 400.30, 600.1, 512.43, 711.32};
        double[] dataimsi_2 = new double[] {61.4, 720.4, 430.5, 640.50, 750.12, 460.11, 770.20};

        for (int i = 0; i < 7; i++) {

           values1.add(new BarEntry(i, (float) dataimsi_1[i] * 1));
           values2.add(new BarEntry(i, (float) dataimsi_2[i] * 1));
        }


        BarDataSet set1, set2, set3, set4;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "우리집");
            set1.setColor(Color.rgb(107, 210, 255));

            set2 = new BarDataSet(values2, "동평균");
            set2.setColor(Color.rgb(110, 121, 143));

            //BarData data = new BarData(set1, set2, set3, set4);
            BarData data = new BarData(set1, set2);
            String[] values = new String[] { "전기", "수도", "가스", "온수", "난"};
            LargeValueFormatter largeValueFormatter = new LargeValueFormatter();
            String[] suffix ={"", "", "", "", ""};
            largeValueFormatter.setSuffix(suffix);
            data.setValueFormatter(new LargeValueFormatter());
            //data.setValueTypeface(tfLight);


            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startYear);


        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.invalidate();
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
