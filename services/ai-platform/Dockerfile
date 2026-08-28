# ==========================================
# BhashaSetu AI Platform — Multi-Stage Dockerfile
# Python 3.12-slim with Non-Root Security & Healthcheck
# ==========================================

FROM python:3.12-slim AS builder

WORKDIR /build

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    curl \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --user --no-cache-dir -r requirements.txt

# --- Production Final Image ---
FROM python:3.12-slim AS runner

WORKDIR /app

# Create unprivileged user for container security
RUN groupadd -r bhasha && useradd -r -g bhasha -d /app -s /sbin/nologin bhasha

# Copy installed Python packages from builder
COPY --from=builder /root/.local /home/bhasha/.local
ENV PATH=/home/bhasha/.local/bin:$PATH
ENV PYTHONUNBUFFERED=1
ENV PYTHONDONTWRITEBYTECODE=1

# Copy source code
COPY --chown=bhasha:bhasha . /app

USER bhasha

EXPOSE 8000

HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=3 \
  CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:8000/health').read()" || exit 1

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000", "--workers", "2", "--access-log"]
