export default function WithoutTopbarLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <div>{children}</div>;
}
