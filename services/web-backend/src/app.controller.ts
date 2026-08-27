import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get('health')
  getHealth() {
    return {
      status: 'UP',
      service: 'BhashaSetu NestJS Backend',
      timestamp: new Date().toISOString(),
      tenancy: 'ENABLED'
    };
  }
}
