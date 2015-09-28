import java.util.*;
import java.text.*;
public class Calculator {

	private static boolean shouldICalculateWithSigFigs = true;

	public static void main (String[]args){
		Scanner console = new Scanner(System.in);
		System.out.println("This is a text based calculator.\nWhat would you like to calculate?");
		String given = console.nextLine();
		if (shouldICalculateWithSigFigs) {
			System.out.println("Answer: " + calculateWithSigFigs(given));
		}
		else {
			System.out.println("Answer: " + calculate(given));
		}
	}

	//7.799 - 6.25 + 0.0588/0.677*0.925874 =
	//(-38.56*68.3/(12.011*2+1.0079*6+16)*1000)/(273.15+78.3) = -162.66 = -163
	//24.022+6.0474+16 = 46.069
	//12.011*2.0+1.0079*6+16 = 24. + 6.0474 + 16
	//12.011*2.0
   //3.0001+2.42-1.763
	public static String calculateWithSigFigs (String given){
		boolean start = true;
		if (given.contains(" ")){
			given = given.replace(" ","");
		}
		while (given.contains(")(")) {
			given = given.replace(")(", ")*(");
		}

		while (given.contains("(") && given.contains(")")){
			System.out.println("Given " + given);
			given = eliminateParenthesis(given);
		}
		String answer = "";
		String temp = "";
		/*Scans the given until it reaches a + or -, afterwards
      it calculates the temporary string with multipliers and divisions
      and rounds to the lowest sig fig in that temp string.*/
		while (given.contains("*") || given.contains("\\") || given.contains("/")){
			start = false;
			while (given.length() != 0){
				if (isNumber("" + given.charAt(0)) || given.charAt(0) == '.' || given.charAt(0) == '*' || given.charAt(0) == '\\'|| given.charAt(0) == '/'){
					temp += given.charAt(0);
					given = given.substring(1);
				}
				else {
					System.out.println("Line 50 " + temp);
					int lowestSigFig = calculateLowestSigFig(temp);
					System.out.println("Line 52 " + lowestSigFig);
					String calculatedAnswer = calculate(temp);
					if (calculateSigFigs(calculatedAnswer) == lowestSigFig){
						answer += calculatedAnswer + given.charAt(0);
					}
					given = given.substring(1);
					temp = "";
				}
			}
			answer += temp;
		}
		if (start){
			answer = given;
		}
		System.out.println("line 64 " + answer);
		int leastExactDecimalPlace = leastExactDecimal(answer);
		given = calculate(answer); //Given is the calculated problem
		given = roundDecimals(given, leastExactDecimalPlace);
		return given;
	}

	public static String roundDecimals (String number, int decimalPlace){
		String format = "";
		System.out.println("Line 70 " + decimalPlace);
		if (decimalPlace < 0){ // If the least exact decimal place is on the right of the decimal
			format += "0.";
			for (int a = 1; a <= Math.abs(decimalPlace); a++){
				format += "0";
			}
		}
		else if (decimalPlace == 0){
			format = ".";
		}
		DecimalFormat df = new DecimalFormat(format);
		return df.format(Double.parseDouble(number));
	}

	//24.022+6.0474+16
	//168000 + 120000
	/*Scans through the entire given string and finds the least 
	 exact decimal in the entire calculation.*/
	public static int leastExactDecimal (String given){
		given += " ";
		boolean start = true;
		int decimalPlace = 0;
		String temp = "";
		while (given.length() != 0){
			if (isNumber("" + given.charAt(0)) || given.charAt(0) == '.'){
				temp += given.charAt(0);
				given = given.substring(1);
			}
			else {
				if (temp.contains(".")) {
					if ((temp.indexOf(".") + 1) != temp.length()){ //Look at the right of the decimal place of the number
						if (start){
							decimalPlace = -(temp.length()-(temp.indexOf(".")+1));
							start = false;
						}
						else if (-(temp.length()-(temp.indexOf(".")+1)) > decimalPlace){
							decimalPlace = -(temp.length()-(temp.indexOf(".")+1));
						}
					}
					else if (0 > decimalPlace) {
						System.out.println("line 106");
						if (start){
							start = false;
						}
						decimalPlace = 0;
					}
				}
				System.out.println("temp " + temp);
				temp = "";
				given = given.substring(1);
			}	
		}
		return decimalPlace;
	}

	/*
	1. Scan and find the sig fig for each individual number
	2. store the lowest sig fig into a variable
	3. return it.
	 */
	//12.011*2+1.0079*6+16

	public static int calculateLowestSigFig (String given){
		int lowestSigFig = 100000;
		String temp = "";
		while (given.length() != 0){
			if (isNumber("" + given.charAt(0)) || given.charAt(0) == '.'){
				temp += given.charAt(0);
				given = given.substring(1);
			}
			else {
				if (calculateSigFigs(temp) < lowestSigFig){
					lowestSigFig = calculateSigFigs(temp);
				}
				given = given.substring(1);
				temp = "";
			}
		}
		return lowestSigFig;
	}

	public static int calculateSigFigs (String number){
		int length = number.length()-1;
		if (!number.contains(".")){
			return 100000;
		}
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

	public static String remove (String a, int i){
		return a.substring(0,i) + a.substring(i+1);
	}

	public static String eliminateParenthesis (String given) {
		String parentalPart = "";
		int firstParenIndex = given.indexOf("(") + 1;
		boolean found = false;
		int numOfForwardParen = 1; // Stores the number of "("
		int numOfBackwardParen = 0; // Stores the number of ")")
		while (found == false){
			if (given.charAt(firstParenIndex) == '('){
				numOfForwardParen++;
				firstParenIndex++;
			}
			else if (given.charAt(firstParenIndex) == ')'){
				numOfBackwardParen++;
				firstParenIndex++;
				if (numOfForwardParen == numOfBackwardParen && firstParenIndex == given.length()){
					parentalPart = given.substring(given.indexOf("("), firstParenIndex);
					found = true;
				}
				else if (numOfForwardParen == numOfBackwardParen && given.charAt(firstParenIndex) != ')'){
					parentalPart = given.substring(given.indexOf("("), firstParenIndex);
					found = true;
				}
			}
			else {
				firstParenIndex++;
			}
		}
		String calculatedPart = parentalPart.substring(1,parentalPart.length()-1);

		//If calculated part is already a number, return the number
		//and replace the parental part with the calculated part.
		if (isNumber(calculatedPart)){
			return given.replace(parentalPart, calculatedPart);
		}

		if (shouldICalculateWithSigFigs){
			if (calculatedPart.contains("(") && calculatedPart.contains(")")){
				return "lol";
			}
			else {
				calculatedPart = calculateWithSigFigs(calculatedPart);
			}
		}
		else {
			return given.replace(parentalPart, calculate(calculatedPart));
		}
		return "";
	}

	//This method will given the given calculation. lol
	public static String calculate (String given){ //2 + 2 + 2 + 2
		if (given.length() == 0){
			return "No given input";
		}
		if (given.contains(" ")){
			given = given.replace(" ","");
		}
		boolean start = true; //method just started
		double sum = 0;
		String temp = "";
		double firstValue = 0;
		String expression = "";
		int lastIndex = 0;
		while (given.length() != 0){

			while (given.contains(")(")) {
				given = given.replace(")(", ")*(");
			}

			while (given.contains("(") && given.contains(")")){
				given = eliminateParenthesis(given);
			}

			while (given.contains("^")){
				given = removeExponents(given);
			}

			while (given.contains("*") || given.contains("\\") || given.contains("/")){
				given = pEMDAS(given);
			}

			if (given.charAt(0) == '-'){
				if (isNumber(given.substring(1))){
					return given;
				}
			}

			if (isNumber(given)){
				return given;
			}

			if (start == true){
				boolean isNegative = false;
				if (given.charAt(0) == '-'){
					isNegative = true;
					given = given.substring(1);
				}
				for (int x = 0; x < given.length(); x++){
					if (isNumber("" + given.charAt(x))){
						temp = temp + given.charAt(x);
						lastIndex++;
					}
					else if (given.charAt(x) == '.'){
						temp = temp + given.charAt(x);
						lastIndex++;
					}
					else {
						break;
					}
				}
				given = given.substring(lastIndex);
				if (isNegative){
					sum = Double.parseDouble(temp) * -1;
				}
				else {
					sum = Double.parseDouble(temp);
				}
				temp = "";
				lastIndex = 0;
				start = false;
			}
			expression = "" + given.charAt(0);
			given = given.substring(1);
			for (int x = 0; x < given.length(); x++){
				if (isNumber("" + given.charAt(x))){
					temp = temp + given.charAt(x);
					lastIndex++;
				}
				else if (given.charAt(x) == '.'){
					temp = temp + given.charAt(x);
					lastIndex++;
				}
				else {
					break;
				}
			}
			given = given.substring(lastIndex);
			lastIndex = 0;
			sum = expressionalCalculation(sum, Double.parseDouble(temp), expression);
			temp = "";
			//break;
		}
		return "" + sum;
	}

	public static String removeExponents (String given){
		String firstNumber = "";
		String exponent = "";
		int signIndex = given.indexOf("^");
		int firstIndex = 0;
		int secIndex = 0;
		//2-10^-1
		for (int x = signIndex-1; x >= 0; x--){
			if (isNumber("" + given.charAt(x))){
				firstNumber = given.charAt(x) + firstNumber;
				firstIndex = x;
			}
			else if (given.charAt(x) == '.'){
				firstNumber = given.charAt(x) + firstNumber;
				firstIndex = x;
			}
			else if (given.charAt(x) == '-'){
				firstNumber = given.charAt(x) + firstNumber;
				firstIndex = x;
			}
			else {
				break;
			}
		}
		for (int x = signIndex+1; x < given.length(); x++){
			if (isNumber("" + given.charAt(x))){
				exponent = exponent + given.charAt(x);
				secIndex = x;
			}
			else if (given.charAt(x) == '.'){
				exponent = exponent + given.charAt(x);
				secIndex = x;
			}
			else if (given.charAt(x) == '-'){
				exponent = exponent + given.charAt(x);
				secIndex = x;
			}
			else {
				break;
			}
		}
		return given.replace(given.substring(firstIndex, secIndex + 1), "" + Math.pow(Double.parseDouble(firstNumber), Double.parseDouble(exponent)));
	}


	public static String pEMDAS (String given){
		int divisionIndex = 10000;
		if (given.contains("\\") && given.contains("/")){
			if (given.indexOf("\\") < given.indexOf("/")){
				divisionIndex = given.indexOf("\\");
			}
			else if (given.indexOf("/") < given.indexOf("\\")){
				divisionIndex = given.indexOf("/");
			}
		}
		else if (given.contains("\\")){
			divisionIndex = given.indexOf("\\");
		}
		else if (given.contains("/")){
			divisionIndex = given.indexOf("/");
		}
		int multiplicationIndex = given.indexOf("*");
		if (multiplicationIndex == -1){
			multiplicationIndex = 10000;
		}
		String firstValue = "";
		int firstIndex = 0;
		String secondValue = "";
		int secIndex = 0;
		if (divisionIndex < multiplicationIndex){
			firstIndex = divisionIndex;
			secIndex = divisionIndex;
			for (int x = divisionIndex-1; x >= 0; x--){
				if (isNumber("" + given.charAt(x))){
					firstValue = given.charAt(x) + firstValue;
					firstIndex = x;
				}
				else if (given.charAt(x) == '.'){
					firstValue = given.charAt(x) + firstValue;
					firstIndex = x;
				}
				else {
					break;
				}
			}

			for (int x = divisionIndex+1; x < given.length(); x++){
				if (isNumber("" + given.charAt(x))){
					secondValue = secondValue + given.charAt(x);
					secIndex = x;
				}
				else if (given.charAt(x) == '.'){
					secondValue = secondValue + given.charAt(x);
					secIndex = x;
				}
				else {
					break;
				}
			}
			return given.replace(given.substring(firstIndex, secIndex + 1), "" + Double.parseDouble(firstValue)/Double.parseDouble(secondValue));
		}
		else {
			firstIndex = multiplicationIndex;
			secIndex = multiplicationIndex;
			for (int x = multiplicationIndex-1; x >= 0; x--){
				if (isNumber("" + given.charAt(x))){
					firstValue = given.charAt(x) + firstValue;
					firstIndex = x;
				}
				else if (given.charAt(x) == '.'){
					firstValue = given.charAt(x) + firstValue;
					firstIndex = x;
				}
				else {
					break;
				}
			}

			for (int x = multiplicationIndex+1; x < given.length(); x++){
				if (isNumber("" + given.charAt(x))){
					secondValue = secondValue + given.charAt(x);
					secIndex = x;
				}
				else if (given.charAt(x) == '.'){
					secondValue = secondValue + given.charAt(x);
					secIndex = x;
				}
				else {
					break;
				}
			}
			return given.replace(given.substring(firstIndex, secIndex + 1), "" + Double.parseDouble(firstValue)*Double.parseDouble(secondValue));
		}
	}

	public static double expressionalCalculation (double sum, double tempNum, String expression){
		switch (expression){
		case "+": return sum + tempNum;
		case "-": return sum - tempNum;
		}
		return 10101;
	}

	public static boolean isNumber (String givenChar){
		try {

			if (givenChar.charAt(0) == '+' || givenChar.charAt(0) == '-'){
				return false;
			}
			double d = Double.parseDouble(givenChar);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}