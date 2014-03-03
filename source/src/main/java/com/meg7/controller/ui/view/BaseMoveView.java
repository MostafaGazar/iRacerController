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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.meg7.controller.data.Move;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public abstract class BaseMoveView extends BaseChildView {

    protected Move mMove = new Move();

    public BaseMoveView(Context context) {
        super(context);
        init(context);
    }

    public BaseMoveView(Context context, boolean isNotDraggable) {
        super(context, isNotDraggable);
        init(context);
    }

    public BaseMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        updateDirection();
    }

    @Override
    protected void onClicked() {
        Context context = getContext();

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText(String.valueOf(mMove.threshold));
        input.setLayoutParams(lp);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Change duration")
                .setMessage("Duration")
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mMove.threshold = Integer.parseInt(input.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create();

        alertDialog.show();
    }

    protected abstract void updateDirection();
    public Move getData() {
        return mMove;
    }

}
