import { Controller, Get } from '@nestjs/common';
import { AnalyticsService } from './analytics.service';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('analytics')
@Controller('analytics')
export class AnalyticsController {
  constructor(private readonly analyticsService: AnalyticsService) {}

  @Get('district-summary')
  @ApiOperation({ summary: 'Get real-time district FLN attainment and sync telemetry' })
  @ApiResponse({ status: 200, description: 'District telemetry retrieved successfully' })
  getDistrictSummary() {
    return this.analyticsService.getDistrictSummary();
  }
}
