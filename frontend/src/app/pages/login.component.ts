import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  imports: [FormsModule, RouterLink],
  selector: 'app-login',
  styleUrl: './login.component.css',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  protected username = '';
  protected password = '';
  protected errorMessage = '';
  protected isSubmitting = false;
  protected readonly registered = inject(ActivatedRoute).snapshot.queryParamMap.has('registered');

  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  protected submit(): void {
    this.errorMessage = '';
    this.isSubmitting = true;
    this.auth.login({ username: this.username.trim(), password: this.password }).subscribe({
      next: () => void this.router.navigate(['/quiz']),
      error: (error: unknown) => {
        this.errorMessage =
          error instanceof HttpErrorResponse && error.status === 401
            ? 'That username and password do not match.'
            : 'Could not sign in. Please check your connection and try again.';
        this.isSubmitting = false;
      },
    });
  }
}
