# Energy Marketplace DApp

A decentralized marketplace for trading energy NFTs with an integrated loyalty program on Base. Users can tokenize their energy production, trade energy NFTs, and earn loyalty points for marketplace participation.
Additionally, the platform enables seamless asset bridging between L2s and L1 networks directly within the app.

## Features

- **Energy NFT Minting**: Users can mint NFTs representing their energy production
- **NFT Marketplace**: Trade energy NFTs with other users
- **Cross-chain bridge**: Bridge asset (ETH) from source chains (Ethereum, Optimism, Arbitrum, etc) to destination chain (Base), powered by Across Protocol
- **Loyalty Program**: Earn points from marketplace activities and receive trading fee discounts
- **Web3 Integration**: Connect with MetaMask and other Web3 wallets

## Architecture

![energy-nft-flow-chart-main drawio (1)](https://github.com/user-attachments/assets/be1520e2-6d44-4546-9d44-e4a1aff797c4)

## Demonstration

Here is the demo video: [Youtube](https://youtu.be/dtCQmJSXIFc)

## Technology Stack

### Frontend

- Next.js
- ethers.js
- TailwindCSS

### Smart Contracts

- Solidity
- Foundry

### Backend

- Java
- Spring Boot
- Web3j
- Gradle

## Prerequisites

- Node.js 18+
- Java 21.0.5
- Foundry
- MetaMask

## Installation

1. Clone the repository:

```bash
git clone https://github.com/hgky95/energy-marketplace
```

2. Define the private key in the .env file (local development only):

```bash
PRIVATE_KEY=
```

    Note: For security purpose, please use 'cast wallet import <name> --interactive' to import the private key into the wallet.
    Refer to [Import Key](https://book.getfoundry.sh/reference/cast/cast-wallet-import) for more details.

3. Using docker-compose to start the blockchain node, backend, and frontend:

```bash
docker-compose up
```

3. The application will be available at http://localhost:3000
