import { Injectable } from '@nestjs/common';
import { SyncPushRequest, SyncPushResponse, SyncPullResponse } from '@bhashasetu/contracts';
import { LessonsService } from '../lessons/lessons.service';
import { CurriculumService } from '../curriculum/curriculum.service';

@Injectable()
export class SyncService {
  private processedOperationIds: Set<string> = new Set();
  private storedAttempts: any[] = [];

  constructor(
    private readonly lessonsService: LessonsService,
    private readonly curriculumService: CurriculumService,
  ) {}

  processPush(request: SyncPushRequest): SyncPushResponse {
    const acknowledgedIds: string[] = [];
    const conflicts: any[] = [];

    for (const op of request.operations) {
      if (this.processedOperationIds.has(op.operationId)) {
        // Idempotency: Already processed, acknowledge silently
        acknowledgedIds.push(op.operationId);
        continue;
      }

      if (op.entityType === 'ASSESSMENT_ATTEMPT') {
        // Append-only student attempt log
        this.storedAttempts.push({
          ...op.payload,
          deviceId: request.deviceId,
          serverReceivedAt: new Date().toISOString()
        });
        this.processedOperationIds.add(op.operationId);
        acknowledgedIds.push(op.operationId);
      } else if (op.entityType === 'LESSON') {
        // Teacher lesson draft or review
        this.lessonsService.create(op.payload);
        this.processedOperationIds.add(op.operationId);
        acknowledgedIds.push(op.operationId);
      } else {
        this.processedOperationIds.add(op.operationId);
        acknowledgedIds.push(op.operationId);
      }
    }

    return {
      acknowledgedOperationIds: acknowledgedIds,
      conflicts,
      serverTimestamp: new Date().toISOString()
    };
  }

  processPull(cursor?: string): SyncPullResponse {
    const curriculum = this.curriculumService.findAll();
    const lessons = this.lessonsService.findAll().filter((l) => l.status === 'PUBLISHED');

    return {
      newCursor: `cursor_${Date.now()}`,
      curriculumUpdates: curriculum,
      approvedLessons: lessons
    };
  }

  getAttempts() {
    return {
      count: this.storedAttempts.length,
      data: this.storedAttempts
    };
  }
}
