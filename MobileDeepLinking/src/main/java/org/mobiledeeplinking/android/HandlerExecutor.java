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

package org.mobiledeeplinking.android;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HandlerExecutor
{
    public static Map<String, String> executeHandlers(JSONObject routeOptions, Map<String, String> routeParameters, Map<String, Handler> handlers) throws JSONException
    {
        if (routeOptions.has(Constants.HANDLERS_JSON_NAME) && !TextUtils.isEmpty(routeOptions.getString(Constants.HANDLERS_JSON_NAME)))
        {
            JSONArray routeHandlers = routeOptions.getJSONArray(Constants.HANDLERS_JSON_NAME);
            for (int i = 0; i < routeHandlers.length(); i++)
            {
                Handler handler = handlers.get(routeHandlers.get(i));
                if (handler != null)
                {
                    routeParameters = handler.execute(routeParameters);
                } else
                {
                    MDLLog.e("MobileDeepLinking", "Handler " + routeHandlers.get(i) + " has not been registered with the MobileDeepLinking library.");
                }
            }
        }
        return routeParameters;
    }
}
