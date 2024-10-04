import React from "react";
import { SidebarProps } from "../../types";

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
  return (
    <div className="w-64 bg-white bg-opacity-10 p-6">
      <h1 className="text-2xl font-bold text-white mb-8">Energy NFT</h1>
      <nav>
        {menuItems.map((item) => (
          <button
            key={item.id}
            onClick={() => setActiveSection(item.id)}
            className={`w-full flex items-center p-3 mb-4 rounded-lg text-white ${
              activeSection === item.id
                ? "bg-white bg-opacity-20"
                : "hover:bg-white hover:bg-opacity-10"
            }`}
          >
            {item.label}
          </button>
        ))}
      </nav>
    </div>
  );
}
