package com.moreedejonge.curvefitter;

import com.moreedejonge.curvefitter.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	int amountOfCoords = 0;
	
	public void addXY(View v) {
		amountOfCoords++;
		
		if(amountOfCoords == 1) {
			Button par = (Button) findViewById(R.id.btnParabolic);
			
			par.setClickable(true);
			par.setTextColor(getResources().getColor(R.color.darkGreen));
		}
		
		int xId = 123456780 + 2 * amountOfCoords;
		int yId = 123456781 + 2 * amountOfCoords;
		
		LinearLayout listX = (LinearLayout) findViewById(R.id.listX);
		LinearLayout listY = (LinearLayout) findViewById(R.id.listY);

		EditText xField = (EditText) this.getLayoutInflater().inflate(R.layout.template_edittext, listX, false);
		EditText yField = (EditText) this.getLayoutInflater().inflate(R.layout.template_edittext, listY, false);

		EditText xLast = ((EditText) findViewById(R.id.xCoordLast));
		EditText yLast = ((EditText) findViewById(R.id.yCoordLast));
		
		String xValue = xLast.getText().toString();
		String yValue = yLast.getText().toString();

		xLast.setText("");
		yLast.setText("");
		
		xField.setId(xId);
		xField.setCompoundDrawablesWithIntrinsicBounds(null, null, xLast.getCompoundDrawables()[2], null);
		xField.setText(xValue);
		xField.setNextFocusForwardId(yId);
		
		yField.setId(yId);
		yField.setCompoundDrawablesWithIntrinsicBounds(yLast.getCompoundDrawables()[0], null, null, null);
		yField.setText(yValue);
		yField.setNextFocusForwardId(R.id.xCoordLast);
		
		xLast.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.x), null);
		yLast.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.y), null, null, null);

		if(amountOfCoords == 1) {
			((EditText) findViewById(R.id.yCoordFirst)).setNextFocusForwardId(xId);
		} else {
			findViewById(yId - 2).setNextFocusForwardId(xId);
		}
		
		listX.addView(xField);
		listY.addView(yField);
		
		if(xLast.isFocused()) {
			xField.requestFocus();
		} else if(yLast.isFocused()) {
			yField.requestFocus();
		}
	}
	
	public void reset(View v) {
		for(int i = 1; i < amountOfCoords + 1; i++) {
			((LinearLayout) findViewById(R.id.listX)).removeAllViews();
			((LinearLayout) findViewById(R.id.listY)).removeAllViews();
		}
		
		Drawable x = getResources().getDrawable(R.drawable.x);
		Drawable y = getResources().getDrawable(R.drawable.y);
		
		EditText xCoordFirst = (EditText) findViewById(R.id.xCoordFirst);
		EditText yCoordFirst = (EditText) findViewById(R.id.yCoordFirst);
		EditText xCoordLast = (EditText) findViewById(R.id.xCoordLast);
		EditText yCoordLast = (EditText) findViewById(R.id.yCoordLast);
		
		xCoordFirst.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
		yCoordFirst.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
		xCoordLast.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
		yCoordLast.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);

		xCoordFirst.setText("");
		yCoordFirst.setText("");
		xCoordLast.setText("");
		yCoordLast.setText("");
		
		yCoordFirst.setNextFocusForwardId(R.id.xCoordLast);
		
		xCoordFirst.requestFocus();
		
		Button par = (Button) findViewById(R.id.btnParabolic);
		
		par.setTextColor(getResources().getColor(R.color.lightGreen));
		par.setClickable(false);
		
		amountOfCoords = 0;
	}
	
	public void calcCurve(View v) {
		int n = amountOfCoords + 2;

		String[] xStr = new String[n];
		String[] yStr = new String[n];
		
		int[] succes = new int[n];
		
		int nEmpty = 0;
		
		boolean filledIn = true;

		double[] xCoords = new double[n];
		double[] yCoords = new double[n];
		
		xStr[0] = ((EditText) findViewById(R.id.xCoordFirst)).getText().toString();
		yStr[0] = ((EditText) findViewById(R.id.yCoordFirst)).getText().toString();

		xStr[n - 1] = ((EditText) findViewById(R.id.xCoordLast)).getText().toString();
		yStr[n - 1] = ((EditText) findViewById(R.id.yCoordLast)).getText().toString();
		
		for(int i = 1; i < n - 1; i++) {
			xStr[i] = ((EditText) findViewById(123456780 + 2 * i)).getText().toString();
			yStr[i] = ((EditText) findViewById(123456781 + 2 * i)).getText().toString();
		}
		
		for(int i = 0; i < n; i++) {
			String x = xStr[i];
			String y = yStr[i];
			
			if(x.isEmpty() && y.isEmpty()) {
				succes[i] = 0;
				
				nEmpty++;
				
				filledIn = false;
			} else if(x.isEmpty() && !y.isEmpty()) {
				succes[i] = 1;
				
				filledIn = false;
			} else if(!x.isEmpty() && y.isEmpty()) {
				succes[i] = 10;
				
				filledIn = false;
			} else {
				succes[i] = 11;

				xCoords[i] = Double.parseDouble(x);
				yCoords[i] = Double.parseDouble(y);
			}
		}

		Drawable x = getResources().getDrawable(R.drawable.x);
		Drawable y = getResources().getDrawable(R.drawable.y);
		Drawable x_red = getResources().getDrawable(R.drawable.x_red);
		Drawable y_red = getResources().getDrawable(R.drawable.y_red);

		EditText xFieldFirst = (EditText) findViewById(R.id.xCoordFirst);
		EditText yFieldFirst = (EditText) findViewById(R.id.yCoordFirst);
		EditText xFieldLast = (EditText) findViewById(R.id.xCoordLast);
		EditText yFieldLast = (EditText) findViewById(R.id.yCoordLast);
		
		if(filledIn) {
			xFieldFirst.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
			yFieldFirst.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			
			xFieldLast.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
			yFieldLast.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			
			for(int i = 1; i < (n - 1); i++) {
				((EditText) findViewById(123456780 + 2 * i)).setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
				((EditText) findViewById(123456781 + 2 * i)).setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			}
			
			Intent intent = new Intent(MainActivity.this, ResultActivity.class);
			intent.putExtra("xCoords", xCoords);
			intent.putExtra("yCoords", yCoords);
			intent.putExtra("idViewCurve", v.getId());
			startActivity(intent);
		} else {
			int nNew = n - nEmpty;

			String[] xStrNew;
			String[] yStrNew;
			
			if(nNew < 2) {
				nNew = 2;
				
				xStrNew = new String[2];
				yStrNew = new String[2];

				xStrNew[0] = "";
				xStrNew[1] = "";
				yStrNew[0] = "";
				yStrNew[1] = "";
			} else {
				xStrNew = new String[nNew];
				yStrNew = new String[nNew];
			}
			
			int[] succesNew = new int[nNew];
			
			amountOfCoords = nNew - 2;
			
			int z = 0;
			
			for(int i = 0; i < n; i++) {
				if(succes[i] != 0) {
					xStrNew[z] = xStr[i];
					yStrNew[z] = yStr[i];
					
					succesNew[z] = succes[i];
					
					z++;
				}
			}
			
			for(int i = nNew - 1; i < n - 1; i++) {
				((LinearLayout) findViewById(R.id.listX)).removeView((View) findViewById(123456780 + 2 * i));
				((LinearLayout) findViewById(R.id.listY)).removeView((View) findViewById(123456781 + 2 * i));
			}

			xFieldFirst.setText(xStrNew[0]);
			yFieldFirst.setText(yStrNew[0]);
			
			xFieldLast.setText(xStrNew[nNew - 1]);
			yFieldLast.setText(yStrNew[nNew - 1]);
			
			if(succesNew[0] == 0) {
				xFieldFirst.setCompoundDrawablesWithIntrinsicBounds(null, null, x_red, null);
				yFieldFirst.setCompoundDrawablesWithIntrinsicBounds(y_red, null, null, null);
			} else if(succesNew[0] == 1) {
				xFieldFirst.setCompoundDrawablesWithIntrinsicBounds(null, null, x_red, null);
				yFieldFirst.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			} else if(succesNew[0] == 10) {
				xFieldFirst.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
				yFieldFirst.setCompoundDrawablesWithIntrinsicBounds(y_red, null, null, null);
			} else {
				xFieldFirst.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
				yFieldFirst.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			}

			if(succesNew[nNew - 1] == 0) {
				xFieldLast.setCompoundDrawablesWithIntrinsicBounds(null, null, x_red, null);
				yFieldLast.setCompoundDrawablesWithIntrinsicBounds(y_red, null, null, null);
			} else if(succesNew[nNew - 1] == 1) {
				xFieldLast.setCompoundDrawablesWithIntrinsicBounds(null, null, x_red, null);
				yFieldLast.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			} else if(succesNew[nNew - 1] == 10) {
				xFieldLast.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
				yFieldLast.setCompoundDrawablesWithIntrinsicBounds(y_red, null, null, null);
			} else {
				xFieldLast.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
				yFieldLast.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
			}
			
			for(int i = 1; i < nNew - 1; i++) {
				EditText xField = ((EditText) findViewById(123456780 + 2 * i));
				EditText yField = ((EditText) findViewById(123456781 + 2 * i));

				xField.setText(xStrNew[i]);
				yField.setText(yStrNew[i]);
				
				if(succesNew[i] == 0) {
					xField.setCompoundDrawablesWithIntrinsicBounds(null, null, x_red, null);
					yField.setCompoundDrawablesWithIntrinsicBounds(y_red, null, null, null);
				} else if(succesNew[i] == 1) {
					xField.setCompoundDrawablesWithIntrinsicBounds(null, null, x_red, null);
					yField.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
				} else if(succesNew[i] == 10) {
					xField.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
					yField.setCompoundDrawablesWithIntrinsicBounds(y_red, null, null, null);
				} else {
					xField.setCompoundDrawablesWithIntrinsicBounds(null, null, x, null);
					yField.setCompoundDrawablesWithIntrinsicBounds(y, null, null, null);
				}
			}
			
			if(amountOfCoords == 0) {
				yFieldFirst.setNextFocusForwardId(R.id.xCoordLast);
				
				Button par = (Button) findViewById(R.id.btnParabolic);
				
				par.setTextColor(getResources().getColor(R.color.lightGreen));
				par.setClickable(false);
			} else {
				((EditText) findViewById(123456780 + 2 * amountOfCoords)).setNextFocusForwardId(R.id.xCoordLast);
			}
			
			EditText focusedET = (EditText) getWindow().getCurrentFocus();
			focusedET.setSelection(focusedET.getText().length());
		}
	}
}
