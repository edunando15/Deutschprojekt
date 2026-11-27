import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TimeoutError } from 'rxjs';
import { QuizAnswer, QuizQuestion, QuizResult } from '../core/api.models';
import { QuizService } from '../core/quiz.service';

type QuizPhase = 'idle' | 'loading' | 'playing' | 'submitting' | 'complete' | 'error';

@Component({
  imports: [FormsModule],
  selector: 'app-quiz',
  styleUrl: './quiz.component.css',
  templateUrl: './quiz.component.html',
})
export class QuizComponent {
  protected readonly articles = ['Der', 'Die', 'Das'];
  protected phase: QuizPhase = 'idle';
  protected questions: QuizQuestion[] = [];
  protected answers: QuizAnswer[] = [];
  protected currentIndex = 0;
  protected result: QuizResult | null = null;
  protected errorMessage = '';

  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly quizService = inject(QuizService);

  protected get currentQuestion(): QuizQuestion | null {
    return this.questions[this.currentIndex] ?? null;
  }

  protected get currentAnswer(): QuizAnswer | null {
    return this.answers[this.currentIndex] ?? null;
  }

  protected startQuiz(): void {
    this.phase = 'loading';
    this.errorMessage = '';
    this.quizService.startQuiz().subscribe({
      next: (questions) => {
        this.questions = questions;
        this.answers = questions.map((question) => ({
          termId: question.termId,
          article: '',
          word: '',
          plural: '',
        }));
        this.currentIndex = 0;
        this.phase = 'playing';
        this.changeDetectorRef.markForCheck();
      },
      error: (error: unknown) => this.showError(error),
    });
  }

  protected previousQuestion(): void {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  protected selectArticle(article: string): void {
    if (this.currentAnswer) {
      this.currentAnswer.article = article;
    }
  }

  protected isSkipped(answer: QuizAnswer): boolean {
    return !answer.article && !answer.word.trim() && !answer.plural.trim();
  }

  protected advance(): void {
    if (this.currentIndex < this.questions.length - 1) {
      this.currentIndex++;
      return;
    }

    this.phase = 'submitting';
    this.quizService.submitQuiz(this.answers).subscribe({
      next: (result) => {
        this.result = result;
        this.phase = 'complete';
        this.changeDetectorRef.markForCheck();
      },
      error: (error: unknown) => this.showError(error),
    });
  }

  protected restart(): void {
    this.phase = 'idle';
    this.questions = [];
    this.answers = [];
    this.result = null;
    this.currentIndex = 0;
    this.errorMessage = '';
  }

  private showError(error: unknown): void {
    const wasSubmitting = this.phase === 'submitting';
    this.phase = 'error';
    if (wasSubmitting) {
      this.errorMessage =
        error instanceof TimeoutError
          ? 'Checking your answers took too long. Please try again.'
          : 'Could not submit your answers. Please try again.';
    } else if (error instanceof HttpErrorResponse && error.status === 503) {
      this.errorMessage = 'The quiz needs at least 30 terms. Please try again later.';
    } else if (error instanceof TimeoutError) {
      this.errorMessage = 'The server took too long to prepare your quiz. Please try again.';
    } else {
      this.errorMessage = 'Something went wrong while loading your quiz. Please try again.';
    }
    this.changeDetectorRef.markForCheck();
  }
}
