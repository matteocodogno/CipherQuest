from fastapi.testclient import TestClient

from cheat_detection.main import app

client = TestClient(app)


def test_predict_returns_cheat_probability():
    payload = {
        "session_length": 120.0,
        "coins": 10.0,
    }

    response = client.post("/predict", json=payload)

    assert response.status_code == 200

    data = response.json()
    assert "cheat_probability" in data
    assert isinstance(data["cheat_probability"], float)
