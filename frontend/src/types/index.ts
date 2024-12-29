export interface NFT {
  id: number;
  title: string;
  price: string;
  energyAmount: number;
  seller: string;
  image: string;
  description: string;
  attributes: {
    trait_type: string;
    value: string | number;
    unit?: string;
  }[];
}

export interface SidebarProps {
  activeSection: string;
  setActiveSection: (section: string) => void;
}

export interface NFTGridProps {
  section: string;
}

export interface NFTCardProps {
  title: string;
  price: string;
  energyAmount: number;
  seller: string;
}

export interface BridgeTransaction {
  sourceChain: number;
  destinationChain: number;
  amount: string;
  token: string;
  recipient: string;
}
