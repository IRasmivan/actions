package com.peers.task.auction.helper;

import com.peers.task.auction.common.Constants;
import com.peers.task.auction.common.Constants.VALID_COMMAND;
import com.peers.task.auction.common.Message;
import com.peers.task.auction.service.Auction;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class AuctionHelper.
 */
public class AuctionHelper {

    /**
     * Process auction.
     *
     * This Method get the Input from the user and concert it into Listing of String split by <<SPACE>> or <<TAB>>
     */
    public void processAuction(){
        List<String> input;
        String cmd;
        String rawData;
        PrintStatment.printMessage(Constants.ENTER_COMMAND);
        do{
            rawData = getUserInput();
            rawData = rawData.replaceAll(Constants.TAB, Constants.SPACE).trim().replace(Constants.MULT_SPACE, Constants.SPACE);
            input = Arrays.asList(rawData.split(Constants.SPACE));
            cmd = assignAndProcessCmd(input);
        }while(cmd == null || (cmd != null && !cmd.contains(Constants.VALID_COMMAND.QUIT.name())));
    }

    /**
     * Assign and process cmd.
     *
     * @param input the input
     * @return the string
     *
     * This Method retrieves cmd, auctionId, timeStamp and bid that was entered by the user!!
     *
     * Below are the Key Logic
     * 1) The input's first parameter is CMD
     * 2) If there is second parameter in the input the it is assigned to auctionId
     * 3) If there the CMD is not CREATE and the input have a third parameter is available it is set to timeStamp
     * 4) If the CMD is not CREATE or GET and the input have a forth parameter is available it it set to bid.
     * 5) if there are any more parameter is available and if any of the above condition is not meet then display "Too Many Parameter, Please retry with valid data" to user."
     *
     */
    private String assignAndProcessCmd(List<String> input) {
        String cmd = null;
        String auctionId = null;
        String timeStamp = null;
        String bid = null;
        for(String option : input){
            if(!option.trim().isEmpty()) {
                if (cmd == null) {
                    cmd = option.trim();
                } else if (auctionId == null) {
                    auctionId = option.trim();
                } else if (validateBeforeSettingTimeStamp(cmd, timeStamp)) {
                    timeStamp = option.trim();
                } else if (validateBeforeSettingBid(cmd, bid)) {
                    bid = option.trim();
                } else {
                    PrintStatment.printMessage(Message.TOO_MANY_PARAMS);
                    return cmd;
                }
            }
        }
        processAuctionCommand(cmd, auctionId, timeStamp, bid);
        return cmd;
    }

    /**
     * validate Before Setting TimeStamp.
     *
     * @return the boolean
     *
     * This method validates if we can setting bid
     *
     */
    private boolean validateBeforeSettingBid(String cmd, String bid) {
        return cmd != null && !cmd.equals(VALID_COMMAND.DELETE.name()) && !cmd.equals(VALID_COMMAND.LATEST.name()) && !cmd.equals(VALID_COMMAND.HIGHEST_BID.name()) && !cmd.equals(VALID_COMMAND.CREATE.name()) && !cmd.equals(VALID_COMMAND.GET.name()) && bid == null;
    }

    /**
     * validate Before Setting TimeStamp.
     *
     * @return the boolean
     *
     * This method validates if we can setting timeStamp
     *
     */
    private boolean validateBeforeSettingTimeStamp(String cmd, String timeStamp) {
        return cmd != null && !cmd.equals(VALID_COMMAND.LATEST.name()) && !cmd.equals(VALID_COMMAND.HIGHEST_BID.name()) && !cmd.equals(VALID_COMMAND.CREATE.name()) && timeStamp == null;
    }

    /**
     * Gets the user input.
     *
     * @return the user input
     *
     * This method get the input from user over the command line
     *
     */
    private String getUserInput(){
        return System.console().readLine();
    }

    /**
     * Process auction command.
     *
     * @param cmd the cmd
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @param bid the bid
     *
     * This method process the CMD given for the auction.
     *
     */
    private void processAuctionCommand(String cmd, String auctionId, String timeStamp, String bid) {
        String output = null;
        Auction auction = new Auction();
        if(cmd != null && validateCmd(cmd)){
            output = processCmd(VALID_COMMAND.valueOf(cmd), auctionId, timeStamp, bid, output, auction);
            if(output!=null){
                PrintStatment.printMessage(output);
            }
        } else{
            PrintStatment.printMessage(Message.INVALID_COMMAND);
        }
    }

    /**
     * Process cmd.
     *
     * @param cmd the cmd
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @param bid the bid
     * @param output the output
     * @param auction the auction
     * @return the string
     *
     * This method, based on the CMD from the use. Would trigger relevant process to full fill the CMD.
     *
     */
    private String processCmd(VALID_COMMAND cmd, String auctionId, String timeStamp, String bid, String output, Auction auction) {
        switch (cmd){
            case CREATE:
                output = processCreateCmd(auctionId, auction);
                break;
            case PLACE_BID:
                output = processPlaceBidCmd(auctionId, timeStamp, bid, auction);
                break;
            case LATEST:
                output = processLatestCmd(auctionId, auction);
                break;
            case HIGHEST_BID:
                output = processHighestBidCmd(auctionId, auction);
                break;
            case GET:
                output = processGetCmd(auctionId, timeStamp, auction);
                break;
            case DELETE:
                output = processDeleteCmd(auctionId, timeStamp, auction);
                break;
            case QUIT:
                break;
            default:
                output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Process delete cmd.
     *
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @param auction the auction
     * @return the string
     *
     * This method process the DELETE CMD given by the user
     *  1) if auctionId found and there is timeStamp provided by the user.
     *      1.1) If the timeStamp is found in the auction history then the auction history from that timeStamp is deleted to the latest auction History.
     *      1.2) If the timeStamp is not found in the auction history then displayed INVALID_COMMAND to the user.
     *  2) if auctionId found and there is no timeStamp are provided by the user then all the history for the provided auction is deleted.
     *  3) if the auctionId is not found then display INVALID_COMMAND to the user.
     *
     */
    private String processDeleteCmd(String auctionId, String timeStamp, Auction auction) {
        String output;
        if(auctionId != null && validateBigInt(auctionId)) {
            if(timeStamp != null && validateBigInt(timeStamp)){
                output = auction.deleteBid(new BigInteger(auctionId), new BigInteger(timeStamp));
            } else if(timeStamp == null){
                output = auction.deleteBid(new BigInteger(auctionId),null);
            } else{
                output = Message.INVALID_COMMAND;
            }
        } else{
            output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Process get cmd.
     *
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @param auction the auction
     * @return the string
     *
     * This method process the GET CMD given by the user
     * 1) if the auctionId and timeStamp are found then return the bid for the auctionId and timeStamp
     * 2) if the auctionId and timeStamp are not found then return "ERR No history exists for identifier  with history '<<timeStamp>>'" to the user.
     * 3) if the auctionId or timeStamp is NULL then return INVALID_COMMAND to the user.
     *
     */
    private String processGetCmd(String auctionId, String timeStamp, Auction auction) {
        String output;
        if(auctionId != null && validateBigInt(auctionId) &&
                timeStamp != null && validateBigInt(timeStamp)) {
            output = auction.getBid(new BigInteger(auctionId), new BigInteger(timeStamp));
        } else{
            output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Process highest bid cmd.
     *
     * @param auctionId the auction id
     * @param auction the auction
     * @return the string
     *
     * This method get the highestBid for a given auctionId
     *  1) if the auctionId is found then return the highestBid
     *  2) if the auctionId is not found then return "ERR No history exists for identifier <<auctionId>>" to the user.
     *  3) if the auctionId is NULL then return INVALID_COMMAND to the user.
     *
     */
    private String processHighestBidCmd(String auctionId, Auction auction) {
        String output;
        if(auctionId != null && validateBigInt(auctionId)) {
            output = auction.highestBid(new BigInteger(auctionId));
        } else{
            output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Process latest cmd.
     *
     * @param auctionId the auction id
     * @param auction the auction
     * @return the string
     *
     * This method get the latest bid for a given auctionId
     *  1) if the auctionId is found then return the latest bid.
     *  2) if there are no history for the auctionId is found then return "ERR No history exists for identifier <<auctionId>>" to the user.
     *  3) if the auctionId is NULL then return INVALID_COMMAND to the user.
     *  4) if the auctionId is not found then return "ERR A identifier does not exists <<auctionId>>" to the user.
     *
     */
    private String processLatestCmd(String auctionId, Auction auction) {
        String output;
        if(auctionId != null && validateBigInt(auctionId)) {
            output = auction.latestBid(new BigInteger(auctionId));
        } else{
            output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Process place bid cmd.
     *
     * @param auctionId the auction id
     * @param timeStamp the time stamp
     * @param bid the bid
     * @param auction the auction
     * @return the string
     *
     * This method is used to place a bid for an auctionId and timeStamp
     *  1) if the auctionId, timeStamp and bid are valid then create a bid for the auction on the provided timeStamp.
     *  2) if the timeStamp for the auction is already available then return "ERR A bid already exists for this ID and Timestamp" to the user.
     *  3) if the auctionId or timeStamp or bid are not valid then return INVALID_COMMAND to the user.
     *
     */
    private String processPlaceBidCmd(String auctionId, String timeStamp, String bid, Auction auction) {
        String output;
        if(auctionId != null && validateBigInt(auctionId) &&
                timeStamp != null && validateBigInt(timeStamp) &&
                bid != null && validateDouble(bid)) {
            output = auction.placeBid(new BigInteger(auctionId), new BigInteger(timeStamp), Double.parseDouble(bid));
        } else{
            output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Process create cmd.
     *
     * @param auctionId the auction id
     * @param auction the auction
     * @return the string
     *
     * This method is used to create an auction
     *  1) if the auctionId is valid and not created earlier then create the auction
     *  2) if the auctionId is valid and created earlier then return "ERR A history already exists for identifier <<auctionId>>" to the user
     *  3) if the auctionId is not valid, then return INVALID_COMMAND to the user.
     */
    private String processCreateCmd(String auctionId, Auction auction) {
        String output;
        if(auctionId != null && validateBigInt(auctionId)) {
            output = auction.createAuction(new BigInteger(auctionId));
        } else{
            output = Message.INVALID_COMMAND;
        }
        return output;
    }

    /**
     * Validate cmd.
     *
     * @param cmd the cmd
     * @return true, if successful
     *
     * This method validate if the provided CMD by the user is a valid Command
     *
     */
    private boolean validateCmd(String cmd) {
        return Arrays.stream(Constants.VALID_COMMAND.values()).map(Constants.VALID_COMMAND::name).collect(Collectors.toSet()).contains(cmd);
    }

    /**
     * Validate big int.
     *
     * @param data the data
     * @return true, if successful
     *
     * This method validate if the provided parameter auctionId or timeStamp is valid
     *
     */
    private boolean validateBigInt(String data){
        try{
            new BigInteger(data);
        } catch(Exception ex){
            return false;
        }
        return true;
    }

    /**
     * Validate double.
     *
     * @param data the data
     * @return true, if successful
     *
     * This method validate if the provided parameter bid is valid
     *
     */
    private boolean validateDouble(String data){
        try{
            Double.parseDouble(data);
        } catch(Exception ex){
            return false;
        }
        return true;
    }

}
