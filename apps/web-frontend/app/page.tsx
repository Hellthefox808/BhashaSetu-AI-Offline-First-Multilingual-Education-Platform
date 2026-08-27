import React from 'react';

export default function HomePage() {
  return (
    <div className="space-y-8">
      <section className="bg-white p-8 rounded-2xl shadow-sm border border-slate-200">
        <div className="inline-block bg-emerald-100 text-emerald-800 text-xs font-bold px-3 py-1 rounded-full mb-3">
          Field-Validated MTB-MLE Architecture
        </div>
        <h2 className="text-2xl font-bold text-slate-800">
          Empowering Hindi Teachers with Tribal Language Intelligence
        </h2>
        <p className="text-slate-600 mt-2 max-w-3xl">
          BhashaSetu AI translates and pedagogically adapts Hindi primary school curriculum into 
          <strong className="text-slate-800"> Santhali (Ol Chiki)</strong>, 
          <strong className="text-slate-800"> Ho (Warang Chiti)</strong>, and 
          <strong className="text-slate-800"> Mundari</strong> with local cultural analogies, offline persistence, and sub-3-second voice turnaround.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
          <div className="p-5 rounded-xl border border-slate-100 bg-slate-50">
            <div className="text-3xl mb-2">🎙️</div>
            <h3 className="font-bold text-slate-800">Live Voice Translate</h3>
            <p className="text-xs text-slate-600 mt-1">Bidirectional voice pipeline with under 3s latency for classroom dialogue.</p>
          </div>
          <div className="p-5 rounded-xl border border-slate-100 bg-slate-50">
            <div className="text-3xl mb-2">📚</div>
            <h3 className="font-bold text-slate-800">Curriculum RAG Studio</h3>
            <p className="text-xs text-slate-600 mt-1">NIPUN Bharat & JCERT aligned lesson scaffolding with cultural grounding.</p>
          </div>
          <div className="p-5 rounded-xl border border-slate-100 bg-slate-50">
            <div className="text-3xl mb-2">📡</div>
            <h3 className="font-bold text-slate-800">Offline-First Outbox</h3>
            <p className="text-xs text-slate-600 mt-1">100% functional in remote classrooms with idempotent sync on reconnection.</p>
          </div>
        </div>
      </section>
    </div>
  );
}
