package interview.recursion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Input string - Morse code without pauses.
 * Find all the possible cases that can be encoded in the input string
 */
public class MorseCode {

    private static final Map<String, Character> ALPHABET;
    private static final Map<Character, String> REVERSE;
    private static final int MAX_LENGTH;
    static {
        ALPHABET = new HashMap<String, Character>();
        ALPHABET.put("*-", 'A');
        ALPHABET.put("-***", 'B');
        ALPHABET.put("-*-*", 'C');
        ALPHABET.put("-**", 'D');
        ALPHABET.put("*", 'E');
        ALPHABET.put("**-*", 'F');
        ALPHABET.put("--*", 'G');
        ALPHABET.put("****", 'H');
        ALPHABET.put("**", 'I');
        ALPHABET.put("*---", 'J');
        ALPHABET.put("-*-", 'K');
        ALPHABET.put("*-**", 'L');
        ALPHABET.put("--", 'M');
        ALPHABET.put("-*", 'N');
        ALPHABET.put("---", 'O');
        ALPHABET.put("*--*", 'P');
        ALPHABET.put("--*-", 'Q');
        ALPHABET.put("*-*", 'R');
        ALPHABET.put("***", 'S');
        ALPHABET.put("-", 'T');
        ALPHABET.put("**-", 'U');
        ALPHABET.put("***-", 'V');
        ALPHABET.put("*--", 'W');
        ALPHABET.put("-**-", 'X');
        ALPHABET.put("-*--", 'Y');
        ALPHABET.put("--**", 'Z');

        REVERSE = new HashMap<Character, String>();
        int maxLength = 0;

        for (Map.Entry<String, Character> entry : ALPHABET.entrySet()) {
            REVERSE.put(entry.getValue(), entry.getKey());

            if (entry.getKey().length() > maxLength) {
                maxLength = entry.getKey().length();
            }
        }

        MAX_LENGTH = maxLength;

        if (ALPHABET.size() != 26) {
            throw new IllegalStateException("ALPHABET size is" + ALPHABET.size());
        }

        if (REVERSE.size() != 26) {
            throw new IllegalStateException("REVERSE size is" + REVERSE.size());
        }
    }

    public String encode(String unencrypted) {
        if (unencrypted == null || unencrypted.length() == 0) {
            return unencrypted;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < unencrypted.length(); i++) {
            sb.append(REVERSE.get(unencrypted.charAt(i)));
        }

        return sb.toString();
    }

    public List<String> decode(String encrypted) {
        if (encrypted == null || encrypted.length() == 0) {
            return null;
        }

        List<String> result = new LinkedList<String>();

        for (int i = 1; i <= MAX_LENGTH && i <= encrypted.length(); i++) {
            Character character = ALPHABET.get(encrypted.substring(0, i));
            if (character != null) {
                List<String> recursive = decode(encrypted.substring(i));

                if (recursive == null) {
                    result.add(character.toString());
                } else {
                    for (String suffix : recursive) {
                        result.add(character + suffix);
                    }
                }
            }
        }

        return result;
    }

    public void test(String source) {
        String morse = encode(source);
        List<String> decoded = decode(morse);
        System.out.println("MorseCode.test " + source + " => " + morse);
        for (String s : decoded) {
            System.out.println("MorseCode.test " + morse + " => " + s);
        }
        if (decoded.contains(source)) {
            System.out.println("MorseCode.test '" + source + "' FOUND");
        } else {
            System.out.println("MorseCode.test '" + source + "' NOT FOUND");
        }
    }

    public static void main(String[] args) {
        MorseCode mc = new MorseCode();
        mc.test("HI");
        mc.test("HELLO");
        mc.test("OMG");
    }

}
