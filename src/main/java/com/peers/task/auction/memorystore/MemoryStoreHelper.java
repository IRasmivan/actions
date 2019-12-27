package com.peers.task.auction.memorystore;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class MemoryStoreHelper.
 */
public class MemoryStoreHelper {

    /** The auction. */
    private Map<BigInteger, TreeMap> auction;
    
    /** The memory store helper. */
    private static MemoryStoreHelper memoryStoreHelper;

    /**
     * Instantiates a new memory store helper.
     */
    private MemoryStoreHelper(){
        auction = new HashMap<>();
    }

    /**
     * Gets the memory store helper.
     *
     * @return the memory store helper
     *
     * Singleton method, this method make sure that there is always only one object for memoryStoreHelper
     *
     */
    public static MemoryStoreHelper getMemoryStoreHelper(){
        if(memoryStoreHelper == null){
            memoryStoreHelper = new MemoryStoreHelper();
        }
        return memoryStoreHelper;
    }

    /**
     * Gets the auction.
     *
     * @return the auction
     */
    public Map<BigInteger, TreeMap> getAuction() {
        return auction;
    }

    /**
     * Sets the auction.
     *
     * @param auction the auction
     */
    public void setAuction(Map<BigInteger, TreeMap> auction) {
        this.auction = auction;
    }

}
