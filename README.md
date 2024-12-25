# Energy Marketplace DApp

A decentralized marketplace for trading energy NFTs with an integrated loyalty program. Users can tokenize their energy production, trade energy NFTs, and earn loyalty points for marketplace participation.

## Features

- **Energy NFT Minting**: Users can mint NFTs representing their energy production
- **NFT Marketplace**: Trade energy NFTs with other users
- **Loyalty Program**: Earn points from marketplace activities and receive trading fee discounts
- **Web3 Integration**: Connect with MetaMask and other Web3 wallets

## Architecture

![energy-nft-flow-chart-main drawio](https://github.com/user-attachments/assets/3ebf1f29-0c93-43c6-a825-63c0f6e5f0d8)

## Demonstration

Here is the demo video: [Youtube](https://youtu.be/Rza_KoaK9N4)

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
- Gradle

## Prerequisites

- Node.js 18+
- Java 21.0.5
- Foundry
- MetaMask or another Web3 wallet

## Installation

1. Clone the repository:

```bash
git clone https://github.com/hgky95/energy-marketplace
```

2. Define the private key in the .env file (local development only):

```bash
PRIVATE_KEY=
```

    Note: To prevent the leak of the private key, please use 'cast wallet import <name> --interactive' to import the private key into the wallet.
    Refer to [Import Key](https://book.getfoundry.sh/reference/cast/cast-wallet-import) for more details.

3. Using docker-compose to start the blockchain node, backend, and frontend:

```bash
docker-compose up
```

3. The application will be available at http://localhost:3000
