"use client";
import React, { useState } from "react";
import { ReactReader } from "react-reader";

export default function ReadPage() {
  const [location, setLocation] = useState<string | number>(0);
  return (
    <div className="border p-2 h-full">
      <ReactReader
        url="https://react-reader.metabits.no/files/alice.epub"
        location={location}
        locationChanged={(epubcfi: string) => setLocation(epubcfi)}
      />
    </div>
  );
}
