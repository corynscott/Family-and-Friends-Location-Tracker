package map.teamc.com.maplocationproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

/**
 * 
 * @Class: SmsReceiver
 * @Inheritance: BroadcastReceiver
 * @Description
 * 		Class that will receive an SMS message with an specific text to verify the phone number
 * 			entered for registration
 * @Created: 16/11/2014
 *
 */
@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver{
	
	 private final static String MESSAGE = "CONFIRMATION";

	 /**
	  * @Function: onReceive
	  * @Access: Public
	  * @Description
	  * 	Function that will receive an SMS message, it will return the information to Register Activity
	  * @return void
	  * @param context
	  * 		Context of the Register Activity that created the Broadcast
	  * @param intent
	  * 		Contains the data of the SMS
	  */
	@Override
	public void onReceive(Context context, Intent intent){
		Log.d("SmsReceiver", "onReceive: context = " + context + 
				"\nintent = " + intent);
		Bundle bundle = intent.getExtras();
		SmsMessage msg= null;
		if(bundle != null){
			Log.d("onReceive", "if - true");
			Object[] pdus = (Object[]) bundle.get("pdus");
			msg = SmsMessage.createFromPdu((byte[]) pdus[0]);

			Log.d("message", msg.getOriginatingAddress());
			Log.d("message", msg.getMessageBody().toString());
            Log.d("Map", "Is ok");
            if(//msg.getOriginatingAddress().compareTo("+447517217601") == 0 &&
                    msg.getMessageBody().toString().compareTo(MESSAGE + "FamFri") == 0){
                Log.d("message", "before");
                abortBroadcast();
                Log.d("message", "after");
                Intent ret = new Intent("SmsMessage.intent.MAIN");
                ret.putExtra("get_number", msg.getOriginatingAddress());
                ret.putExtra("get_msg", msg.getMessageBody());
                context.sendBroadcast(ret);
            }
		}
	}
}
