package com.schwabe.parser.math;

import com.schwabe.parser.exceptions.DivisionByZeroException;

public enum MathematicalSign {
	addition, substraction, multiplication, division, equals;

	public double calculateBySign(double left, double right) throws DivisionByZeroException {
		switch (this) {
			case addition:
				return left + right;
			case substraction:
				return left - right;
			case multiplication:
				return left * right;
			case division:
				if (Math.abs(right) < Math.pow(10, -10)) throw new DivisionByZeroException(
						"You try to divide by a number smaller than " + Math.pow(10, -10));
				else return left / right;
			case equals:
				return 0;
			default:
				return 0;
		}
	}

	public int getSignQuality() {
		switch (this) {
			case addition:
				return 1;
			case substraction:
				return 1;
			case multiplication:
				return 0;
			case division:
				return 0;
			case equals:
				return 2;
			default:
				return 2;
		}
	}

	public String toString() {
		switch (this) {
			case addition:
				return "+";
			case substraction:
				return "-";
			case multiplication:
				return "*";
			case division:
				return "/";
			case equals:
				return "=";
			default:
				return "=";
		}
	}
}
