package com.qhubb.qhubb;


import info.androidhive.slidingmenu.adapter.NavDrawerListAdapter;
import info.androidhive.slidingmenu.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.qhubb.qhubb.UpdateStatus.updateTwitterStatus;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.examples.oauth.GetAccessToken;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ViewTwitterActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	private static SharedPreferences mSharedPreferences;
	String statuss;
	
	ListView msgList;
    ArrayList<MessageDetails> details;
    AdapterView.AdapterContextMenuInfo info;
	Button btnViewTwitterBack;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_twitter);
		  mSharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
		postqueue();
		
		
		 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	     mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	     mDrawerLayout.setVisibility(0);
	     
	        

	        
	        
	        mTitle = mDrawerTitle = getTitle();
	        // load slide menu items
	        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
	        
	        //nav drawer icons from resource
	     
	        navDrawerItems = new ArrayList<NavDrawerItem>();

	        // adding nav drawer items to array
	        // Home
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
	        // Find People
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
	        // Photos
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));

	        // Communities, Will add a counter here
	      
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));

	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
	
	    	mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
			// setting the nav drawer list adapter
			adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
			mDrawerList.setAdapter(adapter);

			// enabling action bar app icon and behaving it as toggle button
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, //nav menu toggle icon
					R.string.app_name, // nav drawer open - description for accessibility
					R.string.app_name // nav drawer close - description for accessibility
			) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(mTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					invalidateOptionsMenu();
				}

				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(mDrawerTitle);
					// calling onPrepareOptionsMenu() to hide action bar icons
					invalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				// on first time display view for first nav item
				displayView(0);
			}
		}

		/**
		 * Slide menu item click listener
		 * */
		private class SlideMenuClickListener implements ListView.OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
			
						displayView(position);
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// toggle nav drawer on selecting action bar app icon/title
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
			// Handle action bar actions click
			switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
			case R.id.add:
				startActivity(new Intent(ViewTwitterActivity.this,AddQueuePost.class));
				return true;
			case R.id.edit:
				startActivity(new Intent(ViewTwitterActivity.this,UpdateQueuePost.class));

				return true;
			
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		/* *
		 * Called when invalidateOptionsMenu() is triggered
		 */
		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			// if nav drawer is opened, hide the action items
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			menu.findItem(R.id.add).setVisible(!drawerOpen);
			return super.onPrepareOptionsMenu(menu);
		}

		/**
		 * Diplaying fragment view for selected nav drawer list item
		 * */
		private void displayView(int position) {
			// update the main content by replacing fragments
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new Welcomepage();
				break;
			case 1:
				fragment = new Tweetthroughfragment();
				break;
			case 2:
				fragment = new Timelines();
				break;
			case 3:
				fragment = new QueuePostthroughfragment();
				break;
			case 4:
				fragment = new Searchthroughfragment();
				break;
			

			default:
				break;
			}

			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();

				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navMenuTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				// error in creating fragment
				Log.e("MainActivity", "Error in creating fragment");
			}
		}

		@Override
		public void setTitle(CharSequence title) {
			mTitle = title;
			getActionBar().setTitle(mTitle);
		}

		/**
		 * When using the ActionBarDrawerToggle, you must call it during
		 * onPostCreate() and onConfigurationChanged()...
		 */

		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			// Sync the toggle state after onRestoreInstanceState has occurred.
			mDrawerToggle.syncState();
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			// Pass any configuration change to the drawer toggls
			mDrawerToggle.onConfigurationChanged(newConfig);
		}

		@Override
		public void onBackPressed() {
			startActivity(new Intent(getApplicationContext(),MainActivity.class));
	}
		public void postqueue()
		{
			DatabaseHandler db;
			List<MeetingAttribute> contacts;
			Calendar c = Calendar.getInstance(); 
			db=new DatabaseHandler(ViewTwitterActivity.this);
			
		      
		        
		        contacts = db.getAllQueuedata();    
				 
		        for (MeetingAttribute cn : contacts) {
						String[] time = cn.getTime().split(":");
						int hours=Integer.parseInt(time[0]);
						int mint=Integer.parseInt(time[1]);
						String[] date=cn.getDate().split("/");
						int month=Integer.parseInt(date[0]);
						int day=Integer.parseInt(date[1]);
						int year=Integer.parseInt(date[2]);
						int seconds = c.get(Calendar.SECOND);
						if(year<=c.get(Calendar.YEAR))
						{
							if(month<(c.get(Calendar.MONTH)+1))
							{
								db.deleteQueue(cn.getId());
								statuss=cn.getstutus();
								new updateTwitterStatus().execute(statuss);
								Toast.makeText(ViewTwitterActivity.this, "queue posted", Toast.LENGTH_SHORT).show();
								MainActivity.accessTokenKey=null;
								MainActivity.accessTokenKeySecret=null;
								startActivity(new Intent(getApplicationContext(),MainActivity.class));
							}
							if(month==(c.get(Calendar.MONTH)+1))
							{
								if(day<=c.get(Calendar.DAY_OF_MONTH))
								{
									if(hours<=c.get(Calendar.HOUR_OF_DAY))
									{
										if(mint<=c.get(Calendar.MINUTE))
										{
											db.deleteQueue(cn.getId());
											statuss=cn.getstutus();
											new updateTwitterStatus().execute(statuss);											
											Toast.makeText(ViewTwitterActivity.this, "queue posted complete date", Toast.LENGTH_SHORT).show();
											startActivity(new Intent(getApplicationContext(),MainActivity.class));
											MainActivity.accessTokenKey=null;
											MainActivity.accessTokenKeySecret=null;

										}
									}
								}
							}
						}
						
		        }
		}

		class updateTwitterStatus extends AsyncTask<String, String, String> {

			/**Z 	
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				
			}

			/**
			 * getting Places JSON
			 * */
			protected String doInBackground(String... args) {
				Log.d("Tweet Text", "> " + args[0]);
				String status = args[0];
				try {
					ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(MainActivity.consumerKey);
					builder.setOAuthConsumerSecret(MainActivity.consumerSecret);
					
					// Access Token 
					String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
					// Access Token Secret
					String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
					
					AccessToken accessToken = new AccessToken(access_token, access_token_secret);
					Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
					
					// Update status
					twitter4j.Status response = twitter.updateStatus(status);
					
					
					
					Log.d("Status", "> " + response.getText());
				} catch (TwitterException e) {
					// Error in updating status
					Log.d("Twitter Update Error", e.getMessage());
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog and show
			 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
			 * from background thread, otherwise you will get error
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				
				
				Toast.makeText(getApplicationContext(),"Tweet updated Successfully",Toast.LENGTH_SHORT).show();
				
				
			}
		}
		
		}


		
	
