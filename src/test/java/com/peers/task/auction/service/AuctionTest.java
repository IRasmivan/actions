package com.peers.task.auction.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuctionTest {

    Auction auction = new Auction();

    @Test
    @Order(1)
    public void createAuctionPositiveTest(){
        BigInteger auctionId = new BigInteger("1");
        assertEquals("OK", auction.createAuction(auctionId).trim());
    }

    @Test
    @Order(2)
    public void createAuctionAlreadyCreatedTest(){
        BigInteger auctionId = new BigInteger("1");
        assertEquals("ERR A history already exists for identifier  '1'", auction.createAuction(auctionId));
    }

    @Test
    @Order(3)
    public void createAuctionPositive2Test(){
        BigInteger auctionId = new BigInteger("2");
        assertEquals("OK", auction.createAuction(auctionId).trim());
    }

    @Test
    @Order(4)
    public void placeBidCreateTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("1");
        Double bid = 25.0d;
        assertEquals("OK 25.00", auction.placeBid(auctionId, timestamp, bid));
    }

    @Test
    @Order(5)
    public void placeBidSameTimeStampTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("1");
        Double bid = 25.0d;
        assertEquals("ERR A bid already exists for this ID and Timestamp '1'", auction.placeBid(auctionId, timestamp, bid));
    }

    @Test
    @Order(6)
    public void placeBidDiffTimeStampTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("2");
        Double bid = 20.0d;
        assertEquals("OK 20.00", auction.placeBid(auctionId, timestamp, bid));
        assertEquals("OK 10.00", auction.placeBid(auctionId, new BigInteger("3"), 10.0d));
    }





    @Test
    @Order(7)
    public void placeBidAuctionNotExistsTest(){
        BigInteger auctionId = new BigInteger("0");
        BigInteger timestamp = new BigInteger("2");
        Double bid = 20.0d;
        assertEquals("ERR A identifier does not exists '0'", auction.placeBid(auctionId, timestamp, bid));
    }


    @Test
    @Order(8)
    public void getBigAuctionNotExistsTest(){
        BigInteger auctionId = new BigInteger("0");
        BigInteger timestamp = new BigInteger("2");
        assertEquals("ERR A identifier does not exists '0'", auction.getBid(auctionId, timestamp));
    }

    @Test
    @Order(9)
    public void getBigAuctionExistsTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("2");
        assertEquals("OK 2 20.0", auction.getBid(auctionId, timestamp));
    }

    @Test
    @Order(10)
    public void getBigAuctionExistsNoHisTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("4");
        assertEquals("ERR No history exists for identifier  '1' with history '4'", auction.getBid(auctionId, timestamp));
    }


    @Test
    @Order(11)
    public void latestBidAuctionNotExistsTest(){
        BigInteger auctionId = new BigInteger("0");
        assertEquals("ERR A identifier does not exists '0'", auction.latestBid(auctionId));
    }

    @Test
    @Order(12)
    public void latestBidAuctionExistsTest(){
        BigInteger auctionId = new BigInteger("1");
        assertEquals("OK 3 10.0", auction.latestBid(auctionId));
    }

    @Test
    @Order(13)
    public void latestBidAuctionExistsNoHisTest(){
        BigInteger auctionId = new BigInteger("2");
        assertEquals("ERR No history exists for identifier  '2'", auction.latestBid(auctionId));
    }


    @Test
    @Order(14)
    public void highestBidAuctionNotExistsTest(){
        BigInteger auctionId = new BigInteger("0");
        assertEquals("ERR A identifier does not exists '0'", auction.highestBid(auctionId));
    }

    @Test
    @Order(15)
    public void highestBidAuctionExistsTest(){
        BigInteger auctionId = new BigInteger("1");
        assertEquals("OK 1 25.0", auction.highestBid(auctionId));
    }

    @Test
    @Order(16)
    public void highestBidAuctionExistsNoHisTest(){
        BigInteger auctionId = new BigInteger("2");
        assertEquals("ERR No history exists for identifier  '2'", auction.highestBid(auctionId));
    }


    @Test
    @Order(17)
    public void deleteBidAuctionNotExistsTest(){
        BigInteger auctionId = new BigInteger("0");
        assertEquals("ERR A identifier does not exists '0'", auction.deleteBid(auctionId, null));
    }

    @Test
    @Order(18)
    public void deleteBidAuctionExistsTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("2");
        assertEquals("OK 25.0", auction.deleteBid(auctionId, timestamp));
    }

    @Test
    @Order(19)
    public void deleteBidAuctionExistsNoHisTest(){
        BigInteger auctionId = new BigInteger("2");
        assertEquals("ERR No history exists for identifier  '2'", auction.deleteBid(auctionId, null));
    }

    @Test
    @Order(20)
    public void deleteBidAuctionExistsWithoutHisTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("3");
        assertEquals("ERR No history exists for identifier  '1'", auction.deleteBid(auctionId, timestamp));
    }


    @Test
    @Order(21)
    public void deleteBidAuctionExistsAllClearTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("1");
        Double bid = 25.0d;
        auction.placeBid(auctionId, timestamp, bid);
        assertEquals("OK 25.00", auction.deleteBid(auctionId, null));
    }

    @Test
    @Order(20)
    public void deleteBidAuctionExistsWithHisTest(){
        BigInteger auctionId = new BigInteger("1");
        BigInteger timestamp = new BigInteger("1");
        Double bid = 25.0d;
        auction.placeBid(auctionId, timestamp, bid);
        assertEquals("OK No history exists for identifier  '1'", auction.deleteBid(auctionId, timestamp));
    }

}
