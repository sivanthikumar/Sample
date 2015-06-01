package com.skytree.simpledemo;


import java.sql.Date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.skytree.epub.ClickListener;
import com.skytree.epub.Highlight;
import com.skytree.epub.Highlights;
import com.skytree.epub.NavPoint;
import com.skytree.epub.NavPoints;
import com.skytree.epub.PageInformation;
import com.skytree.epub.PageMovedListener;
import com.skytree.epub.PageTransition;
import com.skytree.epub.ReflowableControl;
import com.skytree.epub.SearchListener;
import com.skytree.epub.SearchResult;
import com.skytree.epub.SelectionListener;
import com.skytree.epub.SkyProvider;
import com.skytree.epub.State;
import com.skytree.epub.StateListener;

public class BookViewActivity extends Activity {
	ReflowableControl rv;
	RelativeLayout ePubView;
	Button debugButton1;
	Button markButton;

	ProgressBar progressBar;
	boolean autoStartPlayingWhenNewPagesLoaded = true;

	final private String TAG = "EPub";
	Highlights highlights;
	int temp = 20;

	double pagePositionInBook = -1;

	ImageButton fontButton,Bookmark;

	ImageButton menuicon;
	ImageButton Firstpage,Lastpage;

	TextView titleLabel;

	TextView pageIndexLabel;

	RelativeLayout myLayout; //Font Menu

	RelativeLayout myLayoutmenu;


	boolean isplays=true;

	String Filename;
	int Getfont;
	String title="Pustaka"; 	

	RelativeLayout Relayadview;
	AdView adview;
	String Getbookname="";
	TextView smalltxt,mediumtxt,largetxt;

	TextView contents,bookmark,highlight;

	ListView ListViewmenu;

	String chapternamedb;

	ArrayList<List<String>> Strbookmarkcontent;

	public void makeLayout() {		

		SharedPreferences app_preferencesnew =PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		ePubView = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		ePubView.setLayoutParams(rlp);

		Relayadview = new RelativeLayout(this);
		Relayadview.setId(500);
		RelativeLayout.LayoutParams paramsad = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		paramsad.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		Relayadview.setLayoutParams(paramsad);
		Relayadview.setBackgroundColor(getResources().getColor(R.color.white));
		Relayadview.setVisibility(View.GONE);

		if (isNetworkStatusAvialable(getApplicationContext()))
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			{
				//	setadvertise();

			}
		}

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		params.addRule(RelativeLayout.BELOW,500);

		rv = new ReflowableControl(this);

		String filepath = getIntent().getStringExtra("filepath");
		String filepathdir = getIntent().getStringExtra("filestrdir");
		//Getbookname=getIntent().getStringExtra("filestrbookname");

		Getbookname="Agni";
		// continue Reading

		String Ctueread=filepath+"~"+filepathdir+"~"+Getbookname;
		SharedPreferences appctuereading =PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = appctuereading.edit();
		editor.putString("ctuereading",Ctueread);
		editor.commit();

		String baseDirectory = getFilesDir().getAbsolutePath() + "/books"; 
		rv.setBaseDirectory(baseDirectory);
		Filename= "Agni.epub";
		rv.setBookName(Filename);

		rv.setDoublePagedForLandscape(true);
		Getfont=app_preferencesnew.getInt("rvfont", 16);
		rv.setFontSize(Getfont);
		rv.setLineSpacing(135); // the value is supposed to be percent(%).
		rv.setHorizontalGapRatio(0.25);
		rv.setVerticalGapRatio(0.35);
		rv.setPageMovedListener(new PageMovedDelegate());
		rv.setSelectionListener(new SelectionDelegate());
		rv.setSearchListener(new SearchDelegate());
		rv.setStateListener(new StateDelegate());
		rv.setClickListener(new ClickDelegate());
		rv.setLicenseKey("f46e-d3f9-213f-5adf"); // license key remove water mark pustaka.
		//rv.setPageTransition(PageTransition.Slide);
		rv.setPageTransition(PageTransition.Curl);

		rv.setLayoutParams(params);
		rv.setFingerTractionForSlide(true);
		SkyProvider skyProvider = new SkyProvider();
		rv.setContentProvider(skyProvider);	
		String Getposbook=app_preferencesnew.getString(Filename, "0");
		Double dposbook = Double.parseDouble(Getposbook);
		rv.setStartPositionInBook(dposbook);
		rv.useDOMForHighlight(false);
		rv.setNavigationAreaWidthRatio(0.22f); // both left and right side.
		ePubView.addView(Relayadview);
		ePubView.addView(rv);
		this.makeControls();
		this.makeIndicator();
		setContentView(ePubView);
	}


	class ClickDelegate implements ClickListener {
		public void onClick(int x,int y) {
			Log.w("EPub","click detected");

		}

		public void onImageClicked(int x,int y,String src) {
			Log.w("EPub","Click on Image Detected at "+x+":"+y+" src:"+src);
		}

		public void onLinkClicked(int x,int y, String href) {
			Log.w("EPub","Link Clicked at "+x+":"+y+" href:"+href);
		}

		@Override
		public boolean ignoreLink(int x,int y,String href) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLinkForLinearNoClicked(int x, int y, String href) {
			// TODO Auto-generated method stub
			Log.w("EPub","Link Clicked at "+x+":"+y+" href:"+href);	
			//			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
			//			startActivity(browserIntent);
		}

		@Override
		public void onIFrameClicked(int x, int y, String src) {
			// TODO Auto-generated method stub
			Log.w("EPub","IFrame Clicked at "+x+":"+y+" src:"+src);
			//			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(src));
			//			startActivity(browserIntent);
		}

		@Override
		public void onVideoClicked(int x, int y, String src) {
			// TODO Auto-generated method stub
			Log.w("EPub","Video Clicked at "+x+":"+y+" src:"+src);			
		}

		@Override
		public void onAudioClicked(int x, int y, String src) {
			// TODO Auto-generated method stub
			Log.w("EPub","Audio Clicked at "+x+":"+y+" src:"+src);			
		}		
	}



	class StateDelegate implements StateListener {
		public void onStateChanged(State state) {
			if (state==State.LOADING) {
				showIndicator();
			}else if (state==State.ROTATING) {
				//				showToast("Rotating...");
				showIndicator();
			}else if (state==State.BUSY) {
				showIndicator();
				//				showToast("Busy...");				
			}else if (state==State.NORMAL) {
				//				showToast("Normal...");
				hideIndicator();
				//				if (dialog!=null) dialog.dismiss();
				//				dialog = null;
			}
		}		
	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.makeLayout();
	}

	public void makeControls() {
		try
		{
			int bs = 38;
			fontButton 			= this.makeImageButton(100, R.drawable.font2x,  getPS(bs),getPS(bs));
			Firstpage = this.makeImageButton(101, R.drawable.dback,  getPS(bs),getPS(bs));
			Lastpage= this.makeImageButton(102, R.drawable.dfront,  getPS(bs),getPS(bs));
			Bookmark 			= this.makeImageButton(103, R.drawable.bookmark2x,  getPS(bs),getPS(bs));
			menuicon	= this.makeImageButton(104, R.drawable.list2x,  getPS(bs),getPS(bs));
			titleLabel				= this.makeLabel(3000,  title,	Gravity.CENTER_HORIZONTAL, 17, Color.argb(240, 94,61,35));
			pageIndexLabel			= this.makeLabel(3000, 	"......",		Gravity.CENTER_HORIZONTAL, 13, Color.argb(240, 94,61,35));

			ePubView.addView(fontButton);
			ePubView.addView(titleLabel);
			ePubView.addView(pageIndexLabel);
			ePubView.addView(Firstpage);
			ePubView.addView(Lastpage);
			ePubView.addView(Bookmark);
			ePubView.addView(menuicon);

			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			param.addRule(RelativeLayout.BELOW,500);
			param.setMargins(0, 15, 0, 0);
			param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.BELOW,500);

			params.setMargins(0, 30, 0, 0);
			titleLabel.setLayoutParams(params);
			titleLabel.setText(Getbookname);
			titleLabel.setTextSize(18);


			RelativeLayout.LayoutParams paramsbookmark = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			paramsbookmark.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			paramsbookmark.addRule(RelativeLayout.BELOW,500);

			paramsbookmark.setMargins(0, 20, 0, 0);
			Bookmark.setLayoutParams(paramsbookmark);

			RelativeLayout.LayoutParams menuicons = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			menuicons.addRule(RelativeLayout.RIGHT_OF,103);
			menuicons.setMargins(0, 20, 0, 0);
			menuicons.addRule(RelativeLayout.BELOW,500);
			menuicon.setLayoutParams(menuicons);




			RelativeLayout.LayoutParams parambottom = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			parambottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			parambottom.addRule(RelativeLayout.CENTER_IN_PARENT);
			parambottom.bottomMargin = 35;
			pageIndexLabel.setLayoutParams(parambottom);

			RelativeLayout.LayoutParams parambottomleft = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			parambottomleft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			parambottomleft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			parambottomleft.bottomMargin = 15;
			Firstpage.setLayoutParams(parambottomleft);


			RelativeLayout.LayoutParams parambottomright = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
			parambottomright.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			parambottomright.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			parambottomright.bottomMargin = 15;
			Lastpage.setLayoutParams(parambottomright);

			fontButton.setLayoutParams(param);
			fontButton.setOnClickListener(listener);
			Firstpage.setOnClickListener(listener);
			Lastpage.setOnClickListener(listener);
			Bookmark.setOnClickListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public TextView makeLabel(int id, String text, int gravity,float textSize,int textColor) {
		TextView label = new TextView(this);
		label.setId(id);
		label.setGravity(gravity);
		label.setBackgroundColor(Color.TRANSPARENT);
		label.setText(text);
		label.setTextColor(textColor);		
		label.setTextSize(textSize);
		return label;
	}

	public int getPS(float dip) {
		float density = this.getDensityDPI();
		float factor = (float)density/240.f;
		int px = (int)(dip*factor);
		return px;		
	}
	public ImageButton makeImageButton(int id,int resId,int width, int height) {
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		Drawable icon;
		ImageButton button = new ImageButton(this);
		button.setAdjustViewBounds(true);
		button.setId(id);
		button.setOnClickListener(listener);
		button.setBackgroundColor(Color.TRANSPARENT);
		icon = getResources().getDrawable(resId);
		icon.setBounds(0,0,width,height);

		Bitmap iconBitmap = ((BitmapDrawable)icon).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(iconBitmap, width, height, false);
		button.setImageBitmap(bitmapResized);
		button.setVisibility(View.VISIBLE);
		param.width = 		(int)(width);		
		param.height = 		(int)(height);
		button.setLayoutParams(param);		
		return button;		
	}






	public int getDensityDPI() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int density = metrics.densityDpi;
		return density;
	}


	public void makeIndicator() {
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyle);		
		ePubView.addView(progressBar);		
		this.hideIndicator();
	}

	public void showIndicator() {
		RelativeLayout.LayoutParams params = 
				(RelativeLayout.LayoutParams)progressBar.getLayoutParams();		
		params.addRule(RelativeLayout.CENTER_IN_PARENT, -1);
		progressBar.setLayoutParams(params);
		progressBar.setVisibility(View.VISIBLE);
	}

	public void hideIndicator() {
		if (progressBar!=null) {
			progressBar.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}		


	private OnClickListener listener = new OnClickListener() {
		public void onClick(View arg) {
			if (arg.getId() == 100) {
				fontPressed();	
			} 
			if (arg.getId() == 101) { // GO To First Page
				rv.gotoPageByPagePositionInBook(0);

			} 
			if (arg.getId() == 102) { //GO To Last Page
				rv.gotoPageByPagePositionInBook(1);
			} 
			if (arg.getId() == 103) { //GO To Last Page
				DataBaseHelper db=new DataBaseHelper(getApplicationContext());
				String Posbooks = Double.toString(pagePositionInBook);
				long time= System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); 
				String dateString = formatter.format(new Date(time));
				Log.d(dateString, "dateString");

				String images=db.insertBookmarkbyValue(Getbookname,Posbooks,chapternamedb,time,dateString,getApplicationContext());
				if (images.equalsIgnoreCase("true"))
				{
					Bookmark.setBackgroundResource(R.drawable.bookmarked2x);
				}

				else
				{
					Bookmark.setBackgroundResource(R.drawable.bookmark2x);
				}
			} 
			if (arg.getId() == 104) {
				rv.setVisibility(View.GONE);
				menubuttonpressed();
			} 

			if (arg.getId() == 110) {
				fillContentsList(1); // table of contents;

			} 

			if (arg.getId() == 111) {
				fillContentsList(2); // Bookmark list

			} 
		}
	};

	public void fillContentsList(int flag) {

		if (flag==1)
		{
			ListViewmenu.setTag(flag);
			ArrayList<String> Strcontent = new ArrayList<String>();
			NavPoints nps = rv.getNavPoints();
			for (int i=0; i<nps.getSize(); i++) {
				NavPoint np = nps.getNavPoint(i);
				String Strcontents=np.text;
				Strcontent.add(Strcontents);
			}	
			CustomListAdapter adapters=new CustomListAdapter(this, Strcontent, Strcontent,1);
			ListViewmenu.setAdapter(adapters);

		}


		if (flag==2)
		{
			ListViewmenu.setTag(flag);
			DataBaseHelper db=new DataBaseHelper(getApplicationContext());
			Strbookmarkcontent =db.getbookmarkslist(Getbookname);
			ArrayList<String> Strcontent = new ArrayList<String>();
			ArrayList<String> Strdate = new ArrayList<String>();
			for (int i=0;i<Strbookmarkcontent.size();i++)
			{
				String Strchapter=Strbookmarkcontent.get(i).get(1);
				String StrDate=Strbookmarkcontent.get(i).get(3);
				Strcontent.add(Strchapter);
				Strdate.add(StrDate);
			}
			/*adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, Strcontent);*/
			CustomListAdapter adapters=new CustomListAdapter(this, Strcontent, Strdate,2);
			ListViewmenu.setAdapter(adapters);
			/*ListViewmenu.setTag(2);
			ArrayList<String> Strcontent = new ArrayList<String>();
			NavPoints nps = rv.getNavPoints();
			for (int i=0; i<nps.getSize(); i++) {
				NavPoint np = nps.getNavPoint(i);
				String Strcontents=np.text;
				Strcontent.add(Strcontents);
			}	
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, Strcontent);
			ListViewmenu.setAdapter(adapter);*/
		}
	}


	private void menubuttonpressed()
	{
		if(!isplays)
		{
			myLayoutmenu.setVisibility(View.GONE);
			isplays=true;
		}
		else
		{
			isplays=false;
			rv.setEnabled(false);
			myLayoutmenu = new RelativeLayout(this);
			myLayoutmenu.setVisibility(View.VISIBLE);
			final View hiddenInfo = getLayoutInflater().inflate(R.layout.menucategory, myLayoutmenu, false);
			myLayoutmenu.addView(hiddenInfo);
			ePubView.addView(myLayoutmenu);

			contents=(TextView)hiddenInfo.findViewById(R.id.contents);
			contents.setId(110);
			bookmark=(TextView)hiddenInfo.findViewById(R.id.bookmark);
			bookmark.setId(111);
			highlight=(TextView)hiddenInfo.findViewById(R.id.highlight);
			ListViewmenu=(ListView)hiddenInfo.findViewById(R.id.listmenu);
			ListViewmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView parent, View v, int position, long id){

					// DO STUFF HERE
					Object gettag=ListViewmenu.getTag();
					int ListViewmenuTag=Integer.parseInt(gettag+"");
					if (ListViewmenuTag==1)
					{
						NavPoints nps = rv.getNavPoints();
						NavPoint	targetNavPoint = nps.getNavPoint(position);
						rv.gotoPageByNavPoint(targetNavPoint);
						rv.setVisibility(View.VISIBLE);
						myLayoutmenu.setVisibility(View.GONE);
					}

					if (ListViewmenuTag==2)
					{
						String pos=Strbookmarkcontent.get(position).get(2);
						Double posvalue = Double.parseDouble(pos);
						rv.gotoPageByPagePositionInBook(posvalue);
						rv.setVisibility(View.VISIBLE);
						myLayoutmenu.setVisibility(View.GONE);
					}

				}
			});

			ListViewmenu.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					// TODO Auto-generated method stub

					String Bookname=Strbookmarkcontent.get(pos).get(0);
					String poss=Strbookmarkcontent.get(pos).get(2);
					DataBaseHelper db=new DataBaseHelper(getApplicationContext());
					db.deleteBookmarkbyValue(Bookname, poss, getApplicationContext());
					return true;
				}
			}); 

			RelativeLayout Relayfont=(RelativeLayout)hiddenInfo.findViewById(R.id.fontview);
			Relayfont.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myLayoutmenu.setVisibility(View.GONE);
				}
			});
			contents.setOnClickListener(listener);
			bookmark.setOnClickListener(listener);

		}

	}
	private void fontPressed() {

		if(!isplays)
		{
			myLayout.setVisibility(View.GONE);
			isplays=true;
		}
		else
		{
			isplays=false;
			rv.setEnabled(false);
			myLayout = new RelativeLayout(this);
			myLayout.setVisibility(View.VISIBLE);
			final View hiddenInfo = getLayoutInflater().inflate(R.layout.epubfont, myLayout, false);
			myLayout.addView(hiddenInfo);
			ePubView.addView(myLayout);

			smalltxt=(TextView)hiddenInfo.findViewById(R.id.small);
			mediumtxt=(TextView)hiddenInfo.findViewById(R.id.medium);
			largetxt=(TextView)hiddenInfo.findViewById(R.id.large);

			RelativeLayout Relayfont=(RelativeLayout)hiddenInfo.findViewById(R.id.fontview);
			Relayfont.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myLayout.setVisibility(View.GONE);
				}
			});
			SharedPreferences app_preferencesnew =PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			int Getflags=app_preferencesnew.getInt("rvfont", 16);

			if(this.isTablet())
			{

				if (Getflags==20)
				{
					smalltxt.setTextColor(getResources().getColor(R.color.black));
				}
				if (Getflags==24)
				{
					mediumtxt.setTextColor(getResources().getColor(R.color.black));
				}

				if (Getflags==28)
				{
					largetxt.setTextColor(getResources().getColor(R.color.black));
				}

			}
			else
			{
				if (Getflags==16)
				{
					smalltxt.setTextColor(getResources().getColor(R.color.black));
				}
				if (Getflags==20)
				{
					mediumtxt.setTextColor(getResources().getColor(R.color.black));
				}

				if (Getflags==24)
				{
					largetxt.setTextColor(getResources().getColor(R.color.black));
				}
			}
			smalltxt.setOnClickListener(myclicklistener);
			mediumtxt.setOnClickListener(myclicklistener);
			largetxt.setOnClickListener(myclicklistener);

		}
	}



	OnClickListener myclicklistener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			int Fontsize = 0;

			if(v.getId()==R.id.small)
			{
				if(this.isTablet1())
				{
					Fontsize=20;	
				}
				else
				{
					Fontsize=16;	
				}
				smalltxt.setTextColor(getResources().getColor(R.color.black));
				mediumtxt.setTextColor(getResources().getColor(R.color.white));
				largetxt.setTextColor(getResources().getColor(R.color.white));
				rv.setEnabled(true);
				myLayout.setVisibility(View.GONE);
				rv.changeFontSize(Fontsize);
			}
			if(v.getId()==R.id.medium)
			{
				if(this.isTablet1())
				{
					Fontsize=24;	
				}
				else
				{
					Fontsize=20;	
				}
				smalltxt.setTextColor(getResources().getColor(R.color.white));
				mediumtxt.setTextColor(getResources().getColor(R.color.black));
				largetxt.setTextColor(getResources().getColor(R.color.white));
				rv.setEnabled(true);
				myLayout.setVisibility(View.GONE);
				rv.changeFontSize(Fontsize);
			}

			if(v.getId()==R.id.large)
			{
				if(this.isTablet1())
				{
					Fontsize=28;	
				}
				else
				{
					Fontsize=24;
				}
				smalltxt.setTextColor(getResources().getColor(R.color.white));
				mediumtxt.setTextColor(getResources().getColor(R.color.white));
				largetxt.setTextColor(getResources().getColor(R.color.black));

				rv.setEnabled(true);
				myLayout.setVisibility(View.GONE);
				rv.changeFontSize(Fontsize);
			}

			SharedPreferences app_preferencesnew =PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = app_preferencesnew.edit();
			editor.putInt("rvfont",Fontsize);
			editor.commit();
		}

		private boolean isTablet1() {
			// TODO Auto-generated method stub
			return (getResources().getConfiguration().screenLayout
					& Configuration.SCREENLAYOUT_SIZE_MASK)
					>= Configuration.SCREENLAYOUT_SIZE_LARGE;
		}



	};


	private boolean isTablet() {
		// TODO Auto-generated method stub
		return (getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}



	public void setLocation(View view,int px, int py) {

	}


	public int pxr(float dp) {
		return this.getPXFromRight(dp);
	}

	public int pyt(float dp) {
		return this.getPYFromTop(dp);
	}

	public int getPXFromLeft(float dip) {
		int ps = this.getPS(dip);
		return ps;		
	}

	public int getPXFromRight(float dip) {
		int ps = this.getPS(dip);
		int ms = this.getWidth()-ps;
		return ms;
	}

	public int getPYFromTop(float dip) {
		int ps = this.getPS(dip);
		return ps;
	}

	public int getWidth() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		return width;		
	}


	public boolean isPortrait() {
		int orientation = getResources().getConfiguration().orientation;
		if (orientation==Configuration.ORIENTATION_PORTRAIT) return true;
		else return false;	
	}

	// this is not 100% accurate function. 





	class PageMovedDelegate implements PageMovedListener {
		public void onPageMoved(PageInformation pi) {
			RefreshAds();
			Bookmark.setBackgroundResource(R.drawable.bookmark2x);
			String Posbook = Double.toString(pagePositionInBook);
			chapternamedb=pi.chapterTitle;
			Log.d(chapternamedb,"chapternamedb");

			DataBaseHelper db=new DataBaseHelper(getApplicationContext());
			String check=db.bookmarkcheck(Getbookname,Posbook);
			if (check.equalsIgnoreCase("false")) // store
			{
				Bookmark.setBackgroundResource(R.drawable.bookmarked2x);

			}
			else
			{
				Bookmark.setBackgroundResource(R.drawable.bookmark2x);

			}

			pagePositionInBook = (float)pi.pagePositionInBook;
			int chapterpages=pi.numberOfPagesInChapter;
			setIndexLabelsText(pi.pageIndex,chapterpages);	
			SharedPreferences app_preferencesnew =PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = app_preferencesnew.edit();
			editor.putString(Filename,Posbook);
			editor.commit();
		}

		@Override
		public void onChapterLoaded(int chapterIndex) {
			// TODO Auto-generated method stub

		}
	}

	public void setIndexLabelsText(int pageIndex, int pageCount) {
		if (pageIndex==-1 || pageCount==-1 || pageCount==0) {
			pageIndexLabel.setText("");
			//secondaryIndexLabel.setText("");
			return;
		}

		int pi = 0;
		int si = 0; 
		int pc;
		if (rv.isDoublePaged()) {
			pc = pageCount*2;
			pi = pageIndex*2+1;
			si = pageIndex*2+2;
		}else {
			pc = pageCount;
			pi = pageIndex+1;
			si = pageIndex+2;
			if (rv.isRTL()) {
				pi = pc-pi+1;
				si = pc-si+1;
			}
		}				
		String pt = String.format("%3d/%3d",pi,pc);
		pageIndexLabel.setText(pt);
		//secondaryIndexLabel.setText(st);	
	}	

	class SearchDelegate implements SearchListener {
		public void onKeySearched(SearchResult searchResult) {

		}

		public void onSearchFinishedForChapter(SearchResult searchResult) {
			rv.pauseSearch();
		}

		public void onSearchFinished(SearchResult searchResult) {
		}
	}


	class SelectionDelegate implements SelectionListener {
		@Override
		public void selectionChanged(Highlight highlight, Rect arg1, Rect arg2) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void selectionEnded(Highlight highlight, Rect rect1, Rect rect2) {
			// TODO Auto-generated method stub

		}
		@Override
		public void selectionStarted(Highlight highlight, Rect arg1, Rect arg2) {
			// TODO Auto-generated method stub
			Log.w("EPub", "selectionStarted");
		}

		@Override
		public void selectionCancelled() {
			// TODO Auto-generated method stub
			Log.w("EPub", "selectionCancelled");
			if (isplays==false)
			{
				myLayout.setVisibility(View.GONE);
				isplays=true;
			}
		}
	}

	private void setadvertise() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			try{

				try{
					adview= new AdView(BookViewActivity.this);
					adview.setAdSize(AdSize.BANNER);
					adview.setAdUnitId( "ca-app-pub-8535203636933888/7812848058");
					adview.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					Relayadview.addView(adview);
					adview.loadAd(new AdRequest.Builder().build());
					//linear_ad
				}catch (Exception e) {
					// TODO: handle exception
				}

				adview.setAdListener(new AdListener() {
					@Override
					public void onAdFailedToLoad(int errorCode) {
						// TODO Auto-generated method stub 
						Log.d("On Fail called","Ad");
						Relayadview.setVisibility(View.GONE);
						super.onAdFailedToLoad(errorCode);
					}
					@Override
					public void onAdLoaded() {
						// TODO Auto-generated method stub
						Log.d("On Load called","Ad");
						//Relayadview.setBackgroundColor(getResources().getColor(R.color.Lgreencolor));
						Relayadview.setVisibility(View.VISIBLE);
						super.onAdLoaded();
					}
				});

			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	private void RefreshAds()
	{
		try{
			if (isNetworkStatusAvialable(getApplicationContext()))
			{
				AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
				adview.loadAd(adRequestBuilder.build());
			}

		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean isNetworkStatusAvialable (Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) 
		{
			NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
			if(netInfos != null)
				if(netInfos.isConnected()) 
					if (netInfos.isAvailable())
						return true;
		}
		return false;

	}
}