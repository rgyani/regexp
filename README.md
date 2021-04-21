# regexp

Implement regular expression matching with the following special characters: . 
1. \. (period) which matches **any single character** 
2. \* (asterisk) which matches **zero or more** of the preceding element 

For example: 

| Regex | Input | Result |
|-------|-------|--------|
|a.b| acb, aab, axb| TRUE|
|a.b| ab, accb, cb| FALSE|
|a*b| b, ab, aab, aaab| TRUE|
|a*b| a, acb| FALSE|
|a*b.*c| bc, bxc, abc, abxc, abxyc| TRUE|
|a*b.*c| ac, ab| FALSE|


### Let's solve this using dynamic programming approach

We see that the above problem exhibits both the propertie
1. Optimal Substructure and 
2. Overlapping Subproblems, so lets solve it using dynamic programming efficiently,

Consider this regex __a\*b.\*c__ and the input string __aaabxc__

Lets build our 2D table like so

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   | T     |         |         |         |         |        |         |
| a (i=1) |       |         |         |         |         |        |         |
| a (i=2) |       |         |         |         |         |        |         |
| a (i=3) |       |         |         |         |         |        |         |
| b (i=4) |       |         |         |         |         |        |         |
| x (i=5) |       |         |         |         |         |        |         |
| c (i=6) |       |         |         |         |         |        |         |

for i = 0, we consider for an empty string, the values for regex upto j, those will be

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   | T     |    F    |     T   |  F      |  F      |     F  |  F      |
| a (i=1) |       |         |         |         |         |        |         |
| a (i=2) |       |         |         |         |         |        |         |
| a (i=3) |       |         |         |         |         |        |         |
| b (i=4) |       |         |         |         |         |        |         |
| x (i=5) |       |         |         |         |         |        |         |
| c (i=6) |       |         |         |         |         |        |         |

**Notice the value of M[0, 2] = T, I will explain this a little down the line**

similarly, for j=0. we consider an empty regex string, and the values for string upto i

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |  F      |  F      |     F  |  F      |
| a (i=1) |   F   |         |         |         |         |        |         |
| a (i=2) |   F   |         |         |         |         |        |         |
| a (i=3) |   F   |         |         |         |         |        |         |
| b (i=4) |   F   |         |         |         |         |        |         |
| x (i=5) |   F   |         |         |         |         |        |         |
| c (i=6) |   F   |         |         |         |         |        |         |

Now lets consider, for i = 1, the values of j
1. M[1, 1], here str[i] == pattern[j], and if we consider the input without these characters, ie M[1-1, 1-1] = T, so M[1, 1] = M[0, 0] = T
2. M[1, 2], here pattern[j] = *, which means we can have 0 or more occurrences of previous char in pattern ie. pattern[j-1]
   ```
    If we have zero occurences, we have to ignore the previous pattern character, hence we take  
   M[i][j-2]
   
   Now consider the previous character in pattern, which could either be a character or a .
    1. if it's a char, we must compare str[i] and pattern[j-1]
    2. if it's a ., it will always match  
   so we have the condition (pattern[j-1] == '.' ? M[i - 1, j] : (str[i] == pattern[j-1]))
   
   Hence the condition becomes
   
   M[i][j-2] | (pattern[j-1] == '.' ? M[i - 1, j] : (str[i] == pattern[j-1]))
   ```
   For our case this evaluates to
M[1,2] = M[1,0] | ('a' == 'a') = F | T = T
   
Now we can co-relate the above for M[0, 2] where we got the value as T

3. M[1, 3], here str[i] != pattern[j] and pattern[j] is not a special character, so we have M[1, 3] = F
4. M[1, 4], again str[i] != pattern[j] but pattern[j] = ., so we consider it to be a match like condition 1, and hence, M[j, j] = M[i-1, j-1]  = F
5. M[1, 5], here pattern[j] = *, so we have M[1, 5] = M[1, 3] | M[0, 5] = F | F = F
6. M[1, 6] = F

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |  F      |  F      |     F  |    F    |
| a (i=1) |   F   |    T    |     T   |  F      |  F      |     F  |    F    |
| a (i=2) |   F   |         |         |         |         |        |         |
| a (i=3) |   F   |         |         |         |         |        |         |
| b (i=4) |   F   |         |         |         |         |        |         |
| x (i=5) |   F   |         |         |         |         |        |         |
| c (i=6) |   F   |         |         |         |         |        |         |

for i = 2, we will have
1. M[2, 1], here str[i] == pattern[j], and if we consider the input without these characters, ie M[2-1, 1-1] = F, so M[2, 1] = M[1, 0] = F
2. M[2, 2], here pattern[j] = *, and we have M[2, 0] | ('a' == 'a') = F | T = T
3. M[2, 3], here str[i] != pattern[j] and pattern[j] is not a special character, so we have M[2, 3] = F
4. M[2, 4], again str[i] != pattern[j] but pattern[j] = ., so we have M[i, j] = M[i-1, j-1] = F
5. M[2, 5], here pattern[j] = *, so we have M[2, 5] = M[2, 3] | (M[1,5]) = F | F = F
6. M[2, 6] = F

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=1) |   F   |    T    |     T   |   F     |    F    |    F   |    F    |
| a (i=2) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=3) |   F   |         |         |         |         |        |         |
| b (i=4) |   F   |         |         |         |         |        |         |
| x (i=5) |   F   |         |         |         |         |        |         |
| c (i=6) |   F   |         |         |         |         |        |         |

Similarly for i = 3, we will have
2. M[3, 1], here str[i] == pattern[j], but ie M[2-1, 1-1] = F
1. M[3, 2], here pattern[j] = *, and we have M[3, 0] | ('a' == 'a') = F | T = T

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=1) |   F   |    T    |     T   |   F     |    F    |    F   |    F    |
| a (i=2) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=3) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| b (i=4) |   F   |         |         |         |         |        |         |
| x (i=5) |   F   |         |         |         |         |        |         |
| c (i=6) |   F   |         |         |         |         |        |         |

However, for i = 4, 
1. at j=2, we have M[4,0] | ('b' == 'a') = F | F F 
1. at j=3, we have str[j] == pattern[j] and M[i-1,j-1] = T, so M[4,3] = T
2. at j=4, we have pattern[j] = ., so M[4,4] = M[3,3] = F
3. at j=5, we have pattern[j] = *, so M[4,5] = M[4, 3] | M[3,5]= T | F = T 


|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=1) |   F   |    T    |     T   |   F     |    F    |    F   |    F    |
| a (i=2) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=3) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| b (i=4) |   F   |    F    |     F   |   T     |    F    |    T   |    F    |
| x (i=5) |   F   |         |         |         |         |        |         |
| c (i=6) |   F   |         |         |         |         |        |         |

Similarly, for i = 5,
1. at j=2, we have pattern[j] = *, so we have M[5, 0] | ('x' == 'a') = F | F = F
2. at j=4, we have pattern[j] = ., so we have M[5, 4] = M[4, 3] = T
3. at j=5, we have pattern[j] = *, so we have M[5, 5] = M[5, 3] | M[4, 5] = F | T = T


|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=1) |   F   |    T    |     T   |   F     |    F    |    F   |    F    |
| a (i=2) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=3) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| b (i=4) |   F   |    F    |     F   |   T     |    F    |    T   |    F    |
| x (i=5) |   F   |    F    |     F   |   F     |    T    |    T   |    F    |
| c (i=6) |   F   |         |         |         |         |        |         |

Finally, for i=6, we get
1. at j=5, we have pattern[j] = *, so we have M[6, 5] = M[6, 3] | ('c' == '.') = F | F = F
2. And most importantly, at j=6, we have str[i] = pattern[j], so we have M[6, 6] = M[5, 5] = T

|         | (j=0) | a (j=1) | * (j=2) | b (j=3) | . (j=4) | * (j=5)| c (j=6) |
|---------|-------|---------|---------|---------|---------|--------|---------|
| (i=0)   |   T   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=1) |   F   |    T    |     T   |   F     |    F    |    F   |    F    |
| a (i=2) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| a (i=3) |   F   |    F    |     T   |   F     |    F    |    F   |    F    |
| b (i=4) |   F   |    F    |     F   |   T     |    F    |    T   |    F    |
| x (i=5) |   F   |    F    |     F   |   F     |    T    |    T   |    F    |
| c (i=6) |   F   |    F    |     F   |   F     |    F    |    F   |    T    |

So our logic simply becomes

```
      for (int i = 1; i <= str.length; i++) 
      {
            for (int j = 1; j <= pattern.length; j++) 
            {
                if (str[i - 1] == pattern[j - 1])
                    M[i][j] = M[i - 1][j - 1];
      
                if (pattern[j - 1] == '*')
                    M[i][j] = M[i][j - 2] | (pattern[j - 2] == '.' ? M[i - 1][j] : (str[i - 1] == pattern[j - 2]));
      
                if (pattern[j - 1] == '.')
                    M[i][j] = M[i - 1][j - 1];
            }
        }
```


### Complexity Analysis
As can be seen above, the Time complexity for this algorithm is O(m*n), 
and the Space complexity is also O(m*n)
