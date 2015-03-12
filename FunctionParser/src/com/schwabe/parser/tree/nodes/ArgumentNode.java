package com.schwabe.parser.tree.nodes;

public class ArgumentNode extends MathematicalNode {

	public ArgumentNode(MathematicalNode parent) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.type = AbstractNodeType.ARGUMENT;
	}
	
	public String toString(){ return "x"; }
	
	public MathematicalNode solve(){
		return this;
	}
	
	protected boolean containsArgument() {
		return true;
	}

}
