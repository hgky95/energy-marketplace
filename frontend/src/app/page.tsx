// src/app/page.tsx
"use client";

import { useState } from "react";
import Sidebar from "./components/Sidebar";
import NFTGrid from "./components/NFTGrid";
import ConnectButton from "./components/ConnectButton";
import { Web3Provider } from "./components/Web3";
import * as APP_CONSTANT from "../constants/AppConstant";

export default function Home() {
  const [activeSection, setActiveSection] = useState<string>(
    APP_CONSTANT.HOME_MENU_ID
  );

  return (
    <Web3Provider>
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
    </Web3Provider>
  );
}
