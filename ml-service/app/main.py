from fastapi import FastAPI

app = FastAPI(title="AI Task Manager ML Service")


@app.get("/health")
def health():
    return {"status": "ok"}
