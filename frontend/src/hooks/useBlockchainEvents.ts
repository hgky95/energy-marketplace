import { useState, useEffect, useCallback } from "react";
import { ethers } from "ethers";

interface NFTEvent {
  tokenId: number;
  seller: string;
  ipfsHash?: string;
  energyValue?: number;
  price: number;
  buyer?: string;
  fee?: number;
}

export const useBlockchainEvents = (marketplace: ethers.Contract | null) => {
  const [lastEvent, setLastEvent] = useState<NFTEvent | null>(null);
  const [shouldRefresh, setShouldRefresh] = useState<boolean>(false);

  const handleNFTMinted = useCallback(
    (
      tokenId: number,
      seller: string,
      ipfsHash: string,
      energyValue: number,
      price: number
    ) => {
      setLastEvent({
        tokenId,
        seller,
        ipfsHash,
        energyValue,
        price,
      });
      setShouldRefresh(true);
    },
    []
  );

  const handleNFTSold = useCallback(
    (
      tokenId: number,
      seller: string,
      buyer: string,
      price: number,
      fee: number
    ) => {
      setLastEvent({
        tokenId,
        seller,
        buyer,
        price,
        fee,
      });
      setShouldRefresh(true);
    },
    []
  );

  const resetRefreshFlag = useCallback(() => {
    setShouldRefresh(false);
  }, []);

  useEffect(() => {
    if (!marketplace) return;

    // Subscribe to events
    const mintedFilter = marketplace.filters.NFTMintedAndListed();
    const soldFilter = marketplace.filters.NFTSold();

    marketplace.on(mintedFilter, handleNFTMinted);
    marketplace.on(soldFilter, handleNFTSold);

    // Cleanup function
    return () => {
      marketplace.off(mintedFilter, handleNFTMinted);
      marketplace.off(soldFilter, handleNFTSold);
    };
  }, [marketplace, handleNFTMinted, handleNFTSold]);

  return { lastEvent, shouldRefresh, resetRefreshFlag };
};
