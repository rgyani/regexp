
class Solution {

    private static boolean evaluate(String regex, String input) {
        char[] pattern = regex.toCharArray();
        char[] str = input.toCharArray();

        boolean[][] M = new boolean[str.length + 1][pattern.length + 1];

        M[0][0] = true;
        for (int i = 1; i <= str.length; i++)
            M[i][0] = false;
        for (int j = 1; j <= pattern.length; j++)
            M[0][j] = (pattern[j - 1] == '*' && M[0][j - 2]);


        for (int i = 1; i <= str.length; i++) {
            for (int j = 1; j <= pattern.length; j++) {
                if (str[i - 1] == pattern[j - 1])
                    M[i][j] = M[i - 1][j - 1];
                if (pattern[j - 1] == '*')
                    M[i][j] = M[i][j - 2] | (pattern[j - 2] == '.' ? M[i - 1][j] : (str[i - 1] == pattern[j - 2]));
                if (pattern[j - 1] == '.')
                    M[i][j] = M[i - 1][j - 1];
            }
        }

        System.out.println(regex + (M[str.length][pattern.length] ? " matches " : " doesnot match ") + input);
        return M[str.length][pattern.length];
    }


    public static void main(String[] args) {
        evaluate("a.b", "acb");
        evaluate("a.b", "aab");
        evaluate("a.b", "axb");
        evaluate("a.b", "ab");
        evaluate("a.b", "accb");
        evaluate("a.b", "cb");


        evaluate("a*b", "b");
        evaluate("a*b", "ab");
        evaluate("a*b", "aab");
        evaluate("a*b", "aaab");
        evaluate("a*b", "a");
        evaluate("a*b", "acb");

        evaluate("a*b.*c", "bc");
        evaluate("a*b.*c", "bxc");
        evaluate("a*b.*c", "abc");
        evaluate("a*b.*c", "abxc");
        evaluate("a*b.*c", "abxyc");
        evaluate("a*b.*c", "ac");
        evaluate("a*b.*c", "ab");

    }

}