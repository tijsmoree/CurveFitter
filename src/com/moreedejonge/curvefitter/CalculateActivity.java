package com.moreedejonge.curvefitter;

import com.moreedejonge.curvefitter.R;

import android.app.Activity;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class CalculateActivity extends Activity {
	ClipboardManager myClipBoard;
	ClipData myClip;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		double[] xCoords = intent.getDoubleArrayExtra("xCoords");
		double[] yCoords = intent.getDoubleArrayExtra("yCoords");
		int idViewCurve = intent.getIntExtra("idViewCurve", 0);
		boolean willReturnX = intent.getBooleanExtra("willReturnX", true);
		double XorY = intent.getDoubleExtra("XorY", 0.0);
    	setContentView(R.layout.activity_calculate);
    	
    	if(willReturnX) {
    		setTitle("Calculate X");
    	} else {
    		setTitle("Calculate Y");
    	}
    	
    	myClipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	
    	Points points = new Points(xCoords, yCoords);
    	
    	TextView XorYView = (TextView) findViewById(R.id.XorYCalculated);
    	
    	Points.Curve curve = points.getCurveById(idViewCurve);

    	double[] result;
    	
    	if(willReturnX) {
			result = curve.getX(XorY);
		} else {
			result = curve.getY(XorY);
		}
    	
    	Drawable x = getResources().getDrawable(R.drawable.x);
    	Drawable y = getResources().getDrawable(R.drawable.y);
    	
    	Drawable XorYresults;
    	Drawable XorYquestion;
    	
    	if(willReturnX) {
    		if(result[0] == 0.0) {
    			XorYView.setText(XorY + " gives no solutions");
    		} else if(result[0] == 1.0) {
    			XorYView.setText(XorY + "");
    		} else {
    			XorYView.setText(XorY + "");
    		}
			
    		XorYresults = x;
    		XorYquestion = y;
    	} else {
    		if(result[0] == 0.0) {
    			XorYView.setText(XorY + " gives no solutions");
    		} else if(result[0] == 1.0) {
    			XorYView.setText(XorY + "");
    		} else {
    			XorYView.setText(XorY + "");
    		}
			
    		XorYresults = y;
    		XorYquestion = x;
    	}
    	
    	LinearLayout calcXorYLL = (LinearLayout) findViewById(R.id.calcXorYLL);
    	
    	XorYView.setCompoundDrawablesWithIntrinsicBounds(XorYquestion, null, null, null);
    	
    	View.OnClickListener ocl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = ((TextView) v).getText().toString();
				myClip = ClipData.newPlainText("Value", value);
				myClipBoard.setPrimaryClip(myClip);
				Toast.makeText(getApplicationContext(), "Value is copied.", Toast.LENGTH_SHORT).show();
			}
		};
    	
    	if(result[0] == 0.0) {
    	} else if(result[0] == 1.0) {
    		TextView view1 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, calcXorYLL, false);
    		
    		view1.setText("" + result[1]);
    		
    		view1.setCompoundDrawablesWithIntrinsicBounds(XorYresults, null, null, null);

    		view1.setOnClickListener(ocl);
    		
    		calcXorYLL.addView(view1);
    	} else if(result[0] == 2.0) {
    		TextView view1 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, calcXorYLL, false);
    		TextView view2 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, calcXorYLL, false);

    		view1.setText("" + result[1]);
    		view2.setText("" + result[2]);
    		
    		view1.setId(54321);
    		view2.setId(54322);
    		
    		view1.setCompoundDrawablesWithIntrinsicBounds(XorYresults, null, null, null);
    		view2.setCompoundDrawablesWithIntrinsicBounds(XorYresults, null, null, null);

    		view1.setOnClickListener(ocl);
    		view2.setOnClickListener(ocl);

    		calcXorYLL.addView(view1);
    		calcXorYLL.addView(view2);
    	}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
