import React, { useContext, useEffect, useState, useCallback } from "react";
import { NFTGridProps } from "../../types";
import NFTCard from "./NFTCard";
import CreateNFTForm from "./CreateNFTForm";
import { Web3 } from "./Web3";
import { useBlockchainEvents } from "../../hooks/useBlockchainEvents";
import { ethers } from "ethers";
import * as APP_CONSTANT from "../../constants/AppConstant";
import Bridge from "./Bridge";

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

interface PaginationState {
  page: number;
  size: number;
  totalElements: number;
}

export default function NFTGrid({ section }: NFTGridProps) {
  const { account, marketplace, isInitialized } = useContext(Web3);
  const [nfts, setNfts] = useState<NFT[]>([]);
  const [loading, setLoading] = useState(true);
  const { shouldRefresh, resetRefreshFlag } = useBlockchainEvents(marketplace);
  const [pagination, setPagination] = useState<PaginationState>({
    page: 0,
    size: 8,
    totalElements: 0,
  });

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
    if (!isInitialized) return;

    try {
      setLoading(true);
      const params = new URLSearchParams({
        page: pagination.page.toString(),
        size: pagination.size.toString(),
        section: section,
      });

      if (account) {
        params.append("account", account);
      }

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/nfts?${params.toString()}`,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const data = await response.json();
      setNfts(data.content);
      setPagination((prev) => ({
        ...prev,
        totalElements: data.totalElements,
      }));
    } catch (error) {
      console.error("Error loading NFTs:", error);
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, section, account, isInitialized]);

  // Pagination controls
  const handlePageChange = (newPage: number) => {
    setPagination((prev) => ({
      ...prev,
      page: newPage,
    }));
  };

  // Load NFTs on initial render and when section changes
  useEffect(() => {
    if (
      section !== APP_CONSTANT.CREATE_MENU_ID &&
      section !== APP_CONSTANT.BRIDGE_MENU_ID &&
      isInitialized
    ) {
      loadNFTs();
    }
  }, [loadNFTs, section, isInitialized]);

  // Refresh when blockchain events occur
  useEffect(() => {
    if (shouldRefresh) {
      const timer = setTimeout(() => {
        loadNFTs();
        resetRefreshFlag();
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [shouldRefresh, loadNFTs, resetRefreshFlag]);

  // Reset pagination when section changes
  useEffect(() => {
    setPagination((prev) => ({
      ...prev,
      page: 0,
    }));
  }, [section]); // Dependency on section

  if (section === APP_CONSTANT.CREATE_MENU_ID) {
    return <CreateNFTForm />;
  }

  if (section === APP_CONSTANT.BRIDGE_MENU_ID) {
    return <Bridge />;
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
          {section === APP_CONSTANT.LISTING_MENU_ID
            ? "You haven't listed any NFTs yet."
            : section === APP_CONSTANT.PURCHASED_MENU_ID
            ? "You haven't purchased any NFTs yet."
            : "There are currently no energy NFTs listed in the marketplace."}
        </p>
      </div>
    );
  }

  return (
    <div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {nfts.map((nft) => (
          <NFTCard
            key={nft.id}
            {...nft}
            loadNFTs={loadNFTs}
            activeSection={section}
          />
        ))}
      </div>

      {/* Pagination Controls */}
      <div className="mt-6 flex justify-center">
        <button
          onClick={() => handlePageChange(pagination.page - 1)}
          disabled={pagination.page === 0}
          className="px-4 py-2 mr-2 bg-gray-200 rounded disabled:opacity-50"
        >
          Previous
        </button>
        <span className="px-4 py-2">
          Page {pagination.page + 1} of{" "}
          {Math.ceil(pagination.totalElements / pagination.size)}
        </span>
        <button
          onClick={() => handlePageChange(pagination.page + 1)}
          disabled={
            pagination.page >=
            Math.ceil(pagination.totalElements / pagination.size) - 1
          }
          className="px-4 py-2 ml-2 bg-gray-200 rounded disabled:opacity-50"
        >
          Next
        </button>
      </div>
    </div>
  );
}
