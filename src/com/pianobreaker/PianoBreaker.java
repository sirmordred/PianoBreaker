/*
 * Copyright (C) 2018 @Sir Mordred @Oğuzhan Yiğit
 *
 * Licensed under the GPLv3 License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pianobreaker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.display.DisplayManagerGlobal;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceControl;

import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Piano Breaker Bot made by Sir Mordred (Oğuzhan Yiğit)
 * It uses native android system APIs for getting image of screen(a.k.a screenshot)
 * and sending tap (e.g injecting input event) to auto-play PianoTiles 2 Game
 */

public class PianoBreaker {
    
    static final float DEFAULT_SIZE = 1.0f;
    static final int DEFAULT_META_STATE = 0;
    static final float DEFAULT_PRECISION_X = 1.0f;
    static final float DEFAULT_PRECISION_Y = 1.0f;
    static final int DEFAULT_EDGE_FLAGS = 0;
    static int DEFAULT_DEVICE_ID = 0;
    
    static int points[] = new int[8];
    
    // Just for debugging, remove in production-builds
    private final static String TAG = "PianoBreaker";

    public static void main(String[] args) {
        // Calc execution time(duration) of script from parameter as second and store it as millisecond
        int duration = Integer.parseInt(args[0]);
        duration = duration * 1000;
        Display display = DisplayManagerGlobal.getInstance()
        .getRealDisplay(Display.DEFAULT_DISPLAY);
        Point displaySize = new Point();
        display.getRealSize(displaySize);
        // Store phone's display width and height in Point object (native API)
        final int displayWidth = displaySize.x;
        final int displayHeight = displaySize.y;
        InputManager inpMng = InputManager.getInstance(); // Get instance of Android's Internal InputManager object
        int[] devIds = InputDevice.getDeviceIds();
        for (int devId : devIds) {
            InputDevice inputDev = InputDevice.getDevice(devId);
            if (inputDev.supportsSource(InputDevice.SOURCE_TOUCHSCREEN)) {
                DEFAULT_DEVICE_ID = devId;
                break;
            }
        }
        points[0] = displayWidth / 8;
        points[1] = 5 * displayHeight / 8;
        points[2] = 3 * displayWidth / 8;
        points[3] = 5 * displayHeight / 8;
        points[4] = 5 * displayWidth / 8;
        points[5] = 5 * displayHeight / 8;
        points[6] = 7 * displayWidth / 8;
        points[7] = 5 * displayHeight / 8;
        
        
        //////////////////
        Bitmap screenShot;
        //// MAIN LOOP BEGIN HERE
        long begin = SystemClock.uptimeMillis();
        long now;
        int px;

        while(true) {
            // take ss
            screenShot =
            SurfaceControl.screenshot(displayWidth, displayHeight);
            
            // check color of points via getPixel API
            for (int i = 0; i < 7; i+=2) {
                px = screenShot.getPixel(points[i], points[i + 1]);
                if (px == -16777216) { // hardcoded integer value of black color 16777216
                    now = SystemClock.uptimeMillis();
                    inpMng.injectInputEvent(getMtEvent(now, MotionEvent.ACTION_DOWN, points[i], points[i + 1], 1.0f), InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
                    inpMng.injectInputEvent(getMtEvent(now, MotionEvent.ACTION_UP, points[i], points[i + 1], 0.0f), InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
                    break;
                }
            }
            if ((SystemClock.uptimeMillis() - begin) >= duration) {
                break; // after hitting into the limit of execution time(duration), break the loop
            }
         }
        //// MAIN LOOP END HERE
        screenShot.recycle(); // release/free/clear screenshot object at the end of execution of script
        screenShot = null; // nullify it so GC can free it
    }

    // Just a helper method which is used for creating MotionEvent that will be injected
    private static MotionEvent getMtEvent(long when, int actionType, int x, int y, float pressure) {
        MotionEvent event = MotionEvent.obtain(when, when, actionType, (float) x, (float) y, pressure, DEFAULT_SIZE,
                                                 DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y,
                                                 DEFAULT_DEVICE_ID, DEFAULT_EDGE_FLAGS);
        event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
        return event;
    }

}
