import { AcrossClient, Quote } from "@across-protocol/app-sdk";
import { ethers } from "ethers";
import {
  baseSepolia,
  sepolia,
  arbitrumSepolia,
  optimismSepolia,
} from "viem/chains";
import { Address, createWalletClient, custom, Hash, parseEther } from "viem";
import { toAccount } from "viem/accounts";

const chains = [baseSepolia, sepolia, arbitrumSepolia, optimismSepolia];

const networkNameToChain: { [key: string]: (typeof chains)[number] } = {
  "base-sepolia": baseSepolia,
  sepolia: sepolia,
  "arbitrum-sepolia": arbitrumSepolia,
  "optimism-sepolia": optimismSepolia,
};

export class BridgeService {
  private sdk: AcrossClient;

  constructor() {
    this.sdk = AcrossClient.create({
      chains,
      useTestnet: true,
      logLevel: "ERROR",
    });
  }

  async getQuote(params: {
    account: string;
    amount: string;
    sourceChainId: number;
    destinationChainId: number;
  }) {
    const routes = await this.sdk.getAvailableRoutes({
      originChainId: params.sourceChainId,
      destinationChainId: params.destinationChainId,
    });

    const route = routes.find((r) => r.inputTokenSymbol === "ETH")!;

    return await this.sdk.getQuote({
      route,
      inputAmount: ethers.parseEther(params.amount),
      recipient: params.account as `0x${string}`,
    });
  }

  async bridge(params: { quote: Quote; account: string; provider: any }) {
    const { quote, account, provider } = params;

    const networkName = await provider
      .getNetwork()
      .then((network: { name: any }) => network.name);
    const chain = networkNameToChain[networkName];

    if (!chain) {
      throw new Error(`Unsupported network: ${networkName}`);
    }

    const walletClient = createWalletClient({
      account: toAccount(account as Address),
      chain: chain,
      transport: custom(window.ethereum!),
    });

    const { request } = await this.sdk.simulateDepositTx({
      walletClient,
      deposit: quote.deposit,
    });

    return await walletClient.writeContract(request);
  }

  async waitForDeposit(params: {
    transactionHash: `0x${string}`;
    originChainId: number;
  }) {
    return await this.sdk.waitForDepositTx(params);
  }

  async waitForFill(params: {
    depositId: number;
    deposit: any;
    fromBlock: bigint;
  }) {
    return await this.sdk.waitForFillTx(params);
  }

  async getDestinationBlock(chainId: number) {
    return await this.sdk.getPublicClient(chainId).getBlockNumber();
  }
}
