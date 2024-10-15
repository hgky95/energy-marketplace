import React, { useState } from "react";

export default function CreateNFTForm() {
  const [price, setPrice] = useState<string>("");
  const [energyAmount, setEnergyAmount] = useState<number>(0);
  const [energySource, setEnergySource] = useState<string>("Solar");
  const [location, setLocation] = useState<string>("Ontario - Farm 1");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [ipfsHash, setIpfsHash] = useState<string | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const uploadToIPFS = async () => {
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
        setIpfsHash(data.ipfsHash);
        console.log("Uploaded to IPFS, IPFS hash:", data.ipfsHash);
      } else {
        throw new Error("Error uploading to IPFS");
      }
    } catch (error: any) {
      console.error("Error uploading to IPFS:", error);
      setErrorMessage("Failed to upload metadata to IPFS");
    } finally {
      setIsLoading(false);
    }
  };

  const handleMintNFT = async (e: React.FormEvent) => {
    e.preventDefault();
    await uploadToIPFS();
  };

  return (
    <div className="relative">
      <div className="bg-white bg-opacity-10 rounded-lg p-6 max-w-2xl mx-auto">
        <h2 className="text-2xl font-bold text-white mb-6">
          Create New Energy NFT
        </h2>
        <div className="mb-6">
          <p className="text-lg text-white">Current Energy Balance: 1000 kWh</p>
        </div>
        <form className="space-y-4" onSubmit={handleMintNFT}>
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
            <label className="block text-white mb-2">Energy Amount (kWh)</label>
            <input
              type="number"
              value={energyAmount}
              onChange={(e) => setEnergyAmount(Number(e.target.value))}
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
            {isLoading ? "Minting..." : "Mint NFT"}
          </button>
        </form>
        {ipfsHash && (
          <div className="mt-4">
            <p className="text-green-400">Mint NFT successfully!</p>
          </div>
        )}
        {errorMessage && <p className="text-red-500 mt-4">{errorMessage}</p>}
      </div>

      {isLoading && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-75 z-50">
          <div className="text-center">
            <div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-24 w-24 mb-4"></div>
            <h2 className="text-white text-2xl font-semibold">Minting...</h2>
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
