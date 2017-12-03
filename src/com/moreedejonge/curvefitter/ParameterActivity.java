package com.moreedejonge.curvefitter;

import com.moreedejonge.curvefitter.R;

import android.app.Activity;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.*;
import android.widget.*;

public class ParameterActivity extends Activity {
	ClipboardManager myClipBoard;
	ClipData myClip;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		double[] xCoords = intent.getDoubleArrayExtra("xCoords");
		double[] yCoords = intent.getDoubleArrayExtra("yCoords");
		int idViewCurve = intent.getIntExtra("idViewCurve", 0);
    	setContentView(R.layout.activity_parameter);
    	
    	myClipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	
    	Points points = new Points(xCoords, yCoords);
    	
    	double[] params = new double[1];
    	
    	TextView function = (TextView) findViewById(R.id.function);
    	
    	Points.Curve curve = points.getCurveById(idViewCurve);
    	
    	params = curve.getParams();
		function.setText(Html.fromHtml(curve.getPlainFunction()));
		function.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(curve.getDrawableId()), null, null, null);
    	
    	Drawable a = getResources().getDrawable(R.drawable.a);
    	Drawable b = getResources().getDrawable(R.drawable.b);
    	Drawable c = getResources().getDrawable(R.drawable.c);
    	
    	LinearLayout showParamsLL = (LinearLayout) findViewById(R.id.showParamsLL);
    	
    	View.OnClickListener ocl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = ((TextView) v).getText().toString();
				myClip = ClipData.newPlainText("Value", value);
				myClipBoard.setPrimaryClip(myClip);
				Toast.makeText(getApplicationContext(), "Value is copied.", Toast.LENGTH_SHORT).show();
			}
		};
    	
    	if(params.length == 2) {
    		TextView view1 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, showParamsLL, false);
    		TextView view2 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, showParamsLL, false);

    		view1.setText("" + params[0]);
    		view2.setText("" + params[1]);

    		view1.setCompoundDrawablesWithIntrinsicBounds(a, null, null, null);
    		view2.setCompoundDrawablesWithIntrinsicBounds(b, null, null, null);

    		view1.setOnClickListener(ocl);
    		view2.setOnClickListener(ocl);

    		showParamsLL.addView(view1);
    		showParamsLL.addView(view2);
    	} else if(params.length == 3) {
    		TextView view1 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, showParamsLL, false);
    		TextView view2 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, showParamsLL, false);
    		TextView view3 = (TextView) this.getLayoutInflater().inflate(R.layout.template_textview, showParamsLL, false);

    		view1.setText("" + params[0]);
    		view2.setText("" + params[1]);
    		view3.setText("" + params[2]);

    		view1.setCompoundDrawablesWithIntrinsicBounds(a, null, null, null);
    		view2.setCompoundDrawablesWithIntrinsicBounds(b, null, null, null);
    		view3.setCompoundDrawablesWithIntrinsicBounds(c, null, null, null);
    		
    		view1.setOnClickListener(ocl);
    		view2.setOnClickListener(ocl);
    		view3.setOnClickListener(ocl);

    		showParamsLL.addView(view1);
    		showParamsLL.addView(view2);
    		showParamsLL.addView(view3);
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
