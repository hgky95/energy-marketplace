import React, { useState, useContext } from "react";
import Image from "next/image";
import { Zap } from "lucide-react";
import { Web3 } from "./Web3";
import { ethers } from "ethers";
import * as APP_CONSTANT from "../../constants/AppConstant";

interface NFTCardProps {
  id: number;
  title: string;
  price: string;
  energyAmount: number;
  seller: string;
  image?: string;
  description?: string;
  attributes?: Array<{
    trait_type: string;
    value: string | number;
    unit?: string;
    fullValue?: string;
  }>;
  loadNFTs?: () => Promise<void>;
  activeSection: string;
}

const DEFAULT_IPFS_HOST =
  process.env.NEXT_PUBLIC_IPFS || "https://ipfs.io/ipfs/";

const formatAddress = (address: string) => {
  if (!address) return "";
  return `${address.slice(0, 6)}...${address.slice(-4)}`;
};

export default function NFTCard({
  id,
  title,
  price,
  energyAmount,
  seller,
  image,
  description = "",
  attributes = [],
  loadNFTs,
  activeSection,
}: NFTCardProps) {
  const { account, marketplace, web3Handler } = useContext(Web3);
  const [imageError, setImageError] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const isOwner =
    account && seller && account.toLowerCase() === seller.toLowerCase();

  const allAttributes = [
    {
      trait_type: "Seller",
      value: formatAddress(seller),
      fullValue: seller,
    },
    ...attributes,
  ];

  const getOptimizedImageUrl = (originalUrl?: string) => {
    if (!originalUrl) return null;
    if (originalUrl.startsWith("ipfs://")) {
      return originalUrl.replace("ipfs://", DEFAULT_IPFS_HOST);
    }
    return originalUrl;
  };

  const imageUrl = getOptimizedImageUrl(image);

  const handleBuy = async () => {
    try {
      setIsLoading(true);

      if (!account) {
        await web3Handler();
        return;
      }

      if (!marketplace) {
        alert("Marketplace contract not available");
        return;
      }

      if (isOwner) {
        alert("You cannot buy your own NFT");
        return;
      }

      const cleanPrice = price.replace(" ETH", "").toLowerCase();
      let priceInWei;
      try {
        if (cleanPrice.includes("e-")) {
          // Handle scientific notation (e.g. 1e-7 ETH = 0.0000001 ETH)
          const [base, exponent] = cleanPrice.split("e-");
          const baseNum = parseFloat(base);
          const exp = parseInt(exponent);
          // Convert directly to wei by multiplying by 10^(18-exp)
          // For 1e-7: 1 * 10^(18-7) = 1 * 10^11 wei
          const weiExponent = 18 - exp;
          priceInWei = ethers.getBigInt(
            Math.floor(baseNum * Math.pow(10, weiExponent))
          );
        } else {
          priceInWei = ethers.parseEther(cleanPrice);
        }
      } catch (error) {
        throw new Error(`Invalid price format: ${cleanPrice}`);
      }

      const transaction = await marketplace.buyNFT(id, {
        value: priceInWei,
      });

      await transaction.wait();

      alert(`Successfully purchased NFT #${id}`);

      // Reload NFTs
      // if (loadNFTs) {
      //   await loadNFTs();
      // }
    } catch (error: any) {
      console.error("Error buying NFT:", error);
      if (error.message.includes("UNSUPPORTED_OPERATION")) {
        alert(`Failed to purchase NFT: Please use Base Chain`);
      } else {
        alert(`Failed to purchase NFT: ${error.message}`);
      }
    } finally {
      setIsLoading(false);
    }
  };

  const getButtonText = () => {
    if (isLoading) return "Processing...";
    if (!account) return "Connect Wallet";
    if (isOwner) return "You Own This";
    return "Buy Now";
  };

  return (
    <>
      <div className="relative bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
        <div className="relative w-full h-48">
          {imageUrl && !imageError ? (
            <Image
              src={imageUrl}
              alt={title}
              fill
              className="object-cover"
              sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
              onError={() => setImageError(true)}
              priority={false}
            />
          ) : (
            <div className="w-full h-full bg-gray-300 flex items-center justify-center">
              <Zap className="w-12 h-12 text-gray-400" />
            </div>
          )}
        </div>

        <div className="p-4">
          <h3 className="font-semibold text-lg mb-2">{title}</h3>
          {description && (
            <p className="text-sm text-gray-600 mb-3">{description}</p>
          )}

          <div className="grid grid-cols-2 gap-2 mb-3">
            {allAttributes.map((attr, index) => (
              <div
                key={index}
                className={`bg-gray-50 p-2 rounded-md ${
                  attr.trait_type === "Seller" ? "col-span-2" : ""
                }`}
              >
                <p className="text-xs text-gray-500">{attr.trait_type}</p>
                <p
                  className="text-sm font-medium truncate"
                  title={
                    attr.trait_type === "Seller" ? attr.fullValue : undefined
                  }
                >
                  {attr.value} {attr.unit || ""}
                </p>
              </div>
            ))}
          </div>

          <div className="flex justify-between items-center pt-2 border-t">
            <div>
              <p className="text-sm text-gray-500">Price</p>
              <p className="font-semibold">{price}</p>
            </div>
            {activeSection === APP_CONSTANT.HOME_MENU_ID && (
              <button
                className={`px-4 py-2 rounded-lg transition-colors ${
                  isLoading
                    ? "bg-gray-400 text-white cursor-not-allowed"
                    : !account
                    ? "bg-gray-300 text-gray-500"
                    : isOwner
                    ? "bg-green-500 text-white cursor-not-allowed"
                    : "bg-blue-600 text-white hover:bg-blue-700"
                }`}
                disabled={
                  isLoading ||
                  !account ||
                  account.toLowerCase() === seller.toLowerCase()
                }
                onClick={handleBuy}
              >
                {getButtonText()}
              </button>
            )}
          </div>
        </div>
      </div>

      {isLoading && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-75 z-50">
          <div className="text-center">
            <div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-24 w-24 mb-4"></div>
            <h2 className="text-white text-2xl font-semibold">Processing...</h2>
          </div>
        </div>
      )}

      <style jsx>{`
        .loader {
          border-top-color: #3498db;
          animation: spin 1s linear infinite;
        }
        @keyframes spin {
          0% {
            transform: rotate(0deg);
          }
          100% {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </>
  );
}
