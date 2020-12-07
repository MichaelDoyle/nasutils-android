/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.content.res.Resources;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.md.nasutils.R;
import com.md.nasutils.ui.activity.AddOnsActivity;
import com.md.nasutils.ui.activity.BackupsActivity;
import com.md.nasutils.ui.activity.LogsActivity;
import com.md.nasutils.ui.activity.PowerActivity;
import com.md.nasutils.ui.activity.ServicesActivity;
import com.md.nasutils.ui.activity.StatusActivity;
import com.md.nasutils.ui.activity.TelemetryActivity;

import androidx.fragment.app.ListFragment;

/**
 * Fragment that represents the main navigation menu.
 * 
 * @author michaeldoyle
 *
 */
public class MainNavigationListFragment extends ListFragment {

    @SuppressWarnings("unused")
    private static final String TAG = MainNavigationListFragment.class.getSimpleName();
    
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        
        TextView textView = (TextView) view.findViewById(R.id.list_item_text);
        String listItemText = textView.getText().toString();
        
        MainNavigation activity = (MainNavigation) getActivity();
        
        // Make callback based on the label of the menu item selected
        Resources r = getResources();
        if (r.getString(R.string.main_navigation_logs).equals(listItemText)) {
            activity.viewLogs();
        } else if (r.getString(R.string.main_navigation_status).equals(listItemText)) {
            activity.viewStatus();
        } else if (r.getString(R.string.main_navigation_power).equals(listItemText)) {
            activity.viewPower();
        } else if (r.getString(R.string.main_navigation_services).equals(listItemText)) {
            activity.viewServices();
        } else if (r.getString(R.string.main_navigation_ssh).equals(listItemText)) {
            activity.launchSsh();
        } else if (r.getString(R.string.main_navigation_ftp).equals(listItemText)) {
            activity.launchFtp();
        } else if (r.getString(R.string.main_navigation_istat).equals(listItemText)) {
            activity.viewTelemetry();
        } else if (r.getString(R.string.main_navigation_backups).equals(listItemText)) {
            activity.viewBackups();
        } else if (r.getString(R.string.main_navigation_addons).equals(listItemText)) {
            activity.viewAddOns();
        }
    }

    /**
     * Callbacks that must be defined by the main navigation
     */
    public interface MainNavigation {
        
        /**
         * Launch the {@link LogsActivity}
         */
        void viewLogs();
        
        /**
         * Launch the {@link StatusActivity}
         */
        void viewStatus();
        
        /**
         * Launch the {@link ServicesActivity}
         */
        void viewServices();
        
        /**
         * Launch the {@link PowerActivity}
         */
        void viewPower();
        
        /**
         * Launch the {@link TelemetryActivity}
         */
        void viewTelemetry();

        /**
         * Launch the {@link BackupsActivity}
         */
        void viewBackups();

        /**
         * Launch the {@link AddOnsActivity}
         */
        void viewAddOns();

        /**
         * Launch an intent for an SSH client
         */
        void launchSsh();
        
        /**
         * Launch an intent for an FTP client
         */
        void launchFtp();
    }
}
