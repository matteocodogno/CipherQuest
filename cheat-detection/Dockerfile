FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

ENV POETRY_VERSION=2.1.3
RUN pip install "poetry==$POETRY_VERSION"

WORKDIR /app

COPY pyproject.toml poetry.lock ./
RUN poetry install --no-root --only main --no-interaction --no-ansi

COPY src ./src
COPY models ./models

EXPOSE 8000

CMD ["poetry", "run", "uvicorn", "cheat_detection.main:app", "--host", "0.0.0.0", "--port", "8000"]
