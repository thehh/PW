package cl.hh.android.PriceWatch;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Watcher extends Activity{
	private final Activity activity = this;
	
	private static final String phoneNumber = "+5623906140";
	private static final String PACKAGE = "com.google.zxing.client.android";
	private static final String TAG = "ConsultaPrecios";
	private static final  String F_URL = "http://www.falabella.com/webapp/commerce/command/ExecMacro/falabella/macros/resp_busq_avanz.d2w/report?npag=1&sprod=0&ConFoto=&crmarca=0&crprecio=0&sfot=0&crdepto=0&busqkey=";
	
	private final DialogBuilder dialog = new DialogBuilder(this,TAG);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
              
        //listener para botones 
        findViewById(R.id.scanButton).setOnClickListener(scanCode);
        findViewById(R.id.textSearchButton).setOnClickListener(buscarFree);
        findViewById(R.id.buttonCall).setOnClickListener(buttonCall);
        
        trimCache(this);
        
    }
    
    public final Button.OnClickListener buscarFree = new Button.OnClickListener() {
        @Override
		public void onClick(View v) {
        	EditText name = (EditText)findViewById(R.id.barraBuscarTxt);
        	name.requestFocus();
	        String searchString = name.getText().toString();
	        String URL;
	        if(searchString == null || searchString.equals(""))
	        	URL="http://www.falabella.com/webapp/commerce/command/ExecMacro/falabella/macros/home.d2w/report";
	        else
	        	URL = F_URL + searchString;
	        Intent i = new Intent(getApplicationContext(), MyWebView.class);
	        i.putExtra("theURL", URL);
	        startActivity(i);
        }
      };
      
   
    public final Button.OnClickListener scanCode = new Button.OnClickListener() {
        @Override
		public void onClick(View v) {
        	try{
        		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        		startActivityForResult(intent, 0);
        	} 
        	catch (ActivityNotFoundException e) {
        	    showDownloadDialog(activity, "Atencion", "Necesita instalar el programa Barcode Scanner \nDesea instalarlo?", "Si", "No");
        	}
        }
      };
      
	public final Button.OnClickListener buttonCall = new Button.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	try {
	    		Intent callIntent = new Intent(Intent.ACTION_CALL);
	    	    callIntent.setData(Uri.parse("tel:" + phoneNumber));
	    	    startActivity(callIntent);
	    	} catch (ActivityNotFoundException e) {
	    	    Log.e(TAG, "La llamada Fallo", e);
	    	}
	    }
	};
          
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	      if (resultCode == RESULT_OK) {
	        String contents = intent.getStringExtra("SCAN_RESULT");
	        if(contents.startsWith("200")){
	        	String finalContent = contents.substring(3, 10);
	        	Log.d(TAG,"SKU: " + finalContent);
	        	String URL = F_URL + finalContent;
	        	Intent i = new Intent(this, MyWebView.class);
	        	i.putExtra("theURL", URL);
	        	startActivity(i);
	        }
	        else{
	        	dialog.showDialog(R.string.warning, "No se puede buscar este artículo en la web");
	        }
	      } else if (resultCode == RESULT_CANCELED) {
	    	  //dialog.showDialog(R.string.result_failed, getString(R.string.result_failed_why));
	      } 
	    }
	}
      
     
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}
      
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
      // Handle item selection
      switch (item.getItemId()) {
      case R.id.itemMapa:
          Uri uri1 = Uri.parse("geo:0,0?q=Falabella"); 
          final Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri1); 
          startActivity(mapIntent);
          return true;
      case R.id.itemRSS:
      	  Intent intent = new Intent(this, RSS.class);
      	  startActivity(intent);
      	  return true;
      case R.id.itemTwitter:
      	  Intent twitter = new Intent(this, Twitter.class);
      	  startActivity(twitter);
      	  return true;  
      default:
          return super.onOptionsItemSelected(item);
      }
	}
      
      private static AlertDialog showDownloadDialog(final Activity activity,CharSequence stringTitle,CharSequence stringMessage,CharSequence stringButtonYes, CharSequence stringButtonNo) {
    	  AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
    	  downloadDialog.setTitle(stringTitle);
    	  downloadDialog.setMessage(stringMessage);
    	  downloadDialog.setPositiveButton(stringButtonYes, new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialogInterface, int i) {
    			  Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
    			  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    			  activity.startActivity(intent);
    		  }
    	  });
    	  downloadDialog.setNegativeButton(stringButtonNo, new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialogInterface, int i) {}
    	  });
    	  return downloadDialog.show();
      }
      
      @Override 
      protected void onStop(){ 
    	  super.onStop(); 
      } 

      @Override 
      protected void onDestroy() { 
    	  super.onDestroy(); 
    	  try { 
    		  trimCache(this); 
    	  } catch (Exception e) { 
    		  e.printStackTrace(); 
    	  } 
      } 

      public static void trimCache(Context context) { 
	      try { 
	    	File dir = context.getCacheDir(); 
	    	if (dir != null && dir.isDirectory()) { 
	    	  deleteDir(dir); 
	    	} 
	      } catch (Exception e) { 
	      	Log.d(TAG, e.toString());
	      } 
      } 

      public static boolean deleteDir(File dir) { 
    	  if (dir != null && dir.isDirectory()) { 
    		  String[] children = dir.list(); 
    		  for (int i = 0; i < children.length; i++) { 
    			  boolean success = deleteDir(new File(dir, children[i])); 
    			  if (!success) { 
    				  return false; 
    			  } 
    		  } 
    	  } 
      // The directory is now empty so delete it 
    	  return dir.delete(); 
      } 
      
}
