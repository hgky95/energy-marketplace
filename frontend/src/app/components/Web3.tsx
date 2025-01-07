import { useState, createContext, useEffect } from "react";
import { ethers } from "ethers";
import MarketplaceABI from "../../contracts/MarketplaceABI.json";
import NFTABI from "../../contracts/NFTABI.json";
import LoyaltyProgramABI from "../../contracts/LoyaltyProgramABI.json";

export const Web3 = createContext<{
  account: string;
  marketplace: ethers.Contract | null;
  nft: ethers.Contract | null;
  loyaltyProgram: ethers.Contract | null;
  web3Handler: () => Promise<void>;
  disconnectWallet: () => void;
  isInitialized: boolean;
  currentChainId: number | null;
}>({
  account: "",
  marketplace: null,
  nft: null,
  loyaltyProgram: null,
  web3Handler: async () => {},
  disconnectWallet: () => {},
  isInitialized: false,
  currentChainId: null,
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
  const [isInitialized, setIsInitialized] = useState(false);
  const [currentChainId, setCurrentChainId] = useState<number | null>(null);

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
        MarketplaceABI,
        provider
      );
      setMarketplace(marketplace);

      const nft = new ethers.Contract(NFT_ADDRESS, NFTABI, provider);
      setNFT(nft);

      const loyaltyProgram = new ethers.Contract(
        LOYALTY_PROGRAM_ADDRESS,
        LoyaltyProgramABI,
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
          await updateChainId();
          const accounts = await window.ethereum.request({
            method: "eth_accounts",
          });
          if (accounts.length > 0) {
            setAccount(accounts[0]);
            const provider = new ethers.BrowserProvider(window.ethereum);
            const signer = await provider.getSigner();
            await loadContracts(signer);
          } else {
            // Load contracts in read-only mode if no wallet is connected
            await loadContractsReadOnly();
          }
        } catch (error) {
          console.error("Error checking wallet connection:", error);
          await loadContractsReadOnly();
        }
      } else {
        await loadContractsReadOnly();
      }
      setIsInitialized(true);
    };

    checkWalletConnection();
  }, []);

  const updateChainId = async () => {
    if (window.ethereum) {
      const chainId = await window.ethereum.request({ method: "eth_chainId" });
      setCurrentChainId(parseInt(chainId, 16));
    }
  };

  // Update the chainChanged listener
  useEffect(() => {
    if (window.ethereum) {
      window.ethereum.on("chainChanged", (chainId: string) => {
        setCurrentChainId(parseInt(chainId, 16));
        loadContractsReadOnly();
        window.ethereum.on("chainChanged", () => {
          window.location.reload();
        });
      });
    }

    return () => {
      if (window.ethereum) {
        window.ethereum.removeListener("chainChanged", updateChainId);
      }
    };
  }, []);

  const web3Handler = async () => {
    try {
      const accounts = await window.ethereum.request({
        method: "eth_requestAccounts",
      });
      setAccount(accounts[0]);

      const provider = new ethers.BrowserProvider(window.ethereum);
      const signer = await provider.getSigner();

      // window.ethereum.on("chainChanged", () => {
      //   window.location.reload();
      // });

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
        MarketplaceABI,
        signer
      );
      setMarketplace(marketplace);

      const nft = new ethers.Contract(NFT_ADDRESS, NFTABI, signer);
      setNFT(nft);

      const loyaltyProgram = new ethers.Contract(
        LOYALTY_PROGRAM_ADDRESS,
        LoyaltyProgramABI,
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
        isInitialized,
        currentChainId,
      }}
    >
      {children}
    </Web3.Provider>
  );
};
