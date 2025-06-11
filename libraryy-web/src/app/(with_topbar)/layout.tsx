import Navbar from "@/components/navbar";

export default function ModuleLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex flex-col w-screen h-screen">
      <Navbar />
      <div className="flex-1 p-3">{children}</div>
    </div>
  );
}
