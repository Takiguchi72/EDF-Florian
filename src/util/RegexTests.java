package util;

import java.util.regex.Pattern;;

public class RegexTests {
	private static String model;
	
	/**
	 * Try to match the sentence with the model
	 * @param The regular expression [String]
	 * @param The sentence to test [String]
	 * @return True if it match ; False if it doesn't match [Boolean]
	 */
	public static boolean executeMatching(String model, String sentence)
	{
		return Pattern.matches(model, sentence);
	}//end executeMatching
	
	/**
	 * Test if the sentence in parameters is matched by a number Regexp
	 * @param The sentence to test [String]
	 * @return True if it match ; False if it doesn't match [Boolean]
	 */
	public static boolean containNumber(String sentence)
	{
		model = ".*[0-9].*";
		//if the sentence contain numbers, it will return true, otherwise, it will return false
		return executeMatching(model, sentence);
	}//end containNumber
	
	/**
	 * Test if the sentence in parameters is matched by a word Regexp
	 * @param The sentence to test [String]
	 * @return True if it match ; False if it doesn't match [Boolean]
	 */
	public static boolean isOneWord(String sentence)
	{
		model = "^[a-zA-Z][a-zA-Z]*$";
		//if the sentence contain numbers or caracters which aren't letters, it will return true, otherwise, it will return false
		return executeMatching(model, sentence);
	}//end isOneWord
	
	/**
	 * Test if the sentence in parameters is matched by a date Regexp in XXXX-XX-XX form
	 * @param The sentence to test [String]
	 * @return True if it match ; False if it doesn't match [Boolean]
	 */
	public static boolean isOneDate(String sentence)
	{
		model = "^[0-9]{4}(-[0-9]{2}){2}$";
		//if the sentence form is XXXX-XX-XX with numbers instead of X, it will return true, otherwise, it will return false
		return executeMatching(model, sentence);
	}
}//end class
