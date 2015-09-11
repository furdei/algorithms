package interview.array;

/**
 * Implement method oneEditApart that return boolean: true, if
 * using one operations (insert or remove or replace) we can modify
 * one string to get another. False otherwise.
 * Signature: boolean oneEditApart(String s1, String s2)
 * Allowing operations insert remove replace
 *
 * Example:
 * oea("cat", "cut") => true // replace "u" -> "a"
 * oea("cat", "cuts") => false // no operations
 * oea("ca", "ca") => false // no operations
 * oea("cats", "cat") => true // remove "s"
 * oea("cat", "at") => true // insert "c"
 * oea("cat", "cbat") => true // remove "b"
 */
public class OneEditApart {

    public boolean oea(String s1, String s2) {
        // start with special cases
        if (s1 == null || s2 == null) {
            return false;
        }

        if (Math.abs(s1.length() - s2.length()) > 1) {
            return false;
        }

        if (s1.length() == 0 || s2.length() == 0) {
            return true;
        }

        // a common case: both strings have non-zero length, equal or differ by 1
        int difCount = 0;
        int i1 = 0;
        int i2 = 0;

        while (i1 < s1.length() && i2 < s2.length()) {
            if (s1.charAt(i1) == s2.charAt(i2)) {
                i1++;
                i2++;
            } else {
                difCount++;
                if (difCount > 1) {
                    return false;
                }

                // try to skip one character in s1 or one character in s2
                // or one in both and check if it is possible to continue
                if (safeEqual(s1, s2, i1 + 1, i2)) {
                    i1 += 2;
                    i2++;
                } else {
                    if (safeEqual(s1, s2, i1, i2 + 1)) {
                        i1++;
                        i2 += 2;
                    } else {
                        if (safeEqual(s1, s2, i1 + 1, i2 + 1)) {
                            i1 += 2;
                            i2 += 2;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return (difCount == 1 && i1 == s1.length() && i2 == s2.length())
                || (difCount == 0 && s1.length() != s2.length());
    }

    private boolean safeEqual(String s1, String s2, int i1, int i2) {
        return i1 < s1.length() && i2 < s2.length() && s1.charAt(i1) == s2.charAt(i2);
    }

    public static void main(String[] args) {
        OneEditApart oneEditApart = new OneEditApart();
        System.out.println("oea(\"cat\", \"cut\") => " + oneEditApart.oea("cat", "cut"));
        System.out.println("oea(\"cat\", \"cuts\") => " + oneEditApart.oea("cat", "cuts"));
        System.out.println("oea(\"ca\", \"ca\") => " + oneEditApart.oea("ca", "ca"));
        System.out.println("oea(\"cats\", \"cat\") => " + oneEditApart.oea("cats", "cat"));
        System.out.println("oea(\"cat\", \"at\") => " + oneEditApart.oea("cat", "at"));
        System.out.println("oea(\"cat\", \"cbat\") => " + oneEditApart.oea("cat", "cbat"));
        System.out.println("oea(\"ccat\", \"cact\") => " + oneEditApart.oea("ccat", "cact"));
        System.out.println("oea(\"cat\", \"ca\") => " + oneEditApart.oea("cat", "ca"));
        System.out.println("oea(\"caat\", \"cat\") => " + oneEditApart.oea("cat", "ca"));
        System.out.println("oea(\"cat\", \"cat\") => " + oneEditApart.oea("cat", "cat"));
        System.out.println("oea(\"ab\", \"ba\") => " + oneEditApart.oea("ab", "ba"));
    }

}
