import React, { useContext } from "react";
import { SidebarProps } from "../../types";
import { Web3 } from "../components/Web3";

const menuItems = [
  { id: "home", label: "Home" },
  { id: "create", label: "Create" },
  { id: "listed", label: "My Listed Items" },
  { id: "purchased", label: "My Purchased" },
];

export default function Sidebar({
  activeSection,
  setActiveSection,
}: SidebarProps) {
  const { account } = useContext(Web3);

  return (
    <div className="w-64 bg-white bg-opacity-10 p-6">
      <h1 className="text-2xl font-bold text-white mb-8">Energy NFT</h1>
      <nav>
        {menuItems.map((item) => (
          <button
            key={item.id}
            onClick={() => account && setActiveSection(item.id)} // Prevent action if no wallet connected
            className={`w-full flex items-center p-3 mb-4 rounded-lg text-white ${
              activeSection === item.id
                ? "bg-white bg-opacity-20"
                : "hover:bg-white hover:bg-opacity-10"
            } ${
              !account && ["create", "listed", "purchased"].includes(item.id)
                ? "cursor-not-allowed opacity-50" // Disable visual and interactivity
                : ""
            }`}
            disabled={
              !account && ["create", "listed", "purchased"].includes(item.id) // Disable button interaction
            }
          >
            {item.label}
          </button>
        ))}
      </nav>
    </div>
  );
}
