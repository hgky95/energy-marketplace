import React from "react";
import { NFTGridProps, NFT } from "../../types";
import NFTCard from "./NFTCard";
import CreateNFTForm from "./CreateNFTForm";

export default function NFTGrid({ section }: NFTGridProps) {
  const dummyNFTs: NFT[] = [
    { id: 1, title: "Solar Energy Token", price: "0.5 ETH" },
    { id: 2, title: "Wind Power NFT", price: "0.7 ETH" },
    { id: 3, title: "Hydro Energy Card", price: "0.6 ETH" },
    { id: 4, title: "Biomass Token", price: "0.4 ETH" },
    { id: 5, title: "Geothermal NFT", price: "0.8 ETH" },
    { id: 6, title: "Tidal Energy Card", price: "0.55 ETH" },
    { id: 7, title: "Nuclear Power Token", price: "0.9 ETH" },
    { id: 8, title: "Solar Farm Share", price: "0.65 ETH" },
  ];

  if (section === "create") {
    return <CreateNFTForm />;
  }

  return (
    <div className="grid grid-cols-4 gap-6">
      {dummyNFTs.map((nft) => (
        <NFTCard key={nft.id} title={nft.title} price={nft.price} />
      ))}
    </div>
  );
}
