/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.nasutils.R;
import com.md.nasutils.model.ListHeader;
import com.md.nasutils.model.Log;
import com.md.nasutils.model.Logs;
import com.md.nasutils.model.Response;
import com.md.nasutils.ui.fragment.ReadyNasLogReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasLogReceiverFragment.OnRetrieveLogs;
import com.md.nasutils.ui.widget.MultiViewAdaptable;
import com.md.nasutils.ui.widget.MultiViewArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

/**
 * Activity for displaying the ReadyNAS logs
 * 
 * @author michaeldoyle
 *
 */
public class LogsActivity extends NasUtilsFragmentActivity implements
        OnRetrieveLogs {

    @SuppressWarnings("unused")
    private static final String TAG = LogsActivity.class.getSimpleName();

    private static final String READY_NAS_LOG_RECEIVER = "ReadyNasLogReceiver";

    private MultiViewArrayAdapter mListAdapter;
    private ArrayList<MultiViewAdaptable> mBackingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // The ListView will be a combination of Log and ListHeader elements
        mBackingList = new ArrayList<>();
        mListAdapter = new MultiViewArrayAdapter(this, mBackingList);
                
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        if (fm.findFragmentById(R.id.fragment_content) == null) {
            ListFragment list = new ListFragment();
            list.setListAdapter(mListAdapter);
            list.setRetainInstance(true);
            ft.replace(R.id.fragment_content, list);
        }
        
        ReadyNasLogReceiverFragment receiver = 
                (ReadyNasLogReceiverFragment) fm.findFragmentByTag(READY_NAS_LOG_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasLogReceiverFragment();
            ft.add(receiver, READY_NAS_LOG_RECEIVER);
        }

        ft.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_logs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.menu_refresh:
            refreshLogs();
            break;
        case R.id.menu_rate:
            viewRate();
            return true;
        case R.id.menu_twitter:
            viewTwitter();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    public void refreshLogs() {
        FragmentManager fm = getSupportFragmentManager();
        
        ReadyNasLogReceiverFragment receiver = 
                (ReadyNasLogReceiverFragment) fm.findFragmentByTag(READY_NAS_LOG_RECEIVER);
        
        if (receiver != null) {
            LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.GONE);
            receiver.refreshLogs();
        }
    }
    
    @Override
    public void onFailure(Response error) {
        resetProgressBar();
        setMessage(error.getMessage());
    }
    
    @Override
    public void onSuccess(Logs logs) {
        mBackingList.clear();

        // if the first log entry is missing Date, assume that all are missing
        List<Log> logList = logs.getLogs();
        if (logList != null && logList.size() > 0 && logList.get(0).getTime() != null) {
            addLogsByDate(logs);
        } else {
            addLogs(logs);
        }
        
        mListAdapter.notifyDataSetChanged();
        
        resetProgressBar();
    }
    
    public void addLogsByDate(Logs logs) {
        Calendar calendar = new GregorianCalendar();
        Date date = new Date();
        calendar.setTime(date);
        
        int day = calendar.get(Calendar.DATE);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        
        calendar.set(Calendar.DATE,day-7);
        Date oneWeekAgo = calendar.getTime();
        
        calendar.set(Calendar.DATE,day-30);
        Date oneMonthAgo = calendar.getTime();
        
        calendar.set(Calendar.DATE,day-365);
        Date oneYearAgo = calendar.getTime();
        
        boolean dayHeader = false;
        boolean weekHeader = false;
        boolean monthHeader = false;
        boolean yearHeader = false;
        boolean allHeader = false;
        
        for (Log log : logs.getLogs()) {
            Date logDate = log.getTime();
            
            if (logDate != null) { // double check
                Calendar logCal = new GregorianCalendar();
                logCal.setTime(logDate);
            
                int logDay = logCal.get(Calendar.DATE);
                int logWeek = logCal.get(Calendar.WEEK_OF_MONTH);
                int logMonth = logCal.get(Calendar.MONTH);
                int logYear = logCal.get(Calendar.YEAR);
                
                if (year == logYear && month == logMonth && week == logWeek && day == logDay) {
                    if (!dayHeader) {
                        dayHeader = true;
                        mBackingList.add(new ListHeader(getResources().getString(R.string.header_today)));
                    }
                }
                else if (logDate.after(oneWeekAgo)) {
                    if (!weekHeader) {
                        weekHeader = true;
                        mBackingList.add(new ListHeader(getResources().getString(R.string.header_seven)));
                    }
                }
                else if (logDate.after(oneMonthAgo)) {
                    if (!monthHeader) {
                        monthHeader = true;
                        mBackingList.add(new ListHeader(getResources().getString(R.string.header_thirty)));
                    }
                }
                else if (logDate.after(oneYearAgo)) {
                    if (!yearHeader) {
                        yearHeader = true;
                        mBackingList.add(new ListHeader(getResources().getString(R.string.header_year)));
                    }
                }
                else {
                    if (!allHeader) {
                        yearHeader = true;
                        mBackingList.add(new ListHeader("> One Year Ago"));
                    }
                }
                mBackingList.add(log);
            }
        }
    }
    
    public void addLogs(Logs logs) {
        for (Log log : logs.getLogs()) {
            mBackingList.add(log);
        }
    }
    
    private void resetProgressBar() {
        LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }
    
    private void setMessage(String message) {
        TextView messageText = (TextView) findViewById(R.id.message);
        messageText.setText(message);
        messageText.setVisibility(View.VISIBLE);
    }
}
