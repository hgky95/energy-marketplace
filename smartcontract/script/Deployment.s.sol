// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

import {Script, console} from "forge-std/Script.sol";
import {EnergyMarketplace} from "../src/EnergyMarketplace.sol";
import {EnergyNFT} from "../src/EnergyNFT.sol";
import {LoyaltyProgram} from "../src/LoyaltyProgram.sol";

contract Deployment is Script {
    EnergyMarketplace public energyMarketplace;
    EnergyNFT public energyNFT;
    LoyaltyProgram public loyaltyProgram;

    function setUp() public {}

    function run() public {
        vm.startBroadcast();

        // Deploy EnergyNFT
        energyNFT = new EnergyNFT();
        console.log("EnergyNFT deployed at:", address(energyNFT));

        // Deploy LoyaltyProgram
        loyaltyProgram = new LoyaltyProgram();
        console.log("LoyaltyProgram deployed at:", address(loyaltyProgram));

        // Deploy EnergyMarketplace with both NFT and LoyaltyProgram addresses
        energyMarketplace = new EnergyMarketplace(
            address(energyNFT),
            address(loyaltyProgram)
        );
        console.log(
            "EnergyMarketplace deployed at:",
            address(energyMarketplace)
        );

        // Set up contract relationships
        energyNFT.setMarketplaceAddress(address(energyMarketplace));
        console.log("Marketplace address set in NFT contract");

        // Authorize marketplace to update loyalty points
        loyaltyProgram.addAuthorizeCaller(address(energyMarketplace));
        console.log("Marketplace authorized in LoyaltyProgram");

        vm.stopBroadcast();
    }
}
