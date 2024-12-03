import { useState, createContext, useEffect } from "react";
import { ethers } from "ethers";
import MarketplaceAbi from "../../contracts/MarketplaceAbi.json";
import NFTAbi from "../../contracts/NFTAbi.json";
import LoyaltyProgramAbi from "../../contracts/LoyaltyProgramABI.json";

export const Web3 = createContext<{
  account: string;
  marketplace: ethers.Contract | null;
  nft: ethers.Contract | null;
  loyaltyProgram: ethers.Contract | null;
  web3Handler: () => Promise<void>;
  disconnectWallet: () => void;
}>({
  account: "",
  marketplace: null,
  nft: null,
  loyaltyProgram: null,
  web3Handler: async () => {},
  disconnectWallet: () => {},
});

const NFT_ADDRESS = process.env.NEXT_PUBLIC_NFT_ADDRESS || "";
const MARKETPLACE_ADDRESS = process.env.NEXT_PUBLIC_MARKETPLACE_ADDRESS || "";
const LOYALTY_PROGRAM_ADDRESS =
  process.env.NEXT_PUBLIC_LOYALTY_PROGRAM_ADDRESS || "";

export const Web3Provider = ({ children }: { children: React.ReactNode }) => {
  const [account, setAccount] = useState<string>("");
  const [marketplace, setMarketplace] = useState<ethers.Contract | null>(null);
  const [nft, setNFT] = useState<ethers.Contract | null>(null);
  const [loyaltyProgram, setLoyaltyProgram] = useState<ethers.Contract | null>(
    null
  );

  // Load contracts in read-only mode on initial load
  useEffect(() => {
    loadContractsReadOnly();
  }, []);

  const loadContractsReadOnly = async () => {
    try {
      // Use public provider for initial read-only access
      const provider = new ethers.JsonRpcProvider(
        process.env.NEXT_PUBLIC_RPC_URL || "http://localhost:8545"
      );

      const marketplace = new ethers.Contract(
        MARKETPLACE_ADDRESS,
        MarketplaceAbi,
        provider
      );
      setMarketplace(marketplace);

      const nft = new ethers.Contract(NFT_ADDRESS, NFTAbi, provider);
      setNFT(nft);

      const loyaltyProgram = new ethers.Contract(
        LOYALTY_PROGRAM_ADDRESS,
        LoyaltyProgramAbi,
        provider
      );
      setLoyaltyProgram(loyaltyProgram);
    } catch (error) {
      console.error("Error loading contracts in read-only mode:", error);
    }
  };

  // Check if wallet is already connected
  useEffect(() => {
    const checkWalletConnection = async () => {
      if (window.ethereum) {
        try {
          const accounts = await window.ethereum.request({
            method: "eth_accounts",
          });
          if (accounts.length > 0) {
            setAccount(accounts[0]);
            const provider = new ethers.BrowserProvider(window.ethereum);
            const signer = await provider.getSigner();
            await loadContracts(signer);
          }
        } catch (error) {
          console.error("Error checking wallet connection:", error);
        }
      }
    };

    checkWalletConnection();
  }, []);

  const web3Handler = async () => {
    try {
      const accounts = await window.ethereum.request({
        method: "eth_requestAccounts",
      });
      setAccount(accounts[0]);

      const provider = new ethers.BrowserProvider(window.ethereum);
      const signer = await provider.getSigner();

      window.ethereum.on("chainChanged", () => {
        window.location.reload();
      });

      window.ethereum.on(
        "accountsChanged",
        async function (accounts: string[]) {
          if (accounts.length > 0) {
            setAccount(accounts[0]);
            await loadContracts(signer);
          } else {
            disconnectWallet();
          }
        }
      );

      await loadContracts(signer);
    } catch (error) {
      console.error("Error connecting wallet:", error);
    }
  };

  const loadContracts = async (signer: ethers.Signer) => {
    try {
      const marketplace = new ethers.Contract(
        MARKETPLACE_ADDRESS,
        MarketplaceAbi,
        signer
      );
      setMarketplace(marketplace);

      const nft = new ethers.Contract(NFT_ADDRESS, NFTAbi, signer);
      setNFT(nft);

      const loyaltyProgram = new ethers.Contract(
        LOYALTY_PROGRAM_ADDRESS,
        LoyaltyProgramAbi,
        signer
      );
      setLoyaltyProgram(loyaltyProgram);
    } catch (error) {
      console.error("Error loading contracts:", error);
    }
  };

  const disconnectWallet = () => {
    setAccount("");
    // Revert to read-only mode
    loadContractsReadOnly();
  };

  return (
    <Web3.Provider
      value={{
        account,
        marketplace,
        nft,
        loyaltyProgram,
        web3Handler,
        disconnectWallet,
      }}
    >
      {children}
    </Web3.Provider>
  );
};
