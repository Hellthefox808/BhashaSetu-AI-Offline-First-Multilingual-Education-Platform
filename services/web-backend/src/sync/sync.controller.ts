import { Controller, Post, Get, Body, Query, HttpCode, HttpStatus } from '@nestjs/common';
import { SyncService } from './sync.service';
import { SyncPushRequest } from '@bhashasetu/contracts';
import { ApiTags, ApiOperation, ApiResponse, ApiQuery } from '@nestjs/swagger';

@ApiTags('sync')
@Controller('sync')
export class SyncController {
  constructor(private readonly syncService: SyncService) {}

  @Post('push')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Durable outbox batch sync push with UUID idempotency deduplication' })
  @ApiResponse({ status: 200, description: 'Batch synchronization successful' })
  syncPush(@Body() body: SyncPushRequest) {
    return this.syncService.processPush(body);
  }

  @Get('pull')
  @ApiOperation({ summary: 'Delta sync pull for verified lessons and curriculum updates' })
  @ApiQuery({ name: 'cursor', required: false })
  @ApiResponse({ status: 200, description: 'Delta packages retrieved' })
  syncPull(@Query('cursor') cursor?: string) {
    return this.syncService.processPull(cursor);
  }

  @Get('attempts')
  @ApiOperation({ summary: 'Get all synced append-only student assessment attempts' })
  getAttempts() {
    return this.syncService.getAttempts();
  }
}
