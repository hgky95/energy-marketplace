import type { NextApiRequest, NextApiResponse } from "next";
import { PinataSDK } from "pinata-web3";

const pinata = new PinataSDK({
  pinataJwt: process.env.PINATA_JWT,
  pinataGateway: process.env.NEXT_PUBLIC_PINATA_GATEWAY,
});
const PINATA_GROUP_ID = process.env.PINATA_GROUP_ID || "";
const IMAGE_HOST_URL = process.env.NEXT_PUBLIC_PINATA_GATEWAY + "/ipfs/";
const SOLAR_IMAGES = [
  "QmYESiiXXG4yepPygzfZR1aUEQQNXAMTM9uja22C9QZEN8",
  "Qmbx2JtJb4iLF6NXtE4TJnmojkGRRufDAy2HQpFKnoFLu2",
];

const WIND_IMAGES = [
  "QmajEwgzQWorQTjBLSAJnVzFsk8KfoNftncD3Gs4Uzkxxk",
  "QmYKjt8EjWK8YbgsErkgAXZsVRCpvydET3QSpkbNPhDSUS",
];

const getRandomImage = (energySource: string) => {
  let images;
  if (energySource === "Solar") {
    images = SOLAR_IMAGES;
  } else if (energySource === "Wind") {
    images = WIND_IMAGES;
  } else {
    throw new Error("Unsupported energy source");
  }

  const randomIndex = Math.floor(Math.random() * images.length);
  return images[randomIndex];
};

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method === "POST") {
    let energyAmount = req.body.energyAmount;
    let energySource = req.body.energySource;
    let location = req.body.location;
    try {
      let image_url = IMAGE_HOST_URL;
      image_url = image_url + getRandomImage(energySource);
      const upload = await pinata.upload
        .json({
          description: `Energy NFT representing ${energyAmount} kW of ${energySource} from ${location}`,
          image: image_url,
          attributes: [
            {
              trait_type: "Energy Amount",
              value: energyAmount,
              unit: "kW",
            },
            {
              trait_type: "Energy Source",
              value: energySource,
            },
            {
              trait_type: "Location",
              value: location,
            },
          ],
        })
        .group(PINATA_GROUP_ID);

      console.log("NFT Metadata uploaded to IPFS:", upload);
      res.status(200).json({ ipfsHash: upload.IpfsHash });
    } catch (error) {
      console.error("Error uploading to Pinata:", error);
      res.status(500).json({ error: "Error uploading to IPFS" });
    }
  } else {
    res.setHeader("Allow", ["POST"]);
    res.status(405).end(`Method ${req.method} Not Allowed`);
  }
}
