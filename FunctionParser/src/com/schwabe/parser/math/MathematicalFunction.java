package com.schwabe.parser.math;

public enum MathematicalFunction {

	sin, cos, tan, sqrt;

	public static String[] getFunctionNames() {
		String[] s = {"sin","cos","tan","sqrt"};
		return s;
	}
	
	public static MathematicalFunction getMathematicalFunctionByArrayIndex(int i){
		MathematicalFunction[] s = {sin, cos, tan, sqrt};
		return s[i];
	}
	
	public static MathematicalFunction getMathematicalFunctionByString(String input){
		String[] s = MathematicalFunction.getFunctionNames();
		for(int i = 0; i < s.length; i++){
			if(s[i].equals(input)){
				return getMathematicalFunctionByArrayIndex(i);
			}
		}
		return null;
	}

	public double calculate(double input) {
		switch(this){
			case sin:
				return Math.sin(input);
			case cos:
				return Math.cos(input);
			case tan:
				return Math.tan(input);
			case sqrt:
				return Math.sqrt(input);
			default:
				return input;
		}
	}
	
	public String toString(){
		switch(this){
			case sin:
				return "sin";
			case cos:
				return "cos";
			case tan:
				return "tan";
			case sqrt:
				return "sqrt";
			default:
				return "";
		}
	}
	
	
}
