package com.energymarket.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class ILoyaltyProgram extends Contract {
    public static final String BINARY = "";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDDISCOUNTTIER = "addDiscountTier";

    public static final String FUNC_ADDLOYALTYPOINTS = "addLoyaltyPoints";

    public static final String FUNC_GETCOMMISSIONRATE = "getCommissionRate";

    public static final String FUNC_GETLOYALTYPOINTS = "getLoyaltyPoints";

    public static final String FUNC_REMOVEDISCOUNTTIER = "removeDiscountTier";

    public static final String FUNC_UPDATEDISCOUNTTIER = "updateDiscountTier";

    @Deprecated
    protected ILoyaltyProgram(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ILoyaltyProgram(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ILoyaltyProgram(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ILoyaltyProgram(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
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

    public RemoteFunctionCall<TransactionReceipt> removeDiscountTier(Uint256 index) {
        final Function function = new Function(
                FUNC_REMOVEDISCOUNTTIER, 
                Arrays.<Type>asList(index), 
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
    public static ILoyaltyProgram load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new ILoyaltyProgram(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ILoyaltyProgram load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ILoyaltyProgram(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ILoyaltyProgram load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new ILoyaltyProgram(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ILoyaltyProgram load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ILoyaltyProgram(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ILoyaltyProgram> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ILoyaltyProgram.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ILoyaltyProgram> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ILoyaltyProgram.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<ILoyaltyProgram> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ILoyaltyProgram.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ILoyaltyProgram> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ILoyaltyProgram.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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
}
