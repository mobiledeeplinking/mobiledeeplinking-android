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

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class IntentBuilder
{
    public static void buildAndFireIntent(JSONObject routeOptions, Map<String, String> routeParameters, Context context) throws JSONException
    {
        String activityName = routeOptions.getString(Constants.CLASS_JSON_NAME);
        try
        {
            Intent intent = new Intent(context, Class.forName(activityName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (routeParameters != null)
            {
                for (Map.Entry<String, String> param : routeParameters.entrySet())
                {
                    intent.putExtra(param.getKey(), param.getValue());
                }
            }
            context.startActivity(intent);
        }
        catch (ClassNotFoundException e)
        {
            MDLLog.e("IntentBuilder", "Activity in JSON not found.");
        }
    }
}
