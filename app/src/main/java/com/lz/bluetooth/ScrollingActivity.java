package com.lz.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ScrollingActivity extends AppCompatActivity implements IConnectionStatus{
    private static final String TAG = "ScrollingActivity";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    
    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    /**
     * ServiceDiscoveryServerServiceClassID_UUID = '{00001000-0000-1000-8000-00805F9B34FB}'
     * BrowseGroupDescriptorServiceClassID_UUID = '{00001001-0000-1000-8000-00805F9B34FB}'
     * PublicBrowseGroupServiceClass_UUID = '{00001002-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #蓝牙串口服务
     * SerialPortServiceClass_UUID = '{00001101-0000-1000-8000-00805F9B34FB}'
     * <p>
     * LANAccessUsingPPPServiceClass_UUID = '{00001102-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #拨号网络服务
     * DialupNetworkingServiceClass_UUID = '{00001103-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #信息同步服务
     * IrMCSyncServiceClass_UUID = '{00001104-0000-1000-8000-00805F9B34FB}'
     * <p>
     * SDP_OBEXObjectPushServiceClass_UUID = '{00001105-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #文件传输服务
     * OBEXFileTransferServiceClass_UUID = '{00001106-0000-1000-8000-00805F9B34FB}'
     * <p>
     * IrMCSyncCommandServiceClass_UUID = '{00001107-0000-1000-8000-00805F9B34FB}'
     * SDP_HeadsetServiceClass_UUID = '{00001108-0000-1000-8000-00805F9B34FB}'
     * CordlessTelephonyServiceClass_UUID = '{00001109-0000-1000-8000-00805F9B34FB}'
     * SDP_AudioSourceServiceClass_UUID = '{0000110A-0000-1000-8000-00805F9B34FB}'
     * SDP_AudioSinkServiceClass_UUID = '{0000110B-0000-1000-8000-00805F9B34FB}'
     * SDP_AVRemoteControlTargetServiceClass_UUID = '{0000110C-0000-1000-8000-00805F9B34FB}'
     * SDP_AdvancedAudioDistributionServiceClass_UUID = '{0000110D-0000-1000-8000-00805F9B34FB}'
     * SDP_AVRemoteControlServiceClass_UUID = '{0000110E-0000-1000-8000-00805F9B34FB}'
     * VideoConferencingServiceClass_UUID = '{0000110F-0000-1000-8000-00805F9B34FB}'
     * IntercomServiceClass_UUID = '{00001110-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #蓝牙传真服务
     * FaxServiceClass_UUID = '{00001111-0000-1000-8000-00805F9B34FB}'
     * <p>
     * HeadsetAudioGatewayServiceClass_UUID = '{00001112-0000-1000-8000-00805F9B34FB}'
     * WAPServiceClass_UUID = '{00001113-0000-1000-8000-00805F9B34FB}'
     * WAPClientServiceClass_UUID = '{00001114-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #个人局域网服务
     * PANUServiceClass_UUID = '{00001115-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #个人局域网服务
     * NAPServiceClass_UUID = '{00001116-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #个人局域网服务
     * GNServiceClass_UUID = '{00001117-0000-1000-8000-00805F9B34FB}'
     * <p>
     * DirectPrintingServiceClass_UUID = '{00001118-0000-1000-8000-00805F9B34FB}'
     * ReferencePrintingServiceClass_UUID = '{00001119-0000-1000-8000-00805F9B34FB}'
     * ImagingServiceClass_UUID = '{0000111A-0000-1000-8000-00805F9B34FB}'
     * ImagingResponderServiceClass_UUID = '{0000111B-0000-1000-8000-00805F9B34FB}'
     * ImagingAutomaticArchiveServiceClass_UUID = '{0000111C-0000-1000-8000-00805F9B34FB}'
     * ImagingReferenceObjectsServiceClass_UUID = '{0000111D-0000-1000-8000-00805F9B34FB}'
     * SDP_HandsfreeServiceClass_UUID = '{0000111E-0000-1000-8000-00805F9B34FB}'
     * HandsfreeAudioGatewayServiceClass_UUID = '{0000111F-0000-1000-8000-00805F9B34FB}'
     * DirectPrintingReferenceObjectsServiceClass_UUID = '{00001120-0000-1000-8000-00805F9B34FB}'
     * ReflectedUIServiceClass_UUID = '{00001121-0000-1000-8000-00805F9B34FB}'
     * BasicPringingServiceClass_UUID = '{00001122-0000-1000-8000-00805F9B34FB}'
     * PrintingStatusServiceClass_UUID = '{00001123-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #人机输入服务
     * HumanInterfaceDeviceServiceClass_UUID = '{00001124-0000-1000-8000-00805F9B34FB}'
     * <p>
     * HardcopyCableReplacementServiceClass_UUID = '{00001125-0000-1000-8000-00805F9B34FB}'
     * <p>
     * #蓝牙打印服务
     * HCRPrintServiceClass_UUID = '{00001126-0000-1000-8000-00805F9B34FB}'
     * <p>
     * HCRScanServiceClass_UUID = '{00001127-0000-1000-8000-00805F9B34FB}'
     * CommonISDNAccessServiceClass_UUID = '{00001128-0000-1000-8000-00805F9B34FB}'
     * VideoConferencingGWServiceClass_UUID = '{00001129-0000-1000-8000-00805F9B34FB}'
     * UDIMTServiceClass_UUID = '{0000112A-0000-1000-8000-00805F9B34FB}'
     * UDITAServiceClass_UUID = '{0000112B-0000-1000-8000-00805F9B34FB}'
     * AudioVideoServiceClass_UUID = '{0000112C-0000-1000-8000-00805F9B34FB}'
     * SIMAccessServiceClass_UUID = '{0000112D-0000-1000-8000-00805F9B34FB}'
     * PnPInformationServiceClass_UUID = '{00001200-0000-1000-8000-00805F9B34FB}'
     * GenericNetworkingServiceClass_UUID = '{00001201-0000-1000-8000-00805F9B34FB}'
     * GenericFileTransferServiceClass_UUID = '{00001202-0000-1000-8000-00805F9B34FB}'
     * GenericAudioServiceClass_UUID = '{00001203-0000-1000-8000-00805F9B34FB}'
     * GenericTelephonyServiceClass_UUID = '{00001204-0000-1000-8000-00805F9B34FB}'
     */
    private static final String BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    
    private BluetoothAdapter bluetoothAdapter;
    private TextView tv;
    private StringBuilder sb = new StringBuilder();
    
    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;
    
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    
    private IConnectionStatus mConnectionStatus;
    
    public IConnectionStatus getConnectionStatus() {
        return mConnectionStatus;
    }
    
    public void setConnectionStatus(IConnectionStatus connectionStatus) {
        mConnectionStatus = connectionStatus;
    }
    
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    sb.append("receiver:\n");
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress();
                    sb.append(deviceName).append(":").append(deviceHardwareAddress).append("\n");
                    tv.setText(sb.toString());
                    bluetoothDeviceList.add(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 扫描完成
            }
        }
    };
    private ConnectThread connectThread;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setConnectionStatus(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBluetooth();
            }
        });
        tv = findViewById(R.id.tv);
        
        // 注册发现设备
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BluetoothDevice device : bluetoothDeviceList) {
                    if ("APM".equalsIgnoreCase(device.getName())) {
                        if (bluetoothAdapter != null) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                        if (connectThread == null) {
                            connectThread = new ConnectThread(device);
                            connectThread.start();
                        }
                        return;
                    }
                }
            }
        });
    
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }
    
    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mChatService = new BluetoothChatService(this, this);
    }
    
    /**
     * 查看蓝牙开关是否开启
     */
    public void initBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            getDeviceInfo();
        }
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                    Log.i(TAG, " When DeviceListActivity returns with a device to connect");
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                    Log.i(TAG, " When DeviceListActivity returns with a device to connect");
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                    Log.i(TAG, " Bluetooth is now enabled, so set up a chat session");
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.i(TAG, "BT not enabled");
                }
        }
    }
    
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
    
    
    public void getDeviceInfo() {
        if (bluetoothAdapter == null) {
            return;
        }
        
        // 获取已配对的设备
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                sb.append(deviceName).append(":").append(deviceHardwareAddress).append("\n");
                bluetoothDeviceList.add(device);
            }
            tv.setText(sb.toString());
        }
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        
        // 扫描设备
        bluetoothAdapter.startDiscovery();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    
    @Override
    public void onConnectionStatus(int status) {
    
    }
    
    @Override
    public void onConnectionDevice(BluetoothDevice device) {
    
    }
    
    private class ConnectThread extends Thread {
        private final BluetoothSocket mSocket;
        private final BluetoothDevice mDevice;
        private byte[] mmBuffer;
        
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(BLUETOOTH_UUID));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mSocket = tmp;
        }
        
        public void run() {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            
            try {
                mSocket.connect();
                if (mSocket.isConnected()) {
                    
                    InputStream tmpIn = null;
                    try {
                        tmpIn = mSocket.getInputStream();
                        mmBuffer = new byte[1024];
                        int numBytes; // bytes returned from read()
                        
                        // Keep listening to the InputStream until an exception occurs.
                        while (true) {
                            try {
                                // Read from the InputStream.
                                numBytes = tmpIn.read(mmBuffer);
                                Log.i(TAG, "read:" + String.valueOf(numBytes));
                            } catch (IOException e) {
                                Log.d(TAG, "Input stream was disconnected", e);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error occurred when creating input stream", e);
                    }
                }
            } catch (IOException connectException) {
                try {
                    mSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }
        }
        
        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}
