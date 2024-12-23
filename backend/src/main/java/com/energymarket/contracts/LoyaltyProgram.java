package com.energymarket.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
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
import org.web3j.tuples.generated.Tuple2;
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
public class LoyaltyProgram extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b50338061003557604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b61003e81610147565b506040805180820182526103e88152600560208083019182526001805480820182555f828152945160029182027fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf68181019290925594517fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf7958601805460ff92831660ff19918216179091558851808a018a526113888152600881880190815286548088018855878b529151918602808601929092555190880180549184169183169190911790558851808a019099526127108952600a958901958652845480860186559490975296519290910290810191909155905191018054919093169116179055610196565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b610acf806101a35f395ff3fe608060405234801561000f575f80fd5b50600436106100cb575f3560e01c806390e5c1f611610088578063d04c911511610063578063d04c9115146101aa578063ed68371e146101d2578063f2fde38b146101e5578063fa84dc2f146101f8575f80fd5b806390e5c1f614610171578063b774353014610184578063c0d6dc1e14610197575f80fd5b80630afd5d10146100cf578063536fff6c146100e45780636019044c1461011b5780637099d8e41461012e578063715018a61461014f5780638da5cb5b14610157575b5f80fd5b6100e26100dd366004610906565b610222565b005b6101066100f2366004610938565b60036020525f908152604090205460ff1681565b60405190151581526020015b60405180910390f35b6100e2610129366004610968565b61036d565b61014161013c36600461099a565b6104ad565b604051908152602001610112565b6100e2610559565b5f546040516001600160a01b039091168152602001610112565b6100e261017f366004610938565b61056c565b6100e2610192366004610938565b6105c7565b6100e26101a53660046109c4565b61061f565b6101416101b8366004610938565b6001600160a01b03165f9081526002602052604090205490565b6100e26101e036600461099a565b610709565b6100e26101f3366004610938565b61081f565b61020b610206366004610906565b61085c565b6040805192835260ff909116602083015201610112565b61022a61088b565b60015481106102755760405162461bcd60e51b8152602060048201526012602482015271092dcecc2d8d2c840e8d2cae440d2dcc8caf60731b60448201526064015b60405180910390fd5b805b600180546102859190610a15565b8110156102fb5760016102988282610a28565b815481106102a8576102a8610a3b565b905f5260205f209060020201600182815481106102c7576102c7610a3b565b5f91825260209091208254600290920201908155600191820154908201805460ff191660ff90921691909117905501610277565b50600180548061030d5761030d610a4f565b5f8281526020812060025f1990930192830201908155600101805460ff1916905590556040517f901cef785a7f00f35091c5e0819b739ac5569d758473279bafa09d5d4818ac13906103629083815260200190565b60405180910390a150565b61037561088b565b60015483106103bb5760405162461bcd60e51b8152602060048201526012602482015271092dcecc2d8d2c840e8d2cae440d2dcc8caf60731b604482015260640161026c565b60648160ff16111561040f5760405162461bcd60e51b815260206004820152601b60248201527f446973636f756e742063616e6e6f742065786365656420313030250000000000604482015260640161026c565b60405180604001604052808381526020018260ff168152506001848154811061043a5761043a610a3b565b5f918252602091829020835160029290920201908155918101516001909201805460ff191660ff938416179055604080518681529182018590529183168183015290517f1c5236ccbb80189bd47eb541498561502b847082b2e7e6a0fc850cc645323b11916060908290030190a1505050565b5f80805b60015481101561052057600181815481106104ce576104ce610a3b565b905f5260205f2090600202015f0154851061051357600181815481106104f6576104f6610a3b565b5f91825260209091206001600290920201015460ff169150610518565b610520565b6001016104b1565b50606461052d8282610a15565b61053b606460ff8716610a63565b6105459190610a63565b61054f9190610a7a565b9150505b92915050565b61056161088b565b61056a5f6108b7565b565b61057461088b565b6001600160a01b0381165f81815260036020908152604091829020805460ff1916600117905590519182527f6acfd92212f0ec670af78f8ba2273e4a85ac17023475650c25be0440602bff2d9101610362565b6105cf61088b565b6001600160a01b0381165f81815260036020908152604091829020805460ff1916905590519182527f0491b0192bae7692618bfa4eff3f4942d2d8ec3300ef2e63d325b45e937c4ff19101610362565b335f9081526003602052604090205460ff168061064557505f546001600160a01b031633145b6106825760405162461bcd60e51b815260206004820152600e60248201526d139bdd08185d5d1a1bdc9a5e995960921b604482015260640161026c565b6001600160a01b0382165f908152600260205260408120805463ffffffff841692906106af908490610a28565b90915550506001600160a01b0382165f81815260026020908152604091829020548251938452908301527fb2a023484df80a89f6add7acfba71534e4f54c5854289a005605693fc7c81e6b91015b60405180910390a15050565b61071161088b565b60648160ff1611156107655760405162461bcd60e51b815260206004820152601b60248201527f446973636f756e742063616e6e6f742065786365656420313030250000000000604482015260640161026c565b60408051808201825283815260ff83811660208084018281526001805480820182555f9190915294517fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf6600290960295860155517fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf7909401805460ff191694909316939093179091558251858152918201527ff5405f744f7cc4dfdfa11eaa1bdfe3783c75424a0072843abc46935eca94aa3991016106fd565b61082761088b565b6001600160a01b03811661085057604051631e4fbdf760e01b81525f600482015260240161026c565b610859816108b7565b50565b6001818154811061086b575f80fd5b5f9182526020909120600290910201805460019091015490915060ff1682565b5f546001600160a01b0316331461056a5760405163118cdaa760e01b815233600482015260240161026c565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b5f60208284031215610916575f80fd5b5035919050565b80356001600160a01b0381168114610933575f80fd5b919050565b5f60208284031215610948575f80fd5b6109518261091d565b9392505050565b803560ff81168114610933575f80fd5b5f805f6060848603121561097a575f80fd5b833592506020840135915061099160408501610958565b90509250925092565b5f80604083850312156109ab575f80fd5b823591506109bb60208401610958565b90509250929050565b5f80604083850312156109d5575f80fd5b6109de8361091d565b9150602083013563ffffffff811681146109f6575f80fd5b809150509250929050565b634e487b7160e01b5f52601160045260245ffd5b8181038181111561055357610553610a01565b8082018082111561055357610553610a01565b634e487b7160e01b5f52603260045260245ffd5b634e487b7160e01b5f52603160045260245ffd5b808202811582820484141761055357610553610a01565b5f82610a9457634e487b7160e01b5f52601260045260245ffd5b50049056fea26469706673582212207055be01432ef904d5b16e0fceff646742de17d44bd08087458c30386a19cec964736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDAUTHORIZECALLER = "addAuthorizeCaller";

    public static final String FUNC_ADDDISCOUNTTIER = "addDiscountTier";

    public static final String FUNC_ADDLOYALTYPOINTS = "addLoyaltyPoints";

    public static final String FUNC_AUTHORIZEDCALLERS = "authorizedCallers";

    public static final String FUNC_DISCOUNTTIERS = "discountTiers";

    public static final String FUNC_GETCOMMISSIONRATE = "getCommissionRate";

    public static final String FUNC_GETLOYALTYPOINTS = "getLoyaltyPoints";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REMOVEDISCOUNTTIER = "removeDiscountTier";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REVOKECALLER = "revokeCaller";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPDATEDISCOUNTTIER = "updateDiscountTier";

    public static final Event CALLERAUTHORIZED_EVENT = new Event("CallerAuthorized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event CALLERREVOKED_EVENT = new Event("CallerRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event DISCOUNTTIERADDED_EVENT = new Event("DiscountTierAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event DISCOUNTTIERREMOVED_EVENT = new Event("DiscountTierRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event DISCOUNTTIERUPDATED_EVENT = new Event("DiscountTierUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event LOYALTYPOINTSADDED_EVENT = new Event("LoyaltyPointsAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected LoyaltyProgram(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LoyaltyProgram(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LoyaltyProgram(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LoyaltyProgram(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CallerAuthorizedEventResponse> getCallerAuthorizedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CALLERAUTHORIZED_EVENT, transactionReceipt);
        ArrayList<CallerAuthorizedEventResponse> responses = new ArrayList<CallerAuthorizedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CallerAuthorizedEventResponse typedResponse = new CallerAuthorizedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.caller = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CallerAuthorizedEventResponse getCallerAuthorizedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CALLERAUTHORIZED_EVENT, log);
        CallerAuthorizedEventResponse typedResponse = new CallerAuthorizedEventResponse();
        typedResponse.log = log;
        typedResponse.caller = (Address) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<CallerAuthorizedEventResponse> callerAuthorizedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCallerAuthorizedEventFromLog(log));
    }

    public Flowable<CallerAuthorizedEventResponse> callerAuthorizedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CALLERAUTHORIZED_EVENT));
        return callerAuthorizedEventFlowable(filter);
    }

    public static List<CallerRevokedEventResponse> getCallerRevokedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CALLERREVOKED_EVENT, transactionReceipt);
        ArrayList<CallerRevokedEventResponse> responses = new ArrayList<CallerRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CallerRevokedEventResponse typedResponse = new CallerRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.caller = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CallerRevokedEventResponse getCallerRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CALLERREVOKED_EVENT, log);
        CallerRevokedEventResponse typedResponse = new CallerRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.caller = (Address) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<CallerRevokedEventResponse> callerRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCallerRevokedEventFromLog(log));
    }

    public Flowable<CallerRevokedEventResponse> callerRevokedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CALLERREVOKED_EVENT));
        return callerRevokedEventFlowable(filter);
    }

    public static List<DiscountTierAddedEventResponse> getDiscountTierAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DISCOUNTTIERADDED_EVENT, transactionReceipt);
        ArrayList<DiscountTierAddedEventResponse> responses = new ArrayList<DiscountTierAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DiscountTierAddedEventResponse typedResponse = new DiscountTierAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.points = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.discountPercentage = (Uint8) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DiscountTierAddedEventResponse getDiscountTierAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DISCOUNTTIERADDED_EVENT, log);
        DiscountTierAddedEventResponse typedResponse = new DiscountTierAddedEventResponse();
        typedResponse.log = log;
        typedResponse.points = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse.discountPercentage = (Uint8) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<DiscountTierAddedEventResponse> discountTierAddedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDiscountTierAddedEventFromLog(log));
    }

    public Flowable<DiscountTierAddedEventResponse> discountTierAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISCOUNTTIERADDED_EVENT));
        return discountTierAddedEventFlowable(filter);
    }

    public static List<DiscountTierRemovedEventResponse> getDiscountTierRemovedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DISCOUNTTIERREMOVED_EVENT, transactionReceipt);
        ArrayList<DiscountTierRemovedEventResponse> responses = new ArrayList<DiscountTierRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DiscountTierRemovedEventResponse typedResponse = new DiscountTierRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DiscountTierRemovedEventResponse getDiscountTierRemovedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DISCOUNTTIERREMOVED_EVENT, log);
        DiscountTierRemovedEventResponse typedResponse = new DiscountTierRemovedEventResponse();
        typedResponse.log = log;
        typedResponse.index = (Uint256) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<DiscountTierRemovedEventResponse> discountTierRemovedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDiscountTierRemovedEventFromLog(log));
    }

    public Flowable<DiscountTierRemovedEventResponse> discountTierRemovedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISCOUNTTIERREMOVED_EVENT));
        return discountTierRemovedEventFlowable(filter);
    }

    public static List<DiscountTierUpdatedEventResponse> getDiscountTierUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DISCOUNTTIERUPDATED_EVENT, transactionReceipt);
        ArrayList<DiscountTierUpdatedEventResponse> responses = new ArrayList<DiscountTierUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DiscountTierUpdatedEventResponse typedResponse = new DiscountTierUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.points = (Uint256) eventValues.getNonIndexedValues().get(1);
            typedResponse.discountPercentage = (Uint8) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DiscountTierUpdatedEventResponse getDiscountTierUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DISCOUNTTIERUPDATED_EVENT, log);
        DiscountTierUpdatedEventResponse typedResponse = new DiscountTierUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.index = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse.points = (Uint256) eventValues.getNonIndexedValues().get(1);
        typedResponse.discountPercentage = (Uint8) eventValues.getNonIndexedValues().get(2);
        return typedResponse;
    }

    public Flowable<DiscountTierUpdatedEventResponse> discountTierUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDiscountTierUpdatedEventFromLog(log));
    }

    public Flowable<DiscountTierUpdatedEventResponse> discountTierUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISCOUNTTIERUPDATED_EVENT));
        return discountTierUpdatedEventFlowable(filter);
    }

    public static List<LoyaltyPointsAddedEventResponse> getLoyaltyPointsAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LOYALTYPOINTSADDED_EVENT, transactionReceipt);
        ArrayList<LoyaltyPointsAddedEventResponse> responses = new ArrayList<LoyaltyPointsAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoyaltyPointsAddedEventResponse typedResponse = new LoyaltyPointsAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.points = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static LoyaltyPointsAddedEventResponse getLoyaltyPointsAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LOYALTYPOINTSADDED_EVENT, log);
        LoyaltyPointsAddedEventResponse typedResponse = new LoyaltyPointsAddedEventResponse();
        typedResponse.log = log;
        typedResponse.user = (Address) eventValues.getNonIndexedValues().get(0);
        typedResponse.points = (Uint256) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<LoyaltyPointsAddedEventResponse> loyaltyPointsAddedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getLoyaltyPointsAddedEventFromLog(log));
    }

    public Flowable<LoyaltyPointsAddedEventResponse> loyaltyPointsAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOYALTYPOINTSADDED_EVENT));
        return loyaltyPointsAddedEventFlowable(filter);
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

    public RemoteFunctionCall<TransactionReceipt> addAuthorizeCaller(Address caller) {
        final Function function = new Function(
                FUNC_ADDAUTHORIZECALLER, 
                Arrays.<Type>asList(caller), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addDiscountTier(Uint256 points,
            Uint8 discountPercentage) {
        final Function function = new Function(
                FUNC_ADDDISCOUNTTIER, 
                Arrays.<Type>asList(points, discountPercentage), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addLoyaltyPoints(Address user, Uint32 points) {
        final Function function = new Function(
                FUNC_ADDLOYALTYPOINTS, 
                Arrays.<Type>asList(user, points), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Bool> authorizedCallers(Address param0) {
        final Function function = new Function(FUNC_AUTHORIZEDCALLERS, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Tuple2<Uint256, Uint8>> discountTiers(Uint256 param0) {
        final Function function = new Function(FUNC_DISCOUNTTIERS, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
        return new RemoteFunctionCall<Tuple2<Uint256, Uint8>>(function,
                new Callable<Tuple2<Uint256, Uint8>>() {
                    @Override
                    public Tuple2<Uint256, Uint8> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Uint256, Uint8>(
                                (Uint256) results.get(0), 
                                (Uint8) results.get(1));
                    }
                });
    }

    public RemoteFunctionCall<Uint256> getCommissionRate(Uint256 loyaltyPoints,
            Uint8 baseCommissionRate) {
        final Function function = new Function(FUNC_GETCOMMISSIONRATE, 
                Arrays.<Type>asList(loyaltyPoints, baseCommissionRate), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Uint256> getLoyaltyPoints(Address user) {
        final Function function = new Function(FUNC_GETLOYALTYPOINTS, 
                Arrays.<Type>asList(user), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Address> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeDiscountTier(Uint256 index) {
        final Function function = new Function(
                FUNC_REMOVEDISCOUNTTIER, 
                Arrays.<Type>asList(index), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeCaller(Address caller) {
        final Function function = new Function(
                FUNC_REVOKECALLER, 
                Arrays.<Type>asList(caller), 
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

    public RemoteFunctionCall<TransactionReceipt> updateDiscountTier(Uint256 index, Uint256 points,
            Uint8 discountPercentage) {
        final Function function = new Function(
                FUNC_UPDATEDISCOUNTTIER, 
                Arrays.<Type>asList(index, points, discountPercentage), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static LoyaltyProgram load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new LoyaltyProgram(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LoyaltyProgram load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LoyaltyProgram(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LoyaltyProgram load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new LoyaltyProgram(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LoyaltyProgram load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LoyaltyProgram(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LoyaltyProgram> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LoyaltyProgram.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<LoyaltyProgram> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LoyaltyProgram.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<LoyaltyProgram> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LoyaltyProgram.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<LoyaltyProgram> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LoyaltyProgram.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class CallerAuthorizedEventResponse extends BaseEventResponse {
        public Address caller;
    }

    public static class CallerRevokedEventResponse extends BaseEventResponse {
        public Address caller;
    }

    public static class DiscountTierAddedEventResponse extends BaseEventResponse {
        public Uint256 points;

        public Uint8 discountPercentage;
    }

    public static class DiscountTierRemovedEventResponse extends BaseEventResponse {
        public Uint256 index;
    }

    public static class DiscountTierUpdatedEventResponse extends BaseEventResponse {
        public Uint256 index;

        public Uint256 points;

        public Uint8 discountPercentage;
    }

    public static class LoyaltyPointsAddedEventResponse extends BaseEventResponse {
        public Address user;

        public Uint256 points;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public Address previousOwner;

        public Address newOwner;
    }
}
