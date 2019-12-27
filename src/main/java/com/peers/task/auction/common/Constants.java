package com.peers.task.auction.common;

/**
 * The Class Constants.
 */
public class Constants {

   /**
    * Instantiates a new constants.
    */
   private Constants(){
        // Default Constructor
    }

    /** The ENUM VALID_COMMAND. */
    public enum VALID_COMMAND{
        CREATE,
        PLACE_BID,
        LATEST,
        HIGHEST_BID,
        GET,
        QUIT,
        DELETE
    }

    /** The Constant ENTER_COMMAND. */
    public static final String ENTER_COMMAND = "~~ Enter your Command ~~";

    /** The Constant SPACE. */
    public static final String SPACE = " ";

    /** The Constant MULT_SPACE. */
    public static final String MULT_SPACE = " +";

    /** The Constant TAB. */
    public static final String TAB = "\t";

    /** The Constant QUOTE. */
    public static final String QUOTE = "'";

    /** The Constant TWO_DECIMAL. */
    public static final String TWO_DECIMAL = "%.2f";

}
