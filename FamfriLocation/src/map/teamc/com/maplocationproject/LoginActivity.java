package map.teamc.com.maplocationproject;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
 * 		Class that manage the Login Activity
 *
 */
@SuppressLint("SimpleDateFormat")
public class LoginActivity extends ActionBarActivity {
    
	/*
	 * SharedPreferences object that will get information stored in the application
	 */
    SharedPreferences preferences;
    
    /*
     * String that contains the name of the application SharedPreferences
     */
    public static final String MyPREFERENCES = "MyPrefs";
    
    /*
	 * The object of the client makes the calls to the server
	 */
    private Client client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestLogin", "onCreate: Bundle: " + savedInstanceState);
        client = new Client();
        authenticate();
    	setContentView(R.layout.test_login);        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_menu_activity, menu);
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
     * 		Code that checks whether there are already logins credentials; if true, make a call to the server to authenticate;
     * 			if false, do nothing
     * @return
     * 		A boolean value with the value true if there are already credentials stored
     * 
     */
    private boolean authenticate(){
    	Log.d("TestLogin", "authenticate");
    	boolean result = false;
    	 preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         if(preferences.contains("user_famfri") &
                 preferences.contains("pwd_famfri")){   
        	 result = true;
             client.sendCredential(this);
         }
         else{
             Log.d("NoDATA","NODATA");
         }
         return result;
    }
    
    /**
     * 		Button code that is pressed after filling the login details in the Login screen
     * @param view
     * 		Button view
     * 
     */
    public void functionLogin(View view){
    	Log.d("TestLogin", "functionLogin: view = " + view);
        EditText userName = (EditText) findViewById(R.id.user_login);
        EditText pass = (EditText) findViewById(R.id.pass_login);
        String userString = "";
        String pwd = "";

        userString = userName.getText().toString();
        pwd = pass.getText().toString();
        
        client.firstLogin(this, userString, pwd);
    }

    /**
     * 		Button code that is pressed to register to the app.
     * 		It could send to the phone verification screen or to the registration screen
     * @param view
     * 		Button view
     * 
     */
    public void functionRegister(View view){
    	Log.d("TestLogin", "functionRegister");
    	preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	if(preferences.contains("verificationPhone_FamFri") & 
    			preferences.contains("expirationDate_FamFri")){
    		Log.d("functionRegister", "if true: user has entered phone");
    		if(!hasExpired(preferences.getLong("expirationDate_FamFri", 0l))){
    			Log.d("functionRegister", "if true: verification code has not expired");
    			Intent register = new Intent(this, RegisterActivity.class);
                this.startActivity(register);
    		}
    		else{
    			Log.d("functionRegister", "if false: verification code has expired");
    			Intent verification = new Intent(this, PhoneVerificationActivity.class);
                this.startActivity(verification);
    		}
    	}
    	else{
    		Log.d("functionRegister", "if false: user has not entered phone");
    		Intent verification = new Intent(this, PhoneVerificationActivity.class);
            this.startActivity(verification);
    	}
    }

    /**
     * 		Checks if the verification code has expired
     * @param expirationDate
     * 		Long variable that contains the date where the code will expire.
     * @return
     * 		A boolean value: if true, the verification code has expired
     * 
     */
    public boolean hasExpired(long expirationDate){
    	Log.d("TestLogin","hasExpired: expirationDate = " + expirationDate);
    	boolean response = true;;
    	Calendar today = Calendar.getInstance();
    	Calendar verificationDate = returnDate(expirationDate);
    	if(verificationDate != null){
    		Log.d("hasExpired", "if true: verificationDate = " + verificationDate);
    		if(today.compareTo(verificationDate) <= 0){
    			Log.d("hasExpired", "if true: verification code has not expired");
    			response = false;
    		}
    		else{
    			Log.d("hasExpired", "if false: verification code has expired");
    			Toast.makeText(getApplicationContext(), "The verification code has expired", 
    					Toast.LENGTH_LONG).show();
    			deleteVerificationCode();
    	        response = true;
    		}
    	}
    	else{
    		Log.d("hasExpired", "if false: date parse had a problem");
    		Toast.makeText(getApplicationContext(), "Date data contains errors\nTry verification again", 
					Toast.LENGTH_LONG).show();
    		deleteVerificationCode();
    		response = true;    		
    	}
    	return response;
    }
    
    /**
     * 		Returns a Calendar object based on numeric date
     * @param date
     * 		Long variable that contains the date.
     * @return
     * 		A Calendar value with the date
     * 
     */
    public Calendar returnDate(long date){
    	Log.d("TestLogin", "returnDate: date = " + date);
    	Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
    	return calendar;
    }
    
    /**
     * 		Delete the credentials from SharedPreferences
     * 
     */
    private void deleteVerificationCode(){
    	SharedPreferences.Editor editor = preferences.edit();
    	editor.remove("phoneNumber_FamFri");
        editor.remove("verificationPhone_FamFri");
        editor.remove("expirationDate_FamFri");
        editor.commit();
    }

}
