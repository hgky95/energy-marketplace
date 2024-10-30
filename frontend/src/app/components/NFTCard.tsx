import React, { useState } from "react";
import Image from "next/image";
import { Zap } from "lucide-react";

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
  }>;
}

const IPFS = process.env.NEXT_PUBLIC_IPFS || "";

export default function NFTCard({
  title,
  price,
  energyAmount,
  seller,
  image,
  description = "",
  attributes = [],
}: NFTCardProps) {
  const [imageError, setImageError] = useState(false);

  const getOptimizedImageUrl = (originalUrl?: string) => {
    if (!originalUrl) return null;

    if (originalUrl.startsWith("ipfs://")) {
      return originalUrl.replace("ipfs://", IPFS);
    }

    return originalUrl;
  };

  const imageUrl = getOptimizedImageUrl(image);

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

        {attributes.length > 0 && (
          <div className="grid grid-cols-2 gap-2 mb-3">
            {attributes.map((attr, index) => (
              <div key={index} className="bg-gray-50 p-2 rounded-md">
                <p className="text-xs text-gray-500">{attr.trait_type}</p>
                <p className="text-sm font-medium">
                  {attr.value} {attr.unit || ""}
                </p>
              </div>
            ))}
          </div>
        )}

        <div className="flex justify-between items-center pt-2 border-t">
          <div>
            <p className="text-sm text-gray-500">Price</p>
            <p className="font-semibold">{price}</p>
          </div>
          <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-primary/90 transition-colors">
            Buy Now
          </button>
        </div>
      </div>
    </div>
  );
}
