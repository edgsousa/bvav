/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is "SMS Library for the Java platform".
 *
 * The Initial Developer of the Original Code is Markus Eriksson.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.nokia;

import java.io.UnsupportedEncodingException;

/**
 * Nokia Picture message
 * <p>
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaPictureMessage extends NokiaMultipartMessage
{
    /**
     * Creates a Nokia Picture Message
     *
     * @param otaBitmap
     * @param msg
     */
    public NokiaPictureMessage(OtaBitmap otaBitmap, String msg)
    {
        this(otaBitmap, msg, false);
    }

    /**
     * Creates a Nokia Picture Message
     *
     * @param bitmapData
     * @param msg
     */
    public NokiaPictureMessage(byte[] bitmapData, String msg)
    {
        this(bitmapData, msg, false);
    }

    /**
     * Creates a Nokia Picture Message
     *
     * @param otaBitmap
     * @param msg
     * @param asUnicode Set to true if text should be sent as unicode
     */
    public NokiaPictureMessage(OtaBitmap otaBitmap, String msg, boolean asUnicode)
    {
        this(otaBitmap.getBytes(), msg, asUnicode);
    }

    /**
     * Creates a Nokia Picture Message
     *
     * @param bitmapData
     * @param msg
     * @param asUnicode Set to true if text should be sent as unicode
     */
    public NokiaPictureMessage(byte[] bitmapData, String msg, boolean asUnicode)
    {
        addBitmap(bitmapData);
        addText(msg, asUnicode);
    }

    /**
     * Used internally to add the image
     *
     * @param bitmapData
     */
    private void addBitmap(byte[] bitmapData)
    {
        addMultipart(NokiaPart.ITEM_OTA_BITMAP, bitmapData);
    }

    /**
     * Used internally to add the image
     * @param otaBitmap
     */
    private void addBitmap(OtaBitmap otaBitmap)
    {
        addMultipart(NokiaPart.ITEM_OTA_BITMAP, otaBitmap.getBytes());
    }

    /**
     * Used internally to add text
     *
     * @param msg
     * @param asUnicode
     */
    private void addText(String msg, boolean asUnicode)
    {
        try
        {
            if (asUnicode)
            {
                addMultipart(NokiaPart.ITEM_TEXT_UNICODE, msg.getBytes("UTF-16BE"));
            }
            else
            {
                addMultipart(NokiaPart.ITEM_TEXT_ISO_8859_1, msg.getBytes("ISO-8859-1"));
            }
        }
        catch (UnsupportedEncodingException ex)
        {
            //log_.fatal("Shouldn't happen, 'UTF-16BE' and 'ISO-8859-1' are in the standard", ex);
        }
    }
}

