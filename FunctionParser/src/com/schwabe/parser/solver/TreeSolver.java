package com.schwabe.parser.solver;

import com.schwabe.parser.exceptions.DivisionByZeroException;
import com.schwabe.parser.exceptions.WrongBracketException;
import com.schwabe.parser.tree.FunctionTree;
import com.schwabe.parser.tree.Solution;

public class TreeSolver {

	public Solution solveTree(FunctionTree T){
		try {
			switch (T.getType()) {
				case CALCULATION:
					return new Solution(T.getRoot().calculate(), T.getType());
				case SOLVING:
					return new Solution(T.getRoot().solve(), T.getType());
				case PARAMETER:
					return new Solution(T.getRoot().ease(), T.getType());
			}
		} catch (DivisionByZeroException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongBracketException e){
			e.printStackTrace();
		}
		return null;
	}
}
