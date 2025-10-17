import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'DietMe - Diet Mentoring Platform',
  description: 'Connect with your dietitian and track your progress',
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  )
}
