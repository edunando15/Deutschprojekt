# Deutschprojekt

Deutschprojekt is a German vocabulary practice app with an Angular frontend and a Spring Boot/PostgreSQL API. Users can register, sign in, and take quizzes with 30 randomly selected English prompts. For each prompt, they enter the German article, singular, and plural; the app scores the complete quiz and shows corrections.

## Run locally

Start PostgreSQL, copy `.env.example` to `.env` in the repository root, and set `DB_USERNAME` and `DB_PASSWORD` in `.env` to your PostgreSQL credentials. Spring Boot imports this file automatically, and `.env` is ignored by Git:

```powershell
Copy-Item .env.example .env
# Edit .env once with your local PostgreSQL credentials.
.\mvnw.cmd spring-boot:run
```

The backend runs at `http://localhost:8080` and adds vocabulary CSV entries not already stored in the database. Optional settings such as `FRONTEND_ORIGIN` and `JWT_SECRET` can also be added to `.env`. The frontend origin defaults to `http://localhost:4200`; a stable JWT secret must be a Base64-encoded key of at least 32 bytes.

In another terminal:

```powershell
Set-Location frontend
npm.cmd install
npm.cmd start
```

Open `http://localhost:4200`, create an account, and sign in. The Angular app calls the backend at `http://localhost:8080/api/v0`; change `frontend/src/app/core/api-url.ts` if the backend address differs.

## API

- `POST /api/v0/users/add` registers a user with `username`, `email`, and `password`.
- `POST /api/v0/users/login` authenticates a user and returns a bearer token.
- `GET /api/v0/terms/quiz` returns 30 randomized prompts containing only each term ID and English translation.
- `POST /api/v0/terms/quiz/submit` accepts 30 answers (`termId`, `article`, `word`, and `plural`) and returns a score with the correct answers. The quiz endpoints require the bearer token returned by login.

## Tests

Run the focused quiz API tests from the repository root:

```powershell
.\mvnw.cmd "-Dtest=TermsControllerTest,StartupRunnerTest" test
```

Build the Angular application:

```powershell
Set-Location frontend
npm.cmd run build
```

Lint the Angular application:

```powershell
Set-Location frontend
npm.cmd run lint
```
