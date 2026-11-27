import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegistrationRequest } from './api.models';
import { API_URL } from './api-url';

const TOKEN_KEY = 'deutschprojekt.access-token';
const EXPIRY_KEY = 'deutschprojekt.token-expiry';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  register(request: RegistrationRequest): Observable<string> {
    return this.http.post(`${API_URL}/users/add`, request, { responseType: 'text' });
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${API_URL}/users/login`, request)
      .pipe(tap((response) => this.storeToken(response)));
  }

  getAccessToken(): string | null {
    const token = sessionStorage.getItem(TOKEN_KEY);
    const expiry = sessionStorage.getItem(EXPIRY_KEY);
    if (!token || !expiry || Date.parse(expiry) <= Date.now()) {
      this.logout();
      return null;
    }
    return token;
  }

  isAuthenticated(): boolean {
    return this.getAccessToken() !== null;
  }

  logout(): void {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(EXPIRY_KEY);
  }

  private storeToken(response: LoginResponse): void {
    sessionStorage.setItem(TOKEN_KEY, response.accessToken);
    sessionStorage.setItem(EXPIRY_KEY, response.expiresAt);
  }
}
