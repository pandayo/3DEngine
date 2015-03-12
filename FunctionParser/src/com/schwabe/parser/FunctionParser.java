package com.schwabe.parser;

import com.schwabe.parser.exceptions.WrongBracketException;
import com.schwabe.parser.exceptions.WrongNumberOfEqualsException;
import com.schwabe.parser.tree.FunctionTree;
import com.schwabe.parser.tree.Solution;

public class FunctionParser {

	private FunctionTree tree;
	
	public FunctionParser(String input) throws WrongBracketException, WrongNumberOfEqualsException{
		tree = new FunctionTree(input);
	}
	
	public static void main(String[] args){
		String input = "(-.4 * (5 + 3)) + ((7 * 2) - sin(cos(3.1415)))";
		String input2 = "1 = 2x+x-x";
		String input3 = "5-(4+1)";
		try {
			FunctionParser FP = new FunctionParser(input2);
			System.out.println(FP.toString());
			System.out.println("The solution is: "+FP.calcuate().toString());
			//FP = new FunctionParser(input2);
		} catch (WrongBracketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongNumberOfEqualsException e) {
			e.printStackTrace();
		}
	}

	private Solution calcuate() {
		return tree.calculate();
	}
	
	public String toString(){
		return tree.toString();
	}
	
}
