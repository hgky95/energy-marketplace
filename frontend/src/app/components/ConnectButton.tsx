// src/components/ConnectButton.tsx
import React, { useState, useEffect } from "react";
import { ethers } from "ethers";

export default function ConnectButton() {
  const [walletAddress, setWalletAddress] = useState<string>("");

  // Check if already connected when component mounts
  useEffect(() => {
    checkConnection();
  }, []);

  // Add listener for account changes
  useEffect(() => {
    if (window.ethereum) {
      window.ethereum.on("accountsChanged", handleAccountsChanged);

      return () => {
        window.ethereum.removeListener(
          "accountsChanged",
          handleAccountsChanged
        );
      };
    }
  }, []);

  async function checkConnection() {
    if (typeof window.ethereum !== "undefined") {
      try {
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const accounts = await provider.listAccounts();

        if (accounts.length > 0) {
          setWalletAddress(accounts[0]);
        }
      } catch (error) {
        console.error("Error checking wallet connection:", error);
      }
    }
  }

  function handleAccountsChanged(accounts: string[]) {
    if (accounts.length > 0) {
      setWalletAddress(accounts[0]);
    } else {
      setWalletAddress("");
    }
  }

  async function connectWallet() {
    if (typeof window.ethereum !== "undefined") {
      try {
        // Request account access
        const accounts = await window.ethereum.request({
          method: "eth_requestAccounts",
        });

        handleAccountsChanged(accounts);
      } catch (error) {
        console.error("Error connecting wallet:", error);
      }
    } else {
      alert("Please install MetaMask to use this feature");
    }
  }

  async function disconnectWallet() {
    setWalletAddress("");
  }

  return (
    <button
      onClick={walletAddress ? disconnectWallet : connectWallet}
      className={`px-6 py-2 rounded-lg transition-colors duration-200 ${
        walletAddress
          ? "bg-blue-600 hover:bg-blue-700"
          : "bg-blue-500 hover:bg-blue-600"
      } text-white`}
    >
      {walletAddress ? (
        <span>
          {`${walletAddress.slice(0, 6)}...${walletAddress.slice(-4)}`}
        </span>
      ) : (
        "Connect Wallet"
      )}
    </button>
  );
}
