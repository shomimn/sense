package com.ubhave.sensormanager.process;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public abstract class CommunicationProcessor extends AbstractProcessor
{
	public CommunicationProcessor(Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	public static String hashPhoneNumber(String phoneNumber)
	{
		if ((phoneNumber == null) || (phoneNumber.length() == 0))
		{
			return "";
		}

		// use only the last 10 digits
		if (phoneNumber.length() > 10)
		{
			phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
		}
		MessageDigest mDigest = null;
		try
		{
			mDigest = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		mDigest.reset();
		byte[] byteArray = mDigest.digest(phoneNumber.getBytes());
		String hash = "";
		for (int i = 0; i < byteArray.length; i++)
		{
			String hexString = Integer.toHexString(0xFF & byteArray[i]);
			if (hexString.length() == 1)
			{
				hexString = "0" + hexString;
			}
			hash += hexString.toUpperCase(Locale.ENGLISH);
		}
		return hash;
	}

	public String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactName;
	}
}
