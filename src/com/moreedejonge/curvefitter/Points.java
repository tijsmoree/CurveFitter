package com.moreedejonge.curvefitter;

import com.moreedejonge.curvefitter.R;

public class Points {
	private double[] xCoords;
	private double[] yCoords;

	private Curve lin;
	private Curve par;
	private Curve exp;
	private Curve log;
	
	public Points(double[] xCoords, double[] yCoords) {
		this.xCoords = xCoords;
		this.yCoords = yCoords;

		this.lin = this.new Line();
		this.par = this.new Parabola();
		this.exp = this.new Exponential();
		this.log = this.new Logarithm();
	}
	
	class Curve {
		protected double a;
		protected double b;
		protected double c;
		
		protected double avgAbsD;
		
		Curve() {
			this.calcParam();
			
			this.calcAvgAbsD();
		}
		
		protected void calcParam() {
			this.a = 0.0;
			this.b = 0.0;
			this.c = 0.0;
		}
		
		protected void calcAvgAbsD() {
			int n = Points.this.xCoords.length;
			
			double x, y, sumYQ = 0.0;
			double[] q;
			
			for(int i = 0; i < n; i++) {
				x = Points.this.xCoords[i];
				y = Points.this.yCoords[i];
				q = this.getY(x);
				
				if(q[0] > 0) {
					sumYQ += Math.abs(y - q[1]);
				}
			}
			
			this.avgAbsD = sumYQ / ((double) n);
		}
		
		protected double[] getY(double x) {
			double[] y = new double[2];
			
			y[0] = 1.0;
			y[1] = x;
			
			for(int i = 0; i < y.length; i++) {
				y[i] = Maths.round(y[i], 10);
			}
			
			return y;
		}
		
		protected double[] getX(double y) {
			double[] x = new double[2];
			
			x[0] = 1.0;
			x[1] = y;
			
			for(int i = 0; i < x.length; i++) {
				x[i] = Maths.round(x[i], 10);
			}
			
			return x;
		}
		
		public double getAvgAbsD() {
			return this.avgAbsD;
		}
		
		public double[] getXCoords() {
			return Points.this.xCoords;
		}
		
		public double[] getYCoords() {
			return Points.this.yCoords;
		}
		
		protected String getFunction() {
			double a_ = Maths.round(this.a, 2);
			double b_ = Maths.round(this.b, 2);
			
			String function = "no " + a_ + " no " + b_;
			
			return function;
		}
		
		protected String getPlainFunction() {
			String function = "no";
			
			return function;
		}
		
		protected double[] getParams() {
			double[] params = new double[2];

			params[0] = this.a;
			params[1] = this.b;
			
			return params;
		}
		
		protected int getDrawableId() {
			return R.drawable.lin_par;
		}
	}
	
	class Line extends Curve {
		@Override
		protected void calcParam() {
			double n = (double) Points.this.xCoords.length, sY = 0.0, sX = 0.0, sYX = 0.0, sX2 = 0.0;
			
			for(int i = 0; i < n; i++) {
				double x = Points.this.xCoords[i];
				double y = Points.this.yCoords[i];
				
				sY += y;
				sX += x;
				sYX += x * y;
				sX2 += x * x;
			}
			
			double d = sX * sX - n * sX2;
			
			double dA = sY * sX - n * sYX;
			double dB = sX * sYX - sY * sX2;
			
			this.a = Maths.round(dA / d, 12);
			this.b = Maths.round(dB / d, 12);
		}
		
		@Override
		protected double[] getY(double x) {
			double[] y = new double[2];
			
			y[0] = 1.0;
			y[1] = this.a * x + this.b;
			
			for(int i = 0; i < y.length; i++) {
				y[i] = Maths.round(y[i], 10);
			}
			
			return y;
		}
		
		@Override
		protected double[] getX(double y) {
			double[] x = new double[2];
			
			x[0] = 1.0;
			x[1] = (y - this.b) / this.a;
			
			for(int i = 0; i < x.length; i++) {
				x[i] = Maths.round(x[i], 10);
			}
			
			return x;
		}
		
		@Override
		protected String getFunction() {
			double a_ = Maths.round(this.a, 2);
			double b_ = Maths.round(this.b, 2);

			String aPart = "";
			String ab = "";
			String bPart = "";
			
			if(this.a == 0.0) {
				aPart = "";
			} else if(this.a == 1.0) {
				aPart = "x";
			} else if(this.a == -1.0) {
				aPart = "-x";
			} else {
				aPart = a_ + "x";
			}
			
			if(this.a != 0.0 && this.b < 0.0) {
				ab = " ";
			} else if(this.a != 0.0 && this.b > 0.0) {
				ab = " + ";
			} else if(this.a == 0.0 && this.b < 0.0) {
				ab = " ";
			} else {
				ab = "";
			}
			
			if(this.b == 0.0) {
				bPart = "";
			} else if(this.b < 0.0) {
				bPart = "- " + -b_;
			} else if(this.b > 0.0) {
				bPart = "" + b_;
			}
			
			String function;
			
			if(aPart == "" && bPart == "") {
				function = "y = 0.0";
			} else {
				function = "y = " + aPart + ab + bPart;
			}
			
			return function;
		}
		
		@Override
		protected String getPlainFunction() {
			String function = "y = ax + b";
			
			return function;
		}
		
		@Override
		protected double[] getParams() {
			double[] params = new double[2];

			params[0] = this.a;
			params[1] = this.b;
			
			return params;
		}
		
		@Override
		protected int getDrawableId() {
			return R.drawable.lin;
		}
	}
	
	class Parabola extends Curve {
		@Override
		protected void calcParam() {
			double n = (double) Points.this.xCoords.length, sY = 0.0, sX2 = 0.0, sX = 0.0, sYX = 0.0, sX3 = 0.0, sYX2 = 0.0, sX4 = 0.0;
			
			for(int i = 0; i < n; i++) {
				double x = Points.this.xCoords[i];
				double y = Points.this.yCoords[i];

				sY += y;
				sX2 += x * x;
				sX += x;
				sYX += x * y;
				sX3 += x * x * x;
				sYX2 += y * x * x;
				sX4 += x * x * x * x;
				
			}

			double d = sX2 * sX2 * sX2 + sX * sX * sX4 + sX3 * sX3 * n - n * sX2 * sX4 - 2 * sX * sX2 * sX3;

			double dA = sY * sX2 * sX2 + sX * sX * sYX2 + sYX * sX3 * n - n * sX2 * sYX2 - sX * sYX * sX2 - sX * sX3 * sY;
			double dB = sX2 * sYX * sX2 + sY * sX * sX4 + sX3 * sYX2 * n - n * sYX * sX4 - sY * sX3 * sX2 - sX * sYX2 * sX2;
			double dC = sX2 * sX2 * sYX2 + sYX * sX * sX4 + sX3 * sX3 * sY - sY * sX2 * sX4 - sX * sX3 * sYX2 - sX2 * sYX * sX3;

			this.a = Maths.round(dA / d, 12);
			this.b = Maths.round(dB / d, 12);
			this.c = Maths.round(dC / d, 12);
		}
		
		@Override
		protected double[] getY(double x) {
			double[] y = new double[2];
			
			y[0] = 1.0;
			y[1] = this.a * x * x + this.b * x + this.c;
			
			for(int i = 0; i < y.length; i++) {
				y[i] = Maths.round(y[i], 10);
			}
			
			return y;
		}
		
		@Override
		protected double[] getX(double y) {
			double[] x = new double[3];
			
			double d = this.b * this.b - 4.0 * this.a * (this.c - y);
			
			if(d < 0.0) {
				x[0] = 0.0;
			} else if(d == 0.0) {
				x[0] = 1.0;
				x[1] = - this.b / 2.0 / this.a;
			} else if(d > 0.0) {
				x[0] = 2.0;
				x[1] = (- this.b + Math.sqrt(d)) / 2.0 / this.a;
				x[2] = (- this.b - Math.sqrt(d)) / 2.0 / this.a;
			}
			
			for(int i = 0; i < x.length; i++) {
				x[i] = Maths.round(x[i], 10);
			}
			
			return x;
		}
		
		@Override
		protected String getFunction() {
			double a_ = Maths.round(this.a, 2);
			double b_ = Maths.round(this.b, 2);
			double c_ = Maths.round(this.c, 2);

			String aPart = "";
			String ab = "";
			String bPart = "";
			String bc = "";
			String cPart = "";
			
			if(this.a == 0.0) {
				aPart = "";
			} else if(this.a == 1.0) {
				aPart = "x<sup><small>2</small></sup>";
			} else if(this.a == -1.0) {
				aPart = "-x<sup><small>2</small></sup>";
			} else {
				aPart = a_ + "x<sup><small>2</small></sup>";
			}
			
			if(this.a != 0.0 && this.b < 0.0) {
				ab = " ";
			} else if(this.a != 0.0 && this.b > 0.0) {
				ab = " + ";
			} else if(this.a != 0.0 && this.c < 0.0) {
				ab = " ";
			} else if(this.a != 0.0 && this.c > 0.0) {
				ab = " + ";
			} else {
				ab = "";
			}
			
			if(this.b == 0.0) {
				bPart = "";
			} else if(this.b == 1.0) {
				bPart = "x";
			} else if(this.b == -1.0) {
				bPart = "- x";
			} else if(this.b < 0.0) {
				bPart = "- " + -b_ + "x";
			} else {
				bPart = b_ + "x";
			}
			
			if(this.b != 0.0 && this.c < 0.0) {
				bc = " ";
			} else if(this.b != 0.0 && this.c > 0.0) {
				bc = " + ";
			} else {
				bc = "";
			}
			
			if(this.c == 0.0) {
				cPart = "";
			} else if(this.c > 0.0) {
				cPart = "" + c_;
			} else if(this.c < 0.0) {
				cPart = "- " + -c_;
			}
			
			String function;
			
			if(aPart == "" && bPart == "" && cPart == "") {
				function = "y = 0.0";
			} else {
				function = "y = " + aPart + ab + bPart + bc + cPart;
			}
			
			return function;
		}
		
		@Override
		protected String getPlainFunction() {
			String function = "y = ax<sup><small>2</small></sup> + bx + c";
			
			return function;
		}
		
		@Override
		protected double[] getParams() {
			double[] params = new double[3];

			params[0] = this.a;
			params[1] = this.b;
			params[2] = this.c;
			
			return params;
		}
		
		@Override
		protected int getDrawableId() {
			return R.drawable.par;
		}
	}
	
	class Exponential extends Curve {
		@Override
		protected void calcParam() {
			double n = (double) Points.this.xCoords.length, sY = 0.0, sX = 0.0, sYX = 0.0, sX2 = 0.0;
			
			for(int i = 0; i < n; i++) {
				double x = Points.this.xCoords[i];
				double y = Points.this.yCoords[i];

				sY += Math.log(y);
				sX += x;
				sYX += x * Math.log(y);
				sX2 += x * x;
			}

			double d = n * sX2 - sX * sX;

			double dA = sY * sX2 - sX * sYX;
			double dB = n * sYX - sY * sX;

			this.a = Maths.round(Math.exp(dA / d), 12);
			this.b = Maths.round(Math.exp(dB / d), 12);
		}
		
		@Override
		protected double[] getY(double x) {
			double[] y = new double[2];
			
			y[0] = 1.0;
			y[1] = this.a * Math.pow(this.b, x);
			
			for(int i = 0; i < y.length; i++) {
				y[i] = Maths.round(y[i], 10);
			}
			
			return y;
		}
		
		@Override
		protected double[] getX(double y) {
			double[] x = new double[2];
			
			if(a < 0 && y < 0 || a > 0 && y > 0) {
				x[0] = 1;
				x[1] = Math.log(y / this.a) / Math.log(this.b);
			} else {
				x[0] = 0;
			}
			
			for(int i = 0; i < x.length; i++) {
				x[i] = Maths.round(x[i], 10);
			}
			
			return x;
		}
		
		@Override
		protected String getFunction() {
			double a_ = Maths.round(this.a, 2);
			double b_ = Maths.round(this.b, 2);

			String aPart = "";
			String bPart = b_ + "<sup><small>x</small></sup>";
			
			if(this.a == 1.0) {
				aPart = "";
			} else if(this.a == -1.0) {
				aPart = "- ";
			} else {
				aPart = a_ + " * ";
			}
			
			String function;
			
			if(this.a == 0.0) {
				function = "y = 0.0";
			} else {
				function = "y = " + aPart + bPart;
			}
			
			return function;
		}
		
		@Override
		protected String getPlainFunction() {
			String function = "y = a * b<sup><small>x</small></sup>";
			
			return function;
		}
		
		@Override
		protected double[] getParams() {
			double[] params = new double[2];

			params[0] = this.a;
			params[1] = this.b;
			
			return params;
		}
		
		@Override
		protected int getDrawableId() {
			return R.drawable.exp;
		}
	}
	
	class Logarithm extends Curve {
		@Override
		protected void calcParam() {
			double n = (double) Points.this.xCoords.length, sY = 0.0, sX = 0.0, sXY = 0.0, slnX = 0.0, sXlnX = 0.0;
			
			for(int i = 0; i < n; i++) {
				double x = Points.this.xCoords[i];
				double y = Points.this.yCoords[i];

				sY += y;
				sX += x;
				sXY += x * y;
				slnX += Math.log(x);
				sXlnX += x * Math.log(x);
			}

			double d = slnX * sX - n * sXlnX;

			double dA = sY * sX - n * sXY;
			double dB = slnX * sXY - sY * sXlnX;

			this.a = Maths.round(dA / d, 12);
			this.b = Maths.round(dB / d, 12);
		}
		
		@Override
		protected double[] getY(double x) {
			double[] y = new double[2];
			
			if(x > 0) {
				y[0] = 1.0;
				y[1] = this.a * Math.log(x) + this.b;
			} else if(this.a == 0.0) {
				y[0] = 1.0;
				y[1] = this.b;
			} else {
				y[0] = 0.0;
			}
			
			for(int i = 0; i < y.length; i++) {
				y[i] = Maths.round(y[i], 10);
			}
			
			return y;
		}
		
		@Override
		protected double[] getX(double y) {
			double[] x = new double[2];
			
			x[0] = 1.0;
			x[1] = Math.exp((y - this.b) / this.a);
			
			for(int i = 0; i < x.length; i++) {
				x[i] = Maths.round(x[i], 10);
			}
			
			return x;
		}
		
		@Override
		protected String getFunction() {
			double a_ = Maths.round(this.a, 2);
			double b_ = Maths.round(this.b, 2);

			String aPart = "";
			String bPart = "";
			
			if(this.a == 1.0) {
				aPart = "ln x";
			} else if(this.a == -1.0) {
				aPart = "- ln x";
			} else {
				aPart = a_ + " * ln x";
			}
			
			if(this.b == 0.0) {
				bPart = "";
			} else if(this.b < 0.0) {
				bPart = " - " + -b_;
			} else {
				bPart = " + " + b_;
			}
			
			String function;
			
			if(this.a == 0.0) {
				function = "y = " + b_;
			} else {
				function = "y = " + aPart + bPart;
			}
			
			return function;
		}
		
		@Override
		protected String getPlainFunction() {
			String function = "y = a * ln x + b";
			
			return function;
		}
		
		@Override
		protected double[] getParams() {
			double[] params = new double[2];

			params[0] = this.a;
			params[1] = this.b;
			
			return params;
		}
		
		@Override
		protected int getDrawableId() {
			return R.drawable.log;
		}
	}
	
	public Curve getLin() {
		return this.lin;
	}
	
	public Curve getPar() {
		return this.par;
	}
	
	public Curve getExp() {
		return this.exp;
	}
	
	public Curve getLog() {
		return this.log;
	}
	
	public Curve getBestCurve() {
		Curve best = this.lin;
		double lowestAvgAbsD = this.lin.avgAbsD;
		
		if(lowestAvgAbsD > exp.avgAbsD) {
			best = this.exp;
			lowestAvgAbsD = exp.avgAbsD;
		}
		
		if(lowestAvgAbsD > log.avgAbsD) {
			best = this.log;
			lowestAvgAbsD = log.avgAbsD;
		}
		
		if(lowestAvgAbsD > par.avgAbsD && Points.this.xCoords.length > 2) {
			best = this.par;
			lowestAvgAbsD = par.avgAbsD;
		}
		
		return best;
	}
	
	public Curve getCurveById(int id) {
		Curve curve = this.getBestCurve();
		
		if(id == R.id.btnLinear) {
    		curve = this.lin;
    	} else if(id == R.id.btnParabolic) {
    		curve = this.par;
    	} else if(id == R.id.btnExponential) {
    		curve = this.exp;
    	} else if(id == R.id.btnLogarithmic) {
    		curve = this.log;
    	}
		
		return curve;
	}
}
