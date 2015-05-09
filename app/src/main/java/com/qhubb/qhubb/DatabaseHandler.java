package com.qhubb.qhubb;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "queuedposts";

	// Contacts table name
	private static final String TABLE_QUEUE = "queue";
	
	//private static final String TABLE_MEETING = "meeting";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	/*private static final String KEY_GROUPNAME = "gname";
	private static final String KEY_CONTACTNAME = "cname";
	private static final String KEY_CONTACTNUMBER = "cnumber";*/
	
	//private static final String TABLE_ASSIGNTASK = "assigntask";
	private static final String KEY_STATUS = "status";
	private static final String KEY_TIME = "time";
	private static final String KEY_DATE = "date";
	
	

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_QUEUE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_STATUS + " TEXT,"
				+ KEY_TIME + " TEXT," +KEY_DATE+ " TEXT" + ")";
		
		// Tag table create statement
	    /* String CREATE_TABLETASK = "CREATE TABLE " + TABLE_ASSIGNTASK
	            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GROUPNAME + " TEXT,"
	            + KEY_CONTACTNAME + " TEXT,"
	            + KEY_TASKINFORMATION + " TEXT" + ")";
	  // Tag table create statement
	     String CREATE_TABLEMEETING = "CREATE TABLE " + TABLE_MEETING
	            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOPIC + " TEXT,"       
	            + KEY_STATUS + " TEXT,"
	            + KEY_TIME + " TEXT,"
	            + KEY_DATE + " TEXT,"
	            + KEY_GROUPNAME + " TEXT" + ")";
	     */
		
		db.execSQL(CREATE_CONTACTS_TABLE);
		//db.execSQL(CREATE_TABLETASK);
		//db.execSQL(CREATE_TABLEMEETING);
	}
	

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEUE);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	/*void Adddata(GroupAttributes contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_GROUPNAME, contact.getGroupname()); // Contact Name
		values.put(KEY_CONTACTNAME, contact.getContactname()); // Contact Phone
		values.put(KEY_CONTACTNUMBER, contact.getContactnumber());
		

		// Inserting Row
		db.insert(TABLE_QUEUE, null, values);
		db.close(); // Closing database connection
	}
	
	

	// Getting single contact
	GroupAttributes getdata(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_QUEUE, new String[] { KEY_ID,
				KEY_GROUPNAME,KEY_CONTACTNAME ,KEY_CONTACTNUMBER }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		GroupAttributes contact = new GroupAttributes(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2),cursor.getString(3));
		// return contact
		return contact;
	}
	
	// Getting All Contacts
	public List<GroupAttributes> getAlldata() {
		List<GroupAttributes> contactList = new ArrayList<GroupAttributes>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEUE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				GroupAttributes contact = new GroupAttributes();
				contact.setId(Integer.parseInt(cursor.getString(0)));
				contact.setGroupname(cursor.getString(1));
				contact.setContactname(cursor.getString(2));
				contact.setContactnumber(cursor.getString(3));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}
	
	//Assign Task table 
	
	
	void AddTaskdata(AssignTaskAttributes contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_GROUPNAME, contact.getGroupname()); // Contact Name
		values.put(KEY_CONTACTNAME, contact.getContactname()); // Contact Phone
		values.put(KEY_TASKINFORMATION, contact.getTaskinformation());
		

		// Inserting Row
		db.insert(TABLE_ASSIGNTASK, null, values);
		db.close(); // Closing database connection
	}
	
	
	// Getting single contact
	AssignTaskAttributes getTaskdata(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_ASSIGNTASK, new String[] { KEY_ID,
					KEY_GROUPNAME,KEY_CONTACTNAME ,KEY_TASKINFORMATION }, KEY_ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();

			AssignTaskAttributes contact = new AssignTaskAttributes(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),cursor.getString(3));
			// return contact
			return contact;
		}
		
		// Getting All Contacts
	public List<AssignTaskAttributes> getAllTaskdata() {
			List<AssignTaskAttributes> contactList = new ArrayList<AssignTaskAttributes>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNTASK;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					AssignTaskAttributes contact = new AssignTaskAttributes();
					contact.setId(Integer.parseInt(cursor.getString(0)));
					contact.setGroupname(cursor.getString(1));
					contact.setContactname(cursor.getString(2));
					contact.setTaskinformation(cursor.getString(3));
					// Adding contact to list
					contactList.add(contact);
				} while (cursor.moveToNext());
			}

			// return contact list
			return contactList;
		}
	*/
	//SEdule meeting table
	
	
		void AddQueue(MeetingAttribute contact) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
		
			values.put(KEY_STATUS, contact.getstutus()); // Contact Phone
			values.put(KEY_TIME, contact.getTime());
			values.put(KEY_DATE, contact.getDate()); // Contact Phone
			

			// Inserting Row
			db.insert(TABLE_QUEUE, null, values);
			db.close(); // Closing database connection
		}
		
		
		// Getting single contact
		MeetingAttribute GetQueueData(int id) {
				SQLiteDatabase db = this.getReadableDatabase();

				Cursor cursor = db.query(TABLE_QUEUE, new String[] { KEY_ID,
						KEY_STATUS,KEY_TIME,KEY_DATE}, KEY_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();

				MeetingAttribute contact = new MeetingAttribute(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1), cursor.getString(2),cursor.getString(3));
				// return contact
				return contact;
			}
			
			// Getting All Contacts
		public List<MeetingAttribute> getAllQueuedata() {
				List<MeetingAttribute> contactList = new ArrayList<MeetingAttribute>();
				// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_QUEUE;

				SQLiteDatabase db = this.getWritableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						MeetingAttribute contact = new MeetingAttribute();
						contact.setId(Integer.parseInt(cursor.getString(0)));
						contact.setstatus(cursor.getString(1));
						contact.setTime(cursor.getString(2));
						contact.setDate(cursor.getString(3));
					
						// Adding contact to list
						contactList.add(contact);
					} while (cursor.moveToNext());
				}

				// return contact list
				return contactList;
			}
		  public void deleteQueue(long postid) {
		        SQLiteDatabase db = this.getWritableDatabase();
		        db.delete(TABLE_QUEUE, KEY_ID + " = ?",
		                new String[] { String.valueOf(postid) });
		    }
		  
		  public int updateQueue(MeetingAttribute data) {
			    SQLiteDatabase db = this.getWritableDatabase();
			 
			    ContentValues values = new ContentValues();
			    values.put(KEY_STATUS, data.getstutus());
			    values.put(KEY_TIME, data.getTime());
			    values.put(KEY_DATE, data.getDate());
			 
			    // updating row
			    return db.update(TABLE_QUEUE, values, KEY_ID + " = ?",
			            new String[] { String.valueOf(data.getId()) });
			}
		  public boolean updateContact(long rowId, String status, String time,String date) {
			  SQLiteDatabase db = this.getWritableDatabase();
			Log.v("value : ", rowId+ " : "+status + "time : "+time + " : "+ date);
			    ContentValues values = new ContentValues();
			    values.put(KEY_STATUS, status);
			    values.put(KEY_TIME, time);
			    values.put(KEY_DATE, date);
			    return db.update(TABLE_QUEUE, values, KEY_ID + "=" + rowId, null) > 0;
			  }
		
}
