package com.schwabe.parser.tree.nodes;

import com.schwabe.parser.exceptions.DivisionByZeroException;
import com.schwabe.parser.exceptions.WrongBracketException;
import com.schwabe.parser.math.MathematicalFunction;
import com.schwabe.parser.math.MathematicalSign;
import com.schwabe.parser.tree.FunctionTree;

public abstract class MathematicalNode {

	protected MathematicalNode	parent	= null;
	protected AbstractNodeType	type;

	public void setParent(MathematicalNode p) {
		this.parent = p;
		this.type = AbstractNodeType.ABSTRACT;
	}

	public double calculate() throws DivisionByZeroException {
		return 0;
	}

	public MathematicalNode solve() throws DivisionByZeroException, WrongBracketException {
		return null;
	}

	public MathematicalNode ease() {
		return null;
	}
	
	protected boolean containsArgument(){
		return false;
	}
	
	protected boolean leftChildIsNull(){
		return true;
	}

	protected MathematicalNode parseNode(String input) throws WrongBracketException {
		if (input.matches("(\\-?\\.?[0-9]+(\\.[0-9]+)*)")) { return new NumNode(this, input); }
		MathematicalNode sn = null;
		try {
			sn = findFirstSignNode(input);
		} catch (WrongBracketException e) {
			throw e;
		}
		if (sn != null) {
			return sn;
		} else {
			sn = findFunctionNode(input);
			if (sn != null) {
				return sn;
			} else {
				if (input.startsWith("(") && input.endsWith(")")) {
					return parseNode(input.substring(1, input.length() - 1));
				} else if (input.isEmpty()) {
					return new NumNode(this, "0");
				} else if (input.equals("x")) {
					return new ArgumentNode(this);
				} else {
					throw new IllegalArgumentException("The input is of no correct form. Input is: " + input);
				}
			}
		}
	}

	private FunctionNode findFunctionNode(String input) throws WrongBracketException {
		MathematicalFunction mf = null;
		int functionIndex = -1;
		String[] functionNames = MathematicalFunction.getFunctionNames();
		for (int i = 0; i < functionNames.length; i++) {
			if (input.contains(functionNames[i])) {
				mf = MathematicalFunction.getMathematicalFunctionByArrayIndex(i);
				functionIndex = i;
				i = functionNames.length;
			}
		}
		if (mf != null) {
			int startInd = functionNames[functionIndex].length();
			input = input.substring(startInd, input.length());
		}
		if (input.startsWith("(") && input.endsWith(")") && mf != null) { return new FunctionNode(this, mf,
				input.substring(1, input.lastIndexOf(')'))); }
		return null;
	}

	private SignNode findFirstSignNode(String input) throws WrongBracketException {
		char[] inputAsArray = input.toCharArray();
		int countLeftBracket = 0, countRightBracket = 0, countBracketBalance = 0, highestSign = -1;
		MathematicalSign sign = null;
		for (int i = 0; i < inputAsArray.length; i++) {
			char c = inputAsArray[i];
			if (c == '(') {
				countLeftBracket++;
				countBracketBalance++;
			}
			if (c == ')') {
				countRightBracket++;
				countBracketBalance--;
			}
			if ((c == '+' || c == '-') && countBracketBalance == 0) {
				highestSign = i;
				if (c == '+') sign = MathematicalSign.addition;
				else sign = MathematicalSign.substraction;
			}
			if ((c == '*' || c == '/') && countBracketBalance == 0 && sign != MathematicalSign.substraction
					&& sign != MathematicalSign.addition && sign != MathematicalSign.equals) {
				highestSign = i;
				if (c == '*') sign = MathematicalSign.multiplication;
				else sign = MathematicalSign.division;
			}

		}
		if (countLeftBracket != countRightBracket) throw new WrongBracketException(
				"The number of brackets didn't match up. There are " + countLeftBracket + " (-brackets and "
						+ countRightBracket + " )-brackets.");
		if (highestSign != -1) {
			return new SignNode(this, sign, input.substring(0, highestSign), input.substring(highestSign + 1,
					input.length()));
		} else {
			return null;
		}
	}

}
