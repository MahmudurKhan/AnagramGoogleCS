/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;

    private Random random = new Random();

    ArrayList<String> wordList = new ArrayList<String>();
    HashMap<String, ArrayList<String>> lettersToWords = new HashMap<>();
    HashSet<String> wordSet = new HashSet<>();
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        ArrayList<String> wordMapList;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);

            if (sizeToWords.containsKey(word.length())) {
                wordMapList = sizeToWords.get(word.length());
                wordMapList.add(word);
                sizeToWords.put(word.length(), wordMapList);
            } else {
                ArrayList<String> newWordList = new ArrayList<>();
                newWordList.add(word);
                sizeToWords.put(word.length(), newWordList);
            }


            ArrayList<String> sortedList = new ArrayList<>();
            String sortedWord = sortLetters(word);


            if ( !(lettersToWords.containsKey(sortedWord)) ) {
                sortedList.add(word);
                lettersToWords.put(sortedWord, sortedList);
            } else {
                sortedList = lettersToWords.get(sortedWord);
                sortedList.add(word);
                lettersToWords.put(sortedWord, sortedList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word) && !(base.contains(word))) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        /*for (int i=0;i<wordList.size();i++)
        {
            String temp = wordList.get(i);
            String tempNew= sortLetters(temp);
            String newTarget = sortLetters(targetWord);
            if (tempNew.equalsIgnoreCase(newTarget))
            {
                result.add(temp);
                Log.i("my",temp);
            }
        }*/

        return result;
    }



    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> tempList;
        ArrayList<String> resultList = new ArrayList<>();

        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String anagram = word + alphabet;
            String sortedAnagram = sortLetters(anagram);

            if (lettersToWords.containsKey(sortedAnagram)) {
                tempList = lettersToWords.get(sortedAnagram);

                for (int i = 0; i < tempList.size(); i++) {
                    if ( !(tempList.get(i).contains(word)) ) {
                        resultList.add(tempList.get(i));
                        Log.i("mystr",tempList.get(i));
                    }
                }
            }
        }

        return resultList;

    }

    public String pickGoodStarterWord() {

        int randomNumber;
        String starterWord;

        do {
            randomNumber = random.nextInt(sizeToWords.get(wordLength).size());
            starterWord = sizeToWords.get(wordLength).get(randomNumber);
        } while (getAnagramsWithOneMoreLetter(starterWord).size() < MIN_NUM_ANAGRAMS);

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }
        return starterWord;
    }

    public String sortLetters(String word){
        char [] chars = word.toCharArray();
        Arrays.sort(chars);
        String sortedWord = new String (chars);
        return sortedWord;
    }
}
