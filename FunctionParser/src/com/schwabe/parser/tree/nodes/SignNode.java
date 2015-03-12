package com.schwabe.parser.tree.nodes;

import com.schwabe.parser.exceptions.DivisionByZeroException;
import com.schwabe.parser.exceptions.WrongBracketException;
import com.schwabe.parser.math.MathematicalSign;
import com.schwabe.parser.tree.FunctionTree;

public class SignNode extends MathematicalNode {

	private MathematicalNode	leftChild, rightChild;
	private MathematicalSign	sign;

	public SignNode(MathematicalNode node, MathematicalSign sign, String left, String right)
			throws WrongBracketException {
		this.parent = node;
		this.sign = sign;
		this.type = AbstractNodeType.SIGN;
		this.leftChild = parseNode(left);
		this.rightChild = parseNode(right);
	}

	public SignNode(MathematicalNode node, MathematicalSign sign, MathematicalNode leftNode, MathematicalNode rightNode) {
		this.parent = node;
		this.sign = sign;
		this.leftChild = leftNode;
		this.rightChild = rightNode;
	}

	public MathematicalSign getSign() {
		return this.sign;
	}

	public MathematicalNode getLeftChild() {
		return leftChild;
	}

	public MathematicalNode getRightChild() {
		return rightChild;
	}

	public double calculate() throws DivisionByZeroException {
		return sign.calculateBySign(leftChild.calculate(), rightChild.calculate());
	}

	public MathematicalNode solve() throws DivisionByZeroException, WrongBracketException {
		MathematicalSign newSign = this.sign;
		MathematicalNode leftNode = null, rightNode = null;
		if (!leftChild.containsArgument()) {
			leftNode = new NumNode(null, "" + leftChild.calculate());
		} else {
			leftNode = leftChild.solve();
		}
		if (!rightChild.containsArgument()) {
			rightNode = new NumNode(null, "" + rightChild.calculate());
		} else {
			rightNode = rightChild.solve();
		}

		/*
		 * Solve the easiest of all examples: ((()-(x)) + (?)) => ((?) - (x))
		 * Where brackets are showing nodes including their children.
		 */
		if (rightChild.containsArgument() && leftChild.containsArgument()) {
			if (leftChild.leftChildIsNull() && leftChild.getClass() == SignNode.class) {
				if (((SignNode) leftChild).getSign() == MathematicalSign.substraction
						&& this.sign == MathematicalSign.addition) {
					leftNode = this.rightChild;
					rightNode = ((SignNode) leftChild).getRightChild();
					newSign = MathematicalSign.substraction;
				}
			}
		}
		
		if (newSign == MathematicalSign.substraction || newSign == MathematicalSign.addition) {
			/*
			 * Check whether the sign is of type * or / and act accordingly
			 */
			if (leftNode.containsArgument() && rightNode.containsArgument()) {
				/*
				 * Only if both children contain argumentNodes, things are likely to get messy and we can't do anything but checking all possibilities. (At least I
				 * haven't had any other idea)
				 */
				if (leftNode.getClass() == SignNode.class && rightNode.getClass() == SignNode.class) {
					/*
					 * Check whether both children are signNodes.
					 */
					SignNode leftNodeS = (SignNode) leftNode;
					SignNode rightNodeS = (SignNode) rightNode;
					if (leftNodeS.getSign() == rightNodeS.getSign()
							&& (rightNodeS.getSign() == MathematicalSign.multiplication || rightNodeS.getSign() == MathematicalSign.division)) {

					}
				} else if (rightNode.getClass() == ArgumentNode.class && rightNode.getClass() == leftNode.getClass()) {
					/*
					 * If both children are argumentNodes, there is not much to do. We can't solve x*x nor x/x so far. x/x may never be solved automatically as
					 * it is unknown whether x will become 0 eventually
					 */
					if (newSign == MathematicalSign.substraction) {
						return new NumNode(null, "0");
					} else {
						return new SignNode(null, MathematicalSign.multiplication, "2", "x");
					}

				} else if (rightNode.getClass() == ArgumentNode.class && leftNode.getClass() == SignNode.class) {
					/*
					 * rightNode is an argumentNode whilst leftNode is a signNode containing an argumentNode somewhere within its children.
					 */
					SignNode leftNodeS = (SignNode) leftNode;
					if (leftNodeS.getSign() == MathematicalSign.multiplication) {
						if (leftNodeS.getLeftChild().getClass() == NumNode.class
								&& leftNodeS.getRightChild().getClass() == ArgumentNode.class) {
							if (newSign == MathematicalSign.substraction) {
								leftNode = new NumNode(null, ((NumNode) leftNodeS.getLeftChild()).getNumNodeValue() - 1);
								newSign = leftNodeS.getSign();
								if (((NumNode) leftNode).getNumNodeValue() == 1.0) return new ArgumentNode(this.parent);
							} else {
								leftNode = new NumNode(null, ((NumNode) leftNodeS.getLeftChild()).getNumNodeValue() + 1);
								newSign = leftNodeS.getSign();
							}
						} else if (leftNodeS.getLeftChild().getClass() == ArgumentNode.class
								&& leftNodeS.getRightChild().getClass() == NumNode.class) {
							leftNodeS = new SignNode(null, leftNodeS.getSign(), leftNodeS.getRightChild(),
									leftNodeS.getLeftChild());
						}
					}
				}
			}
		}
		// System.out.println(leftNode.toString());
		// System.out.println(rightNode.toString());
		SignNode sn = new SignNode(null, newSign, leftNode, rightNode);
		sn.getLeftChild().setParent(sn);
		sn.getRightChild().setParent(sn);
		return sn;
	}

	protected boolean containsArgument() {
		return (leftChild.containsArgument() || rightChild.containsArgument());
	}

	protected boolean leftChildIsNull() {
		if (NumNode.class == this.leftChild.getClass()) {
			if (((NumNode) leftChild).getNumNodeValue() <= Math.pow(10, -10)) return true;
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (parent != null) {
			if (parent.getClass() == this.getClass()) {
				if ((((SignNode) parent).getSign().getSignQuality() != this.sign.getSignQuality()
						&& this.sign.getSignQuality() == 1 && ((SignNode) parent).getSign().getSignQuality() != 2)
						|| (((SignNode) parent).getSign() == MathematicalSign.substraction && this.sign == MathematicalSign.addition)) sb
						.append("(");
			}
		}
		if (leftChild.getClass() == NumNode.class) {
			if (Math.abs(((NumNode) leftChild).getNumNodeValue()) >= Math.pow(10, -10) || sign.getSignQuality() == 2) {
				sb.append(leftChild.toString());
				sb.append(sign.toString());
			} else if (sign.toString().equals("-")) {
				sb.append(sign.toString());
			}
		} else {
			sb.append(leftChild.toString());
			sb.append(sign.toString());
		}
		sb.append(rightChild.toString());
		if (parent != null) {
			if (parent.getClass() == this.getClass()) {
				if ((((SignNode) parent).getSign().getSignQuality() != this.sign.getSignQuality()
						&& this.sign.getSignQuality() == 1 && ((SignNode) parent).getSign().getSignQuality() != 2)
						|| (((SignNode) parent).getSign() == MathematicalSign.substraction && this.sign == MathematicalSign.addition)) sb
						.append(")");
			}
		}
		return sb.toString();
	}
}
