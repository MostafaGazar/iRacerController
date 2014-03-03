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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.meg7.controller.ui.view.LoopViewGroup;
import com.meg7.controller.ui.view.MoveBackwardView;
import com.meg7.controller.ui.view.MoveForwardView;
import com.meg7.controller.ui.view.MoveLeftView;
import com.meg7.controller.ui.view.MoveRightView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommandsDropListener implements View.OnDragListener {

    private Context mContext;
    private ViewGroup mParent;

    public CommandsDropListener(Context context, ViewGroup parent) {
        mContext = context;
        mParent = parent;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // No action necessary.
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                // No action necessary.
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // No action necessary.
                break;
            case DragEvent.ACTION_DROP:
                // Handle the dragged view being dropped over a drop view.
                String droppedViewClassName = null;
                if (dragEvent.getClipDescription() != null) {
                    droppedViewClassName = (String) dragEvent.getClipDescription().getLabel();
                }

                View newView = null;
                if (MoveBackwardView.class.getSimpleName().equals(droppedViewClassName)) {
                    newView = new MoveBackwardView(mContext, true);
                } else if (MoveForwardView.class.getSimpleName().equals(droppedViewClassName)) {
                    newView = new MoveForwardView(mContext, true);
                } else if (MoveLeftView.class.getSimpleName().equals(droppedViewClassName)) {
                    newView = new MoveLeftView(mContext, true);
                } else if (MoveRightView.class.getSimpleName().equals(droppedViewClassName)) {
                    newView = new MoveRightView(mContext, true);
                } else if (LoopViewGroup.class.getSimpleName().equals(droppedViewClassName)) {
                    newView = new LoopViewGroup(mContext, true);
                }

                if (newView != null) {
                    mParent.addView(newView);
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                // No action necessary.
                break;
            default:
                break;
        }

        return true;
    }
}