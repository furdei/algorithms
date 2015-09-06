package interview.array;

import java.util.Stack;

/**
 * Given a string and a pattern, where:
 * '.' - matches any single character.
 * '*' - matches zero or more of the preceding element.
 * Find the first substring matching this pattern
 *
 * match("abcdef", "bc") => "bc"
 * match("abcdef", "bc*d") => "bcd"
 * match("abcdef", "be*d") => "bcd"
 * match("abcdef", "b.*e") => "bcde"
 */
public class PatternMatch3 {

    private static class ParseState {
        String str;
        String pattern;
        String foundBefore;
    }

    public String match(String srcStr, String srcPattern) {
        if (srcStr == null || srcPattern == null
                || srcStr.length() == 0 || srcPattern.length() == 0) {
            return null;
        }

        Stack<ParseState> stateStack = new Stack<>();

        ParseState parseState = new ParseState();
        parseState.str = srcStr;
        parseState.pattern = srcPattern;
        parseState.foundBefore = "";

        stateStack.push(parseState);

        while (stateStack.size() > 0) {
            parseState = stateStack.pop();
            String str = parseState.str;
            String pattern = parseState.pattern;
            String foundBefore = parseState.foundBefore;
            int strInd = 0;
            int patternInd = 0;

            if (str.length() > 1) {
                // in case we don't match the pattern against the current position in a string
                // push the next position in a source string and the whole pattern into a stack;
                parseState = new ParseState();
                parseState.str = str.substring(1);
                parseState.pattern = srcPattern;
                parseState.foundBefore = "";
                stateStack.push(parseState);
            }

            boolean matches = true;

            while (strInd < str.length() && patternInd < pattern.length() && matches) {
                if (patternInd < pattern.length() - 1 && pattern.charAt(patternInd + 1) == '*') {
                    // handle asterisk
                    char repeatChar = pattern.charAt(patternInd);

                    if (repeatChar == '*') {
                        throw new IllegalArgumentException("Asterisk not allowed one right after another");
                    }

                    // we will process an asterisk right here
                    // in case we fail, we will save alternative path to stack
                    // alternative path is the one which does not skip any characters
                    // in a string, but skips an asterisk in a pattern
                    patternInd += 2;

                    parseState = new ParseState();
                    parseState.str = str.substring(strInd);
                    parseState.pattern = pattern.substring(patternInd);
                    parseState.foundBefore = foundBefore.concat(str.substring(0, strInd));
                    stateStack.push(parseState);

                    while (strInd < str.length()
                            && (str.charAt(strInd) == repeatChar || repeatChar == '.')) {
                        strInd++;
                        parseState = new ParseState();
                        parseState.str = str.substring(strInd);
                        parseState.pattern = pattern.substring(patternInd);
                        parseState.foundBefore = foundBefore.concat(str.substring(0, strInd));
                        stateStack.push(parseState);
                    }
                } else {
                    char patChar = pattern.charAt(patternInd);

                    if (patChar == '.' || patChar == str.charAt(strInd)) {
                        strInd++;
                        patternInd++;
                    } else {
                        matches = false;
                    }
                }
            }

            if (matches && patternInd >= pattern.length()) {
                return foundBefore.concat(str.substring(0, strInd));
            }
        }

        return null;
    }

    public static void test(PatternMatch3 matcher, String str, String pattern) {
        String result;
        try {
            result = matcher.match(str, pattern);
        } catch (RuntimeException e) {
            result = e.getMessage();
            e.printStackTrace();
        }

        System.out.println(String.format("match(%s, %s) => %s", str, pattern,
                result));
    }

    public static void main(String[] args) {
        PatternMatch3 matcher = new PatternMatch3();
        test(matcher, "b", "bc");
        test(matcher, "bc", "bc");
        test(matcher, "abcdef", "bc");
        test(matcher, "abccccdef", "bc*d");
        test(matcher, "abcdef", "be*d");
        test(matcher, "abcdefghik", "b.*e.*i");
        test(matcher, "abcdeeeefghik", "b.*e.*i");
        test(matcher, "abcdfghik", "b.*e.*i");
        test(matcher, "abcdef", "b.*e");
        test(matcher, "abcdef", ".*");
        test(matcher, "abcdef", "*");
        test(matcher, "abcdef", "**");
    }

}
