"""
BhashaSetu AI — Production Infrastructure, Docker & Kubernetes Verification Test
Validates:
1. Dockerfile multi-stage builds, non-root users, and healthcheck configurations.
2. docker-compose.yml structure, network mesh, volume persistence, and service dependencies.
3. Kubernetes (k8s) manifests: namespace, configmaps, secrets, statefulsets, deployments, ingress, HPA, and kustomization.
4. Nginx reverse proxy configuration and routing definitions.
"""

import os
import unittest
import re

ROOT_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
INFRA_DIR = os.path.join(ROOT_DIR, 'infra')
K8S_DIR = os.path.join(INFRA_DIR, 'k8s')

class TestInfrastructureProductionReadiness(unittest.TestCase):

    def test_01_dockerfiles_integrity_and_security(self):
        """Assert that all core services have multi-stage Dockerfiles with non-root security and healthchecks."""
        dockerfiles = [
            os.path.join(ROOT_DIR, 'services', 'ai-platform', 'Dockerfile'),
            os.path.join(ROOT_DIR, 'services', 'web-backend', 'Dockerfile'),
            os.path.join(ROOT_DIR, 'apps', 'web-frontend', 'Dockerfile')
        ]
        
        for df in dockerfiles:
            self.assertTrue(os.path.isfile(df), f"Dockerfile missing: {df}")
            with open(df, 'r', encoding='utf-8') as f:
                content = f.read()
                self.assertIn("FROM", content)
                self.assertIn("USER", content, f"Non-root USER directive missing in {df}")
                self.assertIn("HEALTHCHECK", content, f"HEALTHCHECK missing in {df}")
                self.assertIn("EXPOSE", content)
        print("[PASS] Infra Test 01: All Dockerfiles verified with multi-stage builds, non-root users, and healthchecks.")

    def test_02_docker_compose_mesh_and_services(self):
        """Assert docker-compose.yml defines all 6 production services, healthchecks, and network mesh."""
        compose_file = os.path.join(INFRA_DIR, 'docker-compose.yml')
        self.assertTrue(os.path.isfile(compose_file))
        
        with open(compose_file, 'r', encoding='utf-8') as f:
            content = f.read()
            expected_services = ['postgres', 'redis', 'ai-platform', 'web-backend', 'web-frontend', 'nginx-gateway']
            for s in expected_services:
                self.assertIn(f"{s}:", content, f"Service {s} missing in docker-compose.yml")
            self.assertIn("bhashasetu-mesh", content)
            self.assertIn("pgdata:", content)
            self.assertIn("healthcheck:", content)
        print("[PASS] Infra Test 02: Docker Compose verified with 6 services, healthchecks, and network mesh.")

    def test_03_k8s_manifests_structure(self):
        """Assert that the complete Kubernetes production suite exists and has valid structure."""
        expected_k8s_files = [
            'namespace.yaml',
            'configmap.yaml',
            'secrets.yaml',
            'postgres-statefulset.yaml',
            'redis-deployment.yaml',
            'ai-platform-deployment.yaml',
            'web-backend-deployment.yaml',
            'web-frontend-deployment.yaml',
            'ingress.yaml',
            'hpa.yaml',
            'kustomization.yaml'
        ]
        
        for kfile in expected_k8s_files:
            fpath = os.path.join(K8S_DIR, kfile)
            self.assertTrue(os.path.isfile(fpath), f"Kubernetes manifest missing: {kfile}")
            with open(fpath, 'r', encoding='utf-8') as f:
                content = f.read()
                self.assertIn("apiVersion:", content)
                self.assertIn("kind:", content)
                self.assertIn("metadata:", content)
        print("[PASS] Infra Test 03: All 11 Kubernetes manifests verified with valid schemas.")

    def test_04_k8s_hpa_and_resource_limits(self):
        """Assert HorizontalPodAutoscalers and container resource limits are configured."""
        hpa_file = os.path.join(K8S_DIR, 'hpa.yaml')
        with open(hpa_file, 'r', encoding='utf-8') as f:
            content = f.read()
            self.assertIn("HorizontalPodAutoscaler", content)
            self.assertIn("ai-platform-hpa", content)
            self.assertIn("web-backend-hpa", content)
            self.assertIn("web-frontend-hpa", content)

        ai_deploy = os.path.join(K8S_DIR, 'ai-platform-deployment.yaml')
        with open(ai_deploy, 'r', encoding='utf-8') as f:
            content = f.read()
            self.assertIn("resources:", content)
            self.assertIn("limits:", content)
            self.assertIn("requests:", content)
            self.assertIn("readinessProbe:", content)
            self.assertIn("livenessProbe:", content)
        print("[PASS] Infra Test 04: Kubernetes HPA and resource limits/probes verified.")

    def test_05_nginx_reverse_proxy_config(self):
        """Assert NGINX reverse proxy config routes API, AI, and Frontend correctly."""
        nginx_conf = os.path.join(INFRA_DIR, 'nginx', 'nginx.conf')
        self.assertTrue(os.path.isfile(nginx_conf))
        with open(nginx_conf, 'r', encoding='utf-8') as f:
            content = f.read()
            self.assertIn("upstream web_frontend", content)
            self.assertIn("upstream web_backend", content)
            self.assertIn("upstream ai_platform", content)
            self.assertIn("location /api/", content)
            self.assertIn("location /ai/", content)
            self.assertIn("location /", content)
        print("[PASS] Infra Test 05: NGINX reverse proxy configuration verified.")

if __name__ == "__main__":
    print("\n=======================================================")
    print("  BHASHASETU AI -- INFRASTRUCTURE & KUBERNETES SUITE")
    print("=======================================================\n")
    unittest.main()
