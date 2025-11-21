from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import pandas as pd

app = FastAPI()

bundle = joblib.load("cheat_detector_rf.joblib")
model = bundle["model"]
feature_cols = bundle["feature_cols"]

class SessionInput(BaseModel):
    session_length: float
    coins: float

class SessionOutput(BaseModel):
    cheat_probability: float

@app.post("/predict", response_model=SessionOutput)
def predict(session: SessionInput):

    row = pd.DataFrame([{
        "session_length": session.session_length,
        "coins": session.coins,
    }])[feature_cols]

    prob = model.predict_proba(row)[0, 1]

    return SessionOutput(cheat_probability=float(prob))


