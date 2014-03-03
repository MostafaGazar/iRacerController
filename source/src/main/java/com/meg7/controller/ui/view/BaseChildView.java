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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.meg7.controller.R;
import com.meg7.controller.ui.listener.CommandTouchListener;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public abstract class BaseChildView extends TextView {

    private static final String STATE_IS_NOT_DRAGGABLE = "isNotDraggable";

    private boolean mIsNotDraggable;

    public BaseChildView(Context context) {
        super(context);
        init(context);
    }

    public BaseChildView(Context context, boolean isNotDraggable) {
        super(context);
        mIsNotDraggable = isNotDraggable;

        init(context);
    }

    public BaseChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.ic_command_item_child);
        setGravity(Gravity.CENTER);

        setTextColor(Color.WHITE);
        String text = getLabelText();
        if (text != null) {
            setText(text);
        }
//        Drawable compoundDrawable = getCompoundDrawable();
//        if (compoundDrawable != null) {
//            setCompoundDrawables(compoundDrawable, null, null, null);
//        }

        // Add listeners.
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Resources res = getResources();

        final int desiredHSpec = MeasureSpec.makeMeasureSpec(
                (int) res.getDimension(R.dimen.command_item_child_height), MeasureSpec.EXACTLY);
        final int desiredWSpec = MeasureSpec.makeMeasureSpec(
                (int) res.getDimension(R.dimen.command_item_child_width), MeasureSpec.EXACTLY);
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
    protected abstract String getLabelText();
    protected abstract Drawable getCompoundDrawable();
    
}
