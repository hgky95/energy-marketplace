import { useState, useEffect, useCallback } from "react";
import { ethers } from "ethers";

export const useBlockchainEvents = (marketplace: ethers.Contract | null) => {
  const [shouldRefresh, setShouldRefresh] = useState<boolean>(false);

  const resetRefreshFlag = useCallback(() => {
    setShouldRefresh(false);
  }, []);

  const handleNFTMinted = async (event: any) => {
    console.log("Event NFTMintedAndListed is triggered");
    setShouldRefresh(true);
  };

  const handleNFTSold = async (event: any) => {
    console.log("Event NFTSold is triggered");
    setShouldRefresh(true);
  };

  useEffect(() => {
    if (!marketplace) {
      return;
    }
    console.log("Subscribed NFTMintedAndListed and NFTSold");
    const nftMintedAndListed = marketplace.filters.NFTMintedAndListed();
    const nftSold = marketplace.filters.NFTSold();
    marketplace.on(nftMintedAndListed, handleNFTMinted);
    marketplace.on(nftSold, handleNFTSold);

    // Cleanup function
    return () => {
      console.log("Unsubscribed NFTMintedAndListed and NFTSold");
      marketplace.off(nftMintedAndListed, () => {});
      marketplace.off(nftSold, () => {});
    };
  }, [marketplace]);

  return { shouldRefresh, resetRefreshFlag };
};
