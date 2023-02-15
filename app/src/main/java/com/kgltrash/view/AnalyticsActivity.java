package com.kgltrash.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kgltrash.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kgltrash.callback.GetReceipts;
import com.kgltrash.controller.CreateAnalytics;
import com.kgltrash.model.Payment;
import java.util.ArrayList;


/**
 * Author: Yves Byiringiro
 */
public class AnalyticsActivity extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<Payment> availableReceipts;
    private int jan = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_analytics_activity);

        CreateAnalytics createAnalytics = (CreateAnalytics) getIntent().getSerializableExtra("createAnalytics");


        createAnalytics.getPaymentReceipts(new GetReceipts() {
            @Override
            public void getReceipts(ArrayList<Payment> receipts) {
                if(receipts.size()>0)
                {
                    displayGraph(receipts);
                }else{
                    Toast.makeText(AnalyticsActivity.this, "No payments receipts found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayGraph(ArrayList<Payment> receipts){
        int jan = 0; int feb = 0; int mar = 0; int apr = 0; int may = 0; int jun = 0;
        int jul = 0; int aug = 0; int sep = 0; int oct = 0; int nov = 0; int dec = 0;

        for(Payment receipt: receipts){
            if(receipt.getMonth().equalsIgnoreCase("January"))
                jan += 1;
            if(receipt.getMonth().equalsIgnoreCase("February"))
                feb += 1;
            if(receipt.getMonth().equalsIgnoreCase("March"))
                mar += 1;
            if(receipt.getMonth().equalsIgnoreCase("April"))
                apr += 1;
            if(receipt.getMonth().equalsIgnoreCase("May"))
                may += 1;
            if(receipt.getMonth().equalsIgnoreCase("June"))
                jun += 1;
            if(receipt.getMonth().equalsIgnoreCase("July"))
                jul += 1;
            if(receipt.getMonth().equalsIgnoreCase("August"))
                aug += 1;
            if(receipt.getMonth().equalsIgnoreCase("September"))
                sep += 1;
            if(receipt.getMonth().equalsIgnoreCase("October"))
                oct += 1;
            if(receipt.getMonth().equalsIgnoreCase("November"))
                nov += 1;
            if(receipt.getMonth().equalsIgnoreCase("December"))
                dec += 1;
        }


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("January");
        xAxisLabel.add("February");
        xAxisLabel.add("March");
        xAxisLabel.add("April");
        xAxisLabel.add("May");
        xAxisLabel.add("June");
        xAxisLabel.add("July");
        xAxisLabel.add("August");
        xAxisLabel.add("September");
        xAxisLabel.add("October");
        xAxisLabel.add("November");
        xAxisLabel.add("December");

        barChart = (BarChart) findViewById(R.id.barchart);

        barChart.setDrawBarShadow(false);
//        barChart.setMaxVisibleValueCount(50);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(1, jan));
        barEntries.add(new BarEntry(2, feb));
        barEntries.add(new BarEntry(3, mar));
        barEntries.add(new BarEntry(4, apr));
        barEntries.add(new BarEntry(5, may));
        barEntries.add(new BarEntry(6, jun));
        barEntries.add(new BarEntry(7, jul));
        barEntries.add(new BarEntry(8, aug));
        barEntries.add(new BarEntry(9, sep));
        barEntries.add(new BarEntry(10, oct));
        barEntries.add(new BarEntry(11, nov));
        barEntries.add(new BarEntry(12, dec));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Months");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.8f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(((int)value) < xAxisLabel.size())
                    return xAxisLabel.get((int) value) ;
                else
                    return "0";
            }
        };
        xAxis.setValueFormatter(formatter);

        barChart.setData((data));
    }
}
