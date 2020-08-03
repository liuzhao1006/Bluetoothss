package com.lz.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface IConnectionStatus {
    void onConnectionStatus(int status);
    void onConnectionDevice(BluetoothDevice device);
}
