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

    constructor(address _nftContract) Ownable(msg.sender) {
        nftContract = EnergyNFT(_nftContract);
        marketplaceFeePercentage = 1;
    }

    function updateFee(uint256 _newFeePercentage) external onlyOwner {
        require(_newFeePercentage > 0, "Fee percentage cannot less than 0%");
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

    function buyNFT(uint256 _tokenId) external payable {
        //Use storage to modify state of isActive
        Item storage item = items[_tokenId];
        require(item.isActive, "NFT not for sale");
        console.log("msg.sender: ", msg.sender);
        console.log("msg.value: ", msg.value);
        console.log("item.price: ", item.price);
        require(msg.value >= item.price, "Insufficient payment");

        //Transfer NFT to buyer
        console.log("address(this)", address(this));
        nftContract.transferFrom(address(this), msg.sender, _tokenId);

        address seller = item.seller;
        uint256 price = item.price;
        uint256 fee = price * (marketplaceFeePercentage / 100);
        console.log("Fee: ", fee);
        uint256 sellerProceeds = price - fee;
        console.log("sellerProceeds: ", sellerProceeds);

        //Transfer payment to seller
        payable(seller).transfer(sellerProceeds);
        console.log("Seller paid!!!");
        //Transfer fee to marketplace owner
        payable(owner()).transfer(fee);
        console.log("Transfered fee to market owner!!!");

        nftContract.transferEnergy(seller, msg.sender, item.energyAmount);

        item.isActive = false;
        emit NFTSold(_tokenId, seller, msg.sender, price, fee);
    }
}
