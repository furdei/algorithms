package interview.hashmap;

import java.util.*;

/**
 * Given a set of contacts with their names, phones and emails, find all the duplicates
 * and mark them the same. Print something like: [[0, 2, 4], [1]]
 */
public class FindDuplicates {

    public Set<Set<Integer>> findDuplicates(String[][] contacts) {
        if (contacts == null) {
            return null;
        }

        Map<String, Set<Integer>> searchMap = new HashMap<>();

        for (int i = 0; i < contacts.length; i++) {
            String[] contact = contacts[i];

            Set<Integer> newContactGroup = new HashSet<>();
            newContactGroup.add(i);

            for (int j = 1; j < contact.length; j++) {
                Set<Integer> existing = searchMap.get(contact[j]);
                if (existing != null) {
                    newContactGroup.addAll(existing);
                }
            }

            for (Integer member : newContactGroup) {
                for (int j = 1; j < contacts[member].length; j++) {
                    searchMap.put(contacts[member][j], newContactGroup);
                }
            }
        }

        Set<Set<Integer>> result = new HashSet<>();
        result.addAll(searchMap.values());

        return result;
    }

    public static void main(String[] args) {
        String[][] contacts = new String[][] {
                { "John", "+123", "john@mail.ru" },
                { "Sam", "+234", "sam@gmail.com" },
                { "John45", "+345", "j@gmail.com" },
                { "mj", "john@mail.ru", "j@gmail.com"}
        };

        FindDuplicates findDuplicates = new FindDuplicates();
        System.out.println("FindDuplicates.main " + findDuplicates.findDuplicates(contacts));
    }
}
