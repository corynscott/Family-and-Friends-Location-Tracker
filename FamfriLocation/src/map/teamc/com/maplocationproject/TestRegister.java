package map.teamc.com.maplocationproject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class TestRegister extends ActionBarActivity{
	
	//This part of the final onCreate for the register
	BroadcastReceiver sendB ;
	//This part of the final onCreate for the register
	String SENT = "SMS_SENT";
	//This part of the final onCreate for the register
    private BroadcastReceiver mInentReceiver;
    private Client client = new Client();
    
    private final static String MESSAGE = "CONFIRMATION";
    
    User registerUser;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_register);
        
        //This part of the final onCreate for the register
        sendB = new BroadcastReceiver(){
        	/**
        	 * BroadcastReceiver for sending a SMS
        	 */
            @Override
            public void onReceive(Context arg0, Intent arg1){
                //String phone = "07517217601";
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Log.d("message", "Sent");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.d("message", "Generic Failure");
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

      //This part of the final onCreate for the register
        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mInentReceiver = new BroadcastReceiver(){
        	/**
        	 * BroadcastReceiver after getting the SMS
        	 */
            @Override
            public void onReceive(Context context, Intent intent){
                String phone = intent.getStringExtra("get_number");
                String message = intent.getStringExtra("get_msg");
                Log.d("message_received", phone + " : " + message);
                if(message.compareTo(MESSAGE + "FamFri") == 0){
                	registerUser.setNumberChecked(true);
                    sendRegisterInformation();
                }
                else{
                    Log.d("Error", "Message is not OK");
                }
            }
        };
      //This part of the final onCreate for the register
        this.registerReceiver(sendB, new IntentFilter(SENT));
        this.registerReceiver(mInentReceiver, intentFilter);
    }

	/**
	 * @Function
	 * 		sendRegisterInformation
	 * @Access
	 * 		Private
	 * @Description
	 * 		Method to send the register information to Client class
	 * @return
	 * 		void
	 * 
	 * @Created: 17/11/2014
	 * 		
	 */
    private void sendRegisterInformation(){
        Log.d("TestRegister", "sendRegisterInformation");
        
        client.registerUser(this, registerUser);
        
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
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    
    
    public void functionRegister(View view){
    	Log.d("TestRegister", "functionRegister");
    	EditText name = (EditText) findViewById(R.id.name_register);
    	EditText username = (EditText) findViewById(R.id.username_register);
    	EditText phone = (EditText) findViewById(R.id.phone_register);
    	EditText pass = (EditText) findViewById(R.id.pwd_register);
    	
    	if(phone.getText().toString().compareTo("") > 0 &
    			username.getText().toString().compareTo("") > 0 & 
    			pass.getText().toString().compareTo("") > 0){
    		if(registerUser == null){
    			Log.d("functionRegister", "if - true: registerUser = null");
    			registerUser = new User(username.getText().toString(), name.getText().toString(), 
            			phone.getText().toString(), pass.getText().toString());
    			Log.d("Login", registerUser.toString());
    			checkNumber(phone.getText().toString());
    		}
    		else{
    			Log.d("functionRegister", "if - false: registerUser = " + registerUser);
    			if(registerUser.isNumberChecked()){
    				Log.d("functionRegister", "if - true: Number is verified");
    				sendRegisterInformation();
    			}
    			else{
    				Log.d("functionRegister", "if - true: Number is not verified");
    				checkNumber(phone.getText().toString());
    			}
    		}	
    		
    	}
    	else{
    		//Toast - Needs Username and pass
    	}
    	
    }
    
    /**
	 * @Function
	 * 		checkNumber
	 * @Access
	 * 		Private
	 * @Description
	 * 		Method to send an SMS to the specified phone number 
	 * @return
	 * 		void
     * @param phoneNumber
     */
    private void checkNumber(String phoneNumber){
        Log.d("TestRegister", "checkNumber: phoneNumber = " + phoneNumber);
        String message = MESSAGE + "FamFri";
        
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
    	Log.d("message", "Before Receiver");
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
    }
    
    /*private void sendSMS(String number, String message){
    	PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
    	Log.d("message", "Before Receiver");
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(number, null, message, sentPI, null);
    }*/

    @Override
    protected void onPause(){
        super.onPause();
        /*this.unregisterReceiver(sendB);
        this.unregisterReceiver(mInentReceiver);*/
    }

    @Override
    protected void onDestroy(){
    	super.onStop();
    	registerUser = null;
    	/*this.unregisterReceiver(sendB);
        this.unregisterReceiver(mInentReceiver);*/
    }
}
