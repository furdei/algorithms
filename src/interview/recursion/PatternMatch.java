package interview.recursion;

/**
 * Match string S against pattern P.
 * Pattern can contain ? and *
 * ? means any character
 * * means zero or more any characters
 *
 *    0 1 2 3 4 5 6 7 8 9 10
 * s: h e l l o   w o r l d
 * p: h ? l * w o r l d
 *    true
 *
 *    0 1 2 3 4 5 6 7 8 9 1011
 * s: a b c d e f d e f g h i
 * p: a b c * d e f * g h i
 *    true
 *
 */
public class PatternMatch {

    public boolean match(String s, String p) {
        if (s == null || p == null || s.length() == 0 || p.length() == 0) {
            return false;
        }

        int is = 0;
        int ip = 0;

        while (is < s.length() && ip < p.length()) {
            switch (p.charAt(ip)) {
                case '?':
                    is++;
                    ip++;
                    break;

                case '*':
                    if (ip == p.length() - 1) {
                        return true;
                    }

                    if (match(s.substring(is), p.substring(ip + 1))) {
                        return true;
                    }
                    return match(s.substring(is + 1), p.substring(ip));

                default:
                    if (p.charAt(ip) == s.charAt(is)) {
                        is++;
                        ip++;
                    } else {
                        return false;
                    }
            }
        }

        return is == s.length() && ip == p.length();
    }

    public void test(String s, String p) {
        System.out.println("PatternMatch.test match(S=" + s + ", P=" + p + ")= "
                + match(s, p));
    }

    public static void main(String[] args) {
        PatternMatch pm = new PatternMatch();
        pm.test("hello world", "h?l*world");
        pm.test("hello, world", "h?l*world");
        pm.test("hello, world!", "h?l*world");
        pm.test("hello world", "h?l*worl");
        pm.test("hello world", "h?l*");
        pm.test("hello world", "h?l**********ld");
        pm.test("hello world", "h?l**********");
        pm.test("hello wor", "h?l*world");
        pm.test("abcdefdefghi", "abc*def*ghi");
        pm.test("abcdefghidefghi", "abc*def*ghi");
        pm.test("hello world", "*lo*orld");
    }

}
