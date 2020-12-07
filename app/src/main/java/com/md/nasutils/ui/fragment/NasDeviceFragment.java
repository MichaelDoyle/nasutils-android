/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.service.http.NasConfiguration;

import androidx.fragment.app.Fragment;

import static com.md.nasutils.ui.fragment.PreferenceConstants.REGEX_MAC_ADDRESS;
import static com.md.nasutils.ui.fragment.PreferenceConstants.REGEX_MAC_ADDRESS_PARTIAL;

public class NasDeviceFragment extends Fragment {

    private static final String TAG = NasDeviceFragment.class.getSimpleName();

    private EditText mName;
    private Spinner mModelName;
    private EditText mHostname;
    private EditText mPort;
    private EditText mMacAddress;
    private EditText mUsername;
    private EditText mPassword;
    private CheckBox mCheckBox;
    private EditText mSshUsername;
    private EditText mSshPort;
    private EditText mIstatPasscode;
    private EditText mIstatPort;

    private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        if (container == null) {
            return null;
        }
        
        View view = inflater.inflate(R.layout.fragment_nas_details, container, false);
        mName = (EditText) view.findViewById(R.id.edittext_server_name);
        mModelName = (Spinner) view.findViewById(R.id.spinner_model_name);
        mHostname = (EditText) view.findViewById(R.id.edittext_server_hostname);
        mPort = (EditText) view.findViewById(R.id.edittext_server_port);
        mMacAddress = (EditText) view.findViewById(R.id.edittext_server_mac);
        mUsername = (EditText) view.findViewById(R.id.edittext_server_username);
        mPassword = (EditText) view.findViewById(R.id.edittext_server_password);
        mCheckBox = (CheckBox) view .findViewById(R.id.checkbox1);
        mSshUsername = (EditText) view.findViewById(R.id.edittext_server_ssh_username);
        mSshPort = (EditText) view.findViewById(R.id.edittext_server_ssh_port);
        mIstatPasscode = (EditText) view.findViewById(R.id.edittext_server_istat_passcode);
        mIstatPort = (EditText) view.findViewById(R.id.edittext_server_istat_port);
        
        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE);
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mUri = extras.getParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE);
            fillData(mUri);
        }
        
        initMacAddressTextChangedListener();
        initCheckBoxListener();
        
        return view;
    }
    
    public void setMacAddress(String macAddress) {
        if (macAddress!= null && macAddress.matches(REGEX_MAC_ADDRESS)) {
            String mac = mMacAddress.getText().toString();
            if (TextUtils.isEmpty(macAddress)
                    || !mac.matches(REGEX_MAC_ADDRESS)) {
                mMacAddress.setText(macAddress);
            }
        }
    }
    
    public NasConfiguration getNasConfiguration() {    
        String[] firmwareVersions = getResources().getStringArray(R.array.nasModelFirmwareVersions);
        int osVersion = Integer.parseInt(firmwareVersions[mModelName.getSelectedItemPosition()]);
        String hostname = mHostname.getText().toString().trim();
        String macAddress = mMacAddress.getText().toString().trim();
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        
        int port = 443;
        if (!TextUtils.isEmpty(mPort.getText().toString())) {
            port = Integer.parseInt(mPort.getText().toString());
        }
                
        return new NasConfiguration("https", hostname, port, username, password, macAddress, osVersion);
    }
    
    private void fillData(Uri uri) {
        String[] projection = { 
                NasDeviceTable.COLUMN_NAME,
                NasDeviceTable.COLUMN_DNS_NAME,
                NasDeviceTable.COLUMN_OS_VERSION,
                NasDeviceTable.COLUMN_MAC_ADDRESS,
                NasDeviceTable.COLUMN_MODEL,
                NasDeviceTable.COLUMN_PASSWORD,
                NasDeviceTable.COLUMN_PORT,
                NasDeviceTable.COLUMN_USERNAME,
                NasDeviceTable.COLUMN_SSH_PORT,
                NasDeviceTable.COLUMN_SSH_USERNAME,
                NasDeviceTable.COLUMN_ISTAT_PORT,
                NasDeviceTable.COLUMN_ISTAT_PASSCODE,
                NasDeviceTable.COLUMN_ISTAT_PORT};
        
        Cursor cursor = getActivity().getContentResolver().query(uri,
                projection, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            String model = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MODEL));

            for (int i = 0; i < mModelName.getCount(); i++) {
                String s = (String) mModelName.getItemAtPosition(i);
                if (s.equalsIgnoreCase(model)) {
                    mModelName.setSelection(i);
                }
            }

            mName.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_NAME)));
            mHostname.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_DNS_NAME)));
            mPort.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_PORT)));
            mMacAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MAC_ADDRESS)));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_USERNAME));
            mUsername.setText(username);
            mPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_PASSWORD)));
            String sshUsername = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_SSH_USERNAME));
            mSshUsername.setText(sshUsername);
            mSshPort.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_SSH_PORT)));
            String istatPasscode = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_ISTAT_PASSCODE));
            mIstatPasscode.setText(istatPasscode);
            mIstatPort.setText(cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_ISTAT_PORT)));
            
            if (username != null && username.equals(sshUsername)) {
                mCheckBox.setChecked(true);
                mSshUsername.setEnabled(false);
            }

            cursor.close();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE, mUri);
    }

    public void saveState() {
        String name = mName.getText().toString().trim();
        String model = (String) mModelName.getSelectedItem();
        String[] firmwareVersions = getResources().getStringArray(R.array.nasModelFirmwareVersions);
        int osVersion = Integer.parseInt(firmwareVersions[mModelName.getSelectedItemPosition()]);
        String hostname = mHostname.getText().toString().trim();
        String macAddress = mMacAddress.getText().toString().trim();
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String sshUsername = mSshUsername.getText().toString().trim();
        String istatPasscode = mIstatPasscode.getText().toString().trim();
        
        int port = 443;
        if (!TextUtils.isEmpty(mPort.getText().toString())) {
            port = Integer.parseInt(mPort.getText().toString());
        }
        
        int sshPort = 22;
        if (!TextUtils.isEmpty(mSshPort.getText().toString())) {
            sshPort = Integer.parseInt(mSshPort.getText().toString());
        }
        
        int istatPort = 5109;
        if (!TextUtils.isEmpty(mIstatPort.getText().toString())) {
            istatPort = Integer.parseInt(mIstatPort.getText().toString());
        }
        
        if (name.length() == 0 || hostname.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(NasDeviceTable.COLUMN_NAME, name);
        values.put(NasDeviceTable.COLUMN_MODEL, model);
        values.put(NasDeviceTable.COLUMN_OS_VERSION, osVersion);
        values.put(NasDeviceTable.COLUMN_DNS_NAME, hostname);
        values.put(NasDeviceTable.COLUMN_PORT, port);
        values.put(NasDeviceTable.COLUMN_MAC_ADDRESS, macAddress);
        values.put(NasDeviceTable.COLUMN_USERNAME, username);
        values.put(NasDeviceTable.COLUMN_PASSWORD, password);
        values.put(NasDeviceTable.COLUMN_SSH_USERNAME, sshUsername);
        values.put(NasDeviceTable.COLUMN_SSH_PORT, sshPort);
        values.put(NasDeviceTable.COLUMN_ISTAT_PASSCODE, istatPasscode);
        values.put(NasDeviceTable.COLUMN_ISTAT_PORT, istatPort);

        if (mUri == null) {
            mUri = getActivity().getContentResolver().insert(
                    DatabaseContentProvider.CONTENT_URI_NAS_DEVICE, values);
        } else {
            getActivity().getContentResolver().update(mUri, values, null, null);
        }
    }
    
    public boolean validate() {
        mName.setError(null);
        mHostname.setError(null);
        mPort.setError(null);
        mUsername.setError(null);
        mPassword.setError(null);

        boolean isValid = true;

        if (TextUtils.isEmpty(mName.getText().toString())) {
            isValid = false;
            mName.setError(getResources().getString(R.string.error_validation_nickname));
        }

        if (TextUtils.isEmpty(mHostname.getText().toString())) {
            isValid = false;
            mHostname.setError(getResources().getString(R.string.error_validation_dns_name));
        }

        if (TextUtils.isEmpty(mPort.getText().toString())) {
            isValid = false;
            mPort.setError(getResources().getString(R.string.error_validation_port));
        }

        if (TextUtils.isEmpty(mUsername.getText().toString())) {
            isValid = false;
            mUsername.setError(getResources().getString(R.string.error_validation_username));
        }

        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            isValid = false;
            mPassword.setError(getResources().getString(R.string.error_validation_password));
        }
        
        String macAddress = mMacAddress.getText().toString();
        if (macAddress != null && !"".equals(macAddress)
                && !macAddress.matches(REGEX_MAC_ADDRESS)) {
            isValid = false;
            mMacAddress.setError(getResources().getString(R.string.error_validation_mac));
        }

        return isValid;
    }
    
    /**
     * Use a textChangedListener to keep the mac address
     * preference formatted correctly as the user types it in
     */
    private void initMacAddressTextChangedListener() {
        mMacAddress.addTextChangedListener(new TextWatcher() {

            private static final int AUTOCOMPLETE_DELAY = 200;
            private static final int MESSAGE_TEXT_CHANGED = 0;
            
            private int mColonsBefore;
            private int mColonsAfter;
            
            // we use a handler to provide a delay before reformatting
            // the mac address to allow use input to settle
            private Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == MESSAGE_TEXT_CHANGED) {
                        Editable macAddress = (Editable) msg.obj;
                        handleTextChanged(macAddress);
                    }
                }
            };

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // count the number of colons we have
                mColonsBefore = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ':') {
                        mColonsBefore++;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeMessages(MESSAGE_TEXT_CHANGED);
                final Message msg = Message.obtain(handler, MESSAGE_TEXT_CHANGED, s);
                handler.sendMessageDelayed(msg, AUTOCOMPLETE_DELAY);
            }
            
            private void handleTextChanged(Editable s) {
                mColonsAfter = 0;

                // remove listener while we make changes
                mMacAddress.removeTextChangedListener(this);

                // save cursor position
                int cursorPosition = mMacAddress.getSelectionStart();

                // format as valid mac address
                String formattedText = formatAsMacAddress(s);
                mMacAddress.setText(formattedText);

                // calculate cursor positon
                int pos = cursorPosition + (formattedText.length() - s.length());

                // account for automatic mac formatting
                if (mColonsAfter < mColonsBefore) {
                    pos += (mColonsBefore - mColonsAfter);
                }
                pos = pos <= formattedText.length() ? pos : formattedText.length();

                // reset cursor
                mMacAddress.setSelection(pos);

                // add listener back
                mMacAddress.addTextChangedListener(this);
            }

            private String formatAsMacAddress(CharSequence s) {
                String mac = s.toString();

                if (!isValidPartialMacAddress(mac)) {
                    Log.i(TAG, "Changing: " + mac);

                    // strip colons (we'll replace them later)
                    mac = mac.replaceAll(":", "");

                    // replace bad chars with zeros
                    mac = mac.replaceAll("[^A-Fa-f0-9]", "0");

                    StringBuilder stringBuilder = new StringBuilder();

                    // we may discard and/or add some characters
                    // so keep track of our "real" position
                    int counter = 0;

                    for (int i = 0; i < mac.length(); i++) {
                        counter++;
                        char currentChar = mac.charAt(i);

                        // delimit with colon every 3rd character
                        if ((counter) % 3 == 0 && currentChar != ':') {
                            counter++;
                            mColonsAfter++;
                            stringBuilder.append(":");
                        }

                        stringBuilder.append(Character.toUpperCase(currentChar));
                    }

                    int end = stringBuilder.length();
                    mac = stringBuilder.substring(0, end <= 17 ? end : 17);
                }

                return mac;
            }

            /**
             * While the user is still typing we can only 
             * validate for a partial match
             */
            boolean isValidPartialMacAddress(String s) {
                return s.length() == 0 || (s.length() <= 17 && s.toString()
                        .matches(REGEX_MAC_ADDRESS_PARTIAL));
            }
        });
    }
    
    private void initCheckBoxListener() {
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (isChecked) {
                       mSshUsername.setText(mUsername.getText());
                       mSshUsername.setEnabled(false);
                   } else {
                       mSshUsername.setEnabled(true); 
                   }
               }
        });
    }
}
