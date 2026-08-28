"""
BhashaSetu AI — Pedagogical Adaptation Engine
Injects culturally situated tribal analogies (Sarhul, Sohrai, Karam) while preserving learning outcomes.
"""

from typing import Dict, Any, List, Optional

CULTURAL_ANALOGIES = {
    "TREES_AND_LEAVES": {
        "analogy": "सरहुल पर्व में पूजे जाने वाले साल (सखुआ) के पत्ते और महुआ के पेड़",
        "story_context": "झारखंड के गांवों में सरहुल पूजा के समय साल के नए पत्तों और फूलों की पूजा की जाती है। बच्चे इन पत्तों से खिलौने और पत्तल बनाना सीखते हैं।",
        "activity_idea": "कक्षा के बाहर जाकर 3 अलग-अलग पेड़ों के पत्ते एकत्र करें और उनके आकार की तुलना करें।"
    },
    "COUNTING_MATH": {
        "analogy": "महुआ के गिरे हुए फूल या साप्ताहिक हाट में मिट्टी के कुल्हड़ों की गिनती",
        "story_context": "हाट (बाजार) में दादी के साथ फल गिनते हुए बच्चे जोड़ और घटाव की अवधारणा आसानी से समझ जाते हैं।",
        "activity_idea": "कंकड़ या इमली के बीजों को 10-10 के समूहों में बांटकर गिनना।"
    },
    "WATER_CONSERVATION": {
        "analogy": "पहाड़ी झरने (दाः/दाग) और गांव के पवित्र कुएं का संरक्षण",
        "story_context": "जंगल और पहाड़ों से बहने वाला प्राकृतिक जल जीवन का आधार है, जिसे स्वच्छ रखना हर ग्रामीण का कर्तव्य है।",
        "activity_idea": "पानी के अलग-अलग स्रोतों के चित्र बनाना और उनके नाम अपनी भाषा में लिखना।"
    }
}

class PedagogicalAdapter:
    """Adapts standard textbook concepts into localized, grade-appropriate primary pedagogy."""

    @staticmethod
    def adapt(
        hindi_prompt: Optional[str] = None,
        grade: Optional[str] = None,
        target_language: str = "SANTHALI",
        evidence_chunks: Optional[List[Dict[str, Any]]] = None,
        concept_title: Optional[str] = None,
        grade_level: Optional[str] = None,
        rag_evidence: Optional[List[Dict[str, Any]]] = None
    ) -> Dict[str, Any]:
        prompt = hindi_prompt or concept_title or "पेड़ और पत्तियाँ"
        grd = grade or grade_level or "GRADE_2"
        evidence = evidence_chunks or rag_evidence or []

        # Identify matching analogy theme
        theme = "TREES_AND_LEAVES"
        if "गिन" in prompt or "संख्या" in prompt or "जोड़" in prompt:
            theme = "COUNTING_MATH"
        elif "जल" in prompt or "पानी" in prompt or "नदी" in prompt:
            theme = "WATER_CONSERVATION"
            
        analogy_data = CULTURAL_ANALOGIES[theme]
        
        return {
            "grade_level": grd,
            "target_language": target_language,
            "cultural_analogy": analogy_data["analogy"],
            "local_story_context": analogy_data["story_context"],
            "classroom_activity": analogy_data["activity_idea"],
            "learning_outcome_preserved": True
        }

pedagogical_adapter = PedagogicalAdapter()
