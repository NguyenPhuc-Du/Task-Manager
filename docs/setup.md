# Setup

1. Copy `backend/.env` and adjust `DATABASE_URL`, `REDIS_URL`, `JWT_SECRET`.
2. From `docker/`, run `docker compose up` (or start Postgres/Redis manually).
3. Backend: `cd backend && npm install && npm run dev`.
4. ML service: `cd ml-service && pip install -r requirements.txt && uvicorn app.main:app --reload`.
5. Android: open `android/` in Android Studio after adding Gradle project files.
