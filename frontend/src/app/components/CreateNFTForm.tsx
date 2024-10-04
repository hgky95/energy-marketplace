import React from "react";

export default function CreateNFTForm() {
  return (
    <div className="bg-white bg-opacity-10 rounded-lg p-6 max-w-2xl mx-auto">
      <h2 className="text-2xl font-bold text-white mb-6">
        Create New Energy NFT
      </h2>
      <div className="mb-6">
        <p className="text-lg text-white">Current Energy Balance: 1000 kWh</p>
      </div>
      <form className="space-y-4">
        <div>
          <label className="block text-white mb-2">NFT Name</label>
          <input
            type="text"
            className="w-full p-2 rounded-lg bg-white bg-opacity-20 text-white border border-white border-opacity-20"
          />
        </div>
        <div>
          <label className="block text-white mb-2">Energy Amount (kWh)</label>
          <input
            type="number"
            className="w-full p-2 rounded-lg bg-white bg-opacity-20 text-white border border-white border-opacity-20"
          />
        </div>
        <button className="bg-green-500 text-white px-6 py-2 rounded-lg hover:bg-green-600">
          Mint NFT
        </button>
      </form>
    </div>
  );
}
