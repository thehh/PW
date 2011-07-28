package cl.hh.android.PriceWatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class DialogBuilder {
	Context appContext;
	String TAG;
	
	public DialogBuilder(){
		
	}
	
	public DialogBuilder(Context c, String S){
		this.appContext = c;
		this.TAG = S;
	}
	
    void showDialog(int title, String message) {
    	try{
    	    AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
    	    builder.setTitle(title);
    	    builder.setMessage(message);
    	    builder.setPositiveButton("Si", null);
    	    builder.show();
    	}
    	catch (Exception e){
    	    Log.i(TAG, e.toString());
    	}
	}
    
    void showDialog(int title, String message, String strAffirmative){
    	try{
    	    AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
    	    builder.setTitle(title);
    	    builder.setMessage(message);
    	    builder.setPositiveButton(strAffirmative, null);
    	    builder.show();
    	}
    	catch (Exception e){
    	    Log.i(TAG, e.toString());
    	}
    }
    
    void showYNDialog(int title, String message, String stringAffirmative, String stringNegative, DialogInterface.OnClickListener affChoice){
    	try{
    	    AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
    	    builder.setTitle(title);
    	    builder.setMessage(message);
    	    builder.setPositiveButton(stringAffirmative, affChoice);
    	    builder.setNeutralButton(stringNegative, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();					            
		           }
		       });
    	    builder.show();
    	}
    	catch (Exception e){
    	    Log.i(TAG, e.toString());
    	}
    }

    void showCancelDialog(int title, String message, String strNegative){
    	try{
    	    AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
    	    builder.setTitle(title);
    	    builder.setMessage(message);
    	    builder.setNeutralButton(strNegative, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();					            
		           }
		       });
    	    builder.show();
    	}
    	catch (Exception e){
    	    Log.i(TAG, e.toString());
    	}
    }
    
    void showAffirmativeDialog(int title, String message, String strAffirmative, DialogInterface.OnClickListener affChoice){
    	try{
    	    AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
    	    builder.setTitle(title);
    	    builder.setMessage(message);
    	    builder.setNeutralButton(strAffirmative, affChoice);
    	    builder.show();
    	}
    	catch (Exception e){
    	    Log.i(TAG, e.toString());
    	}
    }
    
    void setContext(Context c){
    	appContext = c;
    }
    
    Context getContext(){
    	return this.appContext;
    }
    
    void setTag(String S){
    	this.TAG = S;
    }
    
    String getTag(){
    	return this.TAG;
    }

}