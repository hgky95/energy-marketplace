export interface NFT {
  id: number;
  title: string;
  price: string;
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
}
