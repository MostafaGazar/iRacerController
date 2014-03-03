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

package com.meg7.controller.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.meg7.controller.R;
import com.meg7.controller.ui.listener.CommandTouchListener;
import com.meg7.controller.ui.listener.CommandsDropListener;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public abstract class BaseParentViewGroup extends LinearLayout {

    private static final String STATE_IS_NOT_DRAGGABLE = "isNotDraggable";

    private boolean mIsNotDraggable;

    public BaseParentViewGroup(Context context) {
        super(context);
        init(context);
    }

    public BaseParentViewGroup(Context context, boolean isNotDraggable) {
        super(context);
        mIsNotDraggable = isNotDraggable;

        init(context);
    }

    public BaseParentViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseParentViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.ic_command_item_parent);

        if (!mIsNotDraggable) {
            setOnTouchListener(new CommandTouchListener());
        } else {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClicked();
                }
            });
        }

        setOnDragListener(new CommandsDropListener(getContext(), this));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Resources res = getResources();
        int desiredHSpec = MeasureSpec.makeMeasureSpec(
                (int) res.getDimension(R.dimen.command_item_parent_height), MeasureSpec.EXACTLY);
        int desiredWSpec = MeasureSpec.makeMeasureSpec(
                (int) res.getDimension(R.dimen.command_item_parent_width), MeasureSpec.EXACTLY);
        if (getChildCount() > 0) {
            desiredHSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);

            // XXX :: Wrong width!
            // desiredWSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.UNSPECIFIED);
            int defaultWidth = (int) res.getDimension(R.dimen.command_item_parent_width);
            int measuredWidth = getMeasuredWidth();
            if (measuredWidth < defaultWidth * 2) {
                desiredWSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
            }
        }

        setMeasuredDimension(desiredWSpec, desiredHSpec);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsNotDraggable = bundle.getBoolean(STATE_IS_NOT_DRAGGABLE);
        }

        super.onRestoreInstanceState(state);// BaseSavedState.EMPTY_STATE
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATE_IS_NOT_DRAGGABLE, mIsNotDraggable);
        return bundle;
    }

    protected abstract void onClicked();

}
