"use client";
import { useUserStore } from "@/store/user";
import React, { useState, useEffect } from "react";
import { ReactReader } from "react-reader";

export default function ReadPage() {
  const [location, setLocation] = useState<string | number>(0);
  const { current_book } = useUserStore();

  useEffect(() => {
    const triggerReadingRequest = async () => {
      try {
        const response = await fetch("http://localhost:8000/reading");
        if (!response.ok) {
          console.error("Failed to trigger reading request:", response.status, response.statusText);
        }
        // No need to process the response data as per requirement
      } catch (error) {
        console.error("Error triggering reading request:", error);
      }
    };

    triggerReadingRequest();
  }, []);

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
