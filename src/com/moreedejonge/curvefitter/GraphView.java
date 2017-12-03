package com.moreedejonge.curvefitter;

import com.moreedejonge.curvefitter.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.*;

public class GraphView extends View {
	private Points.Curve curve;
	
	private Paint paint;

	private double xMid, yMid, xRad, yRad, axisLnTnth;
	
	private int[] xValuesPix, yValuesPix;
	
	private float xTouched = 0f;
	private float yTouched = 0f;

	private int n = 100;
	
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		double[] xs = {1.0, 2.0};
		double[] ys = {1.0, 2.0};
		Points p = new Points(xs, ys);
		this.curve = p.getBestCurve();
		
		this.paint = new Paint();

		this.xValuesPix = new int[n];
		this.yValuesPix = new int[n];
		
		setMid();
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float xMoved = 0f, yMoved = 0f;
		
		switch (e.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			this.xTouched = e.getX();
			this.yTouched = e.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			xMoved = e.getX() - this.xTouched;
			yMoved = e.getY() - this.yTouched;

			this.xTouched = e.getX();
			this.yTouched = e.getY();
			break;
		}

		this.xMid -= ((double) xMoved) / ((double) this.getWidth()) * this.xRad * 2.0;
		this.yMid += ((double) yMoved) / ((double) this.getHeight()) * this.yRad * 2.0;
		
		this.invalidate();
		
		return true;
	}
	
	public void setCurve(Points.Curve curve) {
		this.curve = curve;
		
		setMid();
		
		this.invalidate();
	}
	
	public void setMid() {
		double[] xPs = this.curve.getXCoords();
		double[] yPs = this.curve.getYCoords();

		this.xMid = getMid(xPs);
		this.yMid = getMid(yPs);
	}
	
	public void setRad() {
		double[] xPs = this.curve.getXCoords();
		double[] yPs = this.curve.getYCoords();
		
		double domain = getMax(xPs) - getMin(xPs);
		double range = getMax(yPs) - getMin(yPs);
		
		double greatest = Math.max(domain, range);
		
		if(greatest == 0.0) {
			double dX = Math.abs(xPs[0]);
			double dY = Math.abs(yPs[0]);
			
			greatest = Math.max(dX, dY);
		}
		
		this.xRad = 1.8 * greatest;
		
		if(this.getHeight() == 0) {
			this.yRad = this.xRad;
		} else {
			this.yRad = ((double) this.getHeight()) / ((double) this.getWidth()) * this.xRad;
		}
		
		this.axisLnTnth = Math.floor(Math.log10(greatest));
	}
	
	private void createXYArrays() {
		double domain = 2.0 * this.xRad;
		double range = 2.0 * this.yRad;

		double xMin = this.xMid - this.xRad;
		double yMin = this.yMid - this.yRad;

		double[] xValues = new double[n];
		double[] yValues = new double[n];
		
		if(this.curve instanceof Points.Logarithm && this.curve.getParams()[0] != 0.0) {
			for(int i = 0; i < this.n; i++) {
				yValues[i] = yMin + range / ((double) this.n - 1.0) * (double) i;
				
				xValues[i] = this.curve.getX(yValues[i])[1];
			}
		} else {
			for(int i = 0; i < this.n; i++) {
				xValues[i] = xMin + domain / ((double) this.n - 1.0) * (double) i;
				
				yValues[i] = this.curve.getY(xValues[i])[1];
			}
		}

		double[] xPixels = new double[this.n];
		double[] yPixels = new double[this.n];
		
		for (int i = 0; i < this.n; i++) {
			xPixels[i] = (xValues[i] - xMin) / domain * ((double) this.getWidth());
			yPixels[i] = (yValues[i] - yMin) / range * ((double) this.getHeight());
			
			this.xValuesPix[i] = (int) xPixels[i];
			this.yValuesPix[i] = (int) yPixels[i];
		}
	}
	
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		setRad();
		
		createXYArrays();
		
		double domain = 2.0 * this.xRad;
		double range = 2.0 * this.yRad;

		double xMin = this.xMid - this.xRad;
		double yMin = this.yMid - this.yRad;

		double cHeight = this.getHeight();
		double cWidth = this.getWidth();
		
		double axisLns = Math.pow(10, this.axisLnTnth);

		double xLnMin = axisLns * Math.ceil(xMin / axisLns);
		double yLnMin = axisLns * Math.ceil(yMin / axisLns);

		int xAxisPix = (int) ((-yMin) / range * cHeight);
		int yAxisPix = (int) ((-xMin) / domain * cWidth);
		
		//draw axes
		this.paint.setStrokeWidth(4);
		this.paint.setColor(getResources().getColor(R.color.lightGreen));
		c.drawLine(0, (float) (cHeight - xAxisPix), (float) cWidth, (float) (cHeight - xAxisPix), this.paint);
		c.drawLine((float) yAxisPix, 0, (float) yAxisPix, (float) cHeight, this.paint);
		
		
		//draw axis lines
		this.paint.setStrokeWidth(2);
		int nLnX = (int) Math.ceil(range / axisLns);
		for(int i = 0; i < nLnX; i++) {
			float xPix = (float) (int) (cHeight - (yLnMin + i * axisLns - yMin) / range * cHeight);
			float yPix = (float) (int) yAxisPix;
			
			c.drawLine(yPix - 4f, xPix, yPix + 4f, xPix, this.paint);
		}
		int nLnY = (int) Math.ceil(domain / axisLns);
		for(int i = 0; i < nLnY; i++) {
			float xPix = (float) (int) (cHeight - xAxisPix);
			float yPix = (float) (int) ((xLnMin + i * axisLns - xMin) / domain * cWidth);
			
			c.drawLine(yPix, xPix - 4f, yPix, xPix + 4f, this.paint);
		}
		
		//draw curve
		this.paint.setStrokeWidth(4);
		this.paint.setColor(getResources().getColor(R.color.darkGreen));
		for(int i = 0; i < (this.n - 1); i++) {
			float xB = (float) this.xValuesPix[i];
			float xE = (float) this.xValuesPix[i + 1];
			
			float yB = (float) (cHeight - this.yValuesPix[i]);
			float yE = (float) (cHeight - this.yValuesPix[i + 1]);
			
			c.drawLine(xB, yB, xE, yE, this.paint);
		}
		
		//draw points
		this.paint.setStrokeWidth(7);
		this.paint.setColor(getResources().getColor(R.color.lighterGreen));
		for(int i = 0; i < this.curve.getXCoords().length; i++) {
			double x = this.curve.getXCoords()[i];
			double y = this.curve.getYCoords()[i];
			
			float xPix = (float) ((x - xMin) / domain * cWidth);
			float yPix = (float) ((y - yMin) / range * cHeight);
			
			c.drawCircle(xPix, (float) (cHeight - yPix), 5, this.paint);
		}
	}
	
	private double getMin(double[] x) {
		double m = x[0];
		
		for(int i = 1; i < x.length; i++) {
			if(x[i] < m) {
				m = x[i];
			}
		}
		
		return m;
	}
	
	private double getMax(double[] x) {
		double m = x[0];
		
		for(int i = 1; i < x.length; i++) {
			if(x[i] > m) {
				m = x[i];
			}
		}
		
		return m;
	}
	
	private double getMid(double[] x) {
		double m = 0.5 * getMin(x) + 0.5 * getMax(x);
		
		return m;
	}
}
