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

import junit.framework.TestCase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HandlerExecutorTest extends TestCase
{
    private MobileDeepLinking mobileDeepLinking;
    private JSONObject routeOptions;

    public void setUp() throws Exception
    {
        super.setUp();
        mobileDeepLinking = new MobileDeepLinking();
    }

    public void testExecuteHandlers() throws Exception
    {
        routeOptions = new JSONObject("{ \"handlers\" : [\"testHandler\"] }");
        Map<String, Handler> handlers = new HashMap<String, Handler>();
        handlers.put("testHandler", new Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                routeParameters.put("name", "value");
                return routeParameters;
            }
        });

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("name", "value");

        assertEquals(HandlerExecutor.executeHandlers(routeOptions, new HashMap<String, String>(), handlers), expected);
    }

    public void testExecuteHandlersExecuteInOrder() throws Exception
    {
        routeOptions = new JSONObject("{ \"handlers\" : [\"testHandler\", \"testHandler2\"] }");
        Map<String, Handler> handlers = new HashMap<String, Handler>();
        handlers.put("testHandler", new Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                routeParameters.put("name", "value");
                return routeParameters;
            }
        });
        handlers.put("testHandler2", new Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                routeParameters.put("name", "value2");
                return routeParameters;
            }
        });

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("name", "value2");

        assertEquals(HandlerExecutor.executeHandlers(routeOptions, new HashMap<String, String>(), handlers), expected);
    }

    public void testExecuteHandlersExecuteInDifferentOrder() throws Exception
    {
        routeOptions = new JSONObject("{ \"handlers\" : [\"testHandler2\", \"testHandler\"] }");
        Map<String, Handler> handlers = new HashMap<String, Handler>();
        handlers.put("testHandler", new Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                routeParameters.put("name", "value");
                return routeParameters;
            }
        });
        handlers.put("testHandler2", new Handler()
        {
            @Override
            public Map<String, String> execute(Map<String, String> routeParameters)
            {
                routeParameters.put("name", "value2");
                return routeParameters;
            }
        });

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("name", "value");

        assertEquals(HandlerExecutor.executeHandlers(routeOptions, new HashMap<String, String>(), handlers), expected);
    }
}
