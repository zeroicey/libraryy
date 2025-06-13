import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  eslint: {
    ignoreDuringBuilds: true,
  },

  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    domains: [
      "img1.doubanio.com",
      "img9.doubanio.com",
      "img2.doubanio.com",
      "img3.doubanio.com",
    ],
  },
};

export default nextConfig;
