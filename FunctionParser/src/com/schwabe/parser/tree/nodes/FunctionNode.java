package com.schwabe.parser.tree.nodes;

import com.schwabe.parser.exceptions.DivisionByZeroException;
import com.schwabe.parser.exceptions.WrongBracketException;
import com.schwabe.parser.math.MathematicalFunction;

public class FunctionNode extends MathematicalNode{
	
	private MathematicalNode child;
	private MathematicalFunction mf;

	public FunctionNode(MathematicalNode parent, MathematicalFunction mf, String input) throws WrongBracketException {
		this.parent = parent;
		this.mf = mf;
		this.child = parseNode(input);
		this.type = AbstractNodeType.FUNCTION;
	}
	
	public double calculate() throws DivisionByZeroException{ 
		double sol = child.calculate();
		return mf.calculate(sol);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(mf.toString());
		sb.append("(");
		sb.append(child.toString());
		sb.append(")");
		return sb.toString();
	}

}
