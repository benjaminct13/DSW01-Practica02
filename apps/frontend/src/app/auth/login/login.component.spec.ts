import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthApiService } from '../../core/services/auth-api.service';
import { AuthSessionService } from '../../core/services/auth-session.service';

describe('LoginComponent', () => {
  it('should call auth service when form is valid', () => {
    const loginSpy = jasmine.createSpy('login').and.returnValue(of({
      sessionExpiresAt: new Date().toISOString(),
      authMode: 'ui-session',
      empleado: { clave: 'E-001', nombre: 'Ana', email: 'ana@example.local' }
    }));
    const navigateSpy = jasmine.createSpy('navigate').and.returnValue(Promise.resolve(true));
    const setSessionSpy = jasmine.createSpy('setSession');

    TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideHttpClient(),
        { provide: AuthApiService, useValue: { login: loginSpy } },
        { provide: AuthSessionService, useValue: { setSession: setSessionSpy } },
        { provide: Router, useValue: { navigate: navigateSpy } }
      ]
    });

    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    component.form.setValue({ email: 'ana@example.local', password: 'Passw0rd' });

    component.submit();

    expect(loginSpy).toHaveBeenCalledWith({ email: 'ana@example.local', password: 'Passw0rd' });
    expect(setSessionSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/perfil']);
  });
});
