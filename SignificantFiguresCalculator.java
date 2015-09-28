/*Significant Figures Calculator & Tester by Kiet Sam
V.1 - 6/10/13*/

import java.util.*;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;
public class SignificantFiguresCalculator extends JFrame{

	private static ArrayList<String> copies = new ArrayList<String>();
	private static int rand;
	private static int randLength;
	private static int randNum;
	public static Scanner console = new Scanner(System.in);
	
	/*This is the main method that pretty much starts everything.*/
	public static void main (String[]args){
		boolean on = false;
		do {
			String number = JOptionPane.showInputDialog(null, intro());
			//String number = console.nextLine();
			while (!(isNumber(number))){
				number = JOptionPane.showInputDialog(null, "Enter a non-zero number.");
			}
			while (number.contains(" "))
				number = remove(number,number.indexOf(" "));
			if (number.toUpperCase().equals("TEST"))
				on = makeTest();
			else {
				int choice = JOptionPane.showConfirmDialog(null, "Your number \"" + number + "\" has " + calculateSigFigs(number) + " Significant Figures.\nWould you like to input another number?");
				if (choice == JOptionPane.YES_OPTION)
					on = true;
				else
					on = false;
			}
		} while (on == true);
	}
	
	/*This method makes a test for the user depending on how many numbers they would like to be tested on.*/
	public static boolean makeTest (){
		String numbers = JOptionPane.showInputDialog(null, "How many numbers would you like to be tested on?\nPlease enter a number greater than 0:  ");
		while (!(isNumber(numbers))){
			numbers = JOptionPane.showInputDialog(null, "Please enter a number greater than 0: ");
		}
		int tests = Integer.parseInt(numbers);
		System.out.println();
		int right = 0;
		ArrayList<String> testNums = new ArrayList<String>();
		ArrayList<Integer> wrongAns = new ArrayList<Integer>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		for (int x = 1; x <= tests; x++){
			String testNumber = testNumber();
			String output = JOptionPane.showInputDialog(null,x + ": How many significant figures does this number have?\n" + testNumber);
			int answer = Integer.parseInt(output);
			int trueAnswer = calculateSigFigs(testNumber);
			if (answer == trueAnswer){ //Right
				right++;
			}
			else { //Wrong
				testNums.add(testNumber);
				wrongAns.add(answer);
				index.add(x);
			}
		}
		String conclude = "You got " + right + " out of " + tests + " correct!";
		if ((right - tests) == 0){
			conclude += "\nPerfect!";
		}
		else {
			conclude += "\n\nHere are all the numbers that you got incorrect:\n\n";
			for (int x = 0; x < testNums.size(); x++){
				conclude += "Question #" + index.get(x) + ": " + testNums.get(x) + "\n";
				conclude += "You answered: " + wrongAns.get(x) + "\n";
				conclude += "The correct answer is: " + calculateSigFigs(testNums.get(x)) + "\n\n";
			}
		}
		int choice = JOptionPane.showConfirmDialog(null, conclude + " Would you like to enter more numbers?");
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
	
	/*This makes a ranedom test number which could be these formats:
	-100 (Whole Number)
	-100. (Whole Number with Decimal)
	-10.00 (Double)
	-.001 (Decimal Number with no whole numbers)
	-10 x 10^2 or 10 e 10 (Scientific numbers)*/
	public static String testNumber (){
		String num = "";
		rand = (int)(Math.random()*5+1);;
		do{
			//1 & 2
			if (rand == 1 || rand == 2){
				randLength = (int)(Math.random()*5+1);
				randNum = (int)(Math.random()*randLength+1);
				num= "";
				for (int x = 0; x < randNum;x++){
					num+= "" + (int)(Math.random()*9+1);
				}
				for (int x = 0; x < randLength-randNum;x++) {
					num+= "0";
				}
				if (rand == 2){
					num+=".";
				}
			}
			//3 & 4
			else if (rand == 3 || rand == 4){
				randLength = (int)(Math.random()*5+1);
				randNum = (int)(Math.random()*randLength+1);
				num= "";
				for (int x = 0; x < randNum;x++){
					num+= "" + (int)(Math.random()*9+1);
				}
				num += ".";
				for (int x = 0; x < randLength-randNum;x++) {
					num+= "" + (int)(Math.random()*10);
				}
				if (rand == 4){
					randLength = (int)(Math.random()*2+1);
					if (randLength == 1){
						num+=" x 10^";
					}
					else {
						num+=" e ";
					}
					num+= "" + (int)(Math.random()*50);
				}
			}
			//5
			else {
				randLength = (int)(Math.random()*5+1);
				randNum = (int)(Math.random()*randLength+1);
				num= ".";
				for (int x = 0; x < randNum;x++){
					num+= "0";
				}
				for (int x = 0; x < randLength-randNum+1;x++) {
					num+= "" + (int)(Math.random()*9+1);
				}

			}
		}while (copies.contains(num));
		copies.add(num);
		return num;
	}
	
	/*This method calculates the amount of significant figures in a number.*/
	public static int calculateSigFigs (String number){
		int length = number.length()-1;
		if ((number.contains("x") && number.contains("10^")) || number.contains("e")){
			boolean hasDot = false;
			if (number.contains("x") && number.contains("10^")){
				number = number.substring(0,number.indexOf("x"));
			}
			else {
				number = number.substring(0,number.indexOf("e"));
			}
			if (number.contains(".")){
				number = remove(number,number.indexOf("."));
				hasDot = true;
			}
			while (number.charAt(0) == '0'){
				number = remove(number,0);
			}
			length = number.length()-1;
			while (number.charAt(length) == '0' && hasDot == false){
				number = remove(number, length);
				length = number.length()-1;
			}
			while (number.contains(" ")){
				number = remove(number,number.indexOf(" "));
			}
			return number.length();
		}
		if (number.charAt(0) == '0' || number.charAt(0) == '.'){
			while (number.charAt(0) == '0' || number.charAt(0) == '.'){
				number = remove(number,0);
			}
			if (number.contains(".")){
				number = remove(number, number.indexOf("."));
			}
			return number.length();
		}
		else if (number.charAt(0) != '0' && number.contains(".")){
			return remove(number, number.indexOf(".")).length();
		}
		else if (number.charAt(0) != '0' && number.charAt(number.length()-1) != '0'){
			return number.length();
		}
		while (number.charAt(length) == '0'){
			number = remove(number, length);
			length = number.length()-1;
		}
		return number.length();
	}
	
	/*This is a method that prints out the introduction to the program.*/
	public static String intro (){
		String test = "This is a Significant Figures Calculator: Version 1\nAvailable input formats are:\nInteger, Double, and Scientific Notation (Use this format: Number x 10^N)\nEnter a non-zero number or type TEST if you want a test: ";
		/*System.out.println("Available input formats are:");
		System.out.println("Integer, Double, and Scientific Notation (Use this format: Number x 10^N)\n");
		System.out.print("Enter a non-zero number or type TEST if you want a test: ");*/
		return test;
	}
	
	/*Thus is a number that calculates weather a string can turn into a number or not.
	It also tests if the user types in scientific notation or if the user types "TEST" so that it doesn't
	call a false on those cases.*/
	public static boolean isNumber (String s){
		if ((s.contains("x") && s.contains("10^")) || s.contains("e") || s.toUpperCase().equals("TEST")){
			if (s.contains("e")){
				String temp = removeZADs(s.substring(s.indexOf("e")+1));
				s = removeZADs(s.substring(0,s.indexOf("e")));
				if (isNumber(s) && isNumber(temp))
					return true;
				return false;
			}
			return true;
		}
		try {
			Double.parseDouble(s); 
			if (Double.parseDouble(s) == 0){
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	//Removes Zeros And Decimals from the last and first positions
	public static String removeZADs (String a){
		int length = a.length()-1;
		while (a.charAt(length) == '0'){
			a = remove(a, length);
			length = a.length()-1;
		}
		while (a.charAt(0) == '0' || a.charAt(0) == '.'){
			a = remove(a,0);
		}
		return a;
	}
	
	public static String remove (String a, int i){
		return a.substring(0,i) + a.substring(i+1);
	}
}
