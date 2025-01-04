import React, { useState, useContext, useEffect } from "react";
import { Web3 } from "./Web3";
import { ethers } from "ethers";
import { ChevronDown, ArrowDownUp, Loader } from "lucide-react";
import { BridgeService } from "@/services/bridge";
import { DepositStatus, FillStatus, Quote } from "@across-protocol/app-sdk";
import { Hash } from "viem";

interface ChainOption {
  id: number;
  name: string;
}

const chains: ChainOption[] = [
  {
    id: 84532,
    name: "Base",
  },
  {
    id: 11155111,
    name: "Ethereum",
  },
  {
    id: 421614,
    name: "Arbitrum",
  },
  {
    id: 11155420,
    name: "Optimism",
  },
];

const RPC_URLS: { [chainId: number]: string } = {
  84532: process.env.NEXT_PUBLIC_RPC_URL || "https://base-sepolia.rpc.url",
  11155111: process.env.NEXT_PUBLIC_ETH_RPC_URL || "https://sepolia.rpc.url",
  421614:
    process.env.NEXT_PUBLIC_ARB_RPC_URL || "https://arbitrum-sepolia.rpc.url",
  11155420:
    process.env.NEXT_PUBLIC_OPT_RPC_URL || "https://optimism-sepolia.rpc.url",
};

export default function Bridge() {
  const { account, web3Handler } = useContext(Web3);
  const [amount, setAmount] = useState<string>("0");
  const [sourceChain, setSourceChain] = useState<ChainOption>(chains[1]);
  const [destinationChain, setDestinationChain] = useState<ChainOption>(
    chains[0]
  );
  const [isLoading, setIsLoading] = useState(false);
  const [balance, setBalance] = useState<string>("0.0");
  const [provider, setProvider] = useState<ethers.JsonRpcProvider>();

  const [quote, setQuote] = useState<Quote>();
  const [txHash, setTxHash] = useState<Hash>();
  const [txFillDataHash, setTxFillDataHash] = useState<Hash>();
  const [destinationBlock, setDestinationBlock] = useState<bigint>();
  const [depositData, setDepositData] = useState<DepositStatus>();
  const [fillData, setFillData] = useState<FillStatus>();
  const [bridgeService] = useState(() => new BridgeService());

  useEffect(() => {
    fetchBalance();
  }, [account, sourceChain]);

  const fetchBalance = async () => {
    if (!account) return;
    try {
      const rpcUrl = RPC_URLS[sourceChain.id];
      if (!rpcUrl) {
        console.error(`No RPC URL found for chain ID ${sourceChain.id}`);
        return;
      }

      const provider = new ethers.JsonRpcProvider(rpcUrl);
      setProvider(provider);
      const balance = await provider.getBalance(account);
      setBalance(Number(ethers.formatEther(balance)).toFixed(9));
    } catch (error) {
      console.error("Error fetching balance:", error);
      setBalance("0.0");
    }
  };

  const handleSwapChains = () => {
    setSourceChain(destinationChain);
    setDestinationChain(sourceChain);
  };

  const handleGetQuote = async () => {
    if (!account) {
      await web3Handler();
      return;
    }

    try {
      setIsLoading(true);
      const quoteResult = await bridgeService.getQuote({
        account,
        // amount: ethers.parseEther(amount).toString(),
        amount: amount,
        sourceChainId: sourceChain.id,
        destinationChainId: destinationChain.id,
      });
      setQuote(quoteResult);
    } catch (error) {
      console.error("Error getting quote:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleBridge = async () => {
    if (!account || !quote) return;

    try {
      setIsLoading(true);

      // If we're not on the source chain, request network switch
      if (sourceChain.id !== destinationChain.id) {
        try {
          await window.ethereum.request({
            method: "wallet_switchEthereumChain",
            params: [{ chainId: `0x${sourceChain.id.toString(16)}` }],
          });
        } catch (switchError: any) {
          // Handle chain not added to MetaMask error
          if (switchError.code === 4902) {
            console.error(
              "This network is not available in your metamask, please add it manually"
            );
            return;
          }
          throw switchError;
        }
      }

      const destBlock = await bridgeService.getDestinationBlock(
        destinationChain.id
      );
      setDestinationBlock(destBlock);

      // Update provider after potential network switch
      const updatedProvider = new ethers.BrowserProvider(window.ethereum);
      const hash = await bridgeService.bridge({
        quote,
        account,
        provider: updatedProvider,
      });
      setTxHash(hash);
    } catch (error) {
      console.error("Bridge error:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (txHash && quote) {
      bridgeService
        .waitForDeposit({
          transactionHash: txHash,
          originChainId: sourceChain.id,
        })
        // .then(setDepositData)
        .then((depositResult) => {
          setDepositData(depositResult);
          console.log("Wait for Deposit");
        })
        .catch(console.error);
    }
  }, [txHash, quote]);

  useEffect(() => {
    if (depositData && quote && destinationBlock) {
      bridgeService
        .waitForFill({
          depositId: Number(depositData.depositId),
          deposit: quote.deposit,
          fromBlock: destinationBlock,
        })
        .then((fillResult) => {
          setFillData(fillResult);
          setDepositData(undefined);
          setTxFillDataHash(fillResult.fillTxReceipt.transactionHash);
          console.log("Wait for Fill Data");
        })
        .catch(console.error);
    }
  }, [depositData, quote, destinationBlock]);

  return (
    <div className="max-w-md mx-auto bg-white bg-opacity-10 rounded-xl p-6">
      <h2 className="text-2xl font-bold text-white mb-6">Bridge Assets</h2>

      {/* Source Chain Selector */}
      <div className="mb-4">
        <label className="block text-white mb-2">From</label>
        <div className="relative">
          <select
            value={sourceChain.id}
            onChange={(e) =>
              setSourceChain(
                chains.find((c) => c.id === Number(e.target.value)) || chains[1]
              )
            }
            className="w-full p-3 rounded-lg bg-white bg-opacity-20 text-white appearance-none"
          >
            {chains.map((chain) => (
              <option key={chain.id} value={chain.id}>
                {chain.name}
              </option>
            ))}
          </select>
          <ChevronDown className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white" />
        </div>
      </div>

      {/* Swap Button */}
      <button
        onClick={handleSwapChains}
        className="mx-auto block p-2 rounded-full hover:bg-white hover:bg-opacity-10 transition-colors mb-4"
      >
        <ArrowDownUp className="text-white" />
      </button>

      {/* Destination Chain Selector */}
      <div className="mb-6">
        <label className="block text-white mb-2">To</label>
        <div className="relative">
          <select
            value={destinationChain.id}
            onChange={(e) =>
              setDestinationChain(
                chains.find((c) => c.id === Number(e.target.value)) || chains[0]
              )
            }
            className="w-full p-3 rounded-lg bg-white bg-opacity-20 text-white appearance-none"
          >
            {chains.map((chain) => (
              <option key={chain.id} value={chain.id}>
                {chain.name}
              </option>
            ))}
          </select>
          <ChevronDown className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white" />
        </div>
      </div>

      {/* Amount Input */}
      <div className="mb-6">
        <label className="block text-white mb-2">Amount</label>
        <div className="relative">
          <input
            type="number"
            value={amount}
            onChange={(e) => {
              const value = e.target.value;
              const sanitizedValue = e.target.value.replace(",", ".");
              if (sanitizedValue === "" || Number(sanitizedValue) >= 0) {
                setAmount(value);
                setQuote(undefined);
              }
            }}
            min="0"
            className="w-full p-3 rounded-lg bg-white bg-opacity-20 text-white [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
            // placeholder="0"
          />
          <div className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white text-sm">
            Balance: {balance}
          </div>
        </div>
      </div>

      {/* Buttons */}
      <div className="space-y-4">
        {/* Get Quote Button */}
        <button
          onClick={handleGetQuote}
          disabled={isLoading || !amount || amount === "0"}
          className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
        >
          {isLoading ? "Processing..." : "Get Quote"}
        </button>

        {/* Bridge Button - Only show after quote is received */}
        {quote && (
          <button
            onClick={handleBridge}
            disabled={isLoading}
            className="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50"
          >
            {isLoading
              ? "Processing..."
              : account
              ? "Bridge"
              : "Connect Wallet"}
          </button>
        )}
      </div>

      {/* Loading Overlay */}
      {isLoading && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="animate-spin rounded-full h-32 w-32 border-t-2 border-b-2 border-white"></div>
        </div>
      )}

      {/* Add Quote Details */}
      {quote && !depositData && (
        <div className="mt-4 p-4 bg-white bg-opacity-5 rounded-lg">
          <h3 className="text-white font-semibold mb-2">Quote Details</h3>
          <div className="text-sm text-white grid grid-cols-[auto,1fr] gap-2">
            <span>Fee:</span>
            <span className="text-right">
              {Number(
                ethers.formatEther(quote.fees.totalRelayFee.total)
              ).toFixed(9)}{" "}
              ETH
            </span>
            <span>Receive:</span>
            <span className="text-right">
              {(
                Number(amount) -
                Number(ethers.formatEther(quote.fees.totalRelayFee.total))
              ).toFixed(9)}{" "}
              ETH
            </span>
          </div>
        </div>
      )}

      {/* Transaction Status */}
      {depositData && !fillData && (
        <div className="mt-4 text-white flex items-center gap-2">
          <p>Deposit confirmed! Waiting for bridge...</p>
          <Loader className="animate-spin" size={18} />
        </div>
      )}

      {fillData && (
        <div className="mt-4 text-green-400">
          <p>Bridge complete!</p>
          <a
            href={`https://sepolia.basescan.org/tx/${txFillDataHash}`}
            target="_blank"
            rel="noopener noreferrer"
          >
            View on BaseScan
          </a>
        </div>
      )}
    </div>
  );
}
