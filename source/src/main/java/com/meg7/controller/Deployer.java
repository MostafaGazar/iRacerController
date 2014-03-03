/*
 * Copyright 2014 Mostafa Gazar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meg7.controller;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import com.meg7.controller.data.Move;
import com.meg7.controller.utils.BLog;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * i-racer deployer, check supported commands
 * at http://dlnmh9ip6v2uc.cloudfront.net/datasheets/Robotics/DaguCarCommands.pdf
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class Deployer {

    private static final String TAG = Deployer.class.getSimpleName();

    public static enum Result { OK, BLUETOOTH_DISABLED, BLUETOOTH_UNAVAILABLE, DEVICE_NOT_CONNECTED }

    // http://stackoverflow.com/questions/18378340/bluetooth-service-discovery-failed
    private static final UUID BLUETOOTH_SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static Result deploy(Context context, final List<Move> commands) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Device does not support Bluetooth.
        if (mBluetoothAdapter == null) {
            return Result.BLUETOOTH_UNAVAILABLE;
        }

        // Bluetooth disabled, enable it first.
        if (!mBluetoothAdapter.isEnabled()) {
            return Result.BLUETOOTH_DISABLED;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("Select device to connect to:");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_item);

        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices?
        if (pairedDevices == null) {
            return Result.DEVICE_NOT_CONNECTED;
        }

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                arrayAdapter.add(device.getName()  + ", " + device.getAddress());
            }
        }

        builder.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceAddress = arrayAdapter.getItem(which).split(", ")[1];
                        for (BluetoothDevice device : pairedDevices) {
                            if (deviceAddress.equals(device.getAddress())) {
                                new ConnectThread(device, commands).start();
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.show();

        return Result.OK;
    }

    private static class ConnectThread extends Thread {

        private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        @SuppressWarnings("unused")
        private final BluetoothDevice mmDevice;

        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;
        private final List<Move> mCommands;

        public ConnectThread(BluetoothDevice device, List<Move> commands) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            mCommands = commands;

            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            try {
                tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP_UUID);
            } catch (IOException e) {
                BLog.e(TAG, e.toString());
            }
            mmSocket = tmp;

            OutputStream tmpOut = null;
            try {
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                BLog.e(TAG, e.toString());
            }

            mmOutStream = tmpOut;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                BLog.e(TAG, connectException.toString());
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    BLog.e(TAG, closeException.toString());
                }
                return;
            }

            executeCommands(mCommands);

            try {
                mmSocket.close();
            } catch (IOException e) {
                BLog.e(TAG, e.toString());
            }
        }

        public void executeCommands(List<Move> commands) {
            for (Move command : commands) {
                try {
                    mmOutStream.write(new byte[] { command.direction.toByte() });
                    Thread.sleep(command.threshold);
                } catch (Exception e) {
                    BLog.e(TAG, e.toString());
                }
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                BLog.e(TAG, e.toString());
            }
        }
    }

}
