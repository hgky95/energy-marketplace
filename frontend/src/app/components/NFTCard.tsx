import React, { useState, useContext } from "react";
import Image from "next/image";
import { Zap } from "lucide-react";
import { Web3 } from "./Web3";

interface NFTCardProps {
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
}

const DEFAULT_IPFS_HOST =
  process.env.NEXT_PUBLIC_IPFS || "https://ipfs.io/ipfs/";

const formatAddress = (address: string) => {
  if (!address) return "";
  return `${address.slice(0, 6)}...${address.slice(-4)}`;
};

export default function NFTCard({
  title,
  price,
  energyAmount,
  seller,
  image,
  description = "",
  attributes = [],
}: NFTCardProps) {
  const { account, web3Handler } = useContext(Web3);
  const [imageError, setImageError] = useState(false);

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

  const handleBuy = () => {
    //TODO handle buy
  };

  return (
    <div className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
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
          <button
            className={`px-4 py-2 rounded-lg transition-colors ${
              account
                ? "bg-blue-600 text-white hover:bg-primary/90"
                : "bg-gray-300 text-gray-500 cursor-not-allowed"
            }`}
            disabled={!account}
            onClick={handleBuy}
          >
            Buy Now
          </button>
        </div>
      </div>
    </div>
  );
}
