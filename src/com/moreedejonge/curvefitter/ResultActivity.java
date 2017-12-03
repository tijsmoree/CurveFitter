package com.moreedejonge.curvefitter;

import com.moreedejonge.curvefitter.R;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.text.Html;
import android.view.*;
import android.widget.*;

public class ResultActivity extends Activity {
	double[] xCoords;
	double[] yCoords;
	
	Points.Curve curve;
	
	int idViewCurve;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		xCoords = intent.getDoubleArrayExtra("xCoords");
		yCoords = intent.getDoubleArrayExtra("yCoords");
		idViewCurve = intent.getIntExtra("idViewCurve", 0);
		setContentView(R.layout.activity_result);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Points points = new Points(xCoords, yCoords);
		
		Button funcParams = (Button) findViewById(R.id.funcParams);
		
		this.curve = points.getCurveById(idViewCurve);

		funcParams.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(curve.getDrawableId()), null, null, null);
		funcParams.setText(Html.fromHtml(curve.getFunction()));
		
		GraphView graph = (GraphView) findViewById(R.id.graph);
		
		graph.setCurve(this.curve);
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
	
	public void getXorY(View v) {
		boolean isFilled = true;

		EditText xField = (EditText) findViewById(R.id.getYField);
		EditText yField = (EditText) findViewById(R.id.getXField);
		
		double XorY = 0.0;
		
		if(v.getId() == R.id.getX) {
			String XorYString = yField.getText().toString();
			
			if(XorYString.isEmpty()) {
				isFilled = false;
				yField.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.y_red), null);
			} else {
				XorY = Double.parseDouble(XorYString);
				yField.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.y), null);
			}
		} else if(v.getId() == R.id.getY) {
			String XorYString = xField.getText().toString();
			
			if(XorYString.isEmpty()) {
				isFilled = false;
				xField.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.x_red), null);
			} else {
				XorY = Double.parseDouble(XorYString);
				xField.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.x), null);
			}
		}
		
		if(isFilled) {
			Intent intent = new Intent(ResultActivity.this, CalculateActivity.class);
			intent.putExtra("xCoords", xCoords);
			intent.putExtra("yCoords", yCoords);
			intent.putExtra("idViewCurve", idViewCurve);
			intent.putExtra("willReturnX", v.getId() == R.id.getX);
			intent.putExtra("XorY", XorY);
			ResultActivity.this.startActivity(intent);
		}
	}
	
	public void showParams(View v) {
		Intent intent = new Intent(ResultActivity.this, ParameterActivity.class);
		intent.putExtra("xCoords", xCoords);
		intent.putExtra("yCoords", yCoords);
		intent.putExtra("idViewCurve", idViewCurve);
		ResultActivity.this.startActivity(intent);
	}
}
