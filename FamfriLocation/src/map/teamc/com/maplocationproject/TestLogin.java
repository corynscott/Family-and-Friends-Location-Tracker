package map.teamc.com.maplocationproject;

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


/**
 * Created by alex64 on 17/11/2014.
 */
public class TestLogin extends ActionBarActivity {

	String user = "alex";
    String passw = "pass";
    String userString = "";
    String pwd = "";

    SharedPreferences preferences;

    public static final String MyPREFERENCES = "MyPrefs";
    
    private Client client = new Client();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestLogin", "onCreate: Bundle: " + savedInstanceState);
        setContentView(R.layout.test_login);

        authenticate();
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
     * 
     */
    public void authenticate(){
    	Log.d("TestLogin", "authenticate");
    	//Check if there are preferences already in the system
    	 preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         if(preferences.contains("user_famfri") &
                 preferences.contains("pwd_famfri")){
             
             client.sendCredential(this);
             
             /*Potentially, this part will be eliminated*/
             //The map activity should start in the sendCredentials call
        	 /*userString = preferences.getString("user_famfri", "");
             pwd = preferences.getString("pwd_famfri","");
             if(user.compareTo(userString) == 0 &
                     passw.compareTo(pwd) == 0){
            	 Intent map = new Intent(this, MapActivity.class);
             	map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             	Log.d("login","Going to map");
             	this.startActivity(map);
             	finish();
             }*/
         }
         else{
             Log.d("NoDATA","NODATA");
         }
    }
    
    /**
     * 
     * @param view
     */
    public void functionLogin(View view){

        EditText userName = (EditText) findViewById(R.id.user_login);
        EditText pass = (EditText) findViewById(R.id.pass_login);


        userString = userName.getText().toString();
        pwd = pass.getText().toString();
        
        client.firstLogin(this, userString, pwd);
        
        //Potentially it will be deleted
        //**********Change for the called to the server
        /*if(user.compareTo(userString) == 0 &
                passw.compareTo(pwd) == 0){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_famfri",userString);
            editor.putString("pwd_famfri",pwd);
            editor.commit();
            Intent map = new Intent(this, MapActivity.class);
            map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("login","Going to map");
            this.startActivity(map);
            finish();
        }
        else{
            Log.d("Login", "Error Login");
        }*/
        //********
    }

    public void functionRegister(View view){
    	Intent register = new Intent(this, TestRegister.class);
        this.startActivity(register);
        finish();
    }


}
