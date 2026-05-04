"""Trigger training jobs (stub)."""

from fastapi import APIRouter

router = APIRouter(prefix="/train", tags=["train"])


@router.post("/")
def trigger_training():
    return {"accepted": True}
