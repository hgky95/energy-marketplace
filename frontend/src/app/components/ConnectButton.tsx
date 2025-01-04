import { useContext } from "react";
import { Web3 } from "./Web3";
import Image from "next/image";

const getNetworkIcon = (chainId: number | null) => {
  switch (chainId) {
    case 84532:
      return "/Base_Symbol_White.svg";
    case 11155111:
      return "/ethereum.svg";
    case 421614:
      return "/arbitrum.svg";
    case 11155420:
      return "/op.svg";
    default:
      return "/Base_Symbol_White.svg";
  }
};

export default function ConnectButton() {
  const { account, web3Handler, disconnectWallet, currentChainId } =
    useContext(Web3);

  return (
    <button
      onClick={account ? disconnectWallet : web3Handler}
      className={`px-6 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2 ${
        account
          ? "bg-blue-600 hover:bg-blue-700"
          : "bg-blue-500 hover:bg-blue-600"
      } text-white`}
    >
      {account ? (
        <>
          <Image
            src={getNetworkIcon(currentChainId)}
            alt="Network Icon"
            width={20}
            height={20}
          />
          <span>{`${account.slice(0, 6)}...${account.slice(-4)}`}</span>
        </>
      ) : (
        "Connect Wallet"
      )}
    </button>
  );
}
