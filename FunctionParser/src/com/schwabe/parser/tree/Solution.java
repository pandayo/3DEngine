package com.schwabe.parser.tree;

import com.schwabe.parser.tree.nodes.MathematicalNode;

public class Solution {

	private double				solution	= 0.0;
	private MathematicalNode	mn;
	private FunctionType		type;

	public Solution(double solution, FunctionType type) {
		this.solution = solution;
		this.type = type;
	}

	public Solution(MathematicalNode mn, FunctionType type) {
		this.mn = mn;
		this.type = type;
	}

	public String toString() {
		switch (type) {
			case CALCULATION:
				return "" + solution;
			default:
				return mn.toString();
		}
	}

}
