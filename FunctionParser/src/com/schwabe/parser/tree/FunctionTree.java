package com.schwabe.parser.tree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.schwabe.parser.exceptions.DivisionByZeroException;
import com.schwabe.parser.exceptions.WrongBracketException;
import com.schwabe.parser.exceptions.WrongNumberOfEqualsException;
import com.schwabe.parser.math.MathematicalFunction;
import com.schwabe.parser.math.MathematicalSign;
import com.schwabe.parser.solver.TreeSolver;
import com.schwabe.parser.tree.nodes.FunctionNode;
import com.schwabe.parser.tree.nodes.MathematicalNode;
import com.schwabe.parser.tree.nodes.NumNode;
import com.schwabe.parser.tree.nodes.SignNode;

public class FunctionTree {

	private MathematicalNode	root	= null;
	private FunctionType		type;

	public FunctionTree(String input) throws WrongBracketException, WrongNumberOfEqualsException {
		input = input.replaceAll(" ", "");
		if (input.length() - input.replaceAll("=", "").length() > 1) throw new WrongNumberOfEqualsException(
				"There is more than one '=' in the input string!");
		input = replaceRLBrackets(input);
		input = findParamsAndMultiply(input);
		if (input.indexOf('x') == -1 && input.indexOf('=') == -1) {
			type = FunctionType.CALCULATION;
			MathematicalNode sn = null;
			try {
				sn = findFirstSignNode(input);
			} catch (WrongBracketException e) {
				throw e;
			}
			if (sn != null) {
				this.root = sn;
			} else {
				sn = findFunctionNode(input);
				if (sn != null) {
					this.root = sn;
				} else {
					if (input.matches("(\\-*\\.*[0-9]+(\\.[0-9]+)*)")) {
						this.root = new NumNode(null, input);
					} else throw new IllegalArgumentException("The input is of no correct form. Input is: " + input);
				}
			}
		} else if (input.indexOf('x') != -1 && input.indexOf('=') != -1) {
			type = FunctionType.SOLVING;
			root = new SignNode(null, MathematicalSign.equals, input.substring(0, input.indexOf('=')), input.substring(
					input.indexOf('=') + 1, input.length()));
		} else if (input.indexOf('=') == -1) {
			System.out.println("The argument given is an expression with parameter x.");
			type = FunctionType.PARAMETER;
		} else {
			throw new IllegalArgumentException("The input contains an equals-sign ");
		}

		/*
		 * if (c == '=') { if (equalscount != 0) { throw new WrongNumberOfEqualsException( "There is more than one '=' in the input Stream!"); } equalscount++;
		 * sign = MathematicalSign.equals; }
		 */

	}

	/**
	 * Hence there is the possibility to write the parameter following a simple number, we need to first check for that.
	 */
	private String findParamsAndMultiply(String input) {
		Pattern p = Pattern.compile("(\\-*\\.*[0-9]+(\\.[0-9]+)*)x");
		Matcher m = p.matcher(input);
		while (m.find()) {
			String match = m.group();
			String replacement = match.substring(0, match.length() - 1) + "*x";
			input.indexOf(match);
			input = input.substring(0, input.indexOf(match)) + replacement
					+ input.substring(input.indexOf(match) + match.length(), input.length());
		}
		return input;
	}

	/**
	 * Hence there is the possibility to write to brackets without adding the sign for multiplication, we need to add the sign ourself!
	 */
	private String replaceRLBrackets(String input) {
		int i = input.indexOf(")(");
		StringBuilder sb = new StringBuilder();
		while (input.indexOf(")(") != -1) {
			i = input.indexOf(")(");
			sb.append(input.substring(0, i + 1));
			sb.append("*");
			sb.append(input.substring(i + 1, input.length()));
			input = sb.toString();
		}
		return input;
	}

	/**
	 * We are looking for a known function and create a node with it.
	 * 
	 * @param input
	 *            The input string to be solved
	 * @return A functionNode
	 * @throws WrongBracketException
	 *             If the amount of brackets isn't balanced, this exception gets thrown
	 */
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
		if (input.startsWith("(") && input.endsWith(")") && mf != null) { return new FunctionNode(null, mf,
				input.substring(1, input.indexOf(')'))); }
		return null;
	}

	/**
	 * Find the first sign to create a node with. One preferes + and - over * and /, since we calculate a nodes children before calculating the node itself.
	 * 
	 * @param input
	 *            The input string to be solved
	 * @return A signNode
	 * @throws WrongBracketException
	 *             If the amount of brackets isn't balanced, this exception gets thrown
	 */
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
			return new SignNode(null, sign, input.substring(0, highestSign), input.substring(highestSign + 1,
					input.length()));
		} else {
			return null;
		}
	}

	public Solution calculate() {
		return new TreeSolver().solveTree(this);
	}

	public String toString() {
		return root.toString();
	}

	public FunctionType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public MathematicalNode getRoot() {
		return root;
	}
}
