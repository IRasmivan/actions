package com.peers.task.auction;

import com.peers.task.auction.common.Message;
import com.peers.task.auction.helper.AuctionHelper;
import com.peers.task.auction.helper.PrintStatment;

/**
 * The Class Application.
 */
public class Application {

    /**
     * The main method.
     *
     * @param ignore the arguments
     */
    public static void main(String[] ignore){
        AuctionHelper auction = new AuctionHelper();
        auction.processAuction(); // calling the process Auction which is where the core auction process happens
        PrintStatment.printMessage(Message.AUCTION_COMPLETED );
    }

}
