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

public class MobileDeepLinkingTest extends TestCase
{
    MobileDeepLinking mobileDeepLinking = new MobileDeepLinking();

    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void testOnCreate() throws Exception
    {

    }

    public void testRegisterHandler() throws Exception
    {

    }

    public void testTrimDeeplink() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://data/54?name=value&name1=value1");
        Uri expected = Uri.parse("mdldemo://data?name=value&name1=value1");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);

        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

    public void testTrimDeeplinkWithTrailingSlashes() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://data/54/?name=value&name1=value1");
        Uri expected = Uri.parse("mdldemo://data?name=value&name1=value1");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);
        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());


        deeplink = Uri.parse("mdldemo://data/54//////?name=value&name1=value1");
        expected = Uri.parse("mdldemo://data?name=value&name1=value1");
        returned = mobileDeepLinking.trimDeeplink(deeplink);

        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

    public void testTrimDeeplinkWithoutPath() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://data?name=value&name1=value1");
        Uri expected = Uri.parse("mdldemo://?name=value&name1=value1");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);
        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

    public void testTrimDeeplinkWithoutHostAndPath() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://?name=value&name1=value1");
        Uri expected = Uri.parse("mdldemo://?name=value&name1=value1");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);

        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

    public void testTrimDeeplinkWithLongPath() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://data/32/hello/there?name=value&name1=value1");
        Uri expected = Uri.parse("mdldemo://data/32/hello?name=value&name1=value1");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);

        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

    public void testTrimDeeplinkWithHost() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://data?name=value&name1=value1");
        Uri expected = Uri.parse("mdldemo://?name=value&name1=value1");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);

        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

    public void testTrimDeeplinkWithHost2() throws Exception
    {
        Uri deeplink = Uri.parse("mdldemo://data/3");
        Uri expected = Uri.parse("mdldemo://");
        Uri returned = mobileDeepLinking.trimDeeplink(deeplink);

        assertEquals(returned.getScheme(), expected.getScheme());
        assertEquals(returned.getPath(), expected.getPath());
        assertEquals(returned.getQuery(), expected.getQuery());
    }

}
