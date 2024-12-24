import { useState, useEffect, useCallback } from "react";
import { ethers } from "ethers";

export const useBlockchainEvents = (marketplace: ethers.Contract | null) => {
  const [shouldRefresh, setShouldRefresh] = useState<boolean>(false);

  const resetRefreshFlag = useCallback(() => {
    setShouldRefresh(false);
  }, []);

  const handleNFTMinted = async (event: any) => {
    setShouldRefresh(true);
  };

  const handleNFTSold = async (event: any) => {
    setShouldRefresh(true);
  };

  useEffect(() => {
    if (!marketplace) {
      return;
    }
    const nftMintedAndListed = marketplace.filters.NFTMintedAndListed();
    const nftSold = marketplace.filters.NFTSold();
    marketplace.on(nftMintedAndListed, handleNFTMinted);
    marketplace.on(nftSold, handleNFTSold);

    // Cleanup function
    return () => {
      marketplace.off(nftMintedAndListed, () => {});
      marketplace.off(nftSold, () => {});
    };
  }, [marketplace]);

  return { shouldRefresh, resetRefreshFlag };
};
