package cl.hh.android.PriceWatch;

import java.util.HashMap;
import java.util.LinkedList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class Twitter extends Activity {
	private final Activity activity = this;
	static final String DATA_TITLE = "T";
	static final String DATA_LINK  = "L";
	static final String TAG = "ConsultaPrecios";
	private static String feedUrl = "http://twitter.com/statuses/user_timeline/falabella_cl.rss";
	DialogBuilder twitterDialog = new DialogBuilder(activity,TAG);
	
	static LinkedList<HashMap<String, String>> tweets;

	private ProgressDialog progressDialog;
	private ListView lvTweets;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);
        
        Button btn = (Button) findViewById(R.id.btnLoad);     
        btn.setOnClickListener(new OnClickListener() {			
    		@Override
    		public void onClick(View v) {
    			lvTweets = (ListView) findViewById(R.id.lstData);

    			if (lvTweets.getAdapter() != null) {
    				twitterDialog.showYNDialog(R.string.warning, "ya ha cargado tweets, desea de hacerlo de nuevo?", "Si", "No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   loadData();
				           }
				       });  				
    			} else {
    				loadData();
    			}
    		}
    	});
        
        lvTweets = (ListView) findViewById(R.id.lstData);
        lvTweets.setBackgroundColor(Color.WHITE); 
        lvTweets.setCacheColorHint(Color.WHITE);
        lvTweets.setOnItemClickListener(new OnItemClickListener() {

    		@Override
    		public void onItemClick(AdapterView<?> av, View v, int position,
    				long id) {

    			HashMap<String, String> entry = tweets.get(position);
    			Intent browserAction = new Intent(activity, MyWebView.class);
    			browserAction.putExtra("theURL", entry.get(DATA_LINK));
    			startActivity(browserAction);				
    		}
    	}); 
        
        loadData();
    }
    

    private void setData(LinkedList<HashMap<String, String>> data){
    	String theNews[] = new String[] {DATA_TITLE, DATA_LINK};
    	//int theResources[] = new int[] {android.R.id.text1, android.R.id.text2};
    	int titleResources[] = new int[] {android.R.id.text1};
    	SimpleAdapter sAdapter = new SimpleAdapter(getApplicationContext(), data, R.layout.text_list, theNews, titleResources);
    	lvTweets = (ListView) findViewById(R.id.lstData);
    	lvTweets.setAdapter(sAdapter);
    }   
    

    private void loadData() {
    	progressDialog = ProgressDialog.show(Twitter.this,"","Cargando tweets...",false);
    	
    	new Thread(new Runnable(){
    		@Override
    		public void run() {
    			XMLParser parser = new XMLParser(feedUrl); 
                Message msg = tweetsProgressHandler.obtainMessage();
                msg.obj = parser.parse();
    			tweetsProgressHandler.sendMessage(msg);
    		}}).start();
    }   
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.twitter_options, menu);
		return true;
	}
      
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
      // Handle item selection
      switch (item.getItemId()) {
      case R.id.itemRefresh:
    	  loadData();
    	  return true;
      case R.id.itemConsulta:
          Intent consulta = new Intent(this, Watcher.class);
          consulta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(consulta);
    	  return true;
      case R.id.itemMapa:
          Uri uri1 = Uri.parse("geo:0,0?q=Falabella"); 
          final Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri1); 
          startActivity(mapIntent);
          return true;
      case R.id.itemRss:
      	  Intent rssIntent = new Intent(this, RSS.class);
      	  rssIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      	  startActivity(rssIntent);
      	  return true;   
      default:
          return super.onOptionsItemSelected(item);
      }
	}
    
	private final Handler tweetsProgressHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				tweets = (LinkedList<HashMap<String, String>>)msg.obj;
				setData(tweets);					
			}
			try {
				progressDialog.dismiss();
		        progressDialog = null;
		    } catch (Exception e) {
		        // nothing
		    }
			
	    }
	};
}