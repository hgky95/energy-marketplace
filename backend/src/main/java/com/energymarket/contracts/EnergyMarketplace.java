package com.energymarket.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.12.2.
 */
@SuppressWarnings("rawtypes")
public class EnergyMarketplace extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b5060405161103238038061103283398101604081905261002e9161010b565b60015f55338061005757604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b6100608161009f565b50600280546001600160a01b039384166001600160a01b031991821617909155600380549290931691161790556005805460ff1916600117905561013c565b600180546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b80516001600160a01b0381168114610106575f80fd5b919050565b5f806040838503121561011c575f80fd5b610125836100f0565b9150610133602084016100f0565b90509250929050565b610ee9806101495f395ff3fe6080604052600436106100e4575f3560e01c80636bfb0d0111610087578063bfb231d211610057578063bfb231d214610248578063c482228a146102d0578063d56d229d146102ef578063f2fde38b1461030e575f80fd5b80636bfb0d01146101ee578063715018a61461020357806383cb582d146102175780638da5cb5b1461022b575f80fd5b8063400ba069116100c2578063400ba0691461016657806351ed8288146101855780635e318e0714610198578063645d49b0146101b7575f80fd5b80630344ee4c146100e8578063162359f21461011a57806329d7f0c214610145575b5f80fd5b3480156100f3575f80fd5b50610107610102366004610c2d565b61032d565b6040519081526020015b60405180910390f35b348015610125575f80fd5b506005546101339060ff1681565b60405160ff9091168152602001610111565b348015610150575f80fd5b5061016461015f366004610ced565b610485565b005b348015610171575f80fd5b50610107610180366004610d2f565b61053b565b610164610193366004610d59565b610653565b3480156101a3575f80fd5b506101646101b2366004610d59565b61092f565b3480156101c2575f80fd5b506003546101d6906001600160a01b031681565b6040516001600160a01b039091168152602001610111565b3480156101f9575f80fd5b5061010760045481565b34801561020e575f80fd5b50610164610a88565b348015610222575f80fd5b50610133600181565b348015610236575f80fd5b506001546001600160a01b03166101d6565b348015610253575f80fd5b5061029d610262366004610d59565b60066020525f90815260409020805460018201546002830154600390930154919290916001600160a01b03811690600160a01b900460ff1685565b604080519586526020860194909452928401919091526001600160a01b031660608301521515608082015260a001610111565b3480156102db575f80fd5b506101646102ea366004610d70565b610a9b565b3480156102fa575f80fd5b506002546101d6906001600160a01b031681565b348015610319575f80fd5b50610164610328366004610d70565b610b39565b60025460405163ba7aef4360e01b81525f9182916001600160a01b039091169063ba7aef439061036590339089908990600401610db7565b6020604051808303815f875af1158015610381573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906103a59190610dea565b6040805160a081018252828152602080820187815282840189815233606085018181526001608087018181525f8a8152600690975295889020965187559351938601939093559051600285015590516003909301805492511515600160a01b026001600160a81b03199093166001600160a01b0394909416939093179190911790915590519192507f0c9df0593dd0153bede20c2f351097834114b634661d25fe3e8069b94ce49bc891610460918491899089908990610e01565b60405180910390a160048054905f61047783610e52565b909155509095945050505050565b61048d610b73565b5f8160ff16116104f25760405162461bcd60e51b815260206004820152602560248201527f436f6d6d697373696f6e20726174652063616e6e6f74206265206c6573732074604482015264068616e20360dc1b60648201526084015b60405180910390fd5b6005805460ff191660ff83169081179091556040519081527fd3e90a6443d7dbfb11d15fa44fad50afa5261f9a0c8678a0a4e8a5174e194325906020015b60405180910390a150565b60035460405163d04c911560e01b81526001600160a01b0383811660048301525f92839291169063d04c911590602401602060405180830381865afa158015610586573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906105aa9190610dea565b600354600554604051631c26763960e21b81526004810184905260ff90911660248201529192505f916001600160a01b0390911690637099d8e490604401602060405180830381865afa158015610603573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906106279190610dea565b9050610634606480610e6a565b61063e8287610e6a565b6106489190610e81565b925050505b92915050565b61065b610ba0565b5f8181526006602052604090206003810154600160a01b900460ff166106b65760405162461bcd60e51b815260206004820152601060248201526f4e4654206e6f7420666f722073616c6560801b60448201526064016104e9565b80600101543410156107015760405162461bcd60e51b8152602060048201526014602482015273125b9cdd59999a58da595b9d081c185e5b595b9d60621b60448201526064016104e9565b600381015460018201546001600160a01b03909116905f610722828461053b565b90505f61072f8284610ea0565b60038601805460ff60a01b191690556002546040516323b872dd60e01b81526001600160a01b038781166004830152336024830152604482018a90529293509116906323b872dd906064015f604051808303815f87803b158015610791575f80fd5b505af11580156107a3573d5f803e3d5ffd5b5050600254604051636d8f8e0960e11b81526001600160a01b038881166004830152336024830152604482018b9052909116925063db1f1c1291506064015f604051808303815f87803b1580156107f8575f80fd5b505af115801561080a573d5f803e3d5ffd5b505060035460028801546001600160a01b03909116925063c0d6dc1e9150869061083690600a90610e81565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015263ffffffff1660248201526044015f604051808303815f87803b15801561087f575f80fd5b505af1158015610891573d5f803e3d5ffd5b50506040516001600160a01b038716925083156108fc02915083905f818181858888f193505050501580156108c8573d5f803e3d5ffd5b50604080518781526001600160a01b03861660208201523381830152606081018590526080810184905290517f5fafad85dec201c2e73b6a2bef6351129304a624ea8720ecd04cd7f04d83bc399181900360a00190a1505050505061092c60015f55565b50565b610937610b73565b478181101561097f5760405162461bcd60e51b8152602060048201526014602482015273496e73756666696369656e742062616c616e636560601b60448201526064016104e9565b5f6109926001546001600160a01b031690565b6001600160a01b0316836040515f6040518083038185875af1925050503d805f81146109d9576040519150601f19603f3d011682016040523d82523d5f602084013e6109de565b606091505b5050905080610a2f5760405162461bcd60e51b815260206004820152601760248201527f4661696c656420746f207769746864726177206665657300000000000000000060448201526064016104e9565b7f7fcf532c15f0a6db0bd6d0e038bea71d30d808c7d98cb3bf7268a95bf5081b65610a626001546001600160a01b031690565b604080516001600160a01b039092168252602082018690520160405180910390a1505050565b610a90610b73565b610a995f610bc8565b565b610aa3610b73565b6001600160a01b038116610aeb5760405162461bcd60e51b815260206004820152600f60248201526e496e76616c6964206164647265737360881b60448201526064016104e9565b600380546001600160a01b0319166001600160a01b0383169081179091556040519081527f1cd49dd1317d074bd776a0433b917821bd9714962916a8df3503f0d00e74715d90602001610530565b610b41610b73565b6001600160a01b038116610b6a57604051631e4fbdf760e01b81525f60048201526024016104e9565b61092c81610bc8565b6001546001600160a01b03163314610a995760405163118cdaa760e01b81523360048201526024016104e9565b60025f5403610bc257604051633ee5aeb560e01b815260040160405180910390fd5b60025f55565b600180546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b634e487b7160e01b5f52604160045260245ffd5b5f805f60608486031215610c3f575f80fd5b833567ffffffffffffffff811115610c55575f80fd5b8401601f81018613610c65575f80fd5b803567ffffffffffffffff811115610c7f57610c7f610c19565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610cae57610cae610c19565b604052818152828201602001881015610cc5575f80fd5b816020840160208301375f602092820183015297908601359650604090950135949350505050565b5f60208284031215610cfd575f80fd5b813560ff81168114610d0d575f80fd5b9392505050565b80356001600160a01b0381168114610d2a575f80fd5b919050565b5f8060408385031215610d40575f80fd5b82359150610d5060208401610d14565b90509250929050565b5f60208284031215610d69575f80fd5b5035919050565b5f60208284031215610d80575f80fd5b610d0d82610d14565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b6001600160a01b03841681526060602082018190525f90610dda90830185610d89565b9050826040830152949350505050565b5f60208284031215610dfa575f80fd5b5051919050565b8581526001600160a01b038516602082015260a0604082018190525f90610e2a90830186610d89565b606083019490945250608001529392505050565b634e487b7160e01b5f52601160045260245ffd5b5f60018201610e6357610e63610e3e565b5060010190565b808202811582820484141761064d5761064d610e3e565b5f82610e9b57634e487b7160e01b5f52601260045260245ffd5b500490565b8181038181111561064d5761064d610e3e56fea2646970667358221220dbedf7e697da0d04dd0e2f34d2db6054a668f73d998ec856ef062a1ab1a1b41464736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_DEFAULT_BASE_COMMISSION_RATE = "DEFAULT_BASE_COMMISSION_RATE";

    public static final String FUNC_BASECOMMISSIONRATE = "baseCommissionRate";

    public static final String FUNC_BUYNFT = "buyNFT";

    public static final String FUNC_CALCULATEFEE = "calculateFee";

    public static final String FUNC_ITEMCOUNT = "itemCount";

    public static final String FUNC_ITEMS = "items";

    public static final String FUNC_LOYALTYPROGRAM = "loyaltyProgram";

    public static final String FUNC_MINTANDLIST = "mintAndList";

    public static final String FUNC_NFTCONTRACT = "nftContract";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETLOYALTYPROGRAM = "setLoyaltyProgram";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPDATECOMMISSIONRATE = "updateCommissionRate";

    public static final String FUNC_WITHDRAWFEES = "withdrawFees";

    public static final Event COMMISSIONRATEUPDATED_EVENT = new Event("CommissionRateUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event LISTINGCANCELLED_EVENT = new Event("ListingCancelled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event LISTINGUPDATED_EVENT = new Event("ListingUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event LOYALTYPROGRAMUPDATED_EVENT = new Event("LoyaltyProgramUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event NFTMINTEDANDLISTED_EVENT = new Event("NFTMintedAndListed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NFTSOLD_EVENT = new Event("NFTSold", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event WITHDRAWAL_EVENT = new Event("Withdrawal", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected EnergyMarketplace(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EnergyMarketplace(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EnergyMarketplace(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EnergyMarketplace(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CommissionRateUpdatedEventResponse> getCommissionRateUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(COMMISSIONRATEUPDATED_EVENT, transactionReceipt);
        ArrayList<CommissionRateUpdatedEventResponse> responses = new ArrayList<CommissionRateUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CommissionRateUpdatedEventResponse typedResponse = new CommissionRateUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newFeePercentage = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CommissionRateUpdatedEventResponse getCommissionRateUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(COMMISSIONRATEUPDATED_EVENT, log);
        CommissionRateUpdatedEventResponse typedResponse = new CommissionRateUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.newFeePercentage = (Uint256) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<CommissionRateUpdatedEventResponse> commissionRateUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCommissionRateUpdatedEventFromLog(log));
    }

    public Flowable<CommissionRateUpdatedEventResponse> commissionRateUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COMMISSIONRATEUPDATED_EVENT));
        return commissionRateUpdatedEventFlowable(filter);
    }

    public static List<ListingCancelledEventResponse> getListingCancelledEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LISTINGCANCELLED_EVENT, transactionReceipt);
        ArrayList<ListingCancelledEventResponse> responses = new ArrayList<ListingCancelledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ListingCancelledEventResponse typedResponse = new ListingCancelledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ListingCancelledEventResponse getListingCancelledEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LISTINGCANCELLED_EVENT, log);
        ListingCancelledEventResponse typedResponse = new ListingCancelledEventResponse();
        typedResponse.log = log;
        typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<ListingCancelledEventResponse> listingCancelledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getListingCancelledEventFromLog(log));
    }

    public Flowable<ListingCancelledEventResponse> listingCancelledEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LISTINGCANCELLED_EVENT));
        return listingCancelledEventFlowable(filter);
    }

    public static List<ListingUpdatedEventResponse> getListingUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LISTINGUPDATED_EVENT, transactionReceipt);
        ArrayList<ListingUpdatedEventResponse> responses = new ArrayList<ListingUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ListingUpdatedEventResponse typedResponse = new ListingUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.newPrice = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ListingUpdatedEventResponse getListingUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LISTINGUPDATED_EVENT, log);
        ListingUpdatedEventResponse typedResponse = new ListingUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse.newPrice = (Uint256) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<ListingUpdatedEventResponse> listingUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getListingUpdatedEventFromLog(log));
    }

    public Flowable<ListingUpdatedEventResponse> listingUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LISTINGUPDATED_EVENT));
        return listingUpdatedEventFlowable(filter);
    }

    public static List<LoyaltyProgramUpdatedEventResponse> getLoyaltyProgramUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LOYALTYPROGRAMUPDATED_EVENT, transactionReceipt);
        ArrayList<LoyaltyProgramUpdatedEventResponse> responses = new ArrayList<LoyaltyProgramUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoyaltyProgramUpdatedEventResponse typedResponse = new LoyaltyProgramUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newLoyaltyProgram = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static LoyaltyProgramUpdatedEventResponse getLoyaltyProgramUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LOYALTYPROGRAMUPDATED_EVENT, log);
        LoyaltyProgramUpdatedEventResponse typedResponse = new LoyaltyProgramUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.newLoyaltyProgram = (Address) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<LoyaltyProgramUpdatedEventResponse> loyaltyProgramUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getLoyaltyProgramUpdatedEventFromLog(log));
    }

    public Flowable<LoyaltyProgramUpdatedEventResponse> loyaltyProgramUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOYALTYPROGRAMUPDATED_EVENT));
        return loyaltyProgramUpdatedEventFlowable(filter);
    }

    public static List<NFTMintedAndListedEventResponse> getNFTMintedAndListedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NFTMINTEDANDLISTED_EVENT, transactionReceipt);
        ArrayList<NFTMintedAndListedEventResponse> responses = new ArrayList<NFTMintedAndListedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NFTMintedAndListedEventResponse typedResponse = new NFTMintedAndListedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.seller = (Address) eventValues.getNonIndexedValues().get(1);
            typedResponse.ipfsHash = (Utf8String) eventValues.getNonIndexedValues().get(2);
            typedResponse.energyValue = (Uint256) eventValues.getNonIndexedValues().get(3);
            typedResponse.price = (Uint256) eventValues.getNonIndexedValues().get(4);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NFTMintedAndListedEventResponse getNFTMintedAndListedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NFTMINTEDANDLISTED_EVENT, log);
        NFTMintedAndListedEventResponse typedResponse = new NFTMintedAndListedEventResponse();
        typedResponse.log = log;
        typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse.seller = (Address) eventValues.getNonIndexedValues().get(1);
        typedResponse.ipfsHash = (Utf8String) eventValues.getNonIndexedValues().get(2);
        typedResponse.energyValue = (Uint256) eventValues.getNonIndexedValues().get(3);
        typedResponse.price = (Uint256) eventValues.getNonIndexedValues().get(4);
        return typedResponse;
    }

    public Flowable<NFTMintedAndListedEventResponse> nFTMintedAndListedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNFTMintedAndListedEventFromLog(log));
    }

    public Flowable<NFTMintedAndListedEventResponse> nFTMintedAndListedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NFTMINTEDANDLISTED_EVENT));
        return nFTMintedAndListedEventFlowable(filter);
    }

    public static List<NFTSoldEventResponse> getNFTSoldEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NFTSOLD_EVENT, transactionReceipt);
        ArrayList<NFTSoldEventResponse> responses = new ArrayList<NFTSoldEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NFTSoldEventResponse typedResponse = new NFTSoldEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.seller = (Address) eventValues.getNonIndexedValues().get(1);
            typedResponse.buyer = (Address) eventValues.getNonIndexedValues().get(2);
            typedResponse.price = (Uint256) eventValues.getNonIndexedValues().get(3);
            typedResponse.fee = (Uint256) eventValues.getNonIndexedValues().get(4);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NFTSoldEventResponse getNFTSoldEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NFTSOLD_EVENT, log);
        NFTSoldEventResponse typedResponse = new NFTSoldEventResponse();
        typedResponse.log = log;
        typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse.seller = (Address) eventValues.getNonIndexedValues().get(1);
        typedResponse.buyer = (Address) eventValues.getNonIndexedValues().get(2);
        typedResponse.price = (Uint256) eventValues.getNonIndexedValues().get(3);
        typedResponse.fee = (Uint256) eventValues.getNonIndexedValues().get(4);
        return typedResponse;
    }

    public Flowable<NFTSoldEventResponse> nFTSoldEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNFTSoldEventFromLog(log));
    }

    public Flowable<NFTSoldEventResponse> nFTSoldEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NFTSOLD_EVENT));
        return nFTSoldEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
        typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<WithdrawalEventResponse> getWithdrawalEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(WITHDRAWAL_EVENT, transactionReceipt);
        ArrayList<WithdrawalEventResponse> responses = new ArrayList<WithdrawalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            WithdrawalEventResponse typedResponse = new WithdrawalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static WithdrawalEventResponse getWithdrawalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(WITHDRAWAL_EVENT, log);
        WithdrawalEventResponse typedResponse = new WithdrawalEventResponse();
        typedResponse.log = log;
        typedResponse.recipient = (Address) eventValues.getNonIndexedValues().get(0);
        typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<WithdrawalEventResponse> withdrawalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getWithdrawalEventFromLog(log));
    }

    public Flowable<WithdrawalEventResponse> withdrawalEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(WITHDRAWAL_EVENT));
        return withdrawalEventFlowable(filter);
    }

    public RemoteFunctionCall<Uint8> DEFAULT_BASE_COMMISSION_RATE() {
        final Function function = new Function(FUNC_DEFAULT_BASE_COMMISSION_RATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Uint8> baseCommissionRate() {
        final Function function = new Function(FUNC_BASECOMMISSIONRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> buyNFT(Uint256 _tokenId, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BUYNFT, 
                Arrays.<Type>asList(_tokenId), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<Uint256> calculateFee(Uint256 price, Address seller) {
        final Function function = new Function(FUNC_CALCULATEFEE, 
                Arrays.<Type>asList(price, seller), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Uint256> itemCount() {
        final Function function = new Function(FUNC_ITEMCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Tuple5<Uint256, Uint256, Uint256, Address, Bool>> items(
            Uint256 param0) {
        final Function function = new Function(FUNC_ITEMS, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple5<Uint256, Uint256, Uint256, Address, Bool>>(function,
                new Callable<Tuple5<Uint256, Uint256, Uint256, Address, Bool>>() {
                    @Override
                    public Tuple5<Uint256, Uint256, Uint256, Address, Bool> call() throws
                            Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<Uint256, Uint256, Uint256, Address, Bool>(
                                (Uint256) results.get(0), 
                                (Uint256) results.get(1), 
                                (Uint256) results.get(2), 
                                (Address) results.get(3), 
                                (Bool) results.get(4));
                    }
                });
    }

    public RemoteFunctionCall<Address> loyaltyProgram() {
        final Function function = new Function(FUNC_LOYALTYPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mintAndList(Utf8String _tokenURI,
            Uint256 _energyAmount, Uint256 _price) {
        final Function function = new Function(
                FUNC_MINTANDLIST, 
                Arrays.<Type>asList(_tokenURI, _energyAmount, _price), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Address> nftContract() {
        final Function function = new Function(FUNC_NFTCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Address> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setLoyaltyProgram(Address _loyaltyProgram) {
        final Function function = new Function(
                FUNC_SETLOYALTYPROGRAM, 
                Arrays.<Type>asList(_loyaltyProgram), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(Address newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(newOwner), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateCommissionRate(Uint8 _newCommissionRate) {
        final Function function = new Function(
                FUNC_UPDATECOMMISSIONRATE, 
                Arrays.<Type>asList(_newCommissionRate), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawFees(Uint256 _amount) {
        final Function function = new Function(
                FUNC_WITHDRAWFEES, 
                Arrays.<Type>asList(_amount), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static EnergyMarketplace load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new EnergyMarketplace(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EnergyMarketplace load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EnergyMarketplace(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EnergyMarketplace load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new EnergyMarketplace(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EnergyMarketplace load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EnergyMarketplace(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EnergyMarketplace> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, Address _nftContract,
            Address _loyaltyProgram) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_nftContract, _loyaltyProgram));
        return deployRemoteCall(EnergyMarketplace.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<EnergyMarketplace> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            Address _nftContract, Address _loyaltyProgram) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_nftContract, _loyaltyProgram));
        return deployRemoteCall(EnergyMarketplace.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<EnergyMarketplace> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, Address _nftContract,
            Address _loyaltyProgram) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_nftContract, _loyaltyProgram));
        return deployRemoteCall(EnergyMarketplace.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<EnergyMarketplace> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            Address _nftContract, Address _loyaltyProgram) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_nftContract, _loyaltyProgram));
        return deployRemoteCall(EnergyMarketplace.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class CommissionRateUpdatedEventResponse extends BaseEventResponse {
        public Uint256 newFeePercentage;
    }

    public static class ListingCancelledEventResponse extends BaseEventResponse {
        public Uint256 tokenId;
    }

    public static class ListingUpdatedEventResponse extends BaseEventResponse {
        public Uint256 tokenId;

        public Uint256 newPrice;
    }

    public static class LoyaltyProgramUpdatedEventResponse extends BaseEventResponse {
        public Address newLoyaltyProgram;
    }

    public static class NFTMintedAndListedEventResponse extends BaseEventResponse {
        public Uint256 tokenId;

        public Address seller;

        public Utf8String ipfsHash;

        public Uint256 energyValue;

        public Uint256 price;
    }

    public static class NFTSoldEventResponse extends BaseEventResponse {
        public Uint256 tokenId;

        public Address seller;

        public Address buyer;

        public Uint256 price;

        public Uint256 fee;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public Address previousOwner;

        public Address newOwner;
    }

    public static class WithdrawalEventResponse extends BaseEventResponse {
        public Address recipient;

        public Uint256 amount;
    }
}
