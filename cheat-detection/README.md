# Cheat Detection Module

This module provides a small FastAPI service that loads a pre-trained ML model and exposes it as an HTTP API for cheat detection in **CipherQuest**.

The model takes basic gameplay features (such as session length and coins earned) and returns a score or label indicating whether a session looks suspicious.

---

## Features

- ✅ **FastAPI service** that can be run as a standalone microservice.
- ✅ **Pre-trained model** loaded from a bundled `.joblib` file.
- ✅ **Simple input schema** (e.g. `session_length`, `coins`) for easy integration from the main backend.
- ✅ **Poetry-managed dependencies** for reproducible environments.

---

## Project structure

```text
cheat-detection/
├── models/
│   └── cheat_detector_rf.joblib     # Serialized model bundle (model + metadata)
├── src/
│   └── cheat_detection/
│       ├── __init__.py
│       └── main.py                  # FastAPI app & prediction logic
├── pyproject.toml                   # Poetry config & dependencies
├── poetry.lock                      # Locked dependency versions
└── README.md
