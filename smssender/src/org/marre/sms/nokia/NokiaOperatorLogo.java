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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.marre.sms.SmsConstants;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsPortAddressedMessage;
import org.marre.sms.SmsUserData;

/**
 * Nokia Operator Logo message
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaOperatorLogo extends SmsPortAddressedMessage
{
    /**
     * If set to true it will make the message two bytes shorter to make it
     * possible to fit a 72x14 pixel image in one SMS instead of two. <br>
     * <b>Note! </b> This will probably only work on Nokia phones...
     */
    protected boolean discardNokiaHeaders_;
    
    /** The ota image as a byte array */
    protected byte[] bitmapData_;
    
    /** GSM Mobile Country Code */
    protected int mcc_;
    
    /** GSM Mobile Network Code */
    protected int mnc_;

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param otaBitmap
     * @param mcc
     *            GSM Mobile Country Code
     * @param mnc
     *            GSM Mobile Network Code
     */
    public NokiaOperatorLogo(OtaBitmap otaBitmap, int mcc, int mnc)
    {
        this(otaBitmap.getBytes(), mcc, mnc);
    }

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param bitmapData
     *            The ota image as a byte array
     * @param mcc
     *            GSM Mobile Country Code
     * @param mnc
     *            GSM Mobile Network Code
     */
    public NokiaOperatorLogo(byte[] bitmapData, int mcc, int mnc)
    {
        super(SmsConstants.PORT_NOKIA_OPERATOR_LOGO, 0);
        
        bitmapData_ = bitmapData;
        mcc_ = mcc;
        mnc_ = mnc;
    }

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param bitmapData
     *            The ota image as a byte array
     * @param mcc
     *            GSM Mobile Country Code
     * @param mnc
     *            GSM Mobile Network Code
     */
    public NokiaOperatorLogo(byte[] bitmapData, int mcc, int mnc, boolean discardHeaders)
    {
        super(SmsConstants.PORT_NOKIA_OPERATOR_LOGO, 0);
        
        discardNokiaHeaders_ = discardHeaders;
        bitmapData_ = bitmapData;
        mcc_ = mcc;
        mnc_ = mnc;
    }

    public SmsUserData getUserData()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(140);

        try
        {
            if (!discardNokiaHeaders_)
            {
                // Header??
                baos.write(0x30);
            }

            // mcc
            SmsPduUtil.writeBcdNumber(baos, "" + mcc_);
            // mnc
            if (mnc_ < 10)
            {
                SmsPduUtil.writeBcdNumber(baos, "0" + mnc_);
            }
            else
            {
                SmsPduUtil.writeBcdNumber(baos, "" + mnc_);
            }

            if (!discardNokiaHeaders_)
            {
                // Start of content?
                baos.write(0x0A);
            }
            // bitmap
            baos.write(bitmapData_);

            baos.close();
        }
        catch (IOException ex)
        {
            // Should not happen!
        }

        return new SmsUserData(baos.toByteArray());
    }
}
