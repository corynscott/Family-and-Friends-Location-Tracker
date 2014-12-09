package map.teamc.com.maplocationproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 		Class that manage the Register Activity
 *
 */
public class RegisterActivity extends ActionBarActivity{
	
	/*
	 * The object of the client makes the calls to the server
	 */
    private Client client = new Client();
    
    /*
	 * SharedPreferences object that will get information stored in the application
	 */
    private SharedPreferences preferences;
    
    /*
     * String that contains the name of the application SharedPreferences
     */
    public static final String MyPREFERENCES = "MyPrefs";
    
    /*
     * The object of the user that will be stored after the registration and it will be
     * 		sent to the server
     */
    User registerUser;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_register);
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
        //int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 		Button code to send the registration information if the verification code is correct
     * @param view
     * 		Button view
     * 
     */
    public void functionRegister(View view){
    	Log.d("TestRegister", "functionRegister");
    	EditText name = (EditText) findViewById(R.id.name_register);
    	EditText username = (EditText) findViewById(R.id.username_register);
    	EditText code = (EditText) findViewById(R.id.code_register);
    	EditText pass = (EditText) findViewById(R.id.pwd_register);
    	
    	preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	if(code.getText().toString().compareTo("") > 0 &
    			username.getText().toString().compareTo("") > 0 & 
    			pass.getText().toString().compareTo("") > 0){
			if(isCode(code.getText().toString())){
				Log.d("functionRegister", "if true: code is correct");
				registerUser = new User(username.getText().toString(), name.getText().toString(), 
    					preferences.getString("phoneNumber_FamFri", ""), pass.getText().toString());
				sendRegisterInformation();
			}
			else{
				Log.d("functionRegister", "if false: code is not correct");
				Toast.makeText(this, "The code is not correct", 
    					Toast.LENGTH_LONG).show();
			}
    	}
    	else{
    		Log.d("functionRegister", "if false: enter all the information");
    		Toast.makeText(this, "All information is required", 
					Toast.LENGTH_LONG).show();
    	}
    }
    
    /**
     * 		Sends the registration information to the server
     * 
     */
    private void sendRegisterInformation(){
        Log.d("TestRegister", "sendRegisterInformation");
        
        client.registerUser(this, registerUser);
        
    }
    
    /**
     * 		Code that checks if the verification in the user's input is the same as the one in SharedPreferences
     * @param code
     * 		String value of the code from user's input
     * @return
     * 		A boolean value with the value true if the codes match
     * 
     */
    private boolean isCode(String code){
        Log.d("TestRegister", "isCode");
        boolean response = false;
        if(code.compareTo(preferences.getString("verificationPhone_FamFri", "")) == 0){
        	Log.d("isCode", "if true: verification");
        	response = true;
        }
        else{
        	Log.d("isCode", "if false: no verification");
        	response = false;
        }
        return response;
    }

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
