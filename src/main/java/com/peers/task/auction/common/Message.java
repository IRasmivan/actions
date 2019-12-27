package com.peers.task.auction.common;

/**
 * The Class Message.
 */
public class Message {

    /**
     * Instantiates a new message.
     */
    private Message(){
        // default constructor
    }
    
    /** The Constant INVALID_COMMAND. */
    public static final String INVALID_COMMAND = "ERR Please enter a valid command!!";
    
    /** The Constant OK_RESPONSE. */
    public static final String OK_RESPONSE = "OK ";
    
    /** The Constant ERR_IDENTIFIER_NOT_EXISTS. */
    public static final String ERR_IDENTIFIER_NOT_EXISTS = "ERR A identifier does not exists";
    
    /** The Constant ERR_BID_ALREADY_EXISTS. */
    public static final String ERR_BID_ALREADY_EXISTS = "ERR A bid already exists for this ID and Timestamp";
    
    /** The Constant ERR_HISTORY. */
    public static final String ERR_HISTORY = "ERR A history already exists for identifier ";
    
    /** The Constant ERR_NO_HISTORY. */
    public static final String ERR_NO_HISTORY = "ERR No history exists for identifier ";

    /** The Constant TOO_MANY_PARAMS. */
    public static final String TOO_MANY_PARAMS = "ERR Too Many Parameter, Please retry with valid data";

    /** The Constant WITH_HISTORY. */
    public static final String WITH_HISTORY ="with history";

    /** The Constant WITH_NO_HISTORY. */
    public static final String WITH_NO_HISTORY ="No history exists for identifier";

    /** The Constant AUCTION_COMPLETED. */
    public static final String AUCTION_COMPLETED ="Auction Completed";

}
