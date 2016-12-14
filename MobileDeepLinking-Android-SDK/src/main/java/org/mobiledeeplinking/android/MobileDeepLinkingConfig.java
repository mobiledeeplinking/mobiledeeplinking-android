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

import org.json.JSONException;
import org.json.JSONObject;

public class MobileDeepLinkingConfig
{
    private Boolean logging;
    private JSONObject storyboard;
    private JSONObject routes;
    private JSONObject defaultRoute;


    public MobileDeepLinkingConfig(JSONObject json)
    {
        try
        {
            logging = Boolean.valueOf(json.getString(Constants.LOGGING_JSON_NAME));
            storyboard = json.getJSONObject(Constants.ROUTES_JSON_NAME);
            routes = json.getJSONObject(Constants.ROUTES_JSON_NAME);
            defaultRoute = json.getJSONObject(Constants.DEFAULT_ROUTE_JSON_NAME);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public Boolean getLogging()
    {
        return logging;
    }

    public void setLogging(Boolean logging)
    {
        this.logging = logging;
    }

    public JSONObject getStoryboard()
    {
        return storyboard;
    }

    public JSONObject getRoutes()
    {
        return routes;
    }

    public JSONObject getDefaultRoute()
    {
        return defaultRoute;
    }
}
