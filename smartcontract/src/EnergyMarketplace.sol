// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

import "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import {EnergyNFT} from "./EnergyNFT.sol";
import {console} from "forge-std/console.sol";

contract EnergyMarketplace is ReentrancyGuard, Ownable {
    EnergyNFT public nftContract;

    uint256 public itemCount;
    uint256 public marketplaceFeePercentage;

    struct Item {
        uint256 tokenId;
        uint256 price;
        uint256 energyAmount;
        address seller;
        bool isActive;
    }

    mapping(uint256 => Item) public items;

    event NFTMintedAndListed(
        uint256 tokenId,
        address seller,
        string ipfsHash,
        uint256 energyValue,
        uint256 price
    );
    event NFTSold(
        uint256 tokenId,
        address seller,
        address buyer,
        uint256 price,
        uint256 fee
    );
    event ListingUpdated(uint256 tokenId, uint256 newPrice);
    event ListingCancelled(uint256 tokenId);
    event MarketplaceFeeUpdated(uint256 newFeePercentage);
    event Withdrawal(address recipient, uint256 amount);

    constructor(address _nftContract) Ownable(msg.sender) {
        nftContract = EnergyNFT(_nftContract);
        marketplaceFeePercentage = 1;
    }

    function updateFee(uint256 _newFeePercentage) external onlyOwner {
        require(_newFeePercentage > 0, "Fee percentage cannot be less than 0%");
        marketplaceFeePercentage = _newFeePercentage;
        emit MarketplaceFeeUpdated(_newFeePercentage);
    }

    function mintAndList(
        string memory _tokenURI,
        uint256 _energyAmount,
        uint256 _price
    ) external returns (uint256) {
        console.log("minter: ", msg.sender);
        uint256 newTokenId = nftContract.mint(
            msg.sender,
            _tokenURI,
            _energyAmount
        );
        items[newTokenId] = Item(
            newTokenId,
            _price,
            _energyAmount,
            msg.sender,
            true
        );
        emit NFTMintedAndListed(
            newTokenId,
            msg.sender,
            _tokenURI,
            _energyAmount,
            _price
        );
        itemCount++;
        return newTokenId;
    }

    function buyNFT(uint256 _tokenId) external payable nonReentrant {
        Item storage item = items[_tokenId];
        require(item.isActive, "NFT not for sale");
        require(msg.value >= item.price, "Insufficient payment");

        address seller = item.seller;
        uint256 price = item.price;
        uint256 fee = (price * marketplaceFeePercentage) / 100;
        uint256 sellerProceeds = price - fee;
        console.log("fee: ", fee);

        // This flow apply checks-effects-interactions pattern
        // Mark item as inactive before making transfers (prevent reentrancy)
        item.isActive = false;

        nftContract.transferFrom(seller, msg.sender, _tokenId);
        console.log("transferred from seller to buyer");
        nftContract.transferEnergy(seller, msg.sender, _tokenId);
        console.log("transferred energy seller to buyer");
        payable(seller).transfer(sellerProceeds);

        emit NFTSold(_tokenId, seller, msg.sender, price, fee);
    }

    function withdrawFees(uint256 _amount) external onlyOwner {
        uint256 balance = address(this).balance;
        require(balance > _amount, "Balances is not enough to withdraw");

        (bool success, ) = payable(owner()).call{value: _amount}("");
        require(success, "Failed to withdraw fees");
        emit Withdrawal(owner(), _amount);
    }
}
