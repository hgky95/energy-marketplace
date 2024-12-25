import React, { useState, useEffect } from "react";
import { useContext } from "react";
import { Web3 } from "./Web3";
import { ethers } from "ethers";
import { RefreshCw } from "lucide-react";

export default function CreateNFTForm() {
  const { account, marketplace, nft, web3Handler } = useContext(Web3);
  const [price, setPrice] = useState<string>("");
  const [energyAmount, setEnergyAmount] = useState<number>(0);
  const [energySource, setEnergySource] = useState<string>("Solar");
  const [location, setLocation] = useState<string>("Ontario - Farm 1");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [mintingSuccess, setMintingSuccess] = useState<boolean>(false);
  const [energyBalance, setEnergyBalance] = useState<string>("0");
  const [isRefreshing, setIsRefreshing] = useState<boolean>(false);

  const PINATA_HOST = process.env.NEXT_PUBLIC_PINATA_GATEWAY + "/ipfs/";
  const MARKETPLACE_ADDRESS = process.env.NEXT_PUBLIC_MARKETPLACE_ADDRESS || "";

  const fetchEnergyBalance = async () => {
    if (account && nft) {
      try {
        setIsRefreshing(true);
        const balance = await nft.getCurrentEnergy(account);
        setEnergyBalance(balance.toString());
      } catch (error) {
        console.error("Error fetching energy balance:", error);
        setErrorMessage("Failed to fetch energy balance");
      } finally {
        setIsRefreshing(false);
      }
    }
  };

  useEffect(() => {
    fetchEnergyBalance();
    // const intervalId = setInterval(fetchEnergyBalance, 30000);
    // return () => clearInterval(intervalId);
  }, [account, nft]);

  const uploadToIPFS = async (): Promise<string | null> => {
    try {
      setIsLoading(true);
      setErrorMessage(null);

      const response = await fetch("/api/ipfs", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          energyAmount,
          energySource,
          location,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        return data.ipfsHash;
      } else {
        throw new Error("Error uploading to IPFS");
      }
    } catch (error: any) {
      console.error("Error uploading to IPFS:", error);
      setErrorMessage("Failed to upload metadata to IPFS");
      return null;
    } finally {
      setIsLoading(false);
    }
  };

  const mintNFT = async (hash: string) => {
    try {
      if (!account) {
        setErrorMessage("Please connect your wallet.");
        return;
      }
      if (!marketplace || !nft) {
        setErrorMessage("Contracts not loaded.");
        return;
      }

      setIsLoading(true);
      const NFT_URI = PINATA_HOST + hash;
      const tx = await marketplace.mintAndList(
        NFT_URI,
        energyAmount,
        ethers.parseEther(price)
      );

      await (await nft.setApprovalForAll(MARKETPLACE_ADDRESS, true)).wait();

      console.log("NFT minted and listed successfully");
      setMintingSuccess(true);

      await fetchEnergyBalance();
    } catch (error) {
      console.error("Error minting and listing NFT:", error);
      setErrorMessage("Failed to mint and list the NFT. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage(null);
    setMintingSuccess(false);

    try {
      await web3Handler();

      if (energyAmount > Number(energyBalance)) {
        setErrorMessage("Insufficient energy balance");
        return;
      }

      const hash = await uploadToIPFS();
      if (!hash) {
        setErrorMessage("IPFS upload failed. Please try again.");
        return;
      }

      await mintNFT(hash);
    } catch (error) {
      console.error("Error in form submission:", error);
      setErrorMessage("An error occurred. Please try again.");
    }
  };

  return (
    <div className="relative">
      <div className="bg-white bg-opacity-10 rounded-lg p-6 max-w-2xl mx-auto">
        <h2 className="text-2xl font-bold text-white mb-6">
          Create New Energy NFT
        </h2>
        <div className="mb-6 flex items-center space-x-2">
          <p className="text-lg text-white">
            Current Energy Balance: {energyBalance} kW
          </p>
          <button
            onClick={fetchEnergyBalance}
            disabled={isRefreshing}
            className="p-2 rounded-full hover:bg-white hover:bg-opacity-10 transition-colors"
            title="Refresh balance"
          >
            <RefreshCw
              className={`w-5 h-5 text-white ${
                isRefreshing ? "animate-spin" : ""
              }`}
            />
          </button>
        </div>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-white mb-2">Price (ETH)</label>
            <input
              type="number"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              className="w-full p-2 rounded-lg bg-white bg-opacity-20 text-white border border-white border-opacity-20"
              required
            />
          </div>
          <div>
            <label className="block text-white mb-2">Energy Amount (kW)</label>
            <input
              type="number"
              value={energyAmount}
              onChange={(e) => setEnergyAmount(Number(e.target.value))}
              max={Number(energyBalance)}
              className="w-full p-2 rounded-lg bg-white bg-opacity-20 text-white border border-white border-opacity-20"
              required
            />
          </div>
          <div>
            <label className="block text-white mb-2">Energy Source</label>
            <select
              value={energySource}
              onChange={(e) => setEnergySource(e.target.value)}
              className="w-full p-2 rounded-lg bg-white bg-opacity-20 text-white border border-white border-opacity-20"
              required
            >
              <option value="Solar">Solar</option>
              <option value="Wind">Wind</option>
            </select>
          </div>
          <div>
            <label className="block text-white mb-2">Location</label>
            <select
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              className="w-full p-2 rounded-lg bg-white bg-opacity-20 text-white border border-white border-opacity-20"
              required
            >
              <option value="Ontario - Farm 1">Ontario - Farm 1</option>
              <option value="Ontario - Farm 2">Ontario - Farm 2</option>
              <option value="Ontario - Farm 3">Ontario - Farm 3</option>
              <option value="Alberta - Farm 1">Alberta - Farm 1</option>
              <option value="Alberta - Farm 2">Alberta - Farm 2</option>
              <option value="Quebec - Farm 1">Quebec - Farm 1</option>
              <option value="Quebec - Farm 2">Quebec - Farm 2</option>
            </select>
          </div>
          <button
            type="submit"
            className="bg-green-500 text-white px-6 py-2 rounded-lg hover:bg-green-600"
            disabled={isLoading}
          >
            {isLoading ? "Processing..." : "Mint NFT"}
          </button>
        </form>
        {mintingSuccess && (
          <div className="mt-4">
            <p className="text-green-400">NFT minted successfully!</p>
          </div>
        )}
        {errorMessage && <p className="text-red-500 mt-4">{errorMessage}</p>}
      </div>

      {isLoading && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-75 z-50">
          <div className="text-center">
            <div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-24 w-24 mb-4"></div>
            <h2 className="text-white text-2xl font-semibold">Processing...</h2>
          </div>
        </div>
      )}

      <style jsx>{`
        .loader {
          border-top-color: #3498db;
          animation: spin 1s linear infinite;
        }
        @keyframes spin {
          0% {
            transform: rotate(0deg);
          }
          100% {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </div>
  );
}
