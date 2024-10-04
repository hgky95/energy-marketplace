import React from "react";
import { NFTCardProps } from "../../types";

export default function NFTCard({ title, price }: NFTCardProps) {
  return (
    <div className="bg-white bg-opacity-10 rounded-lg p-4">
      <div className="w-full h-48 bg-gray-300 rounded-lg mb-4"></div>
      <h3 className="text-lg font-semibold text-white mb-2">{title}</h3>
      <div className="flex justify-between items-center">
        <span className="text-white">{price}</span>
        <button className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600">
          Buy Now
        </button>
      </div>
    </div>
  );
}
