import java.util.ArrayList;

//each TransitionRule is a list of options of NGrams that can follow its
//associated key NGram:
public final class TransitionRule {

    //MEMBER VARIABLES:
    //this list stores all the possible follower-NGrams that can be added-on
    //to manufacture gibberish. Each follower-NGram has appeared after its key
    //NGram somewhere in the text.
    private final ArrayList<NGram> followerNGramsList;

    //CONSTRUCTOR:
    TransitionRule() {
        this.followerNGramsList = new ArrayList<> ();
    }

    //method to add an NGram to the TransitionRule list:
    public void addNGramToRule( NGram x) {
        followerNGramsList.add(x);
    }

    //this method overrides the parent toString method from Object class:
    @Override
    public String toString( ) {
        String rv = "";
        for (NGram n : followerNGramsList) {
            rv += n.toString() + " ";
        }
        return rv;
    }

    //returns the length of the TransitionRule (length of list of NGrams):
    public int size() {
        return followerNGramsList.size();
    }

    //this gets the value stored at index "i":
    public NGram get(int i) {
        return followerNGramsList.get(i);
    }

}//END class TransitionRule
