import { Injectable, UnauthorizedException } from '@nestjs/common';

export interface UserSession {
  userId: string;
  email: string;
  role: 'TEACHER' | 'NATIVE_REVIEWER' | 'SCHOOL_ADMIN' | 'DISTRICT_ADMIN';
  schoolId: string;
  token: string;
}

@Injectable()
export class AuthService {
  private users = [
    {
      userId: 'USR-001',
      email: 'ramesh.kumar@jcert.in',
      password: 'password123',
      role: 'TEACHER' as const,
      schoolId: 'SCH-DUMKA-042',
      name: 'Ramesh Kumar (Grade 2 Teacher)'
    },
    {
      userId: 'USR-002',
      email: 'sunita.soren@jcert.in',
      password: 'password123',
      role: 'NATIVE_REVIEWER' as const,
      schoolId: 'SCH-HQ-001',
      name: 'Dr. Sunita Soren (Santhali Linguist)'
    }
  ];

  login(credentials: { email: string; password: string }): UserSession {
    const user = this.users.find(
      (u) => u.email === credentials.email && u.password === credentials.password
    );

    if (!user) {
      throw new UnauthorizedException('Invalid email or password');
    }

    return {
      userId: user.userId,
      email: user.email,
      role: user.role,
      schoolId: user.schoolId,
      token: `jwt_${user.userId}_${Date.now()}`
    };
  }
}
