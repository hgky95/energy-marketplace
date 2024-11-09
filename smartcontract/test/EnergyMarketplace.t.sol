// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

import "forge-std/Test.sol";
import "../src/EnergyMarketplace.sol";
import "../src/EnergyNFT.sol";
import "../src/LoyaltyProgram.sol";

contract EnergyMarketplaceTest is Test {
    EnergyMarketplace public marketplace;
    EnergyNFT public nft;
    LoyaltyProgram public loyaltyProgram;
    address public seller;
    address public buyer;

    function setUp() public {
        seller = makeAddr("seller");
        buyer = makeAddr("buyer");
        vm.deal(buyer, 10 ether);
        vm.deal(seller, 1 ether);

        nft = new EnergyNFT();
        loyaltyProgram = new LoyaltyProgram();
        marketplace = new EnergyMarketplace(
            address(nft),
            address(loyaltyProgram)
        );
        nft.setMarketplaceAddress(address(marketplace));

        // Give seller some energy
        nft.produceEnergy(seller, 100000);
    }

    function testBuyOneNFTWithETH() public {
        vm.startPrank(seller);
        nft.setApprovalForAll(address(marketplace), true);

        // list NFT
        uint256 tokenId = marketplace.mintAndList("uri", 500, 1 ether);
        uint256 secondTokenId = marketplace.mintAndList("uri", 3000, 2 ether);
        uint256 thirdTokenId = marketplace.mintAndList("uri", 50000, 3 ether);
        uint256 fourthTokenId = marketplace.mintAndList("uri", 1000, 2 ether);
        vm.stopPrank();

        vm.startPrank(address(loyaltyProgram.owner()));
        loyaltyProgram.addAuthorizeCaller(address(marketplace));
        vm.stopPrank();

        // Buy NFT with ETH
        vm.startPrank(buyer);
        marketplace.buyNFT{value: 1 ether}(tokenId);
        vm.stopPrank();

        // Verify loyalty pts and balance
        assertEq(loyaltyProgram.getLoyaltyPoints(seller), 50);
        assertEq(address(marketplace).balance, 0.01 ether); // 1% fee

        // Buy NFT with ETH
        vm.startPrank(buyer);
        marketplace.buyNFT{value: 2 ether}(secondTokenId);
        vm.stopPrank();

        // Verify loyalty pts and balance
        assertEq(loyaltyProgram.getLoyaltyPoints(seller), 350);
        assertEq(address(marketplace).balance, 0.03 ether);

        // Buy NFT with ETH
        vm.startPrank(buyer);
        marketplace.buyNFT{value: 3 ether}(thirdTokenId);
        vm.stopPrank();

        // At this step, the loyalty pts just get over 5000
        assertEq(address(marketplace).balance, 0.06 ether);
        // Verify loyalty pts and balance
        assertEq(loyaltyProgram.getLoyaltyPoints(seller), 5350);

        // Buy NFT with ETH
        vm.startPrank(buyer);
        marketplace.buyNFT{value: 2 ether}(fourthTokenId);
        vm.stopPrank();

        // Verify loyalty pts and balance
        assertEq(loyaltyProgram.getLoyaltyPoints(seller), 5450);
        // Get discount 8% on the current base commission since loyalty point > 5000
        assertEq(address(marketplace).balance, 0.0784 ether);
    }
}
