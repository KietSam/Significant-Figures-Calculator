import java.util.*;
import java.text.DecimalFormat;
public class Test {
   private static boolean calculateWithSigFigs = true;
	public static void main (String[]args){
		Scanner console = new Scanner(System.in);
      DecimalFormat df = new DecimalFormat("0.##");
      String a = "2+((2+4)*2+(2+3))";
      System.out.println(a.charAt(a.indexOf("(")));
	}
   
   
   public static boolean isNumber (String givenChar){
		try {
			double d = Double.parseDouble(givenChar);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}   
   
	public static String pEMDAS (String given){
		int divisionIndex = given.indexOf("\\");
		int multiplicationIndex = given.indexOf("*");
      if (divisionIndex == -1){
         divisionIndex = 10000;
      }
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
         System.out.println(given.substring(firstIndex, secIndex));
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
         System.out.println(firstValue);
         System.out.println(secondValue);
         //System.out.println(given.substring(firstIndex-1, secIndex+1));
			return given.replace(given.substring(firstIndex, secIndex + 1), "" + Double.parseDouble(firstValue)*Double.parseDouble(secondValue));
		}
	}	
}