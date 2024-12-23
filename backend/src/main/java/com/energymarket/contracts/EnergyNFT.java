package com.energymarket.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
public class EnergyNFT extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b50336040518060400160405280600a815260200169115b995c99de4813919560b21b815250604051806040016040528060048152602001631153919560e21b815250815f908161005f9190610194565b50600161006c8282610194565b5050506001600160a01b03811661009c57604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b6100a5816100ab565b5061024e565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b634e487b7160e01b5f52604160045260245ffd5b600181811c9082168061012457607f821691505b60208210810361014257634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111561018f57805f5260205f20601f840160051c8101602085101561016d5750805b601f840160051c820191505b8181101561018c575f8155600101610179565b50505b505050565b81516001600160401b038111156101ad576101ad6100fc565b6101c1816101bb8454610110565b84610148565b6020601f8211600181146101f3575f83156101dc5750848201515b5f19600385901b1c1916600184901b17845561018c565b5f84815260208120601f198516915b828110156102225787850151825560209485019460019092019101610202565b508482101561023f57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b611a068061025b5f395ff3fe608060405234801561000f575f80fd5b5060043610610153575f3560e01c80637e679c28116100bf578063ba7aef4311610079578063ba7aef4314610301578063c87b56dd14610314578063daa17f4914610327578063db1f1c121461033a578063e985e9c51461034d578063f2fde38b14610360575f80fd5b80637e679c28146102875780638da5cb5b146102af57806395d89b41146102c0578063a22cb465146102c8578063b47cc556146102db578063b88d4fde146102ee575f80fd5b80635fd946cf116101105780635fd946cf146101fa5780636352211e1461020d57806370a0823114610220578063715018a6146102415780637b82a1fb146102495780637e43ed4314610268575f80fd5b806301ffc9a71461015757806306fdde031461017f578063081812fc14610194578063095ea7b3146101bf57806323b872dd146101d457806342842e0e146101e7575b5f80fd5b61016a610165366004611498565b610373565b60405190151581526020015b60405180910390f35b61018761039d565b60405161017691906114e1565b6101a76101a23660046114f3565b61042c565b6040516001600160a01b039091168152602001610176565b6101d26101cd366004611525565b610453565b005b6101d26101e236600461154d565b610462565b6101d26101f536600461154d565b6104f0565b6101d2610208366004611525565b61050f565b6101a761021b3660046114f3565b6105c7565b61023361022e366004611587565b6105d1565b604051908152602001610176565b6101d2610616565b6102336102573660046114f3565b600a6020525f908152604090205481565b610233610276366004611587565b60096020525f908152604090205481565b610233610295366004611587565b6001600160a01b03165f9081526009602052604090205490565b6007546001600160a01b03166101a7565b610187610629565b6101d26102d63660046115a0565b610638565b6101d26102e9366004611587565b610643565b6101d26102fc366004611664565b6106f5565b61023361030f3660046116db565b61070d565b6101876103223660046114f3565b6108c1565b600b546101a7906001600160a01b031681565b6101d261034836600461154d565b6109cc565b61016a61035b366004611742565b610bd5565b6101d261036e366004611587565b610c02565b5f6001600160e01b03198216632483248360e11b1480610397575061039782610c3f565b92915050565b60605f80546103ab90611773565b80601f01602080910402602001604051908101604052809291908181526020018280546103d790611773565b80156104225780601f106103f957610100808354040283529160200191610422565b820191905f5260205f20905b81548152906001019060200180831161040557829003601f168201915b5050505050905090565b5f61043682610c8e565b505f828152600460205260409020546001600160a01b0316610397565b61045e828233610cc6565b5050565b6001600160a01b03821661049057604051633250574960e11b81525f60048201526024015b60405180910390fd5b5f61049c838333610cd3565b9050836001600160a01b0316816001600160a01b0316146104ea576040516364283d7b60e01b81526001600160a01b0380861660048301526024820184905282166044820152606401610487565b50505050565b61050a83838360405180602001604052805f8152506106f5565b505050565b610517610dc5565b6001600160a01b0382165f908152600960205260408120805483929061053e9084906117bf565b9091555050604080516001600160a01b0384168152602081018390527f8837e93703fb5c8f36143e5f52f7d32d649f3e0742d62e216518bba4cf44f78a910160405180910390a16001600160a01b0382165f81815260096020908152604091829020548251938452908301525f805160206119b183398151915291015b60405180910390a15050565b5f61039782610c8e565b5f6001600160a01b0382166105fb576040516322718ad960e21b81525f6004820152602401610487565b506001600160a01b03165f9081526003602052604090205490565b61061e610dc5565b6106275f610df2565b565b6060600180546103ab90611773565b61045e338383610e43565b61064b610dc5565b6001600160a01b0381166106a15760405162461bcd60e51b815260206004820152601b60248201527f496e76616c6964206d61726b6574706c616365206164647265737300000000006044820152606401610487565b600b80546001600160a01b0319166001600160a01b0383169081179091556040519081527f3ff9503ebb449a398e05d1e0984e6bbebc0b2af8f06707fe1111bfc707a9adfe9060200160405180910390a150565b610700848484610462565b6104ea3385858585610ee1565b5f80821161076b5760405162461bcd60e51b815260206004820152602560248201527f456e657267792076616c75652073686f756c6420626520677265617465722074604482015264068616e20360dc1b6064820152608401610487565b6001600160a01b0384165f908152600960205260409020548211156107d25760405162461bcd60e51b815260206004820152601e60248201527f496e73756666696369656e7420656e657267792062616c616e636521212100006044820152606401610487565b60088054905f6107e1836117d2565b91905055506107f284600854611009565b6107fe60085484611022565b6008545f908152600a602090815260408083208590556001600160a01b03871683526009909152812080548492906108379084906117ea565b90915550506008546040517fb108e5b1943d3605128aeef65e7a6d8f31889717cbf208d81b6531f0bd33b3439161087191879087906117fd565b60405180910390a16001600160a01b0384165f81815260096020908152604091829020548251938452908301525f805160206119b1833981519152910160405180910390a1506008549392505050565b60606108cc82610c8e565b505f82815260066020526040812080546108e590611773565b80601f016020809104026020016040519081016040528092919081815260200182805461091190611773565b801561095c5780601f106109335761010080835404028352916020019161095c565b820191905f5260205f20905b81548152906001019060200180831161093f57829003601f168201915b505050505090505f61097860408051602081019091525f815290565b905080515f03610989575092915050565b8151156109bb5780826040516020016109a3929190611846565b60405160208183030381529060405292505050919050565b6109c48461106a565b949350505050565b600b546001600160a01b03163314610a365760405162461bcd60e51b815260206004820152602760248201527f4f6e6c79206d61726b6574706c6163652063616e2063616c6c207468697320666044820152663ab731ba34b7b760c91b6064820152608401610487565b816001600160a01b0316610a49826105c7565b6001600160a01b031614610ab35760405162461bcd60e51b815260206004820152602b60248201527f456e657267792063616e206f6e6c79206265207472616e73666572726564207460448201526a379027232a1037bbb732b960a91b6064820152608401610487565b5f818152600a602052604090205480610b195760405162461bcd60e51b815260206004820152602260248201527f4e6f20656e65726779206173736f63696174656420776974682074686973204e604482015261119560f21b6064820152608401610487565b6001600160a01b0383165f9081526009602052604081208054839290610b409084906117bf565b90915550505f828152600a602090815260408083208390556001600160a01b0387168084526009835292819020548151938452918301919091525f805160206119b1833981519152910160405180910390a16001600160a01b0383165f81815260096020908152604091829020548251938452908301525f805160206119b1833981519152910160405180910390a150505050565b6001600160a01b039182165f90815260056020908152604080832093909416825291909152205460ff1690565b610c0a610dc5565b6001600160a01b038116610c3357604051631e4fbdf760e01b81525f6004820152602401610487565b610c3c81610df2565b50565b5f6001600160e01b031982166380ac58cd60e01b1480610c6f57506001600160e01b03198216635b5e139f60e01b145b8061039757506301ffc9a760e01b6001600160e01b0319831614610397565b5f818152600260205260408120546001600160a01b03168061039757604051637e27328960e01b815260048101849052602401610487565b61050a83838360016110db565b5f828152600260205260408120546001600160a01b0390811690831615610cff57610cff8184866111df565b6001600160a01b03811615610d3957610d1a5f855f806110db565b6001600160a01b0381165f90815260036020526040902080545f190190555b6001600160a01b03851615610d67576001600160a01b0385165f908152600360205260409020805460010190555b5f8481526002602052604080822080546001600160a01b0319166001600160a01b0389811691821790925591518793918516917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4949350505050565b6007546001600160a01b031633146106275760405163118cdaa760e01b8152336004820152602401610487565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b6001600160a01b038216610e7557604051630b61174360e31b81526001600160a01b0383166004820152602401610487565b6001600160a01b038381165f81815260056020908152604080832094871680845294825291829020805460ff191686151590811790915591519182527f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31910160405180910390a3505050565b6001600160a01b0383163b1561100257604051630a85bd0160e11b81526001600160a01b0384169063150b7a0290610f2390889088908790879060040161185a565b6020604051808303815f875af1925050508015610f5d575060408051601f3d908101601f19168201909252610f5a91810190611896565b60015b610fc4573d808015610f8a576040519150601f19603f3d011682016040523d82523d5f602084013e610f8f565b606091505b5080515f03610fbc57604051633250574960e11b81526001600160a01b0385166004820152602401610487565b805181602001fd5b6001600160e01b03198116630a85bd0160e11b1461100057604051633250574960e11b81526001600160a01b0385166004820152602401610487565b505b5050505050565b61045e828260405180602001604052805f815250611243565b5f82815260066020526040902061103982826118f5565b506040518281527ff8e1a15aba9398e019f0b49df1a4fde98ee17ae345cb5f6b5e2c27f5033e8ce7906020016105bb565b606061107582610c8e565b505f61108b60408051602081019091525f815290565b90505f8151116110a95760405180602001604052805f8152506110d4565b806110b38461125a565b6040516020016110c4929190611846565b6040516020818303038152906040525b9392505050565b80806110ef57506001600160a01b03821615155b156111b0575f6110fe84610c8e565b90506001600160a01b0383161580159061112a5750826001600160a01b0316816001600160a01b031614155b801561113d575061113b8184610bd5565b155b156111665760405163a9fbf51f60e01b81526001600160a01b0384166004820152602401610487565b81156111ae5783856001600160a01b0316826001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a45b505b50505f90815260046020526040902080546001600160a01b0319166001600160a01b0392909216919091179055565b6111ea8383836112ea565b61050a576001600160a01b03831661121857604051637e27328960e01b815260048101829052602401610487565b60405163177e802f60e01b81526001600160a01b038316600482015260248101829052604401610487565b61124d838361134b565b61050a335f858585610ee1565b60605f611266836113ac565b60010190505f8167ffffffffffffffff811115611285576112856115d9565b6040519080825280601f01601f1916602001820160405280156112af576020820181803683370190505b5090508181016020015b5f19016f181899199a1a9b1b9c1cb0b131b232b360811b600a86061a8153600a85049450846112b957509392505050565b5f6001600160a01b038316158015906109c45750826001600160a01b0316846001600160a01b0316148061132357506113238484610bd5565b806109c45750505f908152600460205260409020546001600160a01b03908116911614919050565b6001600160a01b03821661137457604051633250574960e11b81525f6004820152602401610487565b5f61138083835f610cd3565b90506001600160a01b0381161561050a576040516339e3563760e11b81525f6004820152602401610487565b5f8072184f03e93ff9f4daa797ed6e38ed64bf6a1f0160401b83106113ea5772184f03e93ff9f4daa797ed6e38ed64bf6a1f0160401b830492506040015b6d04ee2d6d415b85acef81000000008310611416576d04ee2d6d415b85acef8100000000830492506020015b662386f26fc10000831061143457662386f26fc10000830492506010015b6305f5e100831061144c576305f5e100830492506008015b612710831061146057612710830492506004015b60648310611472576064830492506002015b600a83106103975760010192915050565b6001600160e01b031981168114610c3c575f80fd5b5f602082840312156114a8575f80fd5b81356110d481611483565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b602081525f6110d460208301846114b3565b5f60208284031215611503575f80fd5b5035919050565b80356001600160a01b0381168114611520575f80fd5b919050565b5f8060408385031215611536575f80fd5b61153f8361150a565b946020939093013593505050565b5f805f6060848603121561155f575f80fd5b6115688461150a565b92506115766020850161150a565b929592945050506040919091013590565b5f60208284031215611597575f80fd5b6110d48261150a565b5f80604083850312156115b1575f80fd5b6115ba8361150a565b9150602083013580151581146115ce575f80fd5b809150509250929050565b634e487b7160e01b5f52604160045260245ffd5b5f8067ffffffffffffffff841115611607576116076115d9565b50604051601f19601f85018116603f0116810181811067ffffffffffffffff82111715611636576116366115d9565b60405283815290508082840185101561164d575f80fd5b838360208301375f60208583010152509392505050565b5f805f8060808587031215611677575f80fd5b6116808561150a565b935061168e6020860161150a565b925060408501359150606085013567ffffffffffffffff8111156116b0575f80fd5b8501601f810187136116c0575f80fd5b6116cf878235602084016115ed565b91505092959194509250565b5f805f606084860312156116ed575f80fd5b6116f68461150a565b9250602084013567ffffffffffffffff811115611711575f80fd5b8401601f81018613611721575f80fd5b611730868235602084016115ed565b93969395505050506040919091013590565b5f8060408385031215611753575f80fd5b61175c8361150a565b915061176a6020840161150a565b90509250929050565b600181811c9082168061178757607f821691505b6020821081036117a557634e487b7160e01b5f52602260045260245ffd5b50919050565b634e487b7160e01b5f52601160045260245ffd5b80820180821115610397576103976117ab565b5f600182016117e3576117e36117ab565b5060010190565b81810381811115610397576103976117ab565b8381526001600160a01b03831660208201526060604082018190525f90611826908301846114b3565b95945050505050565b5f81518060208401855e5f93019283525090919050565b5f6109c4611854838661182f565b8461182f565b6001600160a01b03858116825284166020820152604081018390526080606082018190525f9061188c908301846114b3565b9695505050505050565b5f602082840312156118a6575f80fd5b81516110d481611483565b601f82111561050a57805f5260205f20601f840160051c810160208510156118d65750805b601f840160051c820191505b81811015611002575f81556001016118e2565b815167ffffffffffffffff81111561190f5761190f6115d9565b6119238161191d8454611773565b846118b1565b6020601f821160018114611955575f831561193e5750848201515b5f19600385901b1c1916600184901b178455611002565b5f84815260208120601f198516915b828110156119845787850151825560209485019460019092019101611964565b50848210156119a157868401515f19600387901b60f8161c191681555b50505050600190811b0190555056fe7040ffb20035add10a6c492892f76f199fdd6e02565bc4b5647e3879f634bce7a2646970667358221220abc32873e919e1850d7698802938b33e560a96150d5675149e9a611757cd432764736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_GETCURRENTENERGY = "getCurrentEnergy";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_MARKETPLACEADDRESS = "marketplaceAddress";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_PRODUCEENERGY = "produceEnergy";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_safeTransferFrom = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SETMARKETPLACEADDRESS = "setMarketplaceAddress";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENENERGYAMOUNT = "tokenEnergyAmount";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TRANSFERENERGY = "transferEnergy";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_USERENERGYBALANCES = "userEnergyBalances";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event BATCHMETADATAUPDATE_EVENT = new Event("BatchMetadataUpdate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ENERGYBALANCEUPDATED_EVENT = new Event("EnergyBalanceUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ENERGYNFTMINTED_EVENT = new Event("EnergyNFTMinted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ENERGYPRODUCED_EVENT = new Event("EnergyProduced", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MARKETPLACEADDRESSUPDATED_EVENT = new Event("MarketplaceAddressUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event METADATAUPDATE_EVENT = new Event("MetadataUpdate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected EnergyNFT(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EnergyNFT(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EnergyNFT(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EnergyNFT(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.approved = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
        typedResponse.approved = (Address) eventValues.getIndexedValues().get(1);
        typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public static List<ApprovalForAllEventResponse> getApprovalForAllEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.operator = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.approved = (Bool) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalForAllEventResponse getApprovalForAllEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, log);
        ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
        typedResponse.operator = (Address) eventValues.getIndexedValues().get(1);
        typedResponse.approved = (Bool) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalForAllEventFromLog(log));
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }

    public static List<BatchMetadataUpdateEventResponse> getBatchMetadataUpdateEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BATCHMETADATAUPDATE_EVENT, transactionReceipt);
        ArrayList<BatchMetadataUpdateEventResponse> responses = new ArrayList<BatchMetadataUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BatchMetadataUpdateEventResponse typedResponse = new BatchMetadataUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._fromTokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse._toTokenId = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BatchMetadataUpdateEventResponse getBatchMetadataUpdateEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BATCHMETADATAUPDATE_EVENT, log);
        BatchMetadataUpdateEventResponse typedResponse = new BatchMetadataUpdateEventResponse();
        typedResponse.log = log;
        typedResponse._fromTokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse._toTokenId = (Uint256) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<BatchMetadataUpdateEventResponse> batchMetadataUpdateEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBatchMetadataUpdateEventFromLog(log));
    }

    public Flowable<BatchMetadataUpdateEventResponse> batchMetadataUpdateEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BATCHMETADATAUPDATE_EVENT));
        return batchMetadataUpdateEventFlowable(filter);
    }

    public static List<EnergyBalanceUpdatedEventResponse> getEnergyBalanceUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ENERGYBALANCEUPDATED_EVENT, transactionReceipt);
        ArrayList<EnergyBalanceUpdatedEventResponse> responses = new ArrayList<EnergyBalanceUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EnergyBalanceUpdatedEventResponse typedResponse = new EnergyBalanceUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.newBalance = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EnergyBalanceUpdatedEventResponse getEnergyBalanceUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ENERGYBALANCEUPDATED_EVENT, log);
        EnergyBalanceUpdatedEventResponse typedResponse = new EnergyBalanceUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.user = (Address) eventValues.getNonIndexedValues().get(0);
        typedResponse.newBalance = (Uint256) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<EnergyBalanceUpdatedEventResponse> energyBalanceUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEnergyBalanceUpdatedEventFromLog(log));
    }

    public Flowable<EnergyBalanceUpdatedEventResponse> energyBalanceUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ENERGYBALANCEUPDATED_EVENT));
        return energyBalanceUpdatedEventFlowable(filter);
    }

    public static List<EnergyNFTMintedEventResponse> getEnergyNFTMintedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ENERGYNFTMINTED_EVENT, transactionReceipt);
        ArrayList<EnergyNFTMintedEventResponse> responses = new ArrayList<EnergyNFTMintedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EnergyNFTMintedEventResponse typedResponse = new EnergyNFTMintedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.owner = (Address) eventValues.getNonIndexedValues().get(1);
            typedResponse.ipfsHash = (Utf8String) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EnergyNFTMintedEventResponse getEnergyNFTMintedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ENERGYNFTMINTED_EVENT, log);
        EnergyNFTMintedEventResponse typedResponse = new EnergyNFTMintedEventResponse();
        typedResponse.log = log;
        typedResponse.tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        typedResponse.owner = (Address) eventValues.getNonIndexedValues().get(1);
        typedResponse.ipfsHash = (Utf8String) eventValues.getNonIndexedValues().get(2);
        return typedResponse;
    }

    public Flowable<EnergyNFTMintedEventResponse> energyNFTMintedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEnergyNFTMintedEventFromLog(log));
    }

    public Flowable<EnergyNFTMintedEventResponse> energyNFTMintedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ENERGYNFTMINTED_EVENT));
        return energyNFTMintedEventFlowable(filter);
    }

    public static List<EnergyProducedEventResponse> getEnergyProducedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ENERGYPRODUCED_EVENT, transactionReceipt);
        ArrayList<EnergyProducedEventResponse> responses = new ArrayList<EnergyProducedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EnergyProducedEventResponse typedResponse = new EnergyProducedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EnergyProducedEventResponse getEnergyProducedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ENERGYPRODUCED_EVENT, log);
        EnergyProducedEventResponse typedResponse = new EnergyProducedEventResponse();
        typedResponse.log = log;
        typedResponse.user = (Address) eventValues.getNonIndexedValues().get(0);
        typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
        return typedResponse;
    }

    public Flowable<EnergyProducedEventResponse> energyProducedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEnergyProducedEventFromLog(log));
    }

    public Flowable<EnergyProducedEventResponse> energyProducedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ENERGYPRODUCED_EVENT));
        return energyProducedEventFlowable(filter);
    }

    public static List<MarketplaceAddressUpdatedEventResponse> getMarketplaceAddressUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MARKETPLACEADDRESSUPDATED_EVENT, transactionReceipt);
        ArrayList<MarketplaceAddressUpdatedEventResponse> responses = new ArrayList<MarketplaceAddressUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MarketplaceAddressUpdatedEventResponse typedResponse = new MarketplaceAddressUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newMarketplace = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MarketplaceAddressUpdatedEventResponse getMarketplaceAddressUpdatedEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MARKETPLACEADDRESSUPDATED_EVENT, log);
        MarketplaceAddressUpdatedEventResponse typedResponse = new MarketplaceAddressUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.newMarketplace = (Address) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<MarketplaceAddressUpdatedEventResponse> marketplaceAddressUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMarketplaceAddressUpdatedEventFromLog(log));
    }

    public Flowable<MarketplaceAddressUpdatedEventResponse> marketplaceAddressUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MARKETPLACEADDRESSUPDATED_EVENT));
        return marketplaceAddressUpdatedEventFlowable(filter);
    }

    public static List<MetadataUpdateEventResponse> getMetadataUpdateEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(METADATAUPDATE_EVENT, transactionReceipt);
        ArrayList<MetadataUpdateEventResponse> responses = new ArrayList<MetadataUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MetadataUpdateEventResponse typedResponse = new MetadataUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MetadataUpdateEventResponse getMetadataUpdateEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(METADATAUPDATE_EVENT, log);
        MetadataUpdateEventResponse typedResponse = new MetadataUpdateEventResponse();
        typedResponse.log = log;
        typedResponse._tokenId = (Uint256) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    public Flowable<MetadataUpdateEventResponse> metadataUpdateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMetadataUpdateEventFromLog(log));
    }

    public Flowable<MetadataUpdateEventResponse> metadataUpdateEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(METADATAUPDATE_EVENT));
        return metadataUpdateEventFlowable(filter);
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

    public static List<TransferEventResponse> getTransferEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
        typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
        typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(Address to, Uint256 tokenId) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(to, tokenId), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Uint256> balanceOf(Address owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(owner), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Address> getApproved(Uint256 tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED, 
                Arrays.<Type>asList(tokenId), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Uint256> getCurrentEnergy(Address user) {
        final Function function = new Function(FUNC_GETCURRENTENERGY, 
                Arrays.<Type>asList(user), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Bool> isApprovedForAll(Address owner, Address operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL, 
                Arrays.<Type>asList(owner, operator), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Address> marketplaceAddress() {
        final Function function = new Function(FUNC_MARKETPLACEADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(Address _from, Utf8String _tokenURI,
            Uint256 _energyAmount) {
        final Function function = new Function(
                FUNC_MINT, 
                Arrays.<Type>asList(_from, _tokenURI, _energyAmount), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Utf8String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Address> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Address> ownerOf(Uint256 tokenId) {
        final Function function = new Function(FUNC_OWNEROF, 
                Arrays.<Type>asList(tokenId), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> produceEnergy(Address user,
            Uint256 _energyAmount) {
        final Function function = new Function(
                FUNC_PRODUCEENERGY, 
                Arrays.<Type>asList(user, _energyAmount), 
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

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(Address from, Address to,
            Uint256 tokenId) {
        final Function function = new Function(
                FUNC_safeTransferFrom, 
                Arrays.<Type>asList(from, to, tokenId), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(Address from, Address to,
            Uint256 tokenId, DynamicBytes data) {
        final Function function = new Function(
                FUNC_safeTransferFrom, 
                Arrays.<Type>asList(from, to, tokenId, data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setApprovalForAll(Address operator,
            Bool approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.<Type>asList(operator, approved), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setMarketplaceAddress(
            Address _marketplaceAddress) {
        final Function function = new Function(
                FUNC_SETMARKETPLACEADDRESS, 
                Arrays.<Type>asList(_marketplaceAddress), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Bool> supportsInterface(Bytes4 interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(interfaceId), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Utf8String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Uint256> tokenEnergyAmount(Uint256 param0) {
        final Function function = new Function(FUNC_TOKENENERGYAMOUNT, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Utf8String> tokenURI(Uint256 tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(tokenId), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferEnergy(Address _from, Address _to,
            Uint256 _tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERENERGY, 
                Arrays.<Type>asList(_from, _to, _tokenId), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(Address from, Address to,
            Uint256 tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(from, to, tokenId), 
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

    public RemoteFunctionCall<Uint256> userEnergyBalances(Address param0) {
        final Function function = new Function(FUNC_USERENERGYBALANCES, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    @Deprecated
    public static EnergyNFT load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new EnergyNFT(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EnergyNFT load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EnergyNFT(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EnergyNFT load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new EnergyNFT(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EnergyNFT load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EnergyNFT(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EnergyNFT> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EnergyNFT.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<EnergyNFT> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EnergyNFT.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EnergyNFT> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EnergyNFT.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EnergyNFT> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EnergyNFT.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class ApprovalEventResponse extends BaseEventResponse {
        public Address owner;

        public Address approved;

        public Uint256 tokenId;
    }

    public static class ApprovalForAllEventResponse extends BaseEventResponse {
        public Address owner;

        public Address operator;

        public Bool approved;
    }

    public static class BatchMetadataUpdateEventResponse extends BaseEventResponse {
        public Uint256 _fromTokenId;

        public Uint256 _toTokenId;
    }

    public static class EnergyBalanceUpdatedEventResponse extends BaseEventResponse {
        public Address user;

        public Uint256 newBalance;
    }

    public static class EnergyNFTMintedEventResponse extends BaseEventResponse {
        public Uint256 tokenId;

        public Address owner;

        public Utf8String ipfsHash;
    }

    public static class EnergyProducedEventResponse extends BaseEventResponse {
        public Address user;

        public Uint256 amount;
    }

    public static class MarketplaceAddressUpdatedEventResponse extends BaseEventResponse {
        public Address newMarketplace;
    }

    public static class MetadataUpdateEventResponse extends BaseEventResponse {
        public Uint256 _tokenId;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public Address previousOwner;

        public Address newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public Address from;

        public Address to;

        public Uint256 tokenId;
    }
}
