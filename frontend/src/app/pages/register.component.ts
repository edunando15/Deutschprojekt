import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  imports: [FormsModule, RouterLink],
  selector: 'app-register',
  styleUrl: './register.component.css',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  protected username = '';
  protected email = '';
  protected password = '';
  protected confirmPassword = '';
  protected errorMessage = '';
  protected isSubmitting = false;

  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  protected submit(): void {
    this.errorMessage = '';
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'The passwords do not match.';
      return;
    }

    this.isSubmitting = true;
    this.auth
      .register({
        username: this.username.trim(),
        email: this.email.trim(),
        password: this.password,
      })
      .pipe(
        finalize(() => {
          this.isSubmitting = false;
          this.changeDetectorRef.markForCheck();
        }),
      )
      .subscribe({
        next: () => void this.router.navigate(['/login'], { queryParams: { registered: 'true' } }),
        error: (error: unknown) => {
          const backendMessage =
            error instanceof HttpErrorResponse && typeof error.error === 'string'
              ? error.error
              : '';
          this.errorMessage =
            error instanceof HttpErrorResponse && error.status === 409
              ? 'That username is already taken.'
              : error instanceof HttpErrorResponse && error.status === 400
                ? backendMessage || 'Please check your details and try again.'
                : 'Could not create your account. Please check your connection and try again.';
          this.changeDetectorRef.markForCheck();
        },
      });
  }
}
