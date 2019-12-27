package com.peers.task.auction.service;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import com.peers.task.auction.common.Constants;
import com.peers.task.auction.common.Message;

import com.peers.task.auction.memorystore.MemoryStoreHelper;

/**
 * The Class Auction.
 */
public class Auction {

    /** The memory store helper. */
    MemoryStoreHelper memoryStoreHelper;

    /**
     * Instantiates a new auction.
     */
    public Auction(){
        memoryStoreHelper = MemoryStoreHelper.getMemoryStoreHelper();
    }

    /**
     * Creates the auction.
     *
     * @param auctionId the auction id
     * @return the string
     */
    public String createAuction(BigInteger auctionId){
        if(memoryStoreHelper.getAuction().containsKey(auctionId)){
            return Message.ERR_HISTORY  + appendAuctionId(auctionId);
        } else{
            memoryStoreHelper.getAuction().put(auctionId, new TreeMap<BigInteger,Double>());
        }
        return Message.OK_RESPONSE;
    }

    /**
     * Place bid.
     *
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @param bid the bid
     * @return the string
     */
    public String placeBid(BigInteger auctionId, BigInteger timeStamp, Double bid){
        if(memoryStoreHelper.getAuction().containsKey(auctionId)){
            TreeMap<BigInteger, Double> bids = memoryStoreHelper.getAuction().get(auctionId);
            if(bids.containsKey(timeStamp)){
                return Message.ERR_BID_ALREADY_EXISTS + appendAuctionId(auctionId);
            } else{
                bids.put(timeStamp, bid);
                memoryStoreHelper.getAuction().put(auctionId, bids);
                return  Message.OK_RESPONSE + String.format(Constants.TWO_DECIMAL,bid);
            }
        } else{
            return Message.ERR_IDENTIFIER_NOT_EXISTS + appendAuctionId(auctionId);
        }
    }

    /**
     * Delete bid.
     *
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @return the string
     */
    public String deleteBid(BigInteger auctionId, BigInteger timeStamp){
        if(memoryStoreHelper.getAuction().containsKey(auctionId)){
            TreeMap<BigInteger, Double> bids = memoryStoreHelper.getAuction().get(auctionId);
            if(timeStamp != null && bids.containsKey(timeStamp)){

                bids.tailMap(timeStamp).clear();
                memoryStoreHelper.getAuction().put(auctionId, bids);
                return  Message.OK_RESPONSE + (bids.size() > 0 ? bids.lastEntry().getValue() :  Message.WITH_NO_HISTORY +  Constants.SPACE  + appendAuctionId(auctionId));
            } else if(timeStamp == null) {
                Object highestBid = getBiggestBid(bids);
                bids.clear();
                memoryStoreHelper.getAuction().put(auctionId, bids);
                if(highestBid == null) {
                    return Message.ERR_NO_HISTORY + appendAuctionId(auctionId);
                } else{
                    return Message.OK_RESPONSE + String.format(Constants.TWO_DECIMAL, ((Map.Entry<BigInteger,Double>) highestBid).getValue());
                }
            } else{
                return Message.ERR_NO_HISTORY  + appendAuctionId(auctionId);
            }
        } else{
            return Message.ERR_IDENTIFIER_NOT_EXISTS + appendAuctionId(auctionId);
        }
    }

    /**
     * Gets the bid.
     *
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @return the bid
     */
    public String getBid(BigInteger auctionId, BigInteger timeStamp){
        if(memoryStoreHelper.getAuction().containsKey(auctionId)){
            if(memoryStoreHelper.getAuction().get(auctionId).containsKey(timeStamp)) {
                return Message.OK_RESPONSE + timeStamp + Constants.SPACE + memoryStoreHelper.getAuction().get(auctionId).get(timeStamp);
            } else{
                return Message.ERR_NO_HISTORY + appendAuctionId(auctionId) + Constants.SPACE +  Message.WITH_HISTORY + Constants.SPACE + Constants.QUOTE + timeStamp + Constants.QUOTE;
            }
        } else{
            return Message.ERR_IDENTIFIER_NOT_EXISTS + appendAuctionId(auctionId);
        }
    }

    /**
     * Latest bid.
     *
     * @param auctionId the auction id
     * @return the string
     */
    public String latestBid(BigInteger auctionId) {
        if(memoryStoreHelper.getAuction().containsKey(auctionId)){
            if(memoryStoreHelper.getAuction().get(auctionId).lastEntry() != null) {
                return Message.OK_RESPONSE + memoryStoreHelper.getAuction().get(auctionId).lastEntry().getKey() + Constants.SPACE + memoryStoreHelper.getAuction().get(auctionId).lastEntry().getValue();
            } else{
                return Message.ERR_NO_HISTORY + appendAuctionId(auctionId);
            }
        } else{
            return Message.ERR_IDENTIFIER_NOT_EXISTS + appendAuctionId(auctionId);
        }
    }

    /**
     * Highest bid.
     *
     * @param auctionId the auction id
     * @return the string
     */
    public String highestBid(BigInteger auctionId){
        if(memoryStoreHelper.getAuction().containsKey(auctionId)){
            Map.Entry<BigInteger,Double> bigEntity = getBiggestBid(memoryStoreHelper.getAuction().get(auctionId));
            if(bigEntity != null) {
                return Message.OK_RESPONSE + bigEntity.getKey() + Constants.SPACE + bigEntity.getValue();
            } else{
                return Message.ERR_NO_HISTORY + appendAuctionId(auctionId);
            }
        } else{
            return Message.ERR_IDENTIFIER_NOT_EXISTS + appendAuctionId(auctionId);
        }
    }

    /**
     * Gets the biggest bid.
     *
     * @param bids the bids
     * @return the biggest bid
     */
    private Map.Entry<BigInteger,Double> getBiggestBid(TreeMap<BigInteger,Double> bids){
        Double big = -9999999.0d;
        Map.Entry<BigInteger,Double> bigEntity = null;
        for (Map.Entry<BigInteger,Double> entry : bids.entrySet()){
            if(big < entry.getValue()){
                big = entry.getValue();
                bigEntity = entry;
            }
        }
        return bigEntity;
    }

    /**
     * Append auction id.
     *
     * @param auctionId the auction id
     * @return the string
     */
    private String appendAuctionId(BigInteger auctionId){
        return Constants.SPACE + Constants.QUOTE + auctionId.intValue() + Constants.QUOTE;
    }

}
