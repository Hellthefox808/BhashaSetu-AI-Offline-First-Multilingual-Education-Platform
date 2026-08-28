import { Controller, Get } from '@nestjs/common';
import { AuditService } from './audit.service';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('audit')
@Controller('audit')
export class AuditController {
  constructor(private readonly auditService: AuditService) {}

  @Get()
  @ApiOperation({ summary: 'Get immutable audit logs with OpenTelemetry trace correlation' })
  @ApiResponse({ status: 200, description: 'Audit logs retrieved' })
  getLogs() {
    const data = this.auditService.findAll();
    return { count: data.length, data };
  }
}
