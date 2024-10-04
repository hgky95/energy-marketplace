"use client";
import { useState } from "react";
import Sidebar from "./components/Sidebar";
import NFTGrid from "./components/NFTGrid";
import ConnectButton from "./components/ConnectButton";

export default function Home() {
  const [activeSection, setActiveSection] = useState<string>("home");

  return (
    <div className="flex min-h-screen">
      <Sidebar
        activeSection={activeSection}
        setActiveSection={setActiveSection}
      />
      <main className="flex-1 p-8">
        <div className="flex justify-end mb-6">
          <ConnectButton />
        </div>
        <NFTGrid section={activeSection} />
      </main>
    </div>
  );
}
