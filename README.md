# Energy Marketplace DApp

A decentralized marketplace for trading energy NFTs with an integrated loyalty program. Users can tokenize their energy production, trade energy NFTs, and earn loyalty points for marketplace participation.

## Features

- **Energy NFT Minting**: Users can mint NFTs representing their energy production
- **NFT Marketplace**: Trade energy NFTs with other users
- **Loyalty Program**: Earn points from marketplace activities and receive trading fee discounts
- **Web3 Integration**: Connect with MetaMask and other Web3 wallets

## Architecture
![energy-nft-flow-chart-main drawio](https://github.com/user-attachments/assets/4bc668fe-282c-451b-abfa-66707e829b27)

## Technology Stack

### Frontend

- Next.js
- ethers.js
- TailwindCSS

### Smart Contracts

- Solidity
- Foundry

## Prerequisites

- Node.js 18+
- Foundry
- MetaMask or another Web3 wallet

## Installation

1. Clone the repository:

```bash
git clone https://github.com/hgky95/energy-marketplace
```

2. Install frontend dependencies:

```bash
cd frontend
npm install
```

3. Install smart contract dependencies:

```bash
cd smartcontract
forge install
```

## Start the development server:

### Frontend

```bash
cd frontend
npm run dev
```

The application will be available at `http://localhost:3000`

### Smart Contracts

1. Compile contracts:

```bash
cd smartcontract
forge build
```

2. Deploy contracts:

```bash
forge script script/Deployment.s.sol --rpc-url <your-rpc-url> --private-key <your-private-key>
```

## Deployment

1. Deploy smart contracts to your chosen network:

```bash
cd smartcontract
forge script script/Deployment.s.sol --rpc-url <your-rpc-url> --private-key <your-private-key> --broadcast
```

2. Update frontend contract addresses:

- Copy the deployed contract addresses
- Update the addresses in your frontend environment variables

3. Deploy frontend:

```bash
cd frontend
npm run build
npm run start
```

## User Interfaces
![nft-listing](https://github.com/user-attachments/assets/048dc673-c9b4-4e5e-ad3b-943461630feb)
![mint-nft](https://github.com/user-attachments/assets/db040acc-b7fc-4366-8cbb-080d9cdcb3c5)
