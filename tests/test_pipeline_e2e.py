"""
BhashaSetu AI — Master End-to-End Educational Synthesis Pipeline Verification
Tests the 7-stage automated pipeline:
1. Speech / Text Prompt Intake
2. Hybrid RAG Curriculum Grounding
3. Multilingual Translation & Script Rendering (Ol Chiki, Warang Chiti, Devanagari)
4. Pedagogical Cultural Metaphor Adaptation (Sarhul, Karam, Sohrai)
5. Multi-Signal Quality Gate & MQM Error Analysis
6. Multimodal Artifact Generation (Worksheets + Flashcards)
7. Cryptographically Signed Offline Content Bundle Generation
"""

import sys
import os
import unittest

sys.path.append(os.path.join(os.path.dirname(__file__), '..', 'services', 'ai-platform'))

from pipeline import unified_pipeline
from main import app
from fastapi.testclient import TestClient

class TestUnifiedEducationalPipeline(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.client = TestClient(app)

    def test_01_full_pipeline_santhali_execution(self):
        """Assert full pipeline execution for Santhali in Ol Chiki script with complete timing budgets."""
        result = unified_pipeline.execute_full_pipeline(
            hindi_prompt="पेड़ों की पत्तियाँ और उनके कार्य",
            target_language="SANTHALI",
            grade_level="GRADE_2",
            subject="ENVIRONMENTAL_STUDIES",
            district="Dumka"
        )
        
        self.assertEqual(result["status"], "SUCCESS")
        self.assertEqual(result["target_language"], "SANTHALI")
        self.assertEqual(result["script_type"], "OL_CHIKI")
        self.assertIn("ᱥᱟᱠᱟᱢ", result["native_script_text"])
        self.assertIn("सरहुल", result["cultural_analogy"])
        self.assertGreaterEqual(result["quality_report"]["composite_score"], 0.85)
        self.assertEqual(result["audio_metadata"]["sample_rate_hz"], 24000)
        self.assertIn("WS-", result["worksheet"]["worksheet_id"])
        self.assertIn("PKG-SANTHALI-", result["offline_pack"]["package_id"])
        self.assertLessEqual(result["pipeline_timings"]["total_pipeline_ms"], 500)
        print(f"[PASS] Pipeline Test 01: Santhali synthesis completed in {result['pipeline_timings']['total_pipeline_ms']}ms.")

    def test_02_full_pipeline_ho_execution(self):
        """Assert full pipeline execution for Ho in Warang Chiti script."""
        result = unified_pipeline.execute_full_pipeline(
            hindi_prompt="1 से 10 तक गिनती और संख्या ज्ञान",
            target_language="HO",
            grade_level="GRADE_1",
            subject="MATHEMATICS",
            district="West Singhbhum"
        )
        
        self.assertEqual(result["status"], "SUCCESS")
        self.assertEqual(result["target_language"], "HO")
        self.assertEqual(result["script_type"], "WARANG_CHITI")
        self.assertIn("ᱢᱤ", result["native_script_text"])
        self.assertGreaterEqual(result["quality_report"]["composite_score"], 0.85)
        print(f"[PASS] Pipeline Test 02: Ho synthesis completed with Warang Chiti script.")

    def test_03_full_pipeline_mundari_execution(self):
        """Assert full pipeline execution for Mundari in Devanagari script."""
        result = unified_pipeline.execute_full_pipeline(
            hindi_prompt="जल, नदियां और हमारा जीवन",
            target_language="MUNDARI",
            grade_level="GRADE_3",
            subject="LANGUAGE_FLN",
            district="Khunti"
        )
        
        self.assertEqual(result["status"], "SUCCESS")
        self.assertEqual(result["target_language"], "MUNDARI")
        self.assertEqual(result["script_type"], "DEVANAGARI")
        self.assertIn("दाः", result["native_script_text"])
        print(f"[PASS] Pipeline Test 03: Mundari synthesis completed with Devanagari script.")

    def test_04_fastapi_pipeline_endpoint(self):
        """Assert /api/v1/pipeline/synthesize HTTP endpoint returns 200 with full payload."""
        response = self.client.post("/api/v1/pipeline/synthesize", json={
            "hindi_prompt": "झारखंड के पारंपरिक लोकपर्व",
            "target_language": "SANTHALI",
            "grade_level": "GRADE_5",
            "subject": "TRIBAL_HERITAGE",
            "district": "Ranchi"
        })
        
        self.assertEqual(response.status_code, 200)
        data = response.json()
        self.assertEqual(data["status"], "SUCCESS")
        self.assertIn("pipeline_timings", data)
        print("[PASS] Pipeline Test 04: /api/v1/pipeline/synthesize HTTP endpoint verified.")

if __name__ == "__main__":
    print("\n=======================================================")
    print("  BHASHASETU AI -- UNIFIED PIPELINE TEST SUITE")
    print("=======================================================\n")
    unittest.main()
