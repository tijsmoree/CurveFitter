package com.moreedejonge.curvefitter;

public class Maths {
	public static double log(double p, double x) {
		return ((Math.log(x)) / (Math.log(p)));
	}
	
	public static double round(double a, int b) {
		return Math.round(a * Math.pow(10, b)) / Math.pow(10, b);
	}
}
