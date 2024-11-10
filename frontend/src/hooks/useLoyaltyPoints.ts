import { useState, useEffect, useCallback, useContext } from "react";
import { ethers } from "ethers";
// import { Web3 } from "./Web3";

export const useLoyaltyPoints = (
  account: string | "",
  loyaltyProgram: ethers.Contract | null
) => {
  //   const { account, loyaltyProgram } = useContext(Web3);
  const [points, setPoints] = useState<number>(0);

  // Fetch initial points
  const fetchPoints = useCallback(async () => {
    if (!account || !loyaltyProgram) return;
    try {
      const userPoints = await loyaltyProgram.getLoyaltyPoints(account);
      setPoints(Number(userPoints));
    } catch (error) {
      console.error("Error fetching loyalty points:", error);
    }
  }, [account, loyaltyProgram]);

  // Listen to loyalty points events
  useEffect(() => {
    if (!loyaltyProgram || !account) return;

    const pointsFilter = loyaltyProgram.filters.LoyaltyPointsAdded();

    const handlePointsAdded = async (event: any) => {
      if (event.args[0] === account.toLowerCase()) {
        setPoints(Number(event.args[1]));
      }
    };

    loyaltyProgram.on(pointsFilter, handlePointsAdded);
    fetchPoints();

    return () => {
      loyaltyProgram.off(pointsFilter, handlePointsAdded);
    };
  }, [loyaltyProgram, account, fetchPoints]);

  return points;
};
