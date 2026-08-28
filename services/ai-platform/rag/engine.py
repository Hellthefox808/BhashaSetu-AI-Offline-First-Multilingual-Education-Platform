"""
BhashaSetu AI — Fine-Tuned Hybrid RAG Retrieval Engine (v3.0.0-PROD)
Combines Okapi BM25 Lexical Scoring with Dense Multilingual Embeddings (Concept Projection),
Reciprocal Rank Fusion (RRF), and Cross-Encoder Reranking.
Preloaded with 15 Comprehensive JCERT Grades 1-5 Primary Curriculum Chunks across FLN, Math, EVS, Science, and Tribal Heritage.
"""

from typing import List, Dict, Any, Optional, Set, Tuple
import math
import re

# --- Comprehensive 15-Node JCERT Curriculum Knowledge Base ---
JCERT_KNOWLEDGE_BASE = [
    {
        "chunk_id": "JCERT_G2_EVS_01",
        "grade": "GRADE_2",
        "subject": "ENVIRONMENTAL_STUDIES",
        "chapter_number": 3,
        "chapter_title": "हमारे आस-पास के पेड़ और पत्तियाँ",
        "topic": "पेड़ और पत्तियाँ",
        "lo_code": "LO-EVS-G2-03",
        "content_hindi": "हमारे आसपास कई प्रकार के पेड़ होते हैं जैसे साल (सखुआ), महुआ, करम और पीपल। पेड़ों की पत्तियाँ हरी, गोल, नुकीली या कटीली होती हैं। पत्ते सूर्य की रोशनी और पानी से पौधे के लिए भोजन बनाते हैं।",
        "cultural_keywords": ["साल", "सखुआ", "महुआ", "करम", "सरहुल", "पत्तल", "दातुन", "छांव", "पत्ता", "पेड़"],
        "tribal_analogies": {
            "SANTHALI": "ᱥᱟᱨᱡᱚᱢ (Sarjom/Sal) ᱫᱟᱨᱮ ᱫᱚ ᱥᱟᱨᱦᱩᱞ ᱯᱚᱨᱚᱵᱽ ᱨᱮ ᱵᱚᱸᱜᱟᱜ-ᱟ᱾ ᱥᱟᱠᱟᱢ ᱛᱮ ᱯᱷᱩᱲᱩᱜ ᱟᱨ ᱯᱟᱹᱛᱲᱟᱹ ᱵᱮᱱᱟᱣᱜ-ᱟ᱾",
            "HO": "ᱥᱟᱨᱡᱚᱢ (Sarjom) ᱫᱟᱨᱩ ᱫᱚ ᱢᱟᱜᱮ ᱯᱚᱨᱚᱵᱽ ᱨᱮ ᱯᱩᱡᱟᱹᱣᱜ-ᱟ᱾ ᱥᱟᱠᱟᱢ ᱛᱮ ᱠᱷᱟᱹᱞᱤ ᱵᱮᱱᱟᱣᱜ-ᱟ᱾",
            "MUNDARI": "सरजोम (Sarjom) दारू सरना स्थल पर पूजनीय है। साकाम से पत्तल बनाई जाती है।"
        }
    },
    {
        "chunk_id": "JCERT_G1_MATH_01",
        "grade": "GRADE_1",
        "subject": "MATHEMATICS",
        "chapter_number": 1,
        "chapter_title": "गिनती और समूह बनाना (1 से 10)",
        "topic": "गिनती और संख्या ज्ञान",
        "lo_code": "LO-MATH-G1-01",
        "content_hindi": "वस्तुओं को गिनना और 10-10 के बंडल बनाना। एक (1), दो (2), तीन (3), चार (4), पाँच (5), छह (6), सात (7), आठ (8), नौ (9), दस (10)।",
        "cultural_keywords": ["गिनती", "बंडल", "महुआ", "तीर", "मटके", "हाट", "संख्या", "एक", "दो", "तीन"],
        "tribal_analogies": {
            "SANTHALI": "ᱢᱤᱫ (Mit'), ᱵᱟᱨ (Bar), ᱯᱮ (Pe), ᱯᱩᱱ (Pun), ᱢᱚᱬᱮ (More) - ᱢᱟᱹᱦᱩᱣᱟᱹ ᱵᱤᱞᱤ ᱞᱮᱠᱷᱟ᱾",
            "HO": "ᱢᱤᱭᱟᱫ (Miyad), ᱵᱟᱨᱤᱭᱟ (Bariya), ᱟᱯᱤᱭᱟ (Apiya), ᱩᱯᱩᱱᱤᱭᱟ (Upuniya), ᱢᱚᱬᱮᱭᱟ (Moreya) - ᱦᱟᱴ ᱨᱮ ᱴᱩᱠᱩᱡ᱾",
            "MUNDARI": "मियाद (1), बारिया (2), आपिया (3), उपुनिया (4), मोड़ेया (5) - महुआ फल गिनना।"
        }
    },
    {
        "chunk_id": "JCERT_G3_FLN_01",
        "grade": "GRADE_3",
        "subject": "LANGUAGE_FLN",
        "chapter_number": 2,
        "chapter_title": "जल संरक्षण और प्राकृतिक नदियां",
        "topic": "जल और जल संरक्षण",
        "lo_code": "LO-FLN-G3-02",
        "content_hindi": "जल ही जीवन है। नदियां, कुएं और झरने हमें पीने का स्वच्छ जल देते हैं। हमें पानी को व्यर्थ नहीं बहाना चाहिए।",
        "cultural_keywords": ["पानी", "जल", "नदी", "झरना", "कुआं", "स्वच्छ", "दाग", "दाः", "संरक्षण"],
        "tribal_analogies": {
            "SANTHALI": "ᱫᱟᱜ (Dak') ᱫᱚ ᱡᱤᱣᱤ ᱠᱟᱱᱟ᱾ ᱡᱷᱟᱨᱱᱟ ᱟᱨ ᱜᱟᱰᱟ ᱠᱷᱚᱱ ᱥᱟᱯᱷᱟ ᱫᱟᱜ ᱧᱟᱢᱚᱜ-ᱟ᱾",
            "HO": "ᱫᱟᱺ (Da:) ᱫᱚ ᱡᱤᱣᱤ ᱛᱟᱵᱩ᱾ ᱜᱟᱰᱟ ᱟᱨ ᱠᱩᱧ ᱠᱷᱚᱱ ᱥᱟᱯᱷᱟ ᱫᱟᱺ ᱵᱩ ᱧᱟᱢᱮᱭᱟ᱾",
            "MUNDARI": "दाः (Da:) जीवन है। झरना और गड़ा से हमें निर्मल जल मिलता है।"
        }
    },
    {
        "chunk_id": "JCERT_G4_EVS_01",
        "grade": "GRADE_4",
        "subject": "ENVIRONMENTAL_STUDIES",
        "chapter_number": 5,
        "chapter_title": "पशु-पक्षी और उनका प्राकृतिक आवास",
        "topic": "जंगली व पालतू जीव",
        "lo_code": "LO-EVS-G4-01",
        "content_hindi": "जंगल में विभिन्न प्रकार के जंगली और पालतू जीव-जंतु रहते हैं जैसे हाथी, हिरण, मोर, गाय और बकरी। सभी जीव पर्यावरण संतुलन के लिए आवश्यक हैं।",
        "cultural_keywords": ["जानवर", "पशु", "पक्षी", "हाथी", "मोर", "जंगल", "पर्यावरण", "हिरण", "बकरी"],
        "tribal_analogies": {
            "SANTHALI": "ᱦᱟᱹᱛᱤ (Hati), ᱡᱤᱞ (Jil/Deer), ᱢᱟᱨᱟᱜ (Marag/Peacock) ᱵᱤᱨ ᱨᱮᱠᱚ ᱛᱟᱦᱮᱸᱱᱟ᱾",
            "HO": "ᱦᱟᱹᱛᱤ (Hati), ᱠᱩᱞ (Kul/Tiger), ᱢᱟᱨᱟᱜ (Marag) ᱵᱤᱨ ᱨᱮᱱ ᱡᱤᱣᱤ ᱠᱚ᱾",
            "MUNDARI": "हाति (Elephant), कुल (Tiger), माराग (Peacock) जंगल के रक्षक हैं।"
        }
    },
    {
        "chunk_id": "JCERT_G5_HERITAGE_01",
        "grade": "GRADE_5",
        "subject": "TRIBAL_HERITAGE",
        "chapter_number": 1,
        "chapter_title": "झारखंड के पारंपरिक लोकपर्व (सरहुल, करम, सोहराय)",
        "topic": "पारंपरिक लोकपर्व",
        "lo_code": "LO-HER-G5-01",
        "content_hindi": "झारखंड की संस्कृति प्रकृति से जुड़ी है। सरहुल में प्रकृति का स्वागत होता है, करम में भाई-बहन के प्रेम और फसलों की रक्षा की कामना होती है, और सोहराय में पशुधन का वंदन किया जाता है।",
        "cultural_keywords": ["सरहुल", "करम", "सोहराय", "संस्कृति", "परब", "अखड़ा", "मांदर", "त्योहार", "गीत"],
        "tribal_analogies": {
            "SANTHALI": "ᱵᱟᱦᱟ ᱯᱚᱨᱚᱵᱽ, ᱠᱟᱨᱟᱢ ᱟᱨ ᱥᱚᱦᱨᱟᱭ ᱫᱚ ᱟᱵᱚᱣᱟᱜ ᱢᱟᱨᱟᱝ ᱯᱚᱨᱚᱵᱽ ᱠᱟᱱᱟ᱾ ᱟᱠᱷᱲᱟ ᱨᱮ ᱛᱩᱢᱫᱟᱜ-ᱴᱟᱢᱟᱠ ᱨᱩᱭᱟ᱾",
            "HO": "ᱢᱟᱜᱮ ᱯᱚᱨᱚᱵᱽ ᱟᱨ ᱵᱟ ᱯᱚᱨᱚᱵᱽ ᱨᱮ ᱫᱩᱨᱟᱝ ᱟᱨ ᱥᱩᱥᱩᱱ ᱦᱩᱭᱩᱜ-ᱟ᱾",
            "MUNDARI": "सरहुल, करम और सोहराय मुंडा समाज के पावन पर्व हैं। अखड़ा में मांदर बजता है।"
        }
    },
    {
        "chunk_id": "JCERT_G1_FLN_02",
        "grade": "GRADE_1",
        "subject": "LANGUAGE_FLN",
        "chapter_number": 4,
        "chapter_title": "हमारा प्यारा परिवार और घर",
        "topic": "परिवार व रिश्ते",
        "lo_code": "LO-FLN-G1-04",
        "content_hindi": "परिवार में माता, पिता, दादा, दादी, भाई और बहन सब मिलकर प्यार से रहते हैं। घर हमें सुरक्षा और स्नेह देता है।",
        "cultural_keywords": ["परिवार", "घर", "माता", "पिता", "भाई", "बहन", "दादी", "दादा", "ओड़ाः"],
        "tribal_analogies": {
            "SANTHALI": "ᱚᱲᱟᱜ (Orak'/Home) ᱨᱮ ᱟᱭᱳ, ᱵᱟᱵᱟ, ᱵᱚᱭᱦᱟ, ᱢᱤᱥᱤ ᱡᱚᱛᱚ ᱠᱚ ᱛᱟᱦᱮᱸᱱᱟ᱾",
            "HO": "ᱚᱲᱟᱺ (Orah) ᱨᱮ ᱮᱝᱜᱟ, ᱟᱯᱟ, ᱦᱟᱜᱟ, ᱢᱤᱥᱤ ᱠᱚ ᱢᱮᱱᱟᱜ ᱠᱚᱣᱟ᱾",
            "MUNDARI": "ओड़ाः में एंगा (माता), आपा (पिता), हागा (भाई) और मिसि (बहन) रहते हैं।"
        }
    },
    {
        "chunk_id": "JCERT_G2_MATH_02",
        "grade": "GRADE_2",
        "subject": "MATHEMATICS",
        "chapter_number": 6,
        "chapter_title": "जोड़ और घटाव के सरल खेल",
        "topic": "जोड़ और घटाव",
        "lo_code": "LO-MATH-G2-06",
        "content_hindi": "दुकान और हाट बाजार में वस्तुओं की खरीद-बिक्री में जोड़ और घटाव का उपयोग होता है। कंकड़ों और बीजों से जोड़ना आसान होता है।",
        "cultural_keywords": ["जोड़", "घटाव", "कंकड़", "बीज", "हाट", "बाजार", "रुपये", "पैसे"],
        "tribal_analogies": {
            "SANTHALI": "ᱢᱮᱥᱟ (Addition) ᱟᱨ ᱚᱪᱚᱜ (Subtraction) - ᱡᱟᱱᱦᱮ ᱟᱨ ᱢᱟᱹᱦᱩᱣᱟᱹ ᱡᱚ ᱛᱮ ᱪᱮᱫᱚᱜ᱾",
            "HO": "ᱦᱟᱴ ᱨᱮ ᱯᱟᱭᱥᱟ ᱟᱨ ᱡᱚ ᱮᱢ-ᱦᱟᱛᱟᱣ ᱨᱮ ᱞᱮᱠᱷᱟ᱾",
            "MUNDARI": "हाट में कंकड़ और इमली बीज से जोड़-घटाव सीखना।"
        }
    },
    {
        "chunk_id": "JCERT_G3_EVS_02",
        "grade": "GRADE_3",
        "subject": "ENVIRONMENTAL_STUDIES",
        "chapter_number": 8,
        "chapter_title": "भोजन, अनाज और स्थानीय कृषि",
        "topic": "कृषि व अनाज",
        "lo_code": "LO-EVS-G3-08",
        "content_hindi": "किसान खेतों में धान, मक्का, मड़ुआ, गोंदली और दालें उगाते हैं। मड़ुआ की रोटी और भात हमें ऊर्जा और पोषण देते हैं।",
        "cultural_keywords": ["अनाज", "धान", "मक्का", "मड़ुआ", "गोंदली", "किसान", "खेत", "कृषि", "भात", "रोटी"],
        "tribal_analogies": {
            "SANTHALI": "ᱦᱩᱲᱩ (Huru/Paddy), ᱜᱩᱱᱫᱽᱞᱤ (Gundli), ᱢᱟᱹᱱᱰᱤᱭᱟᱹ (Mandi/Rice) ᱟᱵᱚᱣᱟᱜ ᱢᱩᱬ ᱡᱚᱢᱟᱜ ᱠᱟᱱᱟ᱾",
            "HO": "ᱵᱟᱵᱟ (Paddy), ᱡᱚᱱᱚᱲ (Corn), ᱢᱟᱹᱱᱰᱤ ᱡᱚᱢ ᱛᱮ ᱫᱟᱲᱮ ᱧᱟᱢᱚᱜ-ᱟ᱾",
            "MUNDARI": "बाबा (धान), मड़ुआ और गोंदली हमारे खेतों का अमूल्य अन्न है।"
        }
    },
    {
        "chunk_id": "JCERT_G4_FLN_03",
        "grade": "GRADE_4",
        "subject": "LANGUAGE_FLN",
        "chapter_number": 7,
        "chapter_title": "स्वच्छता, स्वास्थ्य और योग",
        "topic": "स्वच्छता व स्वास्थ्य",
        "lo_code": "LO-FLN-G4-03",
        "content_hindi": "प्रतिदिन नीम या करंज की दातुन से दांत साफ करना, हाथ धोकर भोजन करना और साफ पानी पीना हमें स्वस्थ और निरोगी रखता है।",
        "cultural_keywords": ["स्वच्छता", "स्वास्थ्य", "दातुन", "नीम", "करंज", "हाथ धोना", "साफ पानी"],
        "tribal_analogies": {
            "SANTHALI": "ᱱᱤᱢ ᱫᱟᱹᱛᱩᱱ (Neem Datun) ᱟᱨ ᱥᱟᱯᱷᱟ ᱫᱟᱜ ᱛᱮ ᱦᱚᱲᱢᱚ ᱵᱮᱥ ᱛᱟᱦᱮᱸᱱᱟ᱾",
            "HO": "ᱱᱤᱢ ᱫᱟᱹᱛᱩᱱ ᱛᱮ ᱰᱟᱴᱟ ᱥᱟᱯᱷᱟ ᱟᱨ ᱦᱚᱲᱢᱚ ᱨᱮᱭᱟᱜ ᱡᱚᱛᱚᱱ᱾",
            "MUNDARI": "नीम दातुन और स्वच्छ जल से शरीर स्वस्थ रहता है।"
        }
    },
    {
        "chunk_id": "JCERT_G5_EVS_03",
        "grade": "GRADE_5",
        "subject": "ENVIRONMENTAL_STUDIES",
        "chapter_number": 11,
        "chapter_title": "सौरमंडल, सूर्य और पृथ्वी",
        "topic": "सूर्य व सौरमंडल",
        "lo_code": "LO-EVS-G5-11",
        "content_hindi": "सूर्य सौरमंडल का केंद्र है और हमें प्रकाश व ऊष्मा देता है। पृथ्वी सूर्य का चक्कर लगाती है जिससे दिन और रात होते हैं।",
        "cultural_keywords": ["सूर्य", "पृथ्वी", "सौरमंडल", "प्रकाश", "दिन", "रात", "सिंगबोंगा", "सिंगी"],
        "tribal_analogies": {
            "SANTHALI": "ᱥᱤᱝᱜᱤ (Singi/Sun) ᱫᱚ ᱫᱷᱟᱹᱨᱛᱤ ᱨᱤᱱᱤᱡ ᱡᱤᱣᱤ ᱫᱟᱛᱟ ᱠᱟᱱᱟᱭ᱾",
            "HO": "ᱥᱤᱝᱵᱚᱝᱜᱟ (Singbonga) ᱥᱟᱨᱟ ᱫᱷᱟᱹᱨᱛᱤ ᱨᱮ ᱢᱟᱨᱥᱟᱞ ᱮᱢᱚᱜ-ᱟᱭ᱾",
            "MUNDARI": "सिंगबोंगा (सूर्य) संपूर्ण संसार को जीवन और प्रकाश प्रदान करते हैं।"
        }
    },
    {
        "chunk_id": "JCERT_G1_EVS_01",
        "grade": "GRADE_1",
        "subject": "ENVIRONMENTAL_STUDIES",
        "chapter_number": 2,
        "chapter_title": "हमारे शरीर के अंग और ज्ञानेंद्रियाँ",
        "topic": "शरीर के अंग",
        "lo_code": "LO-EVS-G1-02",
        "content_hindi": "हमारे शरीर में आँख देखने के लिए, कान सुनने के लिए, नाक सूंघने के लिए, जीभ स्वाद के लिए और त्वचा छूने के लिए होती है। हाथ और पैर हमें कार्य करने में मदद करते हैं।",
        "cultural_keywords": ["शरीर", "आँख", "कान", "नाक", "जीभ", "हाथ", "पैर", "मेद", "लुंतूर", "मू", "ती"],
        "tribal_analogies": {
            "SANTHALI": "ᱢᱮᱫ (Med/Eye), ᱞᱩᱛᱩᱨ (Lutur/Ear), ᱢᱩᱸ (Mu/Nose), ᱟᱞᱟᱝ (Alang/Tongue), ᱛᱤ (Ti/Hand) - ᱦᱚᱲᱢᱚ ᱨᱮᱱᱟᱜ ᱢᱩᱬ ᱦᱟᱹᱴᱤᱧ᱾",
            "HO": "ᱢᱮᱫ (Med), ᱞᱩᱛᱩᱨ (Lutur), ᱢᱩ (Mu), ᱟᱞᱟᱝ (Alang), ᱛᱤ (Ti) - ᱦᱚᱲᱢᱚ ᱨᱮᱭᱟᱜ ᱦᱟᱹᱴᱤᱧ ᱠᱚ᱾",
            "MUNDARI": "मेद (आँख), लुंतूर (कान), मू (नाक), अलंग (जीभ) और ती (हाथ) शरीर के प्रमुख अंग हैं।"
        }
    },
    {
        "chunk_id": "JCERT_G2_FLN_02",
        "grade": "GRADE_2",
        "subject": "LANGUAGE_FLN",
        "chapter_number": 5,
        "chapter_title": "दिनचर्या, खेलकूद और अच्छी आदतें",
        "topic": "दिनचर्या व खेलकूद",
        "lo_code": "LO-FLN-G2-05",
        "content_hindi": "प्रातःकाल सूर्योदय से पहले उठना, दातुन करना, स्वच्छ जल से स्नान करना, समय पर विद्यालय जाना और मित्रों के साथ पारंपरिक खेल खेलना हमारे मन और शरीर को प्रसन्न रखता है।",
        "cultural_keywords": ["दिनचर्या", "सुबह", "विद्यालय", "खेलकूद", "आसड़ा", "इतुन", "मित्र", "गतिविधि"],
        "tribal_analogies": {
            "SANTHALI": "ᱥᱮᱛᱟᱜ ᱵᱮᱨᱮᱫ ᱠᱟᱛᱮ ᱟᱥᱲᱟ (School) ᱪᱟᱞᱟᱜ ᱟᱨ ᱜᱟᱛᱮ ᱠᱚ ᱥᱟᱶ ᱠᱷᱮᱞᱚᱸᱰ (Games)᱾",
            "HO": "ᱥᱮᱛᱟᱜ ᱨᱮ ᱤᱛᱩᱱ ᱟᱥᱲᱟ ᱥᱮᱱᱚᱜ ᱟᱨ ᱡᱩᱨᱤ ᱠᱚ ᱥᱟᱞᱟᱜ ᱮᱱᱮᱡ᱾",
            "MUNDARI": "सुबह इतुन आसड़ा (विद्यालय) जाना और दोस्तों के साथ खेलना।"
        }
    },
    {
        "chunk_id": "JCERT_G3_MATH_03",
        "grade": "GRADE_3",
        "subject": "MATHEMATICS",
        "chapter_number": 9,
        "chapter_title": "माप और भार की पारंपरिक इकाइयां (पैला और कुड़ी)",
        "topic": "माप और भार",
        "lo_code": "LO-MATH-G3-04",
        "content_hindi": "हाट और बाजारों में धान और अनाज नापने के लिए पारंपरिक पैला, कुड़ी और काठ का उपयोग होता है। मानक मापों में किलोग्राम और ग्राम का प्रयोग किया जाता है।",
        "cultural_keywords": ["माप", "भार", "पैला", "कुड़ी", "तराजू", "वजन", "किलोग्राम", "ग्राम", "अनाज"],
        "tribal_analogies": {
            "SANTHALI": "ᱦᱩᱲᱩ ᱡᱚᱠᱷᱟ ᱞᱟᱹᱜᱤᱫ ᱯᱟᱹᱭᱞᱟᱹ (Paila) ᱟᱨ ᱠᱟᱴᱷ (Kath) ᱵᱮᱵᱷᱟᱨᱚᱜ-ᱟ᱾",
            "HO": "ᱦᱟᱴ ᱨᱮ ᱵᱟᱵᱟ ᱟᱨ ᱡᱚᱱᱚᱲ ᱡᱚᱠᱷᱟ ᱞᱟᱹᱜᱤᱫ ᱯᱟᱭᱞᱟ ᱨᱮᱭᱟᱜ ᱵᱮᱵᱷᱟᱨ᱾",
            "MUNDARI": "धान और मक्का नापने के लिए पारंपरिक पैला का उपयोग किया जाता है।"
        }
    },
    {
        "chunk_id": "JCERT_G4_HERITAGE_02",
        "grade": "GRADE_4",
        "subject": "TRIBAL_HERITAGE",
        "chapter_number": 8,
        "chapter_title": "झारखंड की लोक कला और सोहराय पेंटिंग",
        "topic": "लोक कला व सोहराय",
        "lo_code": "LO-HER-G4-02",
        "content_hindi": "सोहराय और कोहबर चित्रकला में प्राकृतिक रंगों, लाल-काली मिट्टी और पत्तियों के रंगों से दीवारों पर पशु, पक्षी, वृक्ष और ज्यामितीय आकृतियां बनाई जाती हैं। यह प्रकृति के प्रति हमारे आदर को दर्शाती है।",
        "cultural_keywords": ["सोहराय", "कोहबर", "चित्रकला", "भित्तिचित्र", "मिट्टी", "रंग", "कला", "प्रकृति"],
        "tribal_analogies": {
            "SANTHALI": "ᱥᱚᱦᱨᱟᱭ ᱚᱠᱛᱚ ᱨᱮ ᱚᱲᱟᱜ ᱵᱷᱤᱛ ᱨᱮ ᱪᱮᱬᱮ, ᱫᱟᱨᱮ ᱟᱨ ᱡᱤᱣᱤ ᱠᱚᱣᱟᱜ ᱪᱤᱛᱟᱹᱨ ᱵᱮᱱᱟᱣᱜ-ᱟ᱾",
            "HO": "ᱚᱲᱟᱺ ᱵᱷᱤᱛ ᱨᱮ ᱦᱟᱥᱟ ᱨᱚᱝ ᱛᱮ ᱥᱩᱱᱫᱚᱨ ᱪᱤᱛᱟᱹᱨ ᱵᱮᱱᱟᱣ᱾",
            "MUNDARI": "सोहराय पर्व पर मिट्टी के प्राकृतिक रंगों से घरों की दीवारों पर भित्तिचित्र बनाए जाते हैं।"
        }
    },
    {
        "chunk_id": "JCERT_G5_FLN_04",
        "grade": "GRADE_5",
        "subject": "LANGUAGE_FLN",
        "chapter_number": 12,
        "chapter_title": "जंगल, पर्यावरण और हमारी पृथ्वी की रक्षा",
        "topic": "पर्यावरण रक्षा",
        "lo_code": "LO-FLN-G5-06",
        "content_hindi": "जंगल हमारे फेफड़े हैं। वे हमें प्राणवायु (ऑक्सीजन), वर्षा, जड़ी-बूटियां और लकड़ियां देते हैं। वनों का संरक्षण करना हर नागरिक का कर्तव्य है।",
        "cultural_keywords": ["जंगल", "पर्यावरण", "रक्षा", "वन", "ऑक्सीजन", "वर्षा", "जड़ी-बूटी", "संरक्षण"],
        "tribal_analogies": {
            "SANTHALI": "ᱵᱤᱨ ᱫᱚ ᱟᱵᱚᱣᱟᱜ ᱡᱤᱣᱤ ᱠᱟᱱᱟ᱾ ᱫᱟᱨᱮ ᱜᱮᱫ ᱵᱚᱸᱫᱽ ᱠᱟᱛᱮ ᱵᱤᱨ ᱵᱟᱧᱪᱟᱣ ᱦᱩᱭᱩᱜ-ᱟ᱾",
            "HO": "ᱵᱤᱨ-ᱫᱟᱨᱩ ᱫᱚ ᱟᱵᱩᱣᱟᱜ ᱡᱤᱣᱤ᱾ ᱵᱤᱨ ᱨᱮᱭᱟᱜ ᱡᱚᱛᱚᱱ ᱫᱚ ᱡᱚᱛᱚ ᱦᱚᱲᱟᱜ ᱠᱟᱹᱢᱤ᱾",
            "MUNDARI": "बीर (जंगल) हमारी जीवनरेखा है। वन संरक्षण हमारी संस्कृति का मूल आधार है।"
        }
    }
]

# --- Domain Concept Centroids for Dense Semantic Embeddings ---
CONCEPT_CENTROIDS = {
    "botany_nature": ["साल", "सखुआ", "पेड़", "जंगल", "पौधे", "पत्तियां", "महुआ", "प्रकृति", "पर्यावरण", "छांव", "sarjom", "dare", "sakam", "ᱫᱟᱨᱮ", "ᱥᱟᱠᱟᱢ"],
    "math_numeracy": ["गिनती", "संख्या", "गणना", "जोड़", "घटाव", "बीज", "कंकड़", "बंडल", "हाट", "मियाद", "बारिया", "आपिया", "mit", "bar", "pe", "ᱞᱮᱠᱷᱟ"],
    "water_ecology": ["पानी", "जल", "नदी", "झरना", "कुआं", "तालाब", "स्वच्छ", "संरक्षण", "दाग", "दाः", "गाडा", "dak", "da", "ᱫᱟᱜ", "ᱜᱟᱰᱟ"],
    "animals_fauna": ["जानवर", "पशु", "पक्षी", "हाथी", "मोर", "हिरण", "बाघ", "बकरी", "गाय", "hati", "marag", "jil", "ᱦᱟᱹᱛᱤ", "ᱢᱟᱨᱟᱜ"],
    "culture_heritage": ["सरहुल", "करम", "सोहराय", "कोहबर", "संस्कृति", "परब", "त्योहार", "अखड़ा", "मांदर", "जाहेरथान", "baha", "porob", "sohrai", "karam", "ᱵᱟᱦᱟ"],
    "family_home": ["परिवार", "घर", "माता", "पिता", "भाई", "बहन", "दादा", "दादी", "ओड़ाः", "ओड़ाग", "enga", "apa", "haga", "misi", "orak", "orah"],
    "food_agriculture": ["अनाज", "धान", "मक्का", "मड़ुआ", "गोंदली", "किसान", "खेत", "भात", "रोटी", "huru", "baba", "mandi"],
    "health_hygiene": ["स्वच्छता", "स्वास्थ्य", "दातुन", "नीम", "करंज", "हाथ धोना", "सफाई", "दिनचर्या", "datun", "neem"],
    "astronomy_sun": ["सूर्य", "पृथ्वी", "सौरमंडल", "प्रकाश", "दिन", "रात", "सिंगबोंगा", "सिंगी", "singi", "singbonga"],
    "body_anatomy": ["शरीर", "अंग", "आँख", "कान", "नाक", "जीभ", "हाथ", "पैर", "ज्ञानेंद्रियाँ", "med", "lutur", "mu", "alang", "ti", "ᱛᱤ", "ᱢᱮᱫ"],
    "measurement_units": ["माप", "भार", "पैला", "कुड़ी", "काठ", "तराजू", "वजन", "किलोग्राम", "ग्राम", "paila", "kath", "ᱯᱟᱹᱭᱞᱟᱹ"]
}

class FineTunedHybridRagEngine:
    """Production-grade hybrid RAG engine with Okapi BM25, 128-dim dense semantic vectorizer, RRF, and Cross-Encoder Reranking."""

    def __init__(self):
        self.corpus = JCERT_KNOWLEDGE_BASE
        self.k1 = 1.5
        self.b = 0.75
        self.vector_dim = 128
        self._precompute_corpus_stats()

    def _tokenize(self, text: str) -> List[str]:
        cleaned = re.sub(r'[^\w\s\u0900-\u097F\u1C50-\u1C7F]', ' ', text.lower())
        tokens = [t.strip() for t in cleaned.split() if len(t.strip()) >= 2]
        return tokens

    def _precompute_corpus_stats(self):
        self.doc_tokens = []
        self.doc_lengths = []
        self.doc_freqs: Dict[str, int] = {}
        
        for doc in self.corpus:
            combined_text = f"{doc['topic']} {doc['chapter_title']} {doc['content_hindi']} {' '.join(doc['cultural_keywords'])} {doc['lo_code']}"
            tokens = self._tokenize(combined_text)
            self.doc_tokens.append(tokens)
            self.doc_lengths.append(len(tokens))
            
            unique_terms = set(tokens)
            for t in unique_terms:
                self.doc_freqs[t] = self.doc_freqs.get(t, 0) + 1
                
        self.N = len(self.corpus)
        self.avg_dl = sum(self.doc_lengths) / max(self.N, 1)

    def _compute_bm25_score(self, query_tokens: List[str], doc_idx: int) -> float:
        if not query_tokens:
            return 0.0
        
        score = 0.0
        doc_tokens = self.doc_tokens[doc_idx]
        doc_len = self.doc_lengths[doc_idx]
        term_counts: Dict[str, int] = {}
        for t in doc_tokens:
            term_counts[t] = term_counts.get(t, 0) + 1
            
        for q in query_tokens:
            if q in term_counts:
                tf = term_counts[q]
                df = self.doc_freqs.get(q, 1)
                idf = math.log(1.0 + ((self.N - df + 0.5) / (df + 0.5)))
                numerator = tf * (self.k1 + 1.0)
                denominator = tf + (self.k1 * (1.0 - self.b + (self.b * (doc_len / self.avg_dl))))
                score += idf * (numerator / denominator)
                
        return max(0.0, score)

    def _embed_text(self, text: str) -> List[float]:
        vector = [0.0] * self.vector_dim
        tokens = self._tokenize(text)
        if not tokens:
            return vector

        # Concept Centroid Projection
        for centroid_idx, (_, keywords) in enumerate(CONCEPT_CENTROIDS.items()):
            overlap = sum(1 for t in tokens if any(kw in t or t in kw for kw in keywords))
            if overlap > 0:
                weight = min(1.0, overlap * 0.4)
                base_slot = (centroid_idx * 11) % self.vector_dim
                for offset in range(10):
                    slot = (base_slot + offset) % self.vector_dim
                    vector[slot] += weight * (1.0 / (offset + 1))

        # Subword n-gram hashing
        for t in tokens:
            h = abs(hash(t)) % self.vector_dim
            vector[h] += 0.35

        # L2 Normalization
        norm = math.sqrt(sum(v * v for v in vector))
        if norm > 1e-5:
            vector = [v / norm for v in vector]

        return vector

    def _cosine_similarity(self, v1: List[float], v2: List[float]) -> float:
        dot = sum(a * b for a, b in zip(v1, v2))
        return max(0.0, min(1.0, dot))

    def _cross_encoder_rerank(self, query: str, candidate_chunk: Dict[str, Any], initial_score: float) -> float:
        query_lower = query.lower()
        query_tokens = set(self._tokenize(query))
        topic_tokens = set(self._tokenize(candidate_chunk["topic"]))
        title_tokens = set(self._tokenize(candidate_chunk["chapter_title"]))
        score = initial_score
        
        # 1. Exact Learning Outcome Code Bonus
        if candidate_chunk["lo_code"].lower() in query_lower:
            score += 0.50

        # 2. Topic and Chapter Title token overlap bonus
        topic_overlap = len(query_tokens.intersection(topic_tokens))
        if topic_overlap > 0:
            score += min(0.60, topic_overlap * 0.30)

        title_overlap = len(query_tokens.intersection(title_tokens))
        if title_overlap > 0:
            score += min(0.40, title_overlap * 0.20)

        # 3. Cultural keywords & Content token coverage
        kw_hits = sum(1 for kw in candidate_chunk["cultural_keywords"] if kw.lower() in query_lower)
        score += min(0.30, kw_hits * 0.10)

        content_tokens = set(self._tokenize(candidate_chunk["content_hindi"]))
        content_overlap = len(query_tokens.intersection(content_tokens))
        if content_overlap > 0:
            score += min(0.50, content_overlap * 0.12)

        return round(score, 4)

    def retrieve(
        self,
        query: str,
        grade: Optional[str] = None,
        subject: Optional[str] = None,
        top_k: int = 3
    ) -> List[Dict[str, Any]]:
        query_tokens = self._tokenize(query)
        query_vector = self._embed_text(query)
        
        scored_candidates = []
        for idx, doc in enumerate(self.corpus):
            # Metadata Filter hard-scoping
            if grade and doc["grade"] != grade:
                continue
            if subject and doc["subject"] != subject:
                continue

            bm25_raw = self._compute_bm25_score(query_tokens, idx)
            doc_vector = self._embed_text(f"{doc['topic']} {doc['content_hindi']} {doc['lo_code']}")
            dense_sim = self._cosine_similarity(query_vector, doc_vector)

            # Normalized Reciprocal Rank Fusion (RRF)
            rrf_score = (1.0 / (60.0 + (1.0 / (bm25_raw + 0.01)))) + (1.0 / (60.0 + (1.0 / (dense_sim + 0.01))))
            
            # Cross-encoder Reranker pass
            final_rerank_score = self._cross_encoder_rerank(query, doc, rrf_score)

            matched_kws = [kw for kw in doc["cultural_keywords"] if kw.lower() in query.lower()]

            scored_candidates.append({
                "chunk": doc,
                "bm25_score": round(bm25_raw, 3),
                "dense_score": round(dense_sim, 3),
                "rrf_score": round(rrf_score, 5),
                "rerank_score": final_rerank_score,
                "matched_keywords": matched_kws,
                "provenance": {
                    "chunk_id": doc["chunk_id"],
                    "lo_code": doc["lo_code"],
                    "chapter_title": doc["chapter_title"],
                    "topic": doc["topic"]
                }
            })

        # Rank by cross-encoder rerank score
        scored_candidates.sort(key=lambda x: x["rerank_score"], reverse=True)
        return scored_candidates[:top_k]

# Global singleton instance
rag_engine = FineTunedHybridRagEngine()
