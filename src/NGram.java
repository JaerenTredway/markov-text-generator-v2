import java.util.ArrayList;

//this class is basically a "wrapper" that encloses a list. Each NGram object
//will therefore be a list of words, "n" words in length (n = "order")
public final class NGram {

    //MEMBER VARIABLES:
    private final ArrayList<String> words;

    //CONSTRUCTOR:
    NGram() {
        this.words = new ArrayList<> ();
    }

    //method to add a word onto the individual NGram:
    void addWord(String nextWord) {
        words.add(nextWord);
    }

    //gets the value at the argument index:
    public String get(int i) {
        return words.get(i);
    }

    //this method overrides the parent toString method:
    @Override
    public String toString( ) {
        StringBuilder rv = new StringBuilder();
        for (String word : words) {
            rv.append(word).append(" ");
        }
        return rv.toString();
    }

    //this overrides Object class hashCode() to prevent HashMap malfunction:
    @Override
		public int hashCode( ) {
			int hash1 = words.get(0).hashCode();
			int hash2 = words.get(1).hashCode();
			return hash1 + hash2;
		}

    //this overrides Object class equals() to prevent HashMap malfunction:
    @Override
    public boolean equals( Object o) {
        if (o == null) return false;
        if (!(o instanceof NGram)) return false;
        NGram b = (NGram) o;
        return (words.get(0).equals(b.words.get(0)) && words.get(1).equals(b.words.get(1)));
    }

    //this returns the size of the ArrayList:
    public int size() {
        return words.size();
    }

}//END NGram class
