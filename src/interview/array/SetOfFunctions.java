package interview.array;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 Suppose we have array of N numbers. We will define N functions on this array. Each function will return the sum of
 all numbers in the array from Li to Ri ( Li is left index, Ri is right index). Now we have 2 types of queries:

 Type1: 1 x y Change the xth element of the array to y
 Type2: 2 l r Return the sum of all functions from m to n.

 Input type:
 First Line is the size of the array i.e. N

 Next Line contains N space separated numbers Ai denoting the array

 Next N line follows denoting Li and Ri for each functions.

 Next Line contains an integer Q , number of queries to follow.

 Next Q line follows , each line containing a query of Type 1 or Type 2

 Here is an example:
 Input:
 5
 1 2 3 4 5
 1 2
 3 4
 1 4
 1 5
 3 5
 5
 1 1 5
 2 2 4
 2 1 3
 1 4 5
 2 1 5

 Output:
 40
 28
 63

 Explanation:
 Function 1 is sum of values from index 1 to index 2 = 1+2=3
 So , F1=3
 Similarly, F2=3+4=7
 F3=1+2+3+4=10
 F4=15
 F5=12

 Now when I query 1 1 5
 means it is type 1 query, so we replace value at index 1 by 5.
 So our new array is,
 5 2 3 4 5
 and
 F1=7
 F2=7(unchanged)
 F3=14
 F4=19
 F5=12(unchanged)

 Then next query is 2 2 4
 means give sum of all functions from index 2 to 4.
 So, ans= 7+14+19 =40 (output 1)

 Similarly are other 2 outputs.
 Index are 1 based in example.
 */
public class SetOfFunctions {

    private enum State {
        ERROR, READ_COUNT, READ_ARRAY, READ_FUNCTIONS, READ_QUERY_COUNT, READ_QUERY, DONE
    }

    private static class Function {
        int min;
        int max;
        int sum;
    }

    private static class Query {
        int type;
        int a;
        int b;
    }

    private int n;
    private int functionsRead;
    private int[] array;
    private Function[] functions;
    private Set<Function>[] functionsByIndex;
    private int q;
    private int queriesRead;

    public void process(InputStream inputStream, OutputStream outputStream) {
        if (inputStream == null) {
            return;
        }

        State state = State.READ_COUNT;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            PrintStream printStream = new PrintStream(outputStream);
            String line;
            while ((line = br.readLine()) != null && state != State.ERROR && state != State.DONE) {
                state = processLine(state, line, printStream);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private State processLine(State state, String line, PrintStream ps) throws IOException {
        switch (state) {
            case READ_COUNT:
                n = Integer.valueOf(line);
                array = new int[n];
                functions = new Function[n];
                functionsByIndex = (Set<Function>[]) new Set<?>[n];
                return State.READ_ARRAY;

            case READ_ARRAY:
                String[] strArray = line.split(" ");
                if (strArray.length != n) {
                    return State.ERROR;
                }
                for (int i = 0; i < n; i++) {
                    array[i] = Integer.valueOf(strArray[i]);
                }
                return State.READ_FUNCTIONS;

            case READ_FUNCTIONS:
                String[] funcArray = line.split(" ");
                Function function = new Function();
                function.min = Integer.valueOf(funcArray[0]) - 1;
                function.max = Integer.valueOf(funcArray[1]) - 1;
                function.sum = computeSum(function.min, function.max);
                functions[functionsRead++] = function;
                indexNewFunction(function);
                if (functionsRead == n) {
                    return State.READ_QUERY_COUNT;
                }
                break;

            case READ_QUERY_COUNT:
                q = Integer.valueOf(line);
                return State.READ_QUERY;

            case READ_QUERY:
                String[] queryArray = line.split(" ");
                Query query = new Query();
                query.type = Integer.valueOf(queryArray[0]);
                query.a = Integer.valueOf(queryArray[1]);
                query.b = Integer.valueOf(queryArray[2]);
                processQuery(query, ps);
                if (++queriesRead == q) {
                    return State.DONE;
                }
                break;
        }

        return state;
    }

    private void processQuery(Query query,PrintStream ps) throws IOException {
        switch (query.type) {
            case 1:
                replaceInt(query.a - 1, query.b);
                break;

            case 2:
                printSum(query.a - 1, query.b - 1, ps);
                break;
        }
    }

    private void replaceInt(int index, int value) {
        int oldValue = array[index];
        array[index] = value;
        Set<Function> funcs = functionsByIndex[index];

        if (funcs != null) {
            for (Function f : funcs) {
                f.sum = f.sum - oldValue + value;
            }
        }
    }

    private void printSum(int a, int b, PrintStream ps) throws IOException {
        int sum = 0;
        for (int i = a; i <= b; i++) {
            sum += functions[i].sum;
        }
        ps.println(sum);
    }

    private int computeSum(int min, int max) {
        int sum = array[min];
        for (int i = min + 1; i <= max; i++) {
            sum += array[i];
        }
        return sum;
    }

    private void indexNewFunction(Function function) {
        for (int i = function.min; i <= function.max; i++) {
            Set<Function> set = functionsByIndex[i];
            if (set == null) {
                set = new HashSet<>();
                functionsByIndex[i] = set;
            }
            set.add(function);
        }
    }

    public static void main(String[] args) {
        String input = "5\n1 2 3 4 5\n1 2\n3 4\n1 4\n1 5\n3 5\n5\n1 1 5\n2 2 4\n2 1 3\n1 4 5\n2 1 5";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        OutputStream out = System.out;
        SetOfFunctions s = new SetOfFunctions();
        s.process(stream, out);
        System.out.println("Done");
    }

}
