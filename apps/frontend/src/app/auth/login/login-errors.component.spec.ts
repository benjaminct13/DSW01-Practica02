import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthApiService } from '../../core/services/auth-api.service';
import { AuthSessionService } from '../../core/services/auth-session.service';

describe('Login errors', () => {
  it('should show friendly message when api fails', () => {
    const loginSpy = jasmine.createSpy('login').and.returnValue(throwError(() => new Error('401')));
    const navigateSpy = jasmine.createSpy('navigate').and.returnValue(Promise.resolve(true));

    TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideHttpClient(),
        { provide: AuthApiService, useValue: { login: loginSpy } },
        { provide: AuthSessionService, useValue: { setSession: jasmine.createSpy('setSession') } },
        { provide: Router, useValue: { navigate: navigateSpy } }
      ]
    });

    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    component.form.setValue({ email: 'ana@example.local', password: 'WrongPass1' });

    component.submit();

    expect(component.errorMessage).toContain('Credenciales invalidas');
  });
});
