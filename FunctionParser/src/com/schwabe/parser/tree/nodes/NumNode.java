package com.schwabe.parser.tree.nodes;

public class NumNode extends MathematicalNode {

	private double	numNodeValue;

	public NumNode(MathematicalNode node, String input) {
		this.parent = node;
		this.numNodeValue = Double.parseDouble(input);
		this.type = AbstractNodeType.NUM;
		// System.out.println("This is a numNode with value "+numNodeValue);
	}
	
	public NumNode(MathematicalNode node, double value) {
		this.parent = node;
		this.numNodeValue = value;
		this.type = AbstractNodeType.NUM;
		// System.out.println("This is a numNode with value "+numNodeValue);
	}

	public double calculate() {
		return this.numNodeValue;
	}
	
	public MathematicalNode solve(){
		return this;
	}

	public String toString() {
		if (this.numNodeValue < 0) return "(" + this.numNodeValue + ")";
		else return "" + this.numNodeValue;
	}

	public double getNumNodeValue() {
		return numNodeValue;
	}
}
