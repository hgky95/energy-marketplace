// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract EnergyNFT is ERC721URIStorage, Ownable {
    uint256 private _tokenIds;
    mapping(address => uint256) public userEnergyBalances;

    event EnergyNFTMinted(uint256 tokenId, address owner, string ipfsHash);
    event EnergyBalanceUpdated(address user, uint256 newBalance);
    event EnergyProduced(address user, uint256 amount);

    constructor() ERC721("Energy NFT", "ENFT") Ownable(msg.sender) {}

    // TODO this method should be interact with trusted oracle or energy meter
    function produceEnergy(
        address user,
        uint256 _energyAmount
    ) external onlyOwner {
        userEnergyBalances[user] += _energyAmount;
        emit EnergyProduced(user, _energyAmount);
        emit EnergyBalanceUpdated(user, userEnergyBalances[user]);
    }

    function mint(
        address _from,
        string memory _tokenURI,
        uint256 _energyAmount
    ) external returns (uint) {
        require(_energyAmount > 0, "Energy value should be greater than 0");
        require(
            userEnergyBalances[_from] >= _energyAmount,
            "Insufficient energy balance!!!"
        );
        _tokenIds++;
        _safeMint(_from, _tokenIds);
        _setTokenURI(_tokenIds, _tokenURI);
        userEnergyBalances[_from] -= _energyAmount;

        emit EnergyNFTMinted(_tokenIds, _from, _tokenURI);
        emit EnergyBalanceUpdated(_from, userEnergyBalances[_from]);

        return (_tokenIds);
    }

    function transferEnergy(
        address _from,
        address _to,
        uint256 _energyAmount
    ) external {
        require(
            userEnergyBalances[_from] - _energyAmount > 0,
            "Insufficient energy!!!"
        );
        userEnergyBalances[_from] -= _energyAmount;
        userEnergyBalances[_to] += _energyAmount;
        emit EnergyBalanceUpdated(_from, userEnergyBalances[_from]);
        emit EnergyBalanceUpdated(_to, userEnergyBalances[_to]);
    }

    function getCurrentEnergy(address user) public view returns (uint256) {
        return userEnergyBalances[user];
    }
}
