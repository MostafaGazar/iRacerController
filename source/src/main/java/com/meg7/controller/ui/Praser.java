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

import android.view.View;

import com.meg7.controller.data.Loop;
import com.meg7.controller.data.Move;
import com.meg7.controller.ui.view.BaseMoveView;
import com.meg7.controller.ui.view.LoopViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert flow to iRacer byte commands.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class Praser {

    public static List<Move> parse(List<View> workflow) {
        List<Move> commands = parseRecursive(workflow, null);

        commands.add(new Move(Move.Direction.STOP, 0));

        return commands;
    }

    private static List<Move> parseRecursive(List<View> workflow, List<Move> commands) {
        if (commands == null) {
            commands = new ArrayList<Move>();
        }

        LoopViewGroup loopViewGroup;
        Loop loop;
        for (View view : workflow) {
            if (view instanceof LoopViewGroup) {
                loopViewGroup = ((LoopViewGroup) view);
                loop = loopViewGroup.getData();

                List<View> children = new ArrayList<View>();
                for (int j = 0; j < loopViewGroup.getChildCount(); j++) {
                    children.add(loopViewGroup.getChildAt(j));
                }

                for (int i = 0; i < loop.count; i++) {
                    parseRecursive(children, commands);
                }
            } else if (view instanceof BaseMoveView) {
                commands.add(((BaseMoveView) view).getData());
            }
        }

        return commands;
    }
}
