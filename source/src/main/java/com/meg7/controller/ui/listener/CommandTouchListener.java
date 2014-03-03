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

package com.meg7.controller.ui.listener;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class CommandTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // Setup drag.
            ClipData data = ClipData.newPlainText(view.getClass().getSimpleName(), "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            // Start dragging the item touched.
            view.startDrag(data, shadowBuilder, view, 0);

            return true;
        }

        return false;
    }
}