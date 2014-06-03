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

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DeeplinkMatcher
{
    public static Map<String, String> match(String route, JSONObject routeOptions, Map<String, String> routeParameters, Uri deeplink) throws JSONException
    {
        Map<String, String> results = new HashMap<String, String>(routeParameters);
        results = matchPathParameters(route, routeOptions, deeplink, results);
        if (results == null)
        {
            return null;
        }
        results = matchQueryParameters(routeOptions, deeplink.getEncodedQuery(), results);
        if (results == null)
        {
            return null;
        }

        if (checkForRequiredRouteParameters(routeOptions, results))
        {
            return results;
        }

        return null;
    }

    static Map<String, String> matchPathParameters(String route, JSONObject routeOptions, Uri deeplink, Map<String, String> results)
    {
        List<String> routeComponents = new LinkedList<String>(Arrays.asList(route.split("/")));
        List<String> deeplinkComponents = new LinkedList<String>(deeplink.getPathSegments());

        if (!route.startsWith("/"))
        {
            String host = deeplink.getHost();
            if (!routeComponents.get(0).equals(host))
            {
                return null;
            }
            routeComponents.remove(0);
        }

        if (routeComponents.size() != deeplinkComponents.size())
        {
            return null;
        }

        String routeComponent;
        String deeplinkComponent;

        for (int i = 0; i < routeComponents.size(); i++)
        {
            routeComponent = routeComponents.get(i);
            deeplinkComponent = deeplinkComponents.get(i);
            if (!routeComponent.equals(deeplinkComponent))
            {
                if (routeComponent.startsWith(":"))
                {
                    String routeComponentName = routeComponent.substring(1);
                    if (validateRouteComponent(routeComponentName, deeplinkComponent, routeOptions))
                    {
                        results.put(routeComponentName, deeplinkComponent);
                    } else
                    {
                        return null;
                    }
                } else
                {
                    return null;
                }
            }

        }

        return results;
    }

    static Boolean validateRouteComponent(String routeComponentName, String deeplinkComponent, JSONObject routeOptions)
    {
        try
        {
            JSONObject routeParameters = routeOptions.getJSONObject(Constants.ROUTE_PARAMS_JSON_NAME);
            JSONObject pathComponentParameters = routeParameters.getJSONObject(routeComponentName);

            String regexString = pathComponentParameters.getString(Constants.REGEX_JSON_NAME);
            if (!TextUtils.isEmpty(regexString))
            {
                return deeplinkComponent.matches(regexString);
            }
        }
        catch (JSONException e)
        {
            // Route Component has no associated route parameters. Default to valid.
        }
        return Boolean.TRUE;
    }

    static Map<String, String> matchQueryParameters(JSONObject routeOptions, String deeplinkQuery, Map<String, String> results) throws JSONException
    {
        JSONObject routeParameters = null;
        if (routeOptions.has(Constants.ROUTE_PARAMS_JSON_NAME))
        {
            routeParameters = routeOptions.getJSONObject(Constants.ROUTE_PARAMS_JSON_NAME);
            if (deeplinkQuery != null) {
                List<String> queryPairs = Arrays.asList(deeplinkQuery.split("&"));
                for (String query : queryPairs)
                {
                    List<String> nameAndValue = Arrays.asList(query.split("="));
                    if (nameAndValue.size() == 2)
                    {
                        String name = Uri.decode(nameAndValue.get(0));
                        if (!routeParameters.has(name))
                        {
                            continue;
                        }

                        String value = Uri.decode(nameAndValue.get(1));
                        if (validateRouteComponent(name, value, routeOptions))
                        {
                            results.put(name, value);
                        } else
                        {
                            MDLLog.e("DeeplinkMatcher", "Query Parameter Regex Checking failed.");
                            return null;
                        }
                    }
                }
            }
        }
        return results;
    }

    static Boolean checkForRequiredRouteParameters(JSONObject routeOptions, Map<String, String> results) throws JSONException
    {
        List<String> requiredRouteParameterValues = getRequiredRouteParameterValues(routeOptions);
        for (String requiredValue : requiredRouteParameterValues)
        {
            if (results.get(requiredValue) == null)
            {
                MDLLog.e("DeeplinkMatcher", "Required route parameter is missing.");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    static List<String> getRequiredRouteParameterValues(JSONObject routeOptions) throws JSONException
    {
        List<String> requiredRouteParameters = new ArrayList<String>();
        if (routeOptions.has(Constants.ROUTE_PARAMS_JSON_NAME))
        {
            JSONObject routeParameters = routeOptions.getJSONObject(Constants.ROUTE_PARAMS_JSON_NAME);
            Iterator<String> keys = routeParameters.keys();

            while (keys.hasNext())
            {
                String key = keys.next();
                JSONObject routeParameterOptions = routeParameters.getJSONObject(key);
                if (routeParameterOptions.has(Constants.REQUIRED_JSON_NAME) && routeParameterOptions.getString(Constants.REQUIRED_JSON_NAME).equals("true"))
                {
                    requiredRouteParameters.add(key);
                }
            }
        }
        return requiredRouteParameters;
    }
}
