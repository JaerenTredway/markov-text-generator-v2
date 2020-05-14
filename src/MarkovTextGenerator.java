import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Jaeren William Tredway
 * Instructions:
 * 1. Files required in src:
 *      MarkovTextGenerator.java
 *      NGram.java
 *      TransitionRule.java
 *      any .txt file (or directory of text files)
 * 2. To compile: javac MarkovTextGenerator.java (from inside src)
 * 3. To run: java MarkovTextGenerator 2 50 text-files/poem.txt
 *      the command line args are:
 *      a. the MRTG order (int)
 *      b. the number of words of gibberish to generate (int)
 *      c. the location of the text file (String)
 ***************************************************************************
 * Definitions:
 * 1. NGram: an object that is a "wrapper" for a short ArrayList of words. Each
 *      NGram is a list of "n" consecutive words taken from the text.
 * 2. "n": the order of the MRTG, which defines how many words are in each
 *      NGram.
 * 3. TransitionRule: an object that is a "wrapper" for an ArrayList,this time
 *      a list of any NGrams that are found to follow another NGram in the text.
 * 4. map: a HashMap object that uses each NGram as keys, and for values uses
 *      TransitionRules that list all NGrams that are found to follow that
 *      key somewhere in the text.
 ***************************************************************************
 * This class will do the following:
 * 1. It will gather three command line arguments:
 *      int n: the order of the MRTG (usually 2 or 3)
 *      int gibberishLength: the number of words of nonsense to make
 *      String uri: the file location of the input text (.txt file). Store
 *      your text files or directory at same level as src (siblings)
 * 2. It will read the .txt file and store the data as individual words in an
 *      ArrayList called "words".
 * 3. It will then use makeNGrams() to take that list of individual words and
 *      make NGram objects that are each "n" words long from it. All the NGrams
 *      are stored in a list called "nGramsList".
 * 4. Then it makes a hashmap called "map" that has these keys/values:
 *      keys:       each NGram becomes a key.
 *      values:     each value is a list of all the NGrams that are found to
 *                  follow that key anywhere in the text, including duplicates.
 *                  These lists are called TransitionRules.
 * 5. It then builds an output String that is made of NGrams. The
 *      probablility of an NGram being chosen from the TransistionRule
 *      corresponds to the frequency of that NGram appearing in the text,
 *      because the TransitionRules keep duplicate NGrams that are
 *      collected every time that NGram is found in the text after the key
 *      NGram.
 * 6. And lastly it displays the output of gibberish.
 */
public class MarkovTextGenerator {

    //***************** CLASS VARIABLES SECTION: ***************************
        // (use static keyword so no instance is required)
    //the URI or file location for the .txt file:
    private static String uri ="";
    //the list of individual words extracted from the text:
    private static ArrayList<String> words = new ArrayList<>();
    //these will be gathered from the command line args at runtime:
    private static int n = 0;
    private static int gibberishLength = 0;
    //the map that will store the NGram objects as keys and TransitionRules
    //(each a list of NGram objects) as values:
    private static HashMap<NGram, TransitionRule> map = new HashMap<>();
    //the empty list to store NGram objects:
    private static ArrayList<NGram> nGramsList = new ArrayList<>();


    // (no constructor)


    //********************** CLASS METHODS SECTION: *************************
    //READ TEXT FILE:
    //this method returns an ArrayList with each word extracted from the .txt
    //file passed into it:
    private static ArrayList<String> readTextFile (String uri) throws Exception {
        //pick up and print out the file name (that was input from the command
        //line):
        //create 3 objects: a File, a FileReader, and a BufferedReader:
        //first declare them outside of try block:
        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;
        //then assign values inside try/catch block:
        try {
            file = new File(uri);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception ex1) {
            ex1.printStackTrace();
            throw new Exception("Unable to open file.");
        }
        //TEST: display the file you are extracting data from:
        //System.out.println("Text extracted from " + filename + ":");

        //set up storage variables for line, lines, and words:
        //int count = 0;
        String line;
        ArrayList<String> lines = new ArrayList<>();
        //**declared above as a Class variable:
        //ArrayList<String> words = new ArrayList<>();

        //set 'line' equal to the next line of text and report:
        try {
            while ( (line = bufferedReader.readLine()) != null) {
                //TEST:
                // the line of text you are currently reading:
                //System.out.println(count + ": " + line);
                //count++;
                lines.add(line);
                for (String word : line.split("[ ]")) {
                    //regex notes for future reference:
                    // * operator: match zero or more of preceding regex
                    // + operator: match 1 or more of preceding regex
                    // \ operator: escape character for next char
                    if (word.length() > 0) {
                        words.add(word);// may add toLowerCase()
                    }
                }
            }
        } catch (IOException ex2) {
            System.out.println("One of the readLines has failed.");
            ex2.printStackTrace();
        }
        //TESTS:
        //report number of lines and number of words harvested from .txt file:
        //System.out.println();
        //System.out.println("line count: " + lines.size());
        //System.out.println("word count: " + words.size());
        //print 5 of the individual words to test:
        //for (int i = 0; i < 5; i++) {
            //System.out.println("word " + i + ": " + words.get(i));
        //}
        return words;
    }//END readTextFile()..........................................


    //MAKE NGRAM OBJECTS AND BUILD A LIST OF THEM:
    //this method creates an ArrayList of NGrams. Each NGram is itself a list
    //with "n" words in it:
    private static ArrayList<NGram> makeNGrams (int n,
                                                ArrayList<String> words) {
        //create individual NGram objects to feed into the list:
        //FIXME: avoid getting too close to the end of the "words" list or
        // you won't have any NGrams linked to the last "n" keys:
        for (int i = 0; i < words.size()-n; i++) {
            NGram nextNGram = new NGram( ); //empty NGram.
            //add "n" words to into the empty n-gram:
            for (int j = 0; j < n; j++) {
                nextNGram.addWord(words.get(i+j));
            }
            //add that completed NGram into the list:
            nGramsList.add(nextNGram);
        }
        //return the completed list of all NGram objects extracted from the
        //list of words:
        return nGramsList;
    }//END makeNGrams()


    //MAKE HASH MAP and TRANSITION RULES:
    //this method takes the list of NGrams, and uses each NGram as a key to
    //make a HashMap. The value linked to each key is a list of other NGrams
    //that can be found to follow the key NGram somewhere in the text:
    private static HashMap<NGram, TransitionRule> makeHashMap
        (ArrayList<NGram> nGramsList) {
        //map is declared as a Class variable up top
        for (int i = 0; i < nGramsList.size()-1; i++) {
            final NGram key = nGramsList.get(i);
            //**FUTURE REF: source of "map mystery" in the next if-block:
            if (map.containsKey(key)) {
                final TransitionRule value = map.get(key);
                value.addNGramToRule(nGramsList.get(i+1));
                //System.out.println("test 1: does this ever run?");
            } else {
                final TransitionRule value = new TransitionRule();
                value.addNGramToRule(nGramsList.get(i+1));
                map.put(key, value);
                //System.out.println("test 2: does this ever run?");
            }
        }
        //FIXME: this is where I get the last key in the map to make sure it
        // has a value:
        //*****************************************************************
        int lastIndex = nGramsList.size()-1;
        int halfIndex = (int)lastIndex/2;
        NGram lastKey = nGramsList.get(lastIndex);
        TransitionRule lastValue = new TransitionRule();
        lastValue.addNGramToRule(nGramsList.get(halfIndex));
        map.put(lastKey, lastValue);
        //*****************************************************************
        return map;
    }//END makeHashMap()


    //MAKE GIBBERISH:
    //this method takes the hashmap and returns a string of nonsense:
    private static String makeGibberish (HashMap<NGram, TransitionRule> m) {
        Random rand = new Random();
        //start with first NGram in nGramsList:
        NGram currentKey = nGramsList.get(0);
        //add *the first word* of it to the output String:
        StringBuilder result = new StringBuilder();
        result.append(currentKey.get(0)).append(" ");
        //find the next NGram randomly selected from currentKey's linked
        //TransitionRule and add it to the output String:
        for (int i = 0; i < gibberishLength; i++) {
            //get the current TransitionRule:
            TransitionRule currentRule = map.get(currentKey);
            //randomly pick an NGram from that TransitionRule:
            int index;
            //FIXME: throws NullPointerException if you get to a key with no
            // value:
            int bound = currentRule.size(); //bound has to be a pos. number
            if (n == 1) {
                index = 0;
            } else {
                //FIXME: source of endless loop when I had bound-1:
                index = rand.nextInt(bound);
            }
            NGram nextKey = currentRule.get(index);
            result.append(nextKey.get(0)).append(" ");
            currentKey = nextKey;
        }
        return result.toString();
    }//END makeGibberish();


    //MAIN METHOD collects these 3 command line arguments:
        //args[0] = int         "n" desired NGram size, or "order" of MRTG
        //args[1] = int         output gibberishLength
        //args[2] = String      .txt file URI (file location) *note: put text
            // file or it's directory at the same level as src (sibling)
    public static void main (String[] args) throws Exception{
    //1. give instructions if there are a lack of command line arguments:
        if (args.length < 3) {
            System.out.println("Three command line arguments required: " +
                    "(int)NGramSize (int)gibberishLength (String)" +
                    "textFileName");
            System.out.println("NGram size (MRTG order must be >= 2");
            System.out.println("example of how to run program:");
            System.out.println("java MarkovTextGenerator 2 100 hamlet.text");
        }
    //2. get command line args and make a list of all the words in the text:
        if (Integer.parseInt(args[0]) >= 2) {
            n = Integer.parseInt(args[0]);
        } else {
            System.out.println("Illegal argument: your first argument NGram " +
                    "size (the MRTG \"order\") must be >= 2");
            System.exit(1);
        }
        gibberishLength = Integer.parseInt(args[1]);
        uri = args[2];
        ArrayList<String> words = readTextFile(uri);

    //3. make a list of NGram objects from the words:
        ArrayList<NGram> nGramsList = makeNGrams(n, words);

        //TESTS FOR NGRAM AND NGRAM LIST: **********************************
        //print out the list of NGram objects using its custom toString method:
        //System.out.println("\nNGrams:");
        //for (int i = 0; i < nGramsList.size(); i++) {
            //System.out.println(i + ": " + nGramsList.get(i).toString());
        //}
        //END NGRAM TESTS **************************************************

    //4. make the HashMap and TransitionRules:
        HashMap<NGram, TransitionRule> map = makeHashMap(nGramsList);

        //TESTS FOR THE HASHMAP AND TRANSITION RULE LISTS: *****************
        //print out the list of TransitionRule objects:
        //System.out.println("\nTransition Rules:");
            //two messy ways to print out the hashmap for future reference:
            //System.out.println(Arrays.asList(map));
            //System.out.println(Collections.singletonList(map));
        //a cleaner way to print out the hashmap:
        //for (Object key : map.keySet()) {
            //System.out.print("key: " + key );
            //System.out.println(" | value: " + map.get(key));
            //TransitionRule poop = map.get(key);
            //System.out.println("t-rule size = " + poop.size());
        //}
        //END HASHMAP TESTS ************************************************

    //5. produce output:
        //make sure the requested gibberish is not bigger than the text file:
        //FIXME: I will remove this requirement:
//        if (gibberishLength > words.size()) {
//            gibberishLength = words.size();
//        }
        System.out.println("\nWELCOME TO JAEREN'S MARKOV TEXT GENERATOR");
        System.out.print("\nThe text file you are using is: ");
        System.out.println(uri);
        System.out.println("The MRTG order is: " + n);
        System.out.println("\nHere is the start of the not-scrambled (" + uri +
                ") file that you are going to turn into gibberish: ");
        for (String word : words) {
            System.out.print(word + " ");
        }
        System.out.println("\n\n*NOTE: The gibberish does not begin until the" +
                " MRTG reaches the first transition rule that has more than " +
                "one NGram in it AND also selects an NGram other than the " +
                "first one in that TransitionRule.");
        System.out.println("\n*ALSO NOTE: The probability-weighting in this " +
                "program is integral to the way my TransitionRule objects " +
                "are built. Each TransitionRule has repeated NGrams every " +
                "time the same NGram is found in the text. So the probability" +
                " of selecting a particular NGram from the list corresponds " +
                "to the frequency of that NGram in the text.");
        System.out.println("\nAnd here is your " + gibberishLength +
                " words of gibberish: ");
        System.out.println(makeGibberish(map));
    }//END main()

}//END class MarkovTextGenerator;

