"use client";
import { useUserStore } from "@/store/user";
import React, { useState } from "react";
import { ReactReader } from "react-reader";

export default function ReadPage() {
  const [location, setLocation] = useState<string | number>(0);
  const { current_book } = useUserStore();
  return (
    <div className="border p-2 h-full">
      <ReactReader
        url={current_book}
        location={location}
        locationChanged={(epubcfi: string) => setLocation(epubcfi)}
      />
    </div>
  );
}
