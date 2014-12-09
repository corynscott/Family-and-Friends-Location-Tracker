package map.teamc.com.maplocationproject;

import java.security.SecureRandom;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 		Class aCtivity - Layout for the Phone Verification screen
 *
 */
@SuppressLint("TrulyRandom")
public class PhoneVerificationActivity extends ActionBarActivity {
   
	/*
	 * SharedPreferences object that will get information stored in the application
	 */
   SharedPreferences preferences;
   
   /*
    * String that contains the name of the application SharedPreferences
    */
   public static final String MyPREFERENCES = "MyPrefs";
   
   /*
    * String variable that contains the name of the receiver to be registered
    */
   private final static String SENT = "SMS_SENT";
   
   /*
    * The BroadcasReceiver that sends the SMS
    */
   private BroadcastReceiver sendB ;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("PhoneVerificationActivity", "onCreate: Bundle: " + savedInstanceState);
       setContentView(R.layout.phone_verification_layout);
       
       sendB = new BroadcastReceiver(){
       	/**
       	 * BroadcastReceiver for sending a SMS
       	 */
           @Override
           public void onReceive(Context arg0, Intent arg1){
               switch(getResultCode()){
                   case Activity.RESULT_OK:
                       Log.d("message", "Sent");
                       Toast.makeText(getApplicationContext(), "Message - Sent", 
           					Toast.LENGTH_LONG).show();
                       Intent register = new Intent(arg0, RegisterActivity.class);
                       arg0.startActivity(register);
                       ((ActionBarActivity) arg0).finish();
                       break;
                   case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                       Log.d("message", "Generic Failure");
                       Toast.makeText(getApplicationContext(), "Message - Not Sent\nVerify your SMS Service", 
              					Toast.LENGTH_LONG).show();
                       break;
                   case SmsManager.RESULT_ERROR_NULL_PDU:
                       Log.d("message", "Null PDU");
                       break;
                   case SmsManager.RESULT_ERROR_RADIO_OFF:
                       Log.d("message", "Radio Off");
                       break;
               }
           }
       };
       
       this.registerReceiver(sendB, new IntentFilter(SENT));
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
       //getMenuInflater().inflate(R.menu.main, menu);
       return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       // Handle action bar item clicks here. The action bar will
       // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.

       return false;
   }
   
   /**
    * @Function: functionVerifyPhone
    * @Access:  public
    * @Return: void
    * @Description:
    * 		Code to generate a verification code and send it to sendCode method
    * @param view
    * 		View of the layout
    */
   @SuppressLint("TrulyRandom")
   public void functionVerifyPhone(View view){
	   Log.d("PhoneVerificationActivity", "functionVerifyPhone");   
	   SecureRandom secureRandom = new SecureRandom();
	   String base64Ran = Base64.encodeToString(String.valueOf(secureRandom.nextDouble()).getBytes(), Base64.DEFAULT);
	   String verificationCode = base64Ran.substring(3, 9);
	   
	   sendCode(verificationCode);
	   Log.d("TestCode", base64Ran);

	   //String base64EncodedEncryptedMsg = BaseEncoding.base64().encode(phoneNumber.getText().toString());
   }
   
   /**
    * @Function: sendCode
    * @Access:  private
    * @Return: void
    * @Description:
    * 		Code to generate the data to be stored in SharedPreferences related to the verification of the phone number
    * 		It will send a message to the entered phone number with the code and it expires in 24 hours
    * 		
    * @param code
    * 		Verification code
    */
   private void sendCode(String code){
       Log.d("PhoneVerificationActivity", "sendCode");
       String message = "Family Locator App Code: " + code;
       Calendar expirationDate = Calendar.getInstance();
       EditText phoneNumber = (EditText) findViewById(R.id.phone_verification);
       expirationDate.add(Calendar.DAY_OF_MONTH, 1);
       
       preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = preferences.edit();
       
       editor.putString("phoneNumber_FamFri", phoneNumber.getText().toString());
       editor.putString("verificationPhone_FamFri",code);
       editor.putLong("expirationDate_FamFri",expirationDate.getTimeInMillis());
       editor.commit();
       
       PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
       SmsManager sms = SmsManager.getDefault();
       sms.sendTextMessage(phoneNumber.getText().toString(), null, message, sentPI, null);
   }
}
