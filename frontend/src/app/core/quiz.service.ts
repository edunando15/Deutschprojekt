import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { QuizAnswer, QuizQuestion, QuizResult } from './api.models';
import { API_URL } from './api-url';

@Injectable({ providedIn: 'root' })
export class QuizService {
  private readonly http = inject(HttpClient);

  startQuiz(): Observable<QuizQuestion[]> {
    return this.http.get<QuizQuestion[]>(`${API_URL}/terms/quiz`);
  }

  submitQuiz(answers: QuizAnswer[]): Observable<QuizResult> {
    return this.http.post<QuizResult>(`${API_URL}/terms/quiz/submit`, answers);
  }
}
