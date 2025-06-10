import MobileMenu from "@/components/mobile-menu";
import { Navbar } from "@/components/navbar";
import { SidebarProvider } from "@/components/ui/sidebar";

export default function ModuleLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <SidebarProvider defaultOpen={false}>
      <MobileMenu />
      <main className="w-full min-h-screen">
        <Navbar />
        {children}
      </main>
    </SidebarProvider>
  );
}
