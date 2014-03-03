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

package com.meg7.controller.data;

import com.meg7.controller.utils.Constants;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class Move {

    public Direction direction = Direction.FORWARD;
    public long threshold = Constants.DEFAULT_THRESHOLD_MOVE;

    public static enum Direction {
        STOP     ((byte) 0x00),
        FORWARD  ((byte) 0x17),
        BACKWARD ((byte) 0x27),
        LEFT_F   ((byte) 0x57),
        RIGHT_F  ((byte) 0x67);

        private final byte mCommand;

        Direction(byte commandValue) {
            mCommand = commandValue;
        }

        public byte toByte() {
            return mCommand;
        }

        @Override
        public String toString() {
            switch (this) {
                case STOP:
                    return "STOP";
                case FORWARD:
                    return "FORWARD";
                case BACKWARD:
                    return "BACKWARD";
                case LEFT_F:
                    return "LEFT_F";
                case RIGHT_F:
                    return "RIGHT_F";
            }

            return super.toString();
        }
    }

    public Move() {
    }

    public Move(Direction direction) {
        this.direction = direction;
    }

    public Move(Direction direction, long threshold) {
        this(direction);
        this.threshold = threshold;
    }
}
