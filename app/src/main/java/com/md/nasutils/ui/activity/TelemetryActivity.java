/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.Telemetry;
import com.md.nasutils.model.TelemetryCpu;
import com.md.nasutils.model.TelemetryNetwork;
import com.md.nasutils.ui.fragment.ReadyNasTelemetryReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasTelemetryReceiverFragment.OnRetrieveTelemetry;
import com.md.nasutils.ui.fragment.TelemetryFragment;
import com.md.nasutils.ui.fragment.TelemetryFragment.OnTelemetryFragmentResume;
import com.md.nasutils.util.ScreenUtils;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity for enabling/disabling ReadyNAS services
 * 
 * @author michaeldoyle
 *
 */
public class TelemetryActivity extends NasUtilsFragmentActivity implements
        OnRetrieveTelemetry, OnTelemetryFragmentResume {

    private static final String TAG = TelemetryActivity.class.getSimpleName();

    private static final String READY_NAS_TELEMETRY_RECEIVER = "ReadyNasTelemetryReceiver";
    
    private Telemetry mTelemetry;
    private TelemetryNetwork mTelemetryNetwork;
    private Response mError;
    private TelemetryFragment mTelemetryFragment;
    private volatile boolean refreshStarted = false;
    private volatile boolean updateInProgress = true;
    
    // Views
    private RelativeLayout mNasRelativeLayout;
    private RelativeLayout mMemRelativeLayout;
    private RelativeLayout mCpuRelativeLayout;  
    private RelativeLayout mNetRelativeLayout;
    private LinearLayout mMemChartLayout;
    private LinearLayout mCpuChartLayout;
    private LinearLayout mNetChartLayout;
    
    // CPU Chart
    private GraphicalView mCpuChart;
    private XYMultipleSeriesDataset mCpuDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mCpuRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCpuSystemSeries = new XYSeries("System");
    private XYSeries mCpuUserSeries = new XYSeries("User");
    private XYSeries mCpuNiceSeries = new XYSeries("Nice");
    private XYSeriesRenderer mCpuSystemRenderer = new XYSeriesRenderer();
    private XYSeriesRenderer mCpuUserRenderer = new XYSeriesRenderer(); 
    private XYSeriesRenderer mCpuNiceRenderer = new XYSeriesRenderer();
    
    // Network Chart
    private GraphicalView mNetChart;
    private XYMultipleSeriesDataset mNetDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mNetRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mNetUploadSeries = new XYSeries("Upload");
    private XYSeries mNetDownloadSeries = new XYSeries("Download");
    private XYSeriesRenderer mNetUploadRenderer = new XYSeriesRenderer();
    private XYSeriesRenderer mNetDownloadRenderer = new XYSeriesRenderer();
    
    // Memory Chart
    private GraphicalView mMemChart;
    private DefaultRenderer mMemRenderer = new DefaultRenderer();
    private CategorySeries mMemDataset = new CategorySeries("Memory");
    
    private Handler mHandler = new Handler();
    
    private Runnable mRunnable = new Runnable() {
           @Override
           public void run() {
              if (!updateInProgress) {
                  refreshTelemetry();
              }
              mHandler.postDelayed(this, 3000);
           }
        };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        mTelemetryFragment = new TelemetryFragment();
        ft.replace(R.id.fragment_content, mTelemetryFragment);
        mTelemetryFragment.setRetainInstance(true);
        
        ReadyNasTelemetryReceiverFragment receiver = 
                (ReadyNasTelemetryReceiverFragment) fm.findFragmentByTag(READY_NAS_TELEMETRY_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasTelemetryReceiverFragment();
            ft.add(receiver, READY_NAS_TELEMETRY_RECEIVER);
        }
                
        ft.commit();  
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_telemetry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    public void refreshTelemetry() {
        FragmentManager fm = getSupportFragmentManager();
        
        ReadyNasTelemetryReceiverFragment receiver = 
                (ReadyNasTelemetryReceiverFragment) fm.findFragmentByTag(READY_NAS_TELEMETRY_RECEIVER);
        
        if (receiver != null) {
            long uptime = mTelemetry != null ? mTelemetry.getUptime() : 0;
            Log.d(TAG, "Incremental refresh for: " + uptime);
            receiver.refreshDetails(uptime);
        }
    }
        
    @Override
    public void onFailure(Response error) {
        if (!refreshStarted) {
            setMessage(View.VISIBLE, error.getMessage());
            setProgressBar(View.GONE);
            if (error.isFailedValidation()) {
                promptForPasscode();
            }
        } else {
            showWarning();
        }
    }
    
    @Override
    public void onSuccess(Telemetry telemetry) {
        mTelemetry = telemetry;
        onTelemetryFragmentResume();
        setProgressBar(View.GONE);
    }
    
    @Override
    public void onTelemetryFragmentResume() {
        if (mTelemetry != null) {
            if (mTelemetry.isAuthorized()) {
                setTelemetry();
                setMessage(View.GONE, null);
            } else {
                promptForPasscode();
                setMessage(View.VISIBLE, getResources().getString(R.string.error_istat));
            }
            setProgressBar(View.GONE);
        } else if (mError != null) {
            setProgressBar(View.GONE);
            setMessage(View.VISIBLE, mError.getMessage());
        } else {
            FragmentManager fm = getSupportFragmentManager();
            ReadyNasTelemetryReceiverFragment receiver = 
                    (ReadyNasTelemetryReceiverFragment) fm.findFragmentByTag(READY_NAS_TELEMETRY_RECEIVER);
            
            if (receiver != null) {
                if (mTelemetry == null) {
                    receiver.refreshDetails(-2);
                } else {
                    receiver.refreshDetails(mTelemetry.getUptime());
                }
            }
        }
    }
    
    private void setTelemetry() {
        if (mTelemetryFragment.isAdded() && mTelemetryFragment.getView() != null) {
            
            LinearLayout view = (LinearLayout) mTelemetryFragment.getView().findViewById(R.id.fragment_content);
            view.setVisibility(View.VISIBLE);

            TextView warningText = (TextView) mTelemetryFragment.getView().findViewById(R.id.warning);
            warningText.setVisibility(View.GONE);
            
            if (mNasRelativeLayout == null) {
                mNasRelativeLayout = (RelativeLayout) view.findViewById(R.id.list_item_telemetry_nas);
                setMaxWidth(mNasRelativeLayout);
                mMemRelativeLayout = (RelativeLayout) view.findViewById(R.id.list_item_telemetry_memory);
                setMaxWidth(mMemRelativeLayout);
                mCpuRelativeLayout = (RelativeLayout) view.findViewById(R.id.list_item_telemetry_cpu);      
                setMaxWidth(mCpuRelativeLayout);
                mNetRelativeLayout = (RelativeLayout) view.findViewById(R.id.list_item_telemetry_network);
                setMaxWidth(mNetRelativeLayout);
                
                mMemChartLayout = (LinearLayout) mMemRelativeLayout.findViewById(R.id.chart_memory);
                mCpuChartLayout = (LinearLayout) mCpuRelativeLayout.findViewById(R.id.chart_cpu);
                mNetChartLayout = (LinearLayout) mNetRelativeLayout.findViewById(R.id.chart_network);
            }
            
            refreshTextView(mNasRelativeLayout, R.id.uptime_value, mTelemetry.getDisplayUptime());
            refreshTextView(mNasRelativeLayout, R.id.load_value, mTelemetry.getLoad().getDisplayAverages());
            
            setMemory();
            setCpu();
            setNetwork();
            
            mTelemetryNetwork = mTelemetry.getNetworkTelemetry().get(
                    mTelemetry.getNetworkTelemetry().size() - 1);
            
            updateInProgress = false;

            if (!refreshStarted) {
                refreshStarted = true;
                mHandler.postDelayed(mRunnable, 3000);
            }
        }
    }
    
    private void setMemory() {
        
        if (mMemDataset.getItemCount() == 0) {
            mMemDataset.add("Wired", mTelemetry.getMemory().getWired().longValue());
            mMemDataset.add("Active",  mTelemetry.getMemory().getActive().longValue());
            mMemDataset.add("Inactive", mTelemetry.getMemory().getInactive().longValue());
            mMemDataset.add("Free", mTelemetry.getMemory().getFree().longValue());

            int colorWired = Color.parseColor("#0000FF");
            int colorActive = Color.parseColor("#910DFF");
            int colorInactive = Color.GRAY;
            int colorFree = Color.DKGRAY;
            int[] colors = new int[] { colorWired, colorActive, colorInactive, colorFree }; 
            for (int color : colors) {
                SimpleSeriesRenderer simpleRenderer = new SimpleSeriesRenderer();
                simpleRenderer.setColor(color);
                mMemRenderer.addSeriesRenderer(simpleRenderer);
            }
            
            mMemRenderer.setMargins(new int[] { 0, 0, 0, 0});
            mMemRenderer.setShowLabels(false);
            mMemRenderer.setShowLegend(false);
            mMemRenderer.setShowLegend(false);
            mMemRenderer.setZoomEnabled(false);
            mMemRenderer.setPanEnabled(false);
            mMemRenderer.setClickEnabled(false);
            mMemRenderer.setBackgroundColor(Color.WHITE);
        } else {
            mMemDataset.set(0, "Wired", mTelemetry.getMemory().getWired().longValue());
            mMemDataset.set(1, "Active",  mTelemetry.getMemory().getActive().longValue());
            mMemDataset.set(2, "Inactive", mTelemetry.getMemory().getInactive().longValue());
            mMemDataset.set(3, "Free", mTelemetry.getMemory().getFree().longValue());
        }
        
        if (mMemChart == null) {
            mMemChart = ChartFactory.getPieChartView(this, mMemDataset, mMemRenderer);
            mMemChartLayout.addView(mMemChart);
        } else {
            mMemChart.repaint();
        }
        
        refreshTextView(mMemRelativeLayout, R.id.wired_value, "" + mTelemetry.getMemory().getWired() + " MB");
        refreshTextView(mMemRelativeLayout, R.id.active_value, "" + mTelemetry.getMemory().getActive() + " MB");
        refreshTextView(mMemRelativeLayout, R.id.inactive_value, "" + mTelemetry.getMemory().getInactive() + " MB");
        refreshTextView(mMemRelativeLayout, R.id.free_value, "" + mTelemetry.getMemory().getFree() + " MB");
        refreshTextView(mMemRelativeLayout, R.id.page_in_value, "" + mTelemetry.getMemory().getPageIns());
        refreshTextView(mMemRelativeLayout, R.id.page_out_value, "" + mTelemetry.getMemory().getPageOuts());
        refreshTextView(mMemRelativeLayout, R.id.swap_total_value, "" + mTelemetry.getMemory().getSwapTotal() + " MB");
        refreshTextView(mMemRelativeLayout, R.id.swap_used_value, "" + mTelemetry.getMemory().getSwapUsed() + " MB");
    }
    
    private void setCpu() {
        int newCount = mTelemetry.getCpuTelemetry().size();
        
        for (TelemetryCpu c : mTelemetry.getCpuTelemetry()) {
            mCpuNiceSeries.add(c.getId().longValue(), c.getNice() + c.getUser() + c.getSystem());
            mCpuUserSeries.add(c.getId().longValue(), c.getUser() + c.getSystem());
            mCpuSystemSeries.add(c.getId().longValue(), c.getSystem());
        }
        
        // keep the number of elements at 299
        for (int i=mCpuSystemSeries.getItemCount(); i>299; i--) {
            mCpuNiceSeries.remove(0);
            mCpuUserSeries.remove(0);
            mCpuSystemSeries.remove(0);
        }
        
        // initial setup only
        if (mCpuDataset.getSeriesCount() == 0) {
            mCpuDataset.addSeries(mCpuNiceSeries);
            mCpuDataset.addSeries(mCpuUserSeries);
            mCpuDataset.addSeries(mCpuSystemSeries);
                        
            int colorNice = Color.GRAY;
            mCpuNiceRenderer.setColor(colorNice);
            FillOutsideLine cpuNiceFill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
            cpuNiceFill.setColor(colorNice);
            mCpuNiceRenderer.addFillOutsideLine(cpuNiceFill);
            mCpuRenderer.addSeriesRenderer(mCpuNiceRenderer);

            int colorUser = Color.parseColor("#0000FF");
            mCpuUserRenderer.setColor(colorUser);
            FillOutsideLine cpuUserFill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
            cpuUserFill.setColor(colorUser);
            mCpuUserRenderer.addFillOutsideLine(cpuUserFill);
            mCpuRenderer.addSeriesRenderer(mCpuUserRenderer);
            
            int colorSystem = Color.parseColor("#910DFF");
            mCpuSystemRenderer.setColor(colorSystem);
            FillOutsideLine cpuSystemFill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
            cpuSystemFill.setColor(colorSystem);
            mCpuSystemRenderer.addFillOutsideLine(cpuSystemFill);
            mCpuRenderer.addSeriesRenderer(mCpuSystemRenderer);
            
            int dip10 = ScreenUtils.dipToPx(this, 10);
            mCpuRenderer.setLabelsTextSize(dip10);
            mCpuRenderer.setMargins(new int[] { dip10, 0, 0, 0 });
            mCpuRenderer.setMarginsColor(Color.WHITE);
            mCpuRenderer.setShowLegend(false);
            mCpuRenderer.setZoomEnabled(false, false);
            mCpuRenderer.setPanEnabled(false, false);
            mCpuRenderer.setClickEnabled(false);
            mCpuRenderer.setBackgroundColor(Color.WHITE);
            mCpuRenderer.setGridColor(Color.GRAY);
            mCpuRenderer.setAntialiasing(true);
            mCpuRenderer.setShowTickMarks(true);
            mCpuRenderer.setShowLabels(true);
            mCpuRenderer.setShowAxes(false);
            mCpuRenderer.setShowGrid(true);
            mCpuRenderer.setShowGridY(false);
            mCpuRenderer.setShowGridX(true);
            mCpuRenderer.setXLabels(0);
            mCpuRenderer.setYLabelsAlign(Align.LEFT);           
        }
                
        mCpuRenderer.setXAxisMin(mCpuSystemSeries.getMinX());
        mCpuRenderer.setXAxisMax(mCpuSystemSeries.getMaxX());

        if (mCpuChart == null) {
            mCpuChart = ChartFactory.getLineChartView(this, mCpuDataset, mCpuRenderer);
            mCpuChartLayout.addView(mCpuChart);
        } else {
            mCpuChart.repaint();
        }
        
        refreshTextView(
                mCpuRelativeLayout,
                R.id.user_value,
                mTelemetry.getCpuTelemetry().get(newCount - 1).getUser() + "%");
        refreshTextView(
                mCpuRelativeLayout,
                R.id.system_value,
                "" + mTelemetry.getCpuTelemetry().get(newCount - 1).getSystem() + "%");
        refreshTextView(
                mCpuRelativeLayout,
                R.id.nice_value,
                "" + mTelemetry.getCpuTelemetry().get(newCount - 1).getNice() + "%");
        refreshTextView(
                mCpuRelativeLayout,
                R.id.idle_value,
                "" + mTelemetry.getCpuTelemetry().get(newCount - 1).getIdle() + "%");
    }
    
    private void setNetwork() {
        
        int newCount = mTelemetry.getNetworkTelemetry().size();
        
        if (mTelemetryNetwork != null) {
            TelemetryNetwork n1 = mTelemetryNetwork;
            TelemetryNetwork n2 = mTelemetry.getNetworkTelemetry().get(0);
            double uploadKbs = n2.getUploadKb().subtract(n1.getUploadKb()).doubleValue() / 
                    (n2.getTime().getTime() - n1.getTime().getTime());
            double downloadKbs = n2.getDownloadKb().subtract(n1.getDownloadKb()).doubleValue() / 
                    (n2.getTime().getTime() - n1.getTime().getTime());
            mNetUploadSeries.add(n2.getId().longValue(), uploadKbs);
            mNetDownloadSeries.add(n2.getId().longValue(), downloadKbs);
        }
        
        for (int i=1; i<newCount; i++) {
            TelemetryNetwork n1 = mTelemetry.getNetworkTelemetry().get(i-1);
            TelemetryNetwork n2 = mTelemetry.getNetworkTelemetry().get(i);
            double uploadKbs = n2.getUploadKb().subtract(n1.getUploadKb()).doubleValue() / 
                    (n2.getTime().getTime() - n1.getTime().getTime());
            double downloadKbs = n2.getDownloadKb().subtract(n1.getDownloadKb()).doubleValue() / 
                    (n2.getTime().getTime() - n1.getTime().getTime());
            mNetUploadSeries.add(n2.getId().longValue(), uploadKbs);
            mNetDownloadSeries.add(n2.getId().longValue(), downloadKbs);
        }
        
        // keep the number of elements at 299
        for (int i=mNetUploadSeries.getItemCount(); i>299; i--) {
            mNetUploadSeries.remove(0);
            mNetDownloadSeries.remove(0);
        }
        
        // initial set up only
        if (mNetDataset.getSeriesCount() == 0) {
            mNetDataset.addSeries(mNetUploadSeries);
            mNetDataset.addSeries(mNetDownloadSeries);

            mNetUploadRenderer.setColor(Color.parseColor("#0000FF"));
            mNetRenderer.addSeriesRenderer(mNetUploadRenderer);
            
            mNetDownloadRenderer.setColor(Color.parseColor("#910DFF"));
            mNetRenderer.addSeriesRenderer(mNetDownloadRenderer);
            
            int dip10 = ScreenUtils.dipToPx(this, 10);
            mNetRenderer.setLabelsTextSize(dip10);
            mNetRenderer.setMargins(new int[] { dip10, 0, 0, 0 });          
            mNetRenderer.setMarginsColor(Color.WHITE);
            mNetRenderer.setShowLegend(false);
            mNetRenderer.setZoomEnabled(false, false);
            mNetRenderer.setPanEnabled(false, false);
            mNetRenderer.setClickEnabled(false);
            mNetRenderer.setBackgroundColor(Color.WHITE);
            mNetRenderer.setGridColor(Color.GRAY);
            mNetRenderer.setAntialiasing(true);
            mNetRenderer.setShowTickMarks(true);
            mNetRenderer.setShowLabels(true);
            mNetRenderer.setShowAxes(false);
            mNetRenderer.setShowGrid(true);
            mNetRenderer.setShowGridY(false);
            mNetRenderer.setShowGridX(true);
            mNetRenderer.setXLabels(0);
            mNetRenderer.setYLabelsAlign(Align.LEFT);
            
        }
        
        mNetRenderer.setXAxisMin(mNetUploadSeries.getMinX());
        mNetRenderer.setXAxisMax(mNetUploadSeries.getMaxX());
        
        if (mNetChart == null) {
            mNetChart = ChartFactory.getLineChartView(this, mNetDataset, mNetRenderer);
            mNetChartLayout.addView(mNetChart);
        } else {
            mNetChart.repaint();
        }

        int end = mNetUploadSeries.getItemCount() -1;
        refreshTextView(
                mNetRelativeLayout,
                R.id.upload_value,
                String.format("%.2f", mNetUploadSeries.getXYMap().getByIndex(end)
                        .getValue().doubleValue()) + " KB/s");
        refreshTextView(
                mNetRelativeLayout,
                R.id.download_value,
                String.format("%.2f", mNetDownloadSeries.getXYMap().getByIndex(end)
                        .getValue().doubleValue())  + " KB/s");
    }
        
    private void setProgressBar(int state) {
        LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);

        // ensure fragment is still visible
        if (progressBar != null) {
            progressBar.setVisibility(state);
        }
    }
    
    private void setMessage(int state, String message) {
        TextView messageText = (TextView) findViewById(R.id.message);

        // ensure fragment is still visible
        if (messageText != null) {
            messageText.setText(message);
            messageText.setVisibility(state);
        }
    }
    
    private void showWarning() {
        TextView warningText = (TextView) findViewById(R.id.warning);
        
        // ensure fragment is still visible
        if (warningText != null) {
            warningText.setVisibility(View.VISIBLE);
        }
    }
    
    private void promptForPasscode() {  
        View dialogContent = getLayoutInflater().inflate(R.layout.dialog_telemetry_passode, null);
        final EditText editText = (EditText) dialogContent.findViewById(R.id.passcode);
        String positiveLabel = getResources().getString(R.string.button_ok);
        String negativeLabel = getResources().getString(R.string.dialog_negative_button_label_restart);
        
        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setPasscode(editText.getText().toString());
                setProgressBar(View.VISIBLE);
                setMessage(View.GONE, null);
                refreshTelemetry();
            }
        };
        
        DialogInterface.OnClickListener negativeOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton(positiveLabel, positiveOnClickListener)
                .setNegativeButton(negativeLabel, negativeOnClickListener)
                .setTitle(getResources().getString(R.string.dialog_title_auth_failed))
                .setIcon(R.drawable.ic_log_warning).setView(dialogContent).create();
        
        if (!isFinishing()) {
            alertDialog.show();
        }
    }
    
    private void setPasscode(String passcode) {
        ContentValues values = new ContentValues();
        values.put(NasDeviceTable.COLUMN_ISTAT_PASSCODE, passcode);

        if (mUri == null) {
            mUri = getContentResolver().insert(
                    DatabaseContentProvider.CONTENT_URI_NAS_DEVICE, values);
        } else {
            getContentResolver().update(mUri, values, null, null);
        }           
    }
}
