/**
 * 
 */
package org.dipankar.datepattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dipankar
 *
 */
public class TestDatePattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String date = "2018-03-21T12:45:50";
		Pattern datePattern = Pattern.compile("^(19|20)\\d\\d-(0[1-9]|1[012])-([012]\\d|3[01])T([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$");
		Matcher dateMatcher = datePattern.matcher(date);
		if (dateMatcher.find()) {
		    System.out.println(dateMatcher.group(0)); //prints /{item}/
		} else {
		    System.out.println("Match not found");
		}
	}

}
