package com.skytree.simpledemo;




import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;


public class DataBaseHelper extends SQLiteOpenHelper {


	private static String DB_PATH = "/data/data/com.skytree.simpledemo/databases/";

	private static String DATABASE_NAME = "PustakaDatabase";



	private static final int DATABASE_VERSION = 1;

	// Database Name

	// Contacts table name
	private static final String TABLE_CONTACTS = "Pustaka";

	private static final String TABLE_CATEGORY= "Category";

	private static final String KEY_IDS = "catid";
	private static final String KEY_BOOKNAMES = "catbooktypes";


	private static final String TABLE_BOOKMARK= "Bookmark";

	private static final String KEY_BOOKMARKNAME = "bookname";
	private static final String KEY_BOOKMARKNAMECHAPTER = "chapter";
	private static final String KEY_BOOKMARKNAMEPOS = "pos";
	private static final String KEY_BOOKMARKDATE = "date";


	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_book_title = "book_title";
	private static final String KEY_book_local_title = "book_local_title";
	private static final String KEY_author = "author";
	private static final String KEY_language = "language";
	private static final String KEY_genre = "genre";
	private static final String KEY_book_type = "book_type";
	private static final String KEY_img_url= "img_url";
	private static final String KEY_book_url = "book_url";


	public SQLiteDatabase myDataBase;




	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " TEXT,"+ KEY_book_title + " TEXT,"+KEY_book_local_title + " TEXT,"+ KEY_author + " TEXT,"+ KEY_language + " TEXT,"
				+ KEY_genre + " TEXT,"+ KEY_book_type + " TEXT,"+ KEY_img_url + " TEXT,"+ KEY_book_url + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);


		String CREATE_CONTACTS_TABLE1 = "CREATE TABLE " + TABLE_CATEGORY + "("+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_IDS + " TEXT,"+ KEY_BOOKNAMES + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE1);


		String CREATE_CONTACTS_TABLE2 = "CREATE TABLE " + TABLE_BOOKMARK + "("+ KEY_BOOKMARKNAME + " TEXT,"+ KEY_BOOKMARKNAMECHAPTER + " TEXT,"+KEY_BOOKMARKNAMEPOS + " TEXT,"+ KEY_BOOKMARKDATE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE2);


	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);


		// Create tables again
		onCreate(db);
	}

	public  void insertbookbyValue(String id, String book_title, String book_local_title, String author,
			String language,String genre,String book_type,String img_url,String book_url) {

		// TODO Auto-generated method stub

		book_title=book_title.trim();
		book_local_title=book_local_title.trim();
		author=author.trim();
		language=language.trim();
		genre=genre.trim();
		book_type=book_type.trim();

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put(KEY_ID, id); 
			values.put(KEY_book_title, book_title); 
			values.put(KEY_book_local_title, book_local_title); 
			values.put(KEY_author, author); 
			values.put(KEY_language, language); 
			values.put(KEY_genre, genre); 
			values.put(KEY_book_type, book_type); 
			values.put(KEY_img_url, img_url); 
			values.put(KEY_book_url, book_url); 

			// Inserting Row

			db.insert(TABLE_CONTACTS, null, values); 
			Log.d("inserted success",TABLE_CONTACTS);
			db.close(); // Closing database connection

		}

		catch(Exception e)
		{
			e.printStackTrace();
		}

	}


	public  void insertCategorybyValue(String id, String strbook_type_text) {

		// TODO Auto-generated method stub

		strbook_type_text=strbook_type_text.trim();


		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put(KEY_IDS, id); 
			values.put(KEY_BOOKNAMES, strbook_type_text); 


			// Inserting Row

			db.insert(TABLE_CATEGORY, null, values); 
			Log.d("inserted success",TABLE_CATEGORY);
			db.close(); // Closing database connection

		}

		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public String bookmarkcheck(String Bookname, String pos ) {

		// TODO Auto-generated method stub

		Cursor mCur = null;
		String ischeck="false";


		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put(KEY_BOOKMARKNAME, Bookname); 
			values.put(KEY_BOOKMARKNAMEPOS, pos); 

			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_BOOKMARK + " WHERE Bookname='"+Bookname+"' AND pos = '"+pos+"'",null);

			// Inserting Row
			if ( c.getCount()==0){
				ischeck="true";
			}
			else
			{

				ischeck="false";
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			ischeck="false";
		}
		finally{
			if (mCur!=null)
				mCur.close();

		}
		return ischeck;

	}


	public  String insertBookmarkbyValue(String Bookname, String pos ,String chaptername,Long time,String dateString ,Context cxt) {

		// TODO Auto-generated method stub
		Cursor c = null;
		Bookname=Bookname.trim();
		pos=pos.trim();
		String ischeck="false";
		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put(KEY_BOOKMARKNAME, Bookname); 
			values.put(KEY_BOOKMARKNAMECHAPTER, chaptername); 
			values.put(KEY_BOOKMARKNAMEPOS, pos); 
			values.put(KEY_BOOKMARKDATE, dateString); 

			c = db.rawQuery("SELECT * FROM " + TABLE_BOOKMARK + " WHERE Bookname='"+Bookname+"' AND pos = '"+pos+"'",null);

			// Inserting Row
			if ( c.getCount()==0){
				db.insert(TABLE_BOOKMARK, null, values); 
				ischeck="true";
			}
			else
			{
				ischeck="false";

				Toast.makeText(cxt, "Already Book Mark Added", Toast.LENGTH_SHORT).show();
			}
			Log.d("inserted success",TABLE_BOOKMARK);
			db.close(); // Closing database connection

		}

		catch (Exception e) {
			e.printStackTrace();
			ischeck="false";
		}
		finally{
			if (c!=null)
				c.close();

		}
		return ischeck;


	}

	public  String deleteBookmarkbyValue(String Bookname, String pos,Context cxt) {

		// TODO Auto-generated method stub
		Cursor c = null;
		Bookname=Bookname.trim();
		pos=pos.trim();
		String ischeck="false";
		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			c = db.rawQuery("Delect * FROM " + TABLE_BOOKMARK + " WHERE Bookname='"+Bookname+"' AND pos = '"+pos+"'",null);

			// Inserting Row
			if ( c.getCount()==0){
				ischeck="true";
			}
			else
			{
				ischeck="false";

				Toast.makeText(cxt, "Already Book Mark Added", Toast.LENGTH_SHORT).show();
			}
			Log.d("inserted success",TABLE_BOOKMARK);
			db.close(); // Closing database connection

		}

		catch (Exception e) {
			e.printStackTrace();
			ischeck="false";
		}
		finally{
			if (c!=null)
				c.close();

		}
		return ischeck;


	}





	public ArrayList<List<String>>  getbookmarkslist(String Bookname) {

		// TODO Auto-generated method stub
		Cursor c = null;
		Bookname=Bookname.trim();
		ArrayList<List<String>> BookmarkslistAll= new ArrayList<List<String>>();

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();

			c = db.rawQuery("SELECT * FROM " + TABLE_BOOKMARK + " WHERE Bookname='"+Bookname+"' ",null);
			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{

					do
					{
						List<String> listitems = new ArrayList<String>() ;
						String strbookname=c.getString(0);
						String strchapter=c.getString(1);
						String pos=c.getString(2);
						String date=c.getString(3);
						listitems.add(strbookname);
						listitems.add(strchapter);
						listitems.add(pos);
						listitems.add(date);
						BookmarkslistAll.add(listitems);
					}
					while(c.moveToNext());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return BookmarkslistAll;



	}


	public HashMap<String, List<ArrayList<String>>> getPurchasedRented()
	{
		HashMap<String, List<ArrayList<String>>> hm = new HashMap<String, List<ArrayList<String>>>();
		/*Cursor c = null;
		Cursor c1 = null;*/
		Cursor c2 = null;
		/*	Cursor c3 = null;
		Cursor c4 = null;*/
		/*List<ArrayList<String>>	Rent= new ArrayList<ArrayList<String>>();
		List<ArrayList<String>>	Purchase= new ArrayList<ArrayList<String>>();*/
		List<ArrayList<String>>	All= new ArrayList<ArrayList<String>>();
		/*List<ArrayList<String>>	Magazine= new ArrayList<ArrayList<String>>();
		List<ArrayList<String>>	Free= new ArrayList<ArrayList<String>>();*/

		// TODO Auto-generated method stub
		try{
			String mypath = DB_PATH + DATABASE_NAME;
			myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
			SQLiteDatabase db =myDataBase;// getReadableDatabase();

			/*c=db.rawQuery("select id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url from Pustaka where book_type='1'",null);
			if (c.getCount()>0)
			{
				if(c.moveToFirst()) // rent
				{
					do
					{
						ArrayList<String> listRent = new ArrayList<String>() ;
						String id=	c.getString(0);
						String book_title=	c.getString(1);
						String book_local_title=	c.getString(2);
						String author=	c.getString(3);
						String language=	c.getString(4);
						String genre=	c.getString(5);
						String	book_type=c.getString(6);
						String img_url=	c.getString(7);
						String book_url=c.getString(8);
						listRent.add(id);
						listRent.add(book_title);
						listRent.add(book_local_title);
						listRent.add(author);
						listRent.add(language);
						listRent.add(genre);
						listRent.add(book_type);
						listRent.add(img_url);
						listRent.add(book_url);
						Rent.add(listRent);
						hm.put("rent", Rent);

					}
					while(c.moveToNext());
				}
			}

			c1=db.rawQuery("select id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url from Pustaka where book_type='2'",null);
			if (c1.getCount()>0)
			{
				if(c1.moveToFirst()) //bought
				{
					do
					{
						ArrayList<String> listPurchase = new ArrayList<String>();
						String id=	c1.getString(0);
						String book_title=	c1.getString(1);
						String book_local_title=	c1.getString(2);
						String author=	c1.getString(3);
						String language=	c1.getString(4);
						String genre=	c1.getString(5);
						String	book_type=c1.getString(6);
						String img_url=	c1.getString(7);
						String book_url=c1.getString(8);
						listPurchase.add(id);
						listPurchase.add(book_title);
						listPurchase.add(book_local_title);
						listPurchase.add(author);
						listPurchase.add(language);
						listPurchase.add(genre);
						listPurchase.add(book_type);
						listPurchase.add(img_url);
						listPurchase.add(book_url);
						Purchase.add(listPurchase);
						hm.put("purchase", Purchase);
					}
					while(c1.moveToNext());
				}
			}*/

			c2=db.rawQuery("select id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url from Pustaka",null);
			if (c2.getCount()>0)
			{
				if(c2.moveToFirst())
				{
					do
					{
						ArrayList<String> listAll= new ArrayList<String>();
						String id=	c2.getString(0);
						String book_title=	c2.getString(1);
						String book_local_title=	c2.getString(2);
						String author=	c2.getString(3);
						String language=	c2.getString(4);
						String genre=	c2.getString(5);
						String	book_type=c2.getString(6);
						String img_url=	c2.getString(7);
						String book_url=c2.getString(8);
						listAll.add(id);
						listAll.add(book_title);
						listAll.add(book_local_title);
						listAll.add(author);
						listAll.add(language);
						listAll.add(genre);
						listAll.add(book_type);
						listAll.add(img_url);
						listAll.add(book_url);
						All.add(listAll);
						hm.put("all", All);
					}
					while(c2.moveToNext());
				}
			}

			/*c3=db.rawQuery("select id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url from Pustaka where book_type='3'",null);
			if (c3.getCount()>0)
			{
				if(c3.moveToFirst()) //magazine
				{
					do
					{
						ArrayList<String> listMagazine= new ArrayList<String>();
						String id=	c3.getString(0);
						String book_title=	c3.getString(1);
						String book_local_title=	c3.getString(2);
						String author=	c3.getString(3);
						String language=	c3.getString(4);
						String genre=	c3.getString(5);
						String	book_type=c3.getString(6);
						String img_url=	c3.getString(7);
						String book_url=c3.getString(8);
						listMagazine.add(id);
						listMagazine.add(book_title);
						listMagazine.add(book_local_title);
						listMagazine.add(author);
						listMagazine.add(language);
						listMagazine.add(genre);
						listMagazine.add(book_type);
						listMagazine.add(img_url);
						listMagazine.add(book_url);
						Magazine.add(listMagazine);
						hm.put("magazine", Magazine);
					}
					while(c3.moveToNext());
				}
			}

			c4=db.rawQuery("select id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url from Pustaka where book_type='4'",null);
			if (c4.getCount()>0)
			{
				if(c4.moveToFirst()) //free
				{
					do
					{
						ArrayList<String> listfree= new ArrayList<String>();
						String id=	c4.getString(0);
						String book_title=	c4.getString(1);
						String book_local_title=	c4.getString(2);
						String author=	c4.getString(3);
						String language=	c4.getString(4);
						String genre=	c4.getString(5);
						String	book_type=c4.getString(6);
						String img_url=	c4.getString(7);
						String book_url=c4.getString(8);
						listfree.add(id);
						listfree.add(book_title);
						listfree.add(book_local_title);
						listfree.add(author);
						listfree.add(language);
						listfree.add(genre);
						listfree.add(book_type);
						listfree.add(img_url);
						listfree.add(book_url);
						Free.add(listfree);
						hm.put("free", Free);
					}
					while(c4.moveToNext());
				}
			}*/
			return hm;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{
			/*if (c!=null)
				c.close();
			if (c1!=null)
				c1.close();*/
			if (c2!=null)
				c2.close();
			/*if (c3!=null)
				c3.close();
			if (c4!=null)
				c4.close();*/
		}
		return null;


	}


	public HashMap<String, List<ArrayList<String>>> getcategory(int flag)
	{
		HashMap<String, List<ArrayList<String>>> hm = new HashMap<String, List<ArrayList<String>>>();
		Cursor c = null;
		List<ArrayList<String>>	Category= new ArrayList<ArrayList<String>>();

		// TODO Auto-generated method stub
		try{
			String mypath = DB_PATH + DATABASE_NAME;
			myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
			SQLiteDatabase db =myDataBase;// getReadableDatabase();

			c=db.rawQuery("select id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url from Pustaka where book_type='"+flag+"'",null);
			if (c.getCount()>0)
			{
				if(c.moveToFirst()) // rent
				{
					do
					{
						ArrayList<String> listRent = new ArrayList<String>() ;
						String id=	c.getString(0);
						String book_title=	c.getString(1);
						String book_local_title=	c.getString(2);
						String author=	c.getString(3);
						String language=	c.getString(4);
						String genre=	c.getString(5);
						String	book_type=c.getString(6);
						String img_url=	c.getString(7);
						String book_url=c.getString(8);
						listRent.add(id);
						listRent.add(book_title);
						listRent.add(book_local_title);
						listRent.add(author);
						listRent.add(language);
						listRent.add(genre);
						listRent.add(book_type);
						listRent.add(img_url);
						listRent.add(book_url);
						Category.add(listRent);
						hm.put("category", Category);

					}
					while(c.moveToNext());
				}
			}


			return hm;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();

		}
		return null;


	}




	public String  deleteinternalbooks(String flag)
	{
		Cursor mCur = null;
		String ischeck="false";

		try {
			String mypath = DB_PATH + DATABASE_NAME;
			myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
			SQLiteDatabase db =myDataBase;// getReadableDatabase();

			mCur=db.rawQuery("select * from Pustaka where id='"+flag+"'",null);
			if(mCur.getCount()==0)
			{
				ischeck="true";
			}
			else
			{
				ischeck="false";
			}



		}
		catch (Exception e) {
			e.printStackTrace();
			ischeck="false";
		}
		finally{
			if (mCur!=null)
				mCur.close();

		}
		return ischeck;


	}



	public HashMap<String, List<ArrayList<String>>> getcategorylanwise(String Strspinlang)
	{
		HashMap<String, List<ArrayList<String>>> hm = new HashMap<String, List<ArrayList<String>>>();
		Cursor c = null;
		List<ArrayList<String>>	Category= new ArrayList<ArrayList<String>>();

		// TODO Auto-generated method stub
		try{
			String mypath = DB_PATH + DATABASE_NAME;
			myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
			SQLiteDatabase db =myDataBase;// getReadableDatabase();

			c=db.rawQuery("select * from Pustaka where language='"+Strspinlang+"'",null);
			if (c.getCount()>0)
			{
				if(c.moveToFirst()) // rent
				{
					do
					{
						ArrayList<String> listRent = new ArrayList<String>() ;
						String genre=	c.getString(5);
						String book_title=	c.getString(1);
						String author=	c.getString(3);
						listRent.add(genre);
						listRent.add(book_title);
						listRent.add(author);
						Category.add(listRent);
						hm.put("searchlangwise", Category);

					}
					while(c.moveToNext());
				}
			}


			return hm;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();

		}
		return null;


	}




	public HashMap<String, List<ArrayList<String>>> getcategoryfeed()
	{
		HashMap<String, List<ArrayList<String>>> hm = new HashMap<String, List<ArrayList<String>>>();
		Cursor c = null;
		List<ArrayList<String>>	Categoryfeed= new ArrayList<ArrayList<String>>();

		// TODO Auto-generated method stub
		try{
			String mypath = DB_PATH + DATABASE_NAME;
			myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
			SQLiteDatabase db =myDataBase;// getReadableDatabase();

			c=db.rawQuery("select catid,catbooktypes from Category",null);
			if (c.getCount()>0)
			{
				if(c.moveToFirst()) // Catfeed
				{
					do
					{
						ArrayList<String> listRent = new ArrayList<String>() ;
						String id=	c.getString(0);
						String book_title=	c.getString(1);
						listRent.add(id);
						listRent.add(book_title);
						Categoryfeed.add(listRent);
						hm.put("categoryfeed", Categoryfeed);

					}
					while(c.moveToNext());
				}
			}


			return hm;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();

		}
		return null;


	}


	public HashMap<String, ArrayList<List<String>>> getAuthorWiseBooks()
	{
		Cursor c = null;
		// TODO Auto-generated method stub
		try{
			SQLiteDatabase db =myDataBase;// getReadableDatabase();
			HashMap<String, ArrayList<List<String>>> hmAuthors = new HashMap<String, ArrayList<List<String>>>();
			c=db.rawQuery("SELECT DISTINCT author FROM Pustaka",null);

			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{
					do
					{
						ArrayList<List<String>> arrBooks=new ArrayList<List<String>>();
						String strAuthor="";
						if(c.getColumnCount()>0)
							strAuthor = c.getString(0);
						Cursor c1=db.rawQuery("SELECT id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url FROM Pustaka WHERE author ='"+strAuthor+"'",null);
						int nRowCount = c1.getCount();
						if (c1.getCount()>0)
						{
							if(c1.moveToFirst())
							{
								int nItemCount=0;
								do
								{
									List<String>	listItem = new ArrayList<String>();
									String id=	c1.getString(0);
									String book_title=	c1.getString(1);
									String book_local_title=	c1.getString(2);
									String author=	c1.getString(3);
									String language=	c1.getString(4);
									String genre=	c1.getString(5);
									String book_type=	c1.getString(6);
									String img_url=	c1.getString(7);
									String book_url=	c1.getString(8);
									listItem.add(c1.getString(0)); //id
									listItem.add(c1.getString(1)); //book english title
									listItem.add(book_local_title);
									listItem.add(author);
									listItem.add(language);
									listItem.add(genre);
									listItem.add(book_type);
									listItem.add(img_url);
									listItem.add(book_url);
									arrBooks.add(listItem);
									nItemCount++;
								}
								while(c1.moveToNext());

								hmAuthors.put(strAuthor, arrBooks);
							}
						}

					}while(c.moveToNext());
				}
			}



			return hmAuthors;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();
		}
		return null;
	}

	public HashMap<String, ArrayList<List<String>>> getGenreWiseBooks()
	{
		Cursor c = null;
		// TODO Auto-generated method stub
		try{
			SQLiteDatabase db =myDataBase;// getReadableDatabase();
			HashMap<String, ArrayList<List<String>>> hmGenres = new HashMap<String, ArrayList<List<String>>>();
			c=db.rawQuery("SELECT DISTINCT genre FROM Pustaka",null);

			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{
					do
					{
						ArrayList<List<String>> arrBooks=new ArrayList<List<String>>();
						String strGenre="";
						if(c.getColumnCount()>0)
							strGenre = c.getString(0);
						Cursor c1=db.rawQuery("SELECT id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url FROM Pustaka WHERE genre ='"+strGenre+"'",null);
						int nRowCount = c1.getCount();
						if (c1.getCount()>0)
						{
							if(c1.moveToFirst())
							{
								int nItemCount=0;
								do
								{
									List<String>	listItem = new ArrayList<String>();
									String id=	c1.getString(0);
									String book_title=	c1.getString(1);
									String book_local_title=	c1.getString(2);
									String author=	c1.getString(3);
									String language=	c1.getString(4);
									String genre=	c1.getString(5);
									String book_type=	c1.getString(6);
									String img_url=	c1.getString(7);
									String book_url=	c1.getString(8);
									listItem.add(c1.getString(0)); //id
									listItem.add(c1.getString(1)); //book english title
									listItem.add(book_local_title);
									listItem.add(author);
									listItem.add(language);
									listItem.add(genre);
									listItem.add(book_type);
									listItem.add(img_url);
									listItem.add(book_url);
									arrBooks.add(listItem);
									nItemCount++;
								}
								while(c1.moveToNext());

								hmGenres.put(strGenre, arrBooks);
							}
						}

					}while(c.moveToNext());
				}
			}
			return hmGenres;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();
		}
		return null;
	}



	public HashMap<String, ArrayList<List<String>>> getMagazinebooks()
	{
		Cursor c = null;
		// TODO Auto-generated method stub
		try{
			SQLiteDatabase db =myDataBase;// getReadableDatabase();
			HashMap<String, ArrayList<List<String>>> hmMagazine = new HashMap<String, ArrayList<List<String>>>();
			c=db.rawQuery("SELECT DISTINCT genre FROM Pustaka",null);

			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{
					do
					{
						ArrayList<List<String>> arrBooks=new ArrayList<List<String>>();
						String strMagazine="";
						if(c.getColumnCount()>0)
							strMagazine = c.getString(0);
						Cursor c1=db.rawQuery("SELECT id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url FROM Pustaka WHERE genre ='"+strMagazine+"'",null);
						int nRowCount = c1.getCount();
						if (c1.getCount()>0)
						{
							if(c1.moveToFirst())
							{
								int nItemCount=0;
								do
								{
									List<String>	listItem = new ArrayList<String>();
									String id=	c1.getString(0);
									String book_title=	c1.getString(1);
									String book_local_title=	c1.getString(2);
									String author=	c1.getString(3);
									String language=	c1.getString(4);
									String genre=	c1.getString(5);
									String book_type=	c1.getString(6);
									String img_url=	c1.getString(7);
									String book_url=	c1.getString(8);
									listItem.add(c1.getString(0)); //id
									listItem.add(c1.getString(1)); //book english title
									listItem.add(book_local_title);
									listItem.add(author);
									listItem.add(language);
									listItem.add(genre);
									listItem.add(book_type);
									listItem.add(img_url);
									listItem.add(book_url);
									arrBooks.add(listItem);
									nItemCount++;
								}
								while(c1.moveToNext());

								hmMagazine.put(strMagazine, arrBooks);
							}
						}

					}while(c.moveToNext());
				}
			}
			return hmMagazine;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();
		}
		return null;
	}

	public HashMap<String, ArrayList<List<String>>> getBookWise()
	{
		Cursor c = null;
		// TODO Auto-generated method stub
		try{
			SQLiteDatabase db =myDataBase;// getReadableDatabase();
			HashMap<String, ArrayList<List<String>>> hmbooks = new HashMap<String, ArrayList<List<String>>>();
			c=db.rawQuery("SELECT DISTINCT book_title FROM Pustaka",null);

			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{
					do
					{
						ArrayList<List<String>> arrBooks=new ArrayList<List<String>>();
						String strBooks="";
						if(c.getColumnCount()>0)
							strBooks = c.getString(0);
						Cursor c1=db.rawQuery("SELECT id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url FROM Pustaka WHERE genre ='"+strBooks+"'",null);
						int nRowCount = c1.getCount();
						if (c1.getCount()>0)
						{
							if(c1.moveToFirst())
							{
								int nItemCount=0;
								do
								{
									List<String>	listItem = new ArrayList<String>();
									String id=	c1.getString(0);
									String book_title=	c1.getString(1);
									String book_local_title=	c1.getString(2);
									String author=	c1.getString(3);
									String language=	c1.getString(4);
									String genre=	c1.getString(5);
									String book_type=	c1.getString(6);
									String img_url=	c1.getString(7);
									String book_url=	c1.getString(8);
									listItem.add(c1.getString(0)); //id
									listItem.add(c1.getString(1)); //book english title
									listItem.add(book_local_title);
									listItem.add(author);
									listItem.add(language);
									listItem.add(genre);
									listItem.add(book_type);
									listItem.add(img_url);
									listItem.add(book_url);
									arrBooks.add(listItem);
									nItemCount++;
								}
								while(c1.moveToNext());

								hmbooks.put(strBooks, arrBooks);
							}
						}

					}while(c.moveToNext());
				}
			}
			return hmbooks;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();
		}
		return null;
	}

	public HashMap<String, ArrayList<List<String>>> getLangauageWise()
	{
		Cursor c = null;
		// TODO Auto-generated method stub
		try{
			SQLiteDatabase db =myDataBase;// getReadableDatabase();
			HashMap<String, ArrayList<List<String>>> hmlanguage = new HashMap<String, ArrayList<List<String>>>();
			c=db.rawQuery("SELECT DISTINCT language FROM Pustaka",null);

			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{
					do
					{
						ArrayList<List<String>> arrBooks=new ArrayList<List<String>>();
						String strlanguage="";
						if(c.getColumnCount()>0)
							strlanguage = c.getString(0);
						Cursor c1=db.rawQuery("SELECT id,book_title,book_local_title,author,language,genre,book_type,img_url,book_url FROM Pustaka WHERE genre ='"+strlanguage+"'",null);
						int nRowCount = c1.getCount();
						if (c1.getCount()>0)
						{
							if(c1.moveToFirst())
							{
								int nItemCount=0;
								do
								{
									List<String>	listItem = new ArrayList<String>();
									String id=	c1.getString(0);
									String book_title=	c1.getString(1);
									String book_local_title=	c1.getString(2);
									String author=	c1.getString(3);
									String language=	c1.getString(4);
									String genre=	c1.getString(5);
									String book_type=	c1.getString(6);
									String img_url=	c1.getString(7);
									String book_url=	c1.getString(8);
									listItem.add(c1.getString(0)); //id
									listItem.add(c1.getString(1)); //book english title
									listItem.add(book_local_title);
									listItem.add(author);
									listItem.add(language);
									listItem.add(genre);
									listItem.add(book_type);
									listItem.add(img_url);
									listItem.add(book_url);
									arrBooks.add(listItem);
									nItemCount++;
								}
								while(c1.moveToNext());

								hmlanguage.put(strlanguage, arrBooks);
							}
						}

					}while(c.moveToNext());
				}
			}
			return hmlanguage;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();
		}
		return null;
	}


	public HashMap<String, List<ArrayList<String>>> getSearch(String Strspinlang,String StrspincBooks,String Strspinbname,String Strspinauthor)
	{
		HashMap<String, List<ArrayList<String>>> hmsearch = new HashMap<String, List<ArrayList<String>>>();
		Cursor c = null;
		Cursor clang = null;
		Cursor ccatbook = null;
		Cursor cbookname = null;
		Cursor cauthorkname = null;
		List<ArrayList<String>>	Search= new ArrayList<ArrayList<String>>();

		String mypath = DB_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
		SQLiteDatabase db =myDataBase;// getReadableDatabase();

		StringBuilder sbLangQuery = new StringBuilder();
		StringBuilder sbGenreQuery = new StringBuilder();
		StringBuilder sbBookName = new StringBuilder();
		StringBuilder sbAuthor = new StringBuilder();

		sbLangQuery.append("language in(");
		if (!Strspinlang.equals("All"))
		{
			sbLangQuery.append("'"+Strspinlang+"'");
		}
		else
		{
			clang=db.rawQuery("SELECT DISTINCT language FROM Pustaka",null);
			if (clang.getCount()>0)
			{
				if(clang.moveToFirst())
				{
					do
					{
						sbLangQuery.append("'"+clang.getString(0)+"'"+",");

					}while(clang.moveToNext());

					sbLangQuery.deleteCharAt(sbLangQuery.length()-1); //remove last comma
				}
			}
		}
		sbLangQuery.append(")");

		sbGenreQuery.append("genre in (");
		if (!StrspincBooks.equals("All"))
		{
			sbGenreQuery.append("'"+StrspincBooks+"'");
		}
		else
		{
			ccatbook=db.rawQuery("SELECT DISTINCT genre FROM Pustaka",null);
			if (ccatbook.getCount()>0)
			{
				if(ccatbook.moveToFirst())
				{
					do
					{
						sbGenreQuery.append("'"+ccatbook.getString(0)+"'"+",");
					}while(ccatbook.moveToNext());

					sbGenreQuery.deleteCharAt(sbGenreQuery.length()-1);
				}
			}
		}

		sbGenreQuery.append(")");

		sbBookName.append("book_title in (");
		if (!Strspinbname.equals("All"))
		{
			sbBookName.append("'"+Strspinbname+"'");
		}
		else
		{
			cbookname=db.rawQuery("SELECT DISTINCT book_title FROM Pustaka",null);
			if (cbookname.getCount()>0)
			{
				if(cbookname.moveToFirst())
				{
					do
					{
						sbBookName.append("'"+cbookname.getString(0)+"'"+",");

					}while(cbookname.moveToNext());

					sbBookName.deleteCharAt(sbBookName.length()-1);
				}
			}
		}

		sbBookName.append(")");

		sbAuthor.append("author in (");
		if (!Strspinauthor.equals("All"))
		{
			sbAuthor.append("'"+Strspinauthor+"'");
		}
		else
		{
			cauthorkname=db.rawQuery("SELECT DISTINCT author FROM Pustaka",null);
			if (cauthorkname.getCount()>0)
			{
				if(cauthorkname.moveToFirst())
				{
					do
					{
						sbAuthor.append("'"+cauthorkname.getString(0)+"'"+",");
					}while(cauthorkname.moveToNext());
					sbAuthor.deleteCharAt(sbAuthor.length()-1);

				}
			}
		}

		sbAuthor.append(")");

		// TODO Auto-generated method stub
		try{
			String strSearchQuery = "select * from Pustaka where "+sbLangQuery.toString()+" and "+ sbGenreQuery.toString() + " and " + sbBookName.toString()+ " and " + sbAuthor.toString();

			//	c=db.rawQuery("select * from Pustaka where language in ('Gowthama Neelambaran')  and genre in ('Family') and book_title in ('Buddhar Piran') and author in ('Sivasankari')",null);
			c=db.rawQuery(strSearchQuery,null);
			if (c.getCount()>0)
			{
				if(c.moveToFirst())
				{
					do
					{
						ArrayList<String> listRent = new ArrayList<String>() ;
						String id=	c.getString(0);
						String book_title=	c.getString(1);
						String book_local_title=	c.getString(2);
						String author=	c.getString(3);
						String language=	c.getString(4);
						String genre=	c.getString(5);
						String	book_type=c.getString(6);
						String img_url=	c.getString(7);
						String book_url=	c.getString(8);
						listRent.add(id);
						listRent.add(book_title);
						listRent.add(book_local_title);
						listRent.add(author);
						listRent.add(language);
						listRent.add(genre);
						listRent.add(book_type);
						listRent.add(img_url);
						listRent.add(book_url);
						Search.add(listRent);
						hmsearch.put("search", Search);

					}
					while(c.moveToNext());
				}

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return hmsearch;
	}

	public HashMap<String, List<String>> getSpinnerBookdetail()
	{
		Cursor c = null;
		Cursor c1 = null;
		Cursor c2 = null;
		Cursor c3 = null;
		// TODO Auto-generated method stub
		try{
			SQLiteDatabase db =myDataBase;// getReadableDatabase();
			HashMap<String, List<String>> hmspinner = new HashMap<String, List<String>>();
			c=db.rawQuery("SELECT DISTINCT book_title FROM Pustaka",null);

			if (c.getCount()>0)
			{
				List<String> listRent = new ArrayList<String>() ;
				if(c.moveToFirst())
				{
					do
					{
						String Strname=c.getString(0);
						listRent.add(Strname);
					}while(c.moveToNext());
				}
				/*if (c.moveToLast())
				{
					listRent.add("All");
				}*/
				hmspinner.put("book_title", listRent);
			}

			c1=db.rawQuery("SELECT DISTINCT author FROM Pustaka",null);

			if (c1.getCount()>0)
			{
				List<String> listRent = new ArrayList<String>() ;
				if(c1.moveToFirst())
				{
					do
					{
						String Strname=c1.getString(0);
						listRent.add(Strname);
					}while(c1.moveToNext());
				}
				/*if (c1.moveToLast())
				{
					listRent.add("All");
				}*/
				hmspinner.put("author", listRent);
			}

			c2=db.rawQuery("SELECT DISTINCT language FROM Pustaka",null);
			if (c2.getCount()>0)
			{
				List<String> listRent = new ArrayList<String>() ;
				if(c2.moveToFirst())
				{
					do
					{
						String Strname=c2.getString(0);
						listRent.add(Strname);
					}while(c2.moveToNext());
				}
				/*if (c2.moveToLast())
				{
					listRent.add("All");
				}*/

				hmspinner.put("language", listRent);

			}

			c3=db.rawQuery("SELECT DISTINCT genre FROM Pustaka",null);
			if (c3.getCount()>0)
			{
				List<String> listRent = new ArrayList<String>() ;
				if(c3.moveToFirst())
				{
					do
					{
						String Strname=c3.getString(0);
						listRent.add(Strname);
					}while(c3.moveToNext());
				}
				/*if (c3.moveToLast())
				{
					listRent.add("All");
				}*/
				hmspinner.put("genre", listRent);
			}



			return hmspinner;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if (c!=null)
				c.close();
		}
		return null;
	}

	public void DeletetablePustaka() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM Pustaka"); //delete all rows in a table
		db.close();
	} 


	public void DeletetableCategory() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM Category"); //delete all rows in a table
		db.close();
	} 


}

