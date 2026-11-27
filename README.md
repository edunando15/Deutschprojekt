# Deutschprojekt

Deutschprojekt is a German vocabulary practice app with an Angular frontend and a Spring Boot/PostgreSQL API. Users can register, sign in, and take quizzes with 30 randomly selected English prompts. For each prompt, they enter the German article, singular, and plural; the app scores the complete quiz and shows corrections.

## Run locally

Start PostgreSQL and configure the backend connection from the repository root in PowerShell:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/deutschprojekt"
$env:DB_USERNAME = "deutschprojekt"
$env:DB_PASSWORD = "<your password>"
.\mvnw.cmd spring-boot:run
```

The backend runs at `http://localhost:8080` and adds vocabulary CSV entries not already stored in the database. If the frontend uses a different origin, set `FRONTEND_ORIGIN` before starting Spring Boot; the default is `http://localhost:4200`. For stable JWTs between backend restarts, set `JWT_SECRET` to a Base64-encoded key of at least 32 bytes.

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
