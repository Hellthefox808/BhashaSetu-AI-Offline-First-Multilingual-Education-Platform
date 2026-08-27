import React from 'react';
import './globals.css';

export const metadata = {
  title: 'BhashaSetu AI — Multilingual MTB-MLE Platform',
  description: 'Bridging Hindi to Santhali, Ho, and Mundari in Jharkhand Classrooms',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="bg-slate-50 text-slate-900 min-h-screen flex flex-col font-sans">
        <header className="bg-[#1e5128] text-white py-4 px-6 shadow-md flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <span className="text-2xl">🌉</span>
            <div>
              <h1 className="font-bold text-lg tracking-wide">BhashaSetu AI (भाषासेतु)</h1>
              <p className="text-xs text-emerald-200">Mother-Tongue-Based Multilingual Education · SIH26042</p>
            </div>
          </div>
          <div className="flex items-center space-x-4 text-xs font-semibold">
            <span className="bg-emerald-800/80 px-3 py-1 rounded-full border border-emerald-500/30">Offline-First Engine</span>
            <span className="bg-amber-500/20 text-amber-300 px-3 py-1 rounded-full border border-amber-500/30">Jharkhand JCERT</span>
          </div>
        </header>
        <main className="flex-1 p-6 max-w-7xl mx-auto w-full">
          {children}
        </main>
        <footer className="bg-slate-100 text-slate-500 text-xs py-4 px-6 text-center border-t border-slate-200">
          BhashaSetu AI · NIPUN Bharat MTB-MLE Initiative · Smart India Hackathon 2026
        </footer>
      </body>
    </html>
  );
}
