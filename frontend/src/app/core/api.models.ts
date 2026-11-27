export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegistrationRequest {
  username: string;
  email: string;
  password: string;
}

export interface QuizQuestion {
  termId: number;
  englishTranslation: string;
}

export interface QuizAnswer {
  termId: number;
  article: string;
  word: string;
  plural: string;
}

export interface AnswerResult {
  termId: number;
  englishTranslation: string;
  correct: boolean;
  correctArticle: string;
  correctWord: string;
  correctPlural: string;
}

export interface QuizResult {
  score: number;
  total: number;
  results: AnswerResult[];
}
