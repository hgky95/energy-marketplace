// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

import {Script, console} from "forge-std/Script.sol";
import {EnergyMarketplace} from "../src/EnergyMarketplace.sol";
import {EnergyNFT} from "../src/EnergyNFT.sol";

contract Deployment is Script {
    EnergyMarketplace public energyMarketplace;
    EnergyNFT public energyNFT;

    function setUp() public {}

    function run() public {
        vm.startBroadcast();

        energyNFT = new EnergyNFT();
        console.log("EnergyNFT deployed at:", address(energyNFT));

        energyMarketplace = new EnergyMarketplace(address(energyNFT));
        console.log(
            "EnergyMarketplace deployed at:",
            address(energyMarketplace)
        );

        energyNFT.setMarketplaceAddress(address(energyMarketplace));
        console.log("Marketplace address set in NFT contract");

        vm.stopBroadcast();
    }
}
