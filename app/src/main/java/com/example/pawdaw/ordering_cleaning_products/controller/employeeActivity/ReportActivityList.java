package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.adapter.PlaceAdapterCustom;
import com.example.pawdaw.ordering_cleaning_products.model.adapter.ReportAdapterCustom;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Report;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.OnCompleteTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawdaw on 15/07/17.
 */

public class ReportActivityList extends AppCompatActivity implements OnCompleteTaskListener {


    // ListView, ArrayList and adapter
    private ListView listView;
    private ArrayList<Report> reports = new ArrayList<Report>();
    public ReportAdapterCustom reportAdapterCustom;
    private Report report;

    private User userExtras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workplace_activity_list);

        //   ---- EXECUTE ASYNCTASK, PROGRESS BAR ----
        Service.getInstance().getDataFromFirebaseReportCallBack(this,this);

        userExtras = (User) getIntent().getSerializableExtra("user");

        listView = (ListView) findViewById(R.id.workPlaceList);
        reportAdapterCustom = new ReportAdapterCustom(this,R.layout.row_report,reports);
        listView.setAdapter(reportAdapterCustom);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ReportActivityList.this,DetailReportActivity.class);
                intent.putExtra("report",reports.get(position));
                startActivity(intent);
            }
        });


        // 1/3 BACK BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 2/3 BACK BUTTON
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleteAsyncTask() {

        reports = Service.getInstance().getReportByPlace(userExtras.getPlaces());
        reportAdapterCustom = new ReportAdapterCustom(this,R.layout.row_report,reports);
        listView.setAdapter(reportAdapterCustom);
        reportAdapterCustom.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
