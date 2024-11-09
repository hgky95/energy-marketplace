// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

import "forge-std/Test.sol";
import "../src/EnergyMarketplace.sol";
import "../src/EnergyNFT.sol";
import "../src/LoyaltyProgram.sol";

contract LoyaltyProgramTest is Test {
    LoyaltyProgram public loyaltyProgram;
    address authorizer;
    address unauthorizer;
    address user;

    struct Discount {
        uint256 points;
        uint8 discountPercentage;
    }

    function setUp() public {
        loyaltyProgram = new LoyaltyProgram();
        authorizer = makeAddr("authorizer");
        unauthorizer = makeAddr("unauthorizer");
        user = makeAddr("user");
    }

    function testAddAuthorizeCaller_returnTrue() public {
        loyaltyProgram.addAuthorizeCaller(authorizer);
        assertTrue(loyaltyProgram.authorizedCallers(authorizer));
    }

    function testRevokeCaller_returnFalse() public {
        loyaltyProgram.addAuthorizeCaller(authorizer);
        assertTrue(loyaltyProgram.authorizedCallers(authorizer));

        loyaltyProgram.revokeCaller(authorizer);
        assertFalse(loyaltyProgram.authorizedCallers(authorizer));
    }

    function testAddLoyaltyPoints_unauthorizeUser_revert() public {
        vm.expectRevert(bytes("Not authorized"));
        vm.startPrank(unauthorizer);
        loyaltyProgram.addLoyaltyPoints(user, 500);
        vm.stopPrank();
    }

    function testAddLoyaltyPoints_returnUpdatedPoints() public {
        loyaltyProgram.addAuthorizeCaller(authorizer);
        assertTrue(loyaltyProgram.authorizedCallers(authorizer));

        // First 500 points
        vm.startPrank(authorizer);
        loyaltyProgram.addLoyaltyPoints(user, 500);
        uint256 firstLoyaltyPoint = loyaltyProgram.getLoyaltyPoints(user);
        assertEq(500, firstLoyaltyPoint);
        uint256 firstCommisionRate = loyaltyProgram.getCommissionRate(
            firstLoyaltyPoint,
            1
        );
        assertEq(100, firstCommisionRate);

        // Add more 1000 pts
        loyaltyProgram.addLoyaltyPoints(user, 1000);
        uint256 secondLoyaltyPoint = loyaltyProgram.getLoyaltyPoints(user);
        assertEq(1500, secondLoyaltyPoint);
        uint256 secondCommissionRate = loyaltyProgram.getCommissionRate(
            secondLoyaltyPoint,
            1
        );
        assertEq(95, secondCommissionRate);

        // Add more 4000 pts
        loyaltyProgram.addLoyaltyPoints(user, 4000);
        uint256 thirdLoyaltyPoint = loyaltyProgram.getLoyaltyPoints(user);
        assertEq(5500, thirdLoyaltyPoint);
        uint256 thirdCommissionRate = loyaltyProgram.getCommissionRate(
            thirdLoyaltyPoint,
            1
        );
        assertEq(92, thirdCommissionRate);
        vm.stopPrank();

        // Add more 4500 pts
        loyaltyProgram.addLoyaltyPoints(user, 4500);
        uint256 latestLoyaltyPoint = loyaltyProgram.getLoyaltyPoints(user);
        assertEq(10000, latestLoyaltyPoint);
        uint256 latestCommissionRate = loyaltyProgram.getCommissionRate(
            latestLoyaltyPoint,
            1
        );
        assertEq(90, latestCommissionRate);
        vm.stopPrank();
    }

    function testAddDiscountTier_invalidValue_revert() public {
        vm.expectRevert(bytes("Discount cannot exceed 100%"));
        loyaltyProgram.addDiscountTier(100, 101);
    }

    function testAddDiscountTier_validValue_addNewTier() public {
        loyaltyProgram.addDiscountTier(15000, 12);
        loyaltyProgram.addDiscountTier(18000, 15);
        (uint256 points3, uint8 discountPercentage3) = loyaltyProgram
            .discountTiers(3);
        (uint256 points4, uint8 discountPercentage4) = loyaltyProgram
            .discountTiers(4);

        assertEq(15000, points3);
        assertEq(12, discountPercentage3);
        assertEq(18000, points4);
        assertEq(15, discountPercentage4);
    }

    function testRemoveDiscountTier_invalidValue_revert() public {
        vm.expectRevert(bytes("Invalid tier index"));
        loyaltyProgram.removeDiscountTier(4);
    }
}
