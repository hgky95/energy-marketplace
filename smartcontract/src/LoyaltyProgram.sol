// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

import "@openzeppelin/contracts/access/Ownable.sol";
import "./ILoyaltyProgram.sol";

contract LoyaltyProgram is ILoyaltyProgram, Ownable {
    // use higher precision (2 decimal places in this case) - fixed-point arithmetic
    uint256 private constant PRECISION = 100;

    struct Discount {
        uint256 points;
        uint8 discountPercentage;
    }

    Discount[] public discountTiers;
    mapping(address => uint256) private userPoints;
    mapping(address => bool) public authorizedCallers;

    event DiscountTierAdded(uint256 points, uint8 discountPercentage);
    event DiscountTierUpdated(
        uint256 index,
        uint256 points,
        uint8 discountPercentage
    );
    event DiscountTierRemoved(uint256 index);
    event LoyaltyPointsAdded(address user, uint256 points);
    event CallerAuthorized(address caller);
    event CallerRevoked(address caller);

    modifier onlyAuthorized() {
        require(
            authorizedCallers[msg.sender] || msg.sender == owner(),
            "Not authorized"
        );
        _;
    }

    constructor() Ownable(msg.sender) {
        discountTiers.push(Discount(1000, 5));
        discountTiers.push(Discount(5000, 8));
        discountTiers.push(Discount(10000, 10));
    }

    function addAuthorizeCaller(address caller) external onlyOwner {
        authorizedCallers[caller] = true;
        emit CallerAuthorized(caller);
    }

    function revokeCaller(address caller) external onlyOwner {
        authorizedCallers[caller] = false;
        emit CallerRevoked(caller);
    }

    function getCommissionRate(
        uint256 loyaltyPoints,
        uint8 baseCommissionRate
    ) external view override returns (uint256) {
        uint256 discountPercentage = 0;

        for (uint256 i = 0; i < discountTiers.length; i++) {
            if (loyaltyPoints >= discountTiers[i].points) {
                discountPercentage = discountTiers[i].discountPercentage;
            } else {
                break;
            }
        }

        return
            (baseCommissionRate * PRECISION * (100 - discountPercentage)) / 100;
    }

    function addLoyaltyPoints(
        address user,
        uint32 points
    ) external override onlyAuthorized {
        userPoints[user] += points;
        emit LoyaltyPointsAdded(user, userPoints[user]);
    }

    function getLoyaltyPoints(
        address user
    ) external view override returns (uint256) {
        return userPoints[user];
    }

    function updateDiscountTier(
        uint256 index,
        uint256 points,
        uint8 discountPercentage
    ) external override onlyOwner {
        require(index < discountTiers.length, "Invalid tier index");
        require(discountPercentage <= 100, "Discount cannot exceed 100%");
        discountTiers[index] = Discount(points, discountPercentage);
        emit DiscountTierUpdated(index, points, discountPercentage);
    }

    function addDiscountTier(
        uint256 points,
        uint8 discountPercentage
    ) external override onlyOwner {
        require(discountPercentage <= 100, "Discount cannot exceed 100%");
        discountTiers.push(Discount(points, discountPercentage));
        emit DiscountTierAdded(points, discountPercentage);
    }

    function removeDiscountTier(uint256 index) external override onlyOwner {
        require(index < discountTiers.length, "Invalid tier index");
        for (uint256 i = index; i < discountTiers.length - 1; i++) {
            discountTiers[i] = discountTiers[i + 1];
        }
        discountTiers.pop();
        emit DiscountTierRemoved(index);
    }
}
