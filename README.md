# PEERS AUCTION TASK
![](https://github.com/IRasmivan/peers-auction-task/workflows/Java%20CI/badge.svg)

## Table of Content
- [PEERS AUCTION TASK](#peers-auction-task)
    - [Table of Content](#table-of-content)
    - [1. Task](#1-task)
    - [2. Core Logic](#2-core-logic)
        - [2.1 Assign And Validating Command](#21-assign-and-validating-command)
        - [2.2 Process Command](#22-process-command)
            - [2.2.1 Create Command](#221-create-command)
            - [2.2.2 Place Bid Command](#222-place-bid-command)
            - [2.2.3 Get Command](#223-get-command)
            - [2.2.4 Latest Command](#224-latest-command)
            - [2.2.5 Highest Command](#225-highest-command)
            - [2.2.6 Delete Command](#226-delete-command)
    - [3. Maven Command](#3-maven-command)
        - [3.1 Command to build the application](#31-command-to-build-the-application)
        - [3.2 Command to Run the Project](#32-command-to-run-the-project)
    - [4. Docker Image](#4-docker-image)
        - [4.1 DockerHub](#41-dockerhub)
        - [4.2 Running Docker Image](#42-running-docker-image)
    - [5. GitHub Actions](#5-GitHub-Actions)
        - [5.1 Continuers Integration](#51-Continuers-Integration)

## 1. Task

This is a task to be written in Java. It should be buildable through Maven. Note that there should not be any run time dependencies other than those provided by JavaSE 8.

You are tasked to write a small program to handle bids for various items that are listed.

The system will handle both listing a new item as well as handling bids for those items. The system will essentially by an in-memory temporal data store.

A temporal data store allows point in time queries about data. We will capture bids and store them in the temporal data store. We can then query the temporal store
to determine at any given time, what the most recent bid was.

This example illustrates the following key terms, which will be used throughout this document:

       1. identifier        - a key (e.g. the auction id), for which we want to store a history of bids
       2. history           - the list of bids for a given identifier
       3. bid 			    - a bid which is associated with a particular auction at a particular timestamp
       4. timestamp         - a given point in time


The latest bid for an identifier at a timestamp is found by searching in the identifier's history for the first bid whose timestamp is less than, or equal to, the sought timestamp.


For the purposes of this scenario, you should assume the following:

       1. identifiers are integers in the inclusive range [0..2^31 - 1]
       2. timestamps are integers in the inclusive range [0..2^63 - 1]
       3. a bid value is represented as a number with two decimal places
       4. users may interact with the temporal store, as well as processes. you should be strict in what data you accept, but should provide suitable error messages where the user has erred.
       5. capacity; there is no requirement to restrict your store to a given size, but you may assume that you will be provided with no more than 10,000 identifiers, each with no more than 1,000 history entries

Your temporal data store will communicate with other processes via standard input and output. A series of commands are read and applied to the data store from standard input. The results of evaluating the commands should be written to standard output. A command will only ever be one line; the result of executing a command will be a single output line, except for the QUIT command.
When there are no more lines to consume, your temporal data store should exit. There is no need to persist data in the store between executions (this is an in-memory temporal store).

The commands which can be executed against your data store are described below. Note that:

- items in <angle brackets> represent required arguments, but the angle brackets themselves do not form part of the command
- items in [square brackets] are optional arguments
- successfully processed commands should always start their response with "OK ", followed by the result of executing the command
- commands which could not be executed should always start their response with "ERR ", followed by a reasonable error message.
- commands are not sensitive to whitespace; arguments must be separated by at least one character of whitespace, but may be separated by more.


CREATE \<id>
       - Creates a new history for the given auction identifier, if there is no existing history. CREATE should not be executed if the provided identifier already has a history.

PLACE_BID \<id> \<timestamp> \<bid>
	   - This will place a bid on an existing auction id. PLACE_BID should not be executed if the auction doesn't exist. Returns the bid value

DELETE \<id> [timestamp]
       - If timestamp is provided, deletes all bids for the given identifier from that timestamp forward. Returns the current bid at the given timestamp, or an error if
       there is no available bid.
       - If timestamp is not provided, deletes the history for the given identifier, and returns the bid with the greatest timestamp from the history which has been deleted.

GET \<id> \<timestamp>
       - Returns the bid for the given auction identifier at the given timestamp, or an error if there is no available bid.

LATEST \<id>
       - Locates the bid with the greatest timestamp from the history for the given identifier, and responds with its timestamp and bid.

HIGHEST_BID \<id>
	   - Locates the highest bid for a given auction identifier. Returns the timestamp and bid

QUIT
       - Terminates the process immediately. No response should be written.



## 2. Core Logic

### 2.1 Assign And Validating Command

Once the input is received from the user the below logic is applied to the provided command by the user.

    1) The input's first parameter is CMD
    2) If there is second parameter in the input the it is assigned to auctionId
    3) If there the CMD is not CREATE and the input have a third parameter is available it is set to timeStamp
    4) If the CMD is not CREATE or GET and the input have a forth parameter is available it it set to bid.
    5) if there are any more parameter is available and if any of the above condition is not meet then display "ERR Too Many Parameter, Please retry with valid data" to user."
  

### 2.2 Process Command

#### 2.2.1 Create Command
If the provided command is to create an auction then the below logic is applied

    1) if the auctionId is valid and not created earlier then create the auction
    2) if the auctionId is valid and created earlier then return "ERR A history already exists for identifier \<<auctionId>>" to the user
    3) if the auctionId is not valid, then return INVALID_COMMAND to the user.

Sample Input/Output

    CREATE
    ERR Please enter a valid command!!
    CREATE 1 0
    ERR Too Many Parameter, Please retry with valid data
    CREATE 1
    OK


#### 2.2.2 Place Bid Command

If the provided command is to place a bid for an auction then the below logic is applied.

    1) if the auctionId, timeStamp and bid are valid then create a bid for the auction on the provided timeStamp.
    2) if the timeStamp for the auction is already available then return 'ERR A bid already exists for this ID and Timestamp' to the user.
    3) if the auctionId or timeStamp or bid are not valid then return INVALID_COMMAND to the user.


Sample Input/Output

    PLACE_BID 1
    ERR Please enter a valid command!!
    PLACE_BID 2 0 1
    ERR A identifier does not exists '2'
    PLACE_BID 1 1 1 1 1
    ERR Too Many Parameter, Please retry with valid data
    PLACE_BID 1 1 1 100
    ERR Too Many Parameter, Please retry with valid data
    PLACE_BID 1 1 100
    OK 100.00
    PLACE_BID 1 1 100
    ERR A bid already exists for this ID and Timestamp '1'

#### 2.2.3 Get Command
If the provided command is to get bid for an auction then the below logic is applied.

    1) if the auctionId and timeStamp are found then return the bid for the auctionId and timeStamp
    2) if the auctionId and timeStamp are not found then return "ERR No history exists for identifier  with history '<<timeStamp>>'" to the user.
    3) if the auctionId or timeStamp is NULL then return INVALID_COMMAND to the user.

Sample Input/Output

    GET 1
    ERR Please enter a valid command!!
    GET 1 2
    ERR No history exists for identifier  '1' with history '2'
    GET 1 1 3
    ERR Too Many Parameter, Please retry with valid data
    GET 1 1
    OK 1 100.0

#### 2.2.4 Latest Command
If the provided command is to latest for an auction then the below logic is applied.

    1) if the auctionId is found then return the latest bid.
    2) if there are no history for the auctionId is found then return "ERR No history exists for identifier <<auctionId>>" to the user.
    3) if the auctionId is NULL then return INVALID_COMMAND to the user.
    4) if the auctionId is not found then return ERR A identifier does not exists '<<auctionId>>' to the user.

Sample Input/Output

    LATEST 1 1 1
    ERR Too Many Parameter, Please retry with valid data
    LATEST 1 1
    ERR Too Many Parameter, Please retry with valid data
    LATEST 1 1 1 1
    ERR Too Many Parameter, Please retry with valid data
    LATEST 1
    OK 1 100.0
    LATEST 2
    ERR A identifier does not exists '2'
    CREATE 2
    OK
    LATEST 2
    ERR No history exists for identifier  '2'


#### 2.2.5 Highest Command
If the provided command is to highest for an auction then the below logic is applied.

    1) if the auctionId is found then return the highestBid
    2) if the auctionId is not found then return "ERR No history exists for identifier <<auctionId>>" to the user.
    3) if the auctionId is NULL then return INVALID_COMMAND to the user.

Sample Input/Output

    HIGHEST_BID 1 1 1
    ERR Too Many Parameter, Please retry with valid data
    HIGHEST_BID 1 1
    ERR Too Many Parameter, Please retry with valid data
    HIGHEST_BID 1
    OK 1 100.0
    PLACE_BID 1 2 110
    OK 110.00
    PLACE_BID 1 3 99
    OK 99.00
    HIGHEST_BID 1
    OK 2 110.0

#### 2.2.6 Delete Command
If the provided command is to delete for an auction then the below logic is applied.

    1) if auctionId found and there is timeStamp provided by the user.  
        1.1) If the timeStamp is found in the auction history then the auction history from that timeStamp is deleted to the latest auction History.
        1.2) If the timeStamp is not found in the auction history then displayed INVALID_COMMAND to the user.
    2) if auctionId found and there is no timeStamp are provided by the user then all the history for the provided auction is deleted.
    3) if the auctionId is not found then display INVALID_COMMAND to the user.

Sample Input/Output

    CREATE 1
    OK
    PLACE_BID 1 1 100
    OK 100.00
    PLACE_BID 1 2 110
    Time Stamp :: 2
    OK 110.00
    PLACE_BID 1 3 99
    Time Stamp :: 3
    OK 99.00
    PLACE_BID 1 4 199
    OK 199.00
    DELETE 1 4
    OK 99.0
    GET 1 3
    Time Stamp :: 3
    OK 3 99.0
    HIGHEST_BID 1
    OK 2 110.0
    DELTE 1 1 1 1
    ERR Too Many Parameter, Please retry with valid data
    DELTE 1
    ERR Please enter a valid command!!
    DELETE 1
    OK 110.00
    GET 1 1
    ERR No history exists for identifier  '1' with history '1'
    GET 1
    ERR Please enter a valid command!!
    LATEST 1
    ERR No history exists for identifier  '1'
    HIGHEST_BID 1
    ERR No history exists for identifier  '1'

## 3. Maven Command

### 3.1 Command to build the application
```
mvn install
```

### 3.2 Command to Run the Project
```
mvn exec:java -Dexec.mainClass="com.peers.task.auction.Application"
```

## 4. Docker Image

### 4.1 DockerHub
The image url is available in [this](https://hub.docker.com/repository/docker/rasmivan/peers-auction) link 

### 4.2 Running Docker Image
```
docker run -it rasmivan/peers-auction:1.0
```

## 5. GitHub Actions

### 5.1 Continuers Integration
If there are any push request to the master branch then Java-CI workflow triggers and performs Maven Clean, Maven Install and then Maven Test and make sure that the. The config for the same is available in this [link](https://github.com/IRasmivan/peers-auction-task/blob/master/.github/workflows/maveen.yml)

