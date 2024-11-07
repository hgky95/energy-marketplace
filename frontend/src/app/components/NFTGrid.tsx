import React, { useContext, useEffect, useState, useCallback } from "react";
import { NFTGridProps } from "../../types";
import NFTCard from "./NFTCard";
import CreateNFTForm from "./CreateNFTForm";
import { Web3 } from "./Web3";
import { useBlockchainEvents } from "../../hooks/useBlockchainEvents";
import { ethers } from "ethers";

interface NFTMetadata {
  description: string;
  image: string;
  attributes: Array<{
    trait_type: string;
    value: string | number;
    unit?: string;
  }>;
}

interface NFT {
  id: number;
  title: string;
  price: string;
  energyAmount: number;
  seller: string;
  image?: string;
  description?: string;
  attributes?: NFTMetadata["attributes"];
}

export default function NFTGrid({ section }: NFTGridProps) {
  const { account, marketplace, nft } = useContext(Web3);
  const [nfts, setNfts] = useState<NFT[]>([]);
  const [loading, setLoading] = useState(true);
  const { shouldRefresh, resetRefreshFlag } = useBlockchainEvents(marketplace);

  const fetchMetadata = async (uri: string): Promise<NFTMetadata | null> => {
    try {
      const response = await fetch(uri);
      const metadata = await response.json();
      return metadata;
    } catch (error) {
      console.error("Error fetching metadata:", error);
      return null;
    }
  };

  const loadNFTs = useCallback(async () => {
    try {
      if (!marketplace || !nft) return;

      setLoading(true);
      const itemCount = await marketplace.itemCount();
      let nfts: NFT[] = [];

      for (let i = 1; i <= itemCount; i++) {
        const item = await marketplace.items(i);

        const uri = await nft.tokenURI(item.tokenId);
        const metadata = await fetchMetadata(uri);
        const nftItem: NFT = {
          id: item.tokenId,
          title: `Energy NFT #${item.tokenId}`,
          price: `${ethers.formatEther(item.price)} ETH`,
          energyAmount: item.energyAmount,
          seller: item.seller,
          image: metadata?.image,
          description: metadata?.description,
          attributes: metadata?.attributes,
        };

        if (item.isActive) {
          if (
            section === "home" ||
            (section === "listed" &&
              item.seller.toLowerCase() === account?.toLowerCase())
          ) {
            nfts.push(nftItem);
          }
        } else {
          const ownerAddress = await nft.ownerOf(item.tokenId);
          if (
            section === "purchased" &&
            ownerAddress.toLowerCase() === account?.toLowerCase()
          ) {
            nfts.push(nftItem);
          }
        }
      }

      setNfts(nfts);
      setLoading(false);
    } catch (error) {
      console.error("Error loading NFTs:", error);
      setLoading(false);
    }
  }, [marketplace, nft, account, section]);

  // Load NFTs on initial render and when section changes
  useEffect(() => {
    loadNFTs();
  }, [loadNFTs]);

  // Refresh when blockchain events occur
  useEffect(() => {
    if (shouldRefresh) {
      loadNFTs();
      resetRefreshFlag();
    }
  }, [shouldRefresh, loadNFTs, resetRefreshFlag]);

  if (section === "create") {
    return <CreateNFTForm />;
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-center">
          <div className="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading NFTs...</p>
        </div>
      </div>
    );
  }

  if (nfts.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[400px] text-center p-8">
        <h3 className="text-xl font-semibold mb-2">No NFTs Listed</h3>
        <p className="text-gray-600 mb-6 max-w-md">
          {section === "listed"
            ? "You haven't listed any NFTs yet."
            : section === "purchased"
            ? "You haven't purchased any NFTs yet."
            : "There are currently no energy NFTs listed in the marketplace."}
        </p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      {nfts.map((nft) => (
        <NFTCard
          key={nft.id}
          id={nft.id}
          title={nft.title}
          price={nft.price}
          energyAmount={nft.energyAmount}
          seller={nft.seller}
          image={nft.image}
          description={nft.description}
          attributes={nft.attributes}
          loadNFTs={loadNFTs}
        />
      ))}
    </div>
  );
}
