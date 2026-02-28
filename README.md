# InstaNote

A Spring Boot web app that generates structured notes and MCQ quizzes from YouTube videos using Google Gemini AI.

## Features

- Generate structured notes from any YouTube video URL
- Generate MCQ quizzes from saved notes
- JWT-based user authentication
- React frontend served via Spring Boot

## Tech Stack

- **Backend:** Java 23, Spring Boot 3.4, Spring Data JPA
- **Database:** PostgreSQL
- **AI:** Google Gemini (via `GeminiSessionHandler`)
- **Auth:** JWT (jjwt)
- **Frontend:** React (pre-built static assets)

## Prerequisites

- Java 23+
- Maven
- PostgreSQL database
- Google Gemini API key

## Setup

1. Clone the repo.
2. Create a `secrets.env` file in the project root:
   ```
   GEMINI_API_KEY=your_gemini_api_key
   DB_URL=jdbc:postgresql://localhost:5432/instanote
   DB_USERNAME=your_db_user
   DB_PASSWORD=your_db_password
   JWT_SECRET=your_jwt_secret
   ```
3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Open `http://localhost:8080` in your browser.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and receive JWT |
| GET | `/auth/me` | Get current user |
| POST | `/api/generate-notes` | Generate notes from a YouTube URL |
| GET | `/api/notes` | Get all notes for the current user |
| GET | `/api/notes/{id}` | Get a specific note |
| POST | `/api/generate-mcqs/{id}/{numQuestions}` | Generate MCQs from a note |

All `/api/*` endpoints require a `Bearer <token>` authorization header.
