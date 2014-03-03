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

package com.meg7.controller.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.meg7.controller.R;
import com.meg7.controller.Deployer;
import com.meg7.controller.data.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class ControllerActivity extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    private ViewGroup mCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        mCanvas = (ViewGroup) findViewById(R.id.canvas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear:
                mCanvas.removeAllViews();
                return true;
            case R.id.action_deploy:
                deploy();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deploy() {
        List<View> workflow = new ArrayList<View>();
        int canvasChildrenCount = mCanvas.getChildCount();
        for (int i = 0; i < canvasChildrenCount; i++) {
            workflow.add(mCanvas.getChildAt(i));
        }

        List<Move> commands = Praser.parse(workflow);
        if (commands != null) {
            Deployer.Result result = Deployer.deploy(this, commands);
            handleDeployerResult(result);
        }
    }

    private void handleDeployerResult(Deployer.Result result) {
        switch (result) {
            case OK:
//                Toast.makeText(this, "All systems go", Toast.LENGTH_SHORT).show();
                break;
            case BLUETOOTH_DISABLED:
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                break;
            case BLUETOOTH_UNAVAILABLE:
                // Who cares.
                break;
            case DEVICE_NOT_CONNECTED:
                Toast.makeText(this, "i-racer not connected", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Let us give it another try.
                deploy();
            } else {
                Toast.makeText(this, "Failed to enable Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
