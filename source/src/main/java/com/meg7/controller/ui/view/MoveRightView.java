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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.meg7.controller.R;
import com.meg7.controller.data.Move;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class MoveRightView extends BaseMoveView {

    public MoveRightView(Context context) {
        super(context);
    }

    public MoveRightView(Context context, boolean isNotDraggable) {
        super(context, isNotDraggable);
    }

    public MoveRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void updateDirection() {
        mMove.direction = Move.Direction.RIGHT_F;
    }

    @Override
    protected String getLabelText() {
        return getResources().getString(R.string.direction_right);
    }

    @Override
    protected Drawable getCompoundDrawable() {
        return null;
    }

}
