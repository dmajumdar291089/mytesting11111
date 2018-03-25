/**
 * 
 */
package org.dipankar.floatpattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dipankar
 *
 */
public class TestFloatingPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String date = "1.03";
		Pattern datePattern = Pattern.compile("^(?=.+)(?:[1-9]\\d*|0)?(?:\\.\\d+)?$");
		Matcher dateMatcher = datePattern.matcher(date);
		if (dateMatcher.find()) {
		    System.out.println(dateMatcher.group(0)); //prints /{item}/
		} else {
		    System.out.println("Match not found");
		}
	}

}
