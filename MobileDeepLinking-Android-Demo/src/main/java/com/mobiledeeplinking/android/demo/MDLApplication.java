/*
 * Copyright (C) 2014 by MobileDeepLinking.org
 *
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and
 * associated documentation files (the "Software"), to
 * deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mobiledeeplinking.android.demo;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.mobiledeeplinking.android.MobileDeepLinking;

import java.util.Map;

public class MDLApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        MobileDeepLinking.registerHandler("testHandler", new org.mobiledeeplinking.android.Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                Log.d("MDLApplication", "Inside callback!");
                routeParameters.put("nameFromCB", "valueFromCB");
                return routeParameters;
            }
        });
        MobileDeepLinking.registerHandler("testHandler2", new org.mobiledeeplinking.android.Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                Log.d("MDLApplication", "Inside callback 2!");
                Log.d("MDLApplication", "nameFromCB: " + routeParameters.get("nameFromCB"));
                return routeParameters;
            }
        });
    }
}