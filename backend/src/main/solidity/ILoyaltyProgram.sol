// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;

interface ILoyaltyProgram {
    function getCommissionRate(
        uint256 loyaltyPoints,
        uint8 baseCommissionRate
    ) external view returns (uint256);

    function addLoyaltyPoints(address user, uint32 points) external;

    function getLoyaltyPoints(address user) external view returns (uint256);

    function updateDiscountTier(
        uint256 index,
        uint256 points,
        uint8 discountPercentage
    ) external;

    function addDiscountTier(uint256 points, uint8 discountPercentage) external;

    function removeDiscountTier(uint256 index) external;
}
