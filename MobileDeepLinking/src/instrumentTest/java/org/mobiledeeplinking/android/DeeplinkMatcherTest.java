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

import junit.framework.TestCase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeeplinkMatcherTest extends TestCase
{
    JSONObject routeOptions;
    Map<String, String> results;

    public DeeplinkMatcherTest()
    {
    }

    public void setUp() throws Exception
    {
        super.setUp();
        routeOptions = new JSONObject("{\"routeParameters\": {}}");
        results = new HashMap<String, String>();
    }

    public void tearDown() throws Exception
    {
        //builder.scheme("mdldemo").authority("data").appendPath("turtles").appendPath("types").appendQueryParameter("type", "1").appendQueryParameter("sort", "relevance");
        results = new HashMap<String, String>();
    }

    public void testMatchPathParametersWithHost() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertEquals(newResults, results);
    }

    public void testMatchPathParametersWithHostReturnsNull() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("dataa");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);
    }

    public void testMatchPathParametersReturnsNull2() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("ata");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);
    }

    public void testMatchPathParametersReturnsNull3() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("dat");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);
    }

    public void testMatchPathParametersWithPath() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("path");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, builder.build(), results);
        assertEquals(newResults, results);
    }

    public void testMatchPathParametersWithPathAndTrailingSlash() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("path/");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, builder.build(), results);
        assertEquals(newResults, results);
    }

    public void testMatchPathParametersFails() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("pathe");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, builder.build(), results);
        assertNull(newResults);
    }

    public void testMatchPathParametersFails2() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("pat");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, builder.build(), results);
        assertNull(newResults);
    }

    public void testMatchPathParametersFails3() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("daa").path("path");
        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, builder.build(), results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, builder.build(), results);
        assertNull(newResults);
    }

    public void testMatchPathParametersWithPathParams() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("path/5");
        Uri uri = builder.build();
        String pathDefinition = "data/path/:pathId";

        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, uri, results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, uri, results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters(pathDefinition, routeOptions, uri, results);
        assertNotNull(newResults.get("pathId"));
    }

    public void testMatchPathParametersWithPathParamsAndTrailingSlash() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("path/5/");
        Uri uri = builder.build();
        String pathDefinition = "data/path/:pathId";

        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, uri, results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, uri, results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters(pathDefinition, routeOptions, uri, results);
        assertNotNull(newResults.get("pathId"));
    }

    public void testMatchPathParametersReturnsFalseWithPathParams() throws Exception
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("path//");
        Uri uri = builder.build();
        String pathDefinition = "data/path/:pathId";

        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters("data", routeOptions, uri, results);
        assertNull(newResults);

        newResults = DeeplinkMatcher.matchPathParameters("data/path", routeOptions, uri, results);
        assertEquals(newResults, results);

        newResults = DeeplinkMatcher.matchPathParameters(pathDefinition, routeOptions, uri, results);
        assertNull(newResults);
    }

    public void testMatchPathParametersWithRegex() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"dataId\" : { \"regex\" : \"[0-9]\"  } } }");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("5");
        Uri uri = builder.build();
        String pathDefinition = "data/:dataId";

        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters(pathDefinition, routeOptions, uri, results);
        assertEquals(newResults.get("dataId"), "5");
    }

    public void testMatchPathParametersWithRegexReturnsFalse() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"dataId\" : { \"regex\" : \"[0-9]\"  } } }");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("54");
        Uri uri = builder.build();
        String pathDefinition = "data/:dataId";

        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters(pathDefinition, routeOptions, uri, results);
        assertNull(newResults);
    }

    public void testMatchPathParametersWithRegexReturnsFalse2() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"dataId\" : { \"regex\" : \"[0-9]\"  } } }");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mdldemo").authority("data").path("somedata");
        Uri uri = builder.build();
        String pathDefinition = "data/:dataId";

        Map<String, String> newResults = DeeplinkMatcher.matchPathParameters(pathDefinition, routeOptions, uri, results);
        assertNull(newResults);
    }

    public void testMatchQueryParameters() throws Exception
    {
        Map<String, String> newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=value", results);
        assertEquals(newResults, new HashMap<String, String>());
    }

    public void testMatchQueryParametersDoesntMatchWithMultipleParameters() throws Exception
    {
        Map<String, String> newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=value&name2=value2", results);
        assertEquals(newResults, new HashMap<String, String>());
    }

    public void testMatchQueryParametersMatch() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"name\" : {} } }");
        Map<String, String> newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=value&name2=value2", results);
        assertEquals(newResults.get("name"), "value");
    }

    public void testMatchQueryParametersMatchWithMultipleParameters() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"name2\" : {} } }");
        Map<String, String> newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=value&name2=value2", results);
        assertEquals(newResults.get("name2"), "value2");
    }

    public void testMatchQueryParametersMatchWithMultipleParameters2() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"name2\" : {}, \"name\" : {} } }");
        Map<String, String> newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=value&name2=value2", results);
        assertEquals(newResults.get("name"), "value");
        assertEquals(newResults.get("name2"), "value2");
    }

    public void testMatchQueryParametersMatchWithRegex() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"name\" : { \"regex\" : \"[0-9]\" } } }");
        Map<String, String> newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=5&name2=value2", results);
        assertEquals(newResults.get("name"), "5");

        results = new HashMap<String, String>();
        newResults = DeeplinkMatcher.matchQueryParameters(routeOptions, "name=banana&name2=value2", results);
        assertNull(newResults);
    }

    public void testCheckForRequiredRouteParameters() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"name\" : { \"required\" : \"true\" } } }");
        List<String> expected = new ArrayList<String>();
        expected.add("name");
        List<String> result = DeeplinkMatcher.getRequiredRouteParameterValues(routeOptions);
        assertEquals(expected, result);
    }

    public void testCheckForRequiredRouteParameters2() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : { \"name\" : { \"required\" : \"false\" } } }");
        List<String> expected = new ArrayList<String>();
        List<String> result = DeeplinkMatcher.getRequiredRouteParameterValues(routeOptions);
        assertEquals(expected, result);
    }

    public void testCheckForRequiredRouteParameters3() throws Exception
    {
        routeOptions = new JSONObject("{ \"routeParameters\" : {} }");
        List<String> expected = new ArrayList<String>();
        List<String> result = DeeplinkMatcher.getRequiredRouteParameterValues(routeOptions);
        assertEquals(expected, result);
    }
}
