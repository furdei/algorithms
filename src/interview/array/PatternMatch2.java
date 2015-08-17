package interview.array;

/**
 * Match string against pattern. Pattern can contain *
 * * means zero or more previous characters
 * Pattern can not start with *
 *
 * isMatch(“aa”,”a”) → false
 * isMatch(“aa”,”aa”) → true
 * isMatch(“aaa”,”aa”) → false
 * isMatch(“aa”, “a*”) → true
 * isMatch(“aa”, “*”) → true
 * isMatch(“ab”, “*”) → true
 * isMatch(“b*a”, “a”) → true
 * isMatch(“a*a”, “a”) → true
 * isMatch(“aab”, “c*a*b”) → true
 */
public class PatternMatch2 {

    public boolean match(String s, String p) {
        if (s == null || p == null || s.length() == 0 || p.length() == 0) {
            return false;
        }

        if (p.length() == 1 && p.charAt(0) == '*') {
            return true;
        }

        int is = s.length() - 1;
        int ip = p.length() - 1;

        while (is >= 0 && ip >= 0) {
            if (p.charAt(ip) == '*') {
                if (ip == 0) {
                    throw new IllegalStateException("Asterisk in the first position of pattern");
                }

                // if we've met *, previous character can occur 0 or more times.
                // but if pattern contains several equal characters before *, we need to make sure that
                // the character is met at least required times
                // aa* means that a should be at least once in the string
                int reqCount = 0;
                ip--;

                while (ip >= 1 && p.charAt(ip - 1) == p.charAt(ip)) {
                    ip--;
                    reqCount++;
                }

                // now let's count required characters in s
                while (is >= 0 && s.charAt(is) == p.charAt(ip)) {
                    is--;
                    reqCount--;
                }

                if (reqCount > 0) {
                    return false;
                }

                ip--;
            } else {
                if (p.charAt(ip) != s.charAt(is)) {
                    return false;
                }

                ip--;
                is--;
            }
        }

        if (ip > 0) {
            // unprocessed pattern should have a form of a*b*c*..z*, e.g. all characters shouldn't be required.
            if ((ip & 1) == 1) {
                while (ip > 0) {
                    if (p.charAt(ip) != '*') {
                        return false;
                    }
                    ip -= 2;
                }
            }
        }

        return ip < 0 && is < 0;
    }

    public void test(String s, String p) {
        System.out.println("PatternMatch2.main match(S=" + s + ", P=" + p + ")= "
                + match(s, p));
    }

    public static void main(String[] args) {
        PatternMatch2 pm = new PatternMatch2();
        pm.test("aa", "a");
        pm.test("aa", "aa");
        pm.test("aaa", "aa");
        pm.test("aa", "a*");
        pm.test("aa", "*");
        pm.test("ab", "*");
        pm.test("a", "b*a");
        pm.test("a", "a*a");
        pm.test("aa", "aa*a");
        pm.test("aaa", "aaa*a");
        pm.test("baaa", "b*aaa*a");
        pm.test("aab", "c*a*b");
    }

}
