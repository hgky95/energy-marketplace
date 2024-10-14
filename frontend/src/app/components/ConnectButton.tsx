// src/components/ConnectButton.tsx
import { useContext } from "react";
import { Web3 } from "./Web3";

export default function ConnectButton() {
  const { account, web3Handler, disconnectWallet } = useContext(Web3);

  return (
    <button
      onClick={account ? disconnectWallet : web3Handler}
      className={`px-6 py-2 rounded-lg transition-colors duration-200 ${
        account
          ? "bg-blue-600 hover:bg-blue-700"
          : "bg-blue-500 hover:bg-blue-600"
      } text-white`}
    >
      {account ? (
        <span>{`${account.slice(0, 6)}...${account.slice(-4)}`}</span>
      ) : (
        "Connect Wallet"
      )}
    </button>
  );
}
