import { useState, useEffect, useCallback, useContext } from "react";
import { ethers } from "ethers";

export const useLoyaltyPoints = (
  account: string | "",
  loyaltyProgram: ethers.Contract | null
) => {
  const [points, setPoints] = useState<number>(0);

  const fetchPoints = useCallback(async () => {
    if (!account || !loyaltyProgram) return;
    try {
      const userPoints = await loyaltyProgram.getLoyaltyPoints(account);
      setPoints(Number(userPoints));
    } catch (error) {
      console.error("Error fetching loyalty points:", error);
    }
  }, [account, loyaltyProgram]);

  const handlePointsAdded = async (event: any) => {
    console.log("Event LoyaltyPointsAdded is triggered");
    if (event.args[0].toLowerCase() === account.toLowerCase()) {
      setPoints(Number(event.args[1]));
    }
  };

  // Listen to loyalty points events
  useEffect(() => {
    if (!loyaltyProgram || !account) return;

    // Fetch initial points
    fetchPoints();

    const pointsFilter = loyaltyProgram.filters.LoyaltyPointsAdded();
    console.log("Subscribed LoyaltyPointsAdded");
    loyaltyProgram.on(pointsFilter, handlePointsAdded);

    return () => {
      console.log("Unsubscribed LoyaltyPointsAdded");
      loyaltyProgram.off(pointsFilter, () => {});
    };
  }, [loyaltyProgram, account]);

  return points;
};
