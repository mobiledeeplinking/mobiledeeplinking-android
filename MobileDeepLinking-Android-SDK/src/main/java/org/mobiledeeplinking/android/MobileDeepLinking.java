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

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobileDeepLinking extends Activity
{
    private static Map<String, Handler> handlers = null;
    private static MobileDeepLinkingConfig config = null;

    public MobileDeepLinking()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Read JSON file and then route to the appropriate place.
        if (config == null)
        {
            config = getConfiguration();
            MDLLog.loggingEnabled = config.getLogging();
        }

        try
        {
            routeUsingUrl(this.getIntent().getData());
        }
        catch (JSONException e)
        {
            MDLLog.e("MobileDeepLinking", "Error parsing JSON!", e);
            throw new RuntimeException();
        }
    }

    public static void registerHandler(String name, org.mobiledeeplinking.android.Handler handler)
    {
        if (handlers == null)
        {
            handlers = new HashMap<String, Handler>();
        }
        handlers.put(name, handler);
    }

    private void routeUsingUrl(Uri deeplink) throws JSONException
    {
        // base case
        if (TextUtils.isEmpty(deeplink.getHost()) && (TextUtils.isEmpty(deeplink.getPath())))
        {
            MDLLog.e("MobileDeepLinking", "No Routes Match.");
            routeToDefault();
            return;
        }

        Iterator<String> keys = config.getRoutes().keys();
        while (keys.hasNext())
        {
            String route = keys.next();
            JSONObject routeOptions = (JSONObject) config.getRoutes().get(route);
            try
            {
                Map<String, String> routeParameters = new HashMap<String, String>();
                Set<String> queryParameterNames = getQueryParameterNames(deeplink);
                if ( !queryParameterNames.isEmpty() )
                {
                    for (String paramName : queryParameterNames)
                    {
                        routeParameters.put(paramName, deeplink.getQueryParameter(paramName));
                    }
                }
                routeParameters = DeeplinkMatcher.match(route, routeOptions, routeParameters, deeplink);
                if (routeParameters != null)
                {
                    handleRoute(routeOptions, routeParameters);
                    return;
                }
            }
            catch (JSONException e)
            {
                MDLLog.e("MobileDeepLinking", "Error parsing JSON!", e);
                break;
            }
            catch (Exception e)
            {
                MDLLog.e("MobileDeepLinking", "Error matching and handling route", e);
                break;
            }
        }

        // deeplink trimmer
        routeUsingUrl(trimDeeplink(deeplink));
    }

    private void routeToDefault() throws JSONException
    {
        MDLLog.d("MobileDeepLinking", "Routing to Default Route.");
        handleRoute(config.getDefaultRoute(), null);
    }

    Uri trimDeeplink(Uri deeplink)
    {
        String host = deeplink.getHost();
        List<String> pathSegments = new LinkedList<String>(deeplink.getPathSegments());
        if (pathSegments.isEmpty())
        {
            // trim off host
            if (!TextUtils.isEmpty(host))
            {
                host = null;
            }
        }

        for (int i = pathSegments.size() - 1; i >= 0; i--)
        {
            // remove trailing slashes
            if (pathSegments.get(i).equals("/"))
            {
                pathSegments.remove(i);
            } else
            {
                pathSegments.remove(i);
                break;
            }
        }

        String pathString = "";
        for (int i = 0; i < pathSegments.size(); i++)
        {
            pathString += "/";
            pathString += pathSegments.get(i);
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(deeplink.getScheme());
        builder.path(pathString);
        builder.query(deeplink.getQuery());

        return builder.build();
    }

    private void handleRoute(JSONObject routeOptions, Map<String, String> routeParameters) throws JSONException
    {
        HandlerExecutor.executeHandlers(routeOptions, routeParameters, handlers);
        IntentBuilder.buildAndFireIntent(routeOptions, routeParameters, this);
    }

    private MobileDeepLinkingConfig getConfiguration()
    {
        try
        {
            String jsonString = readConfigFile();
            JSONObject json = new JSONObject(jsonString);
            return new MobileDeepLinkingConfig(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private String readConfigFile() throws IOException
    {
        Resources resources = this.getApplicationContext().getResources();
        AssetManager assetManager = resources.getAssets();

        InputStream inputStream = assetManager.open("MobileDeepLinkingConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    /**
     * Returns a set of the unique names of all query parameters. Iterating
     * over the set will return the names in order of their first occurrence.
     *
     * @throws UnsupportedOperationException if this isn't a hierarchical URI
     *
     * @return a set of decoded names
     */
    private Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }

        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));

            // Move start to end of name.
            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }
}
