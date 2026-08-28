import { Injectable } from '@nestjs/common';

export interface AuditEventRecord {
  eventId: string;
  timestamp: string;
  actorId: string;
  actorRole: string;
  schoolId: string;
  action: 'LOGIN' | 'LESSON_GENERATE' | 'LESSON_APPROVE' | 'LESSON_PUBLISH' | 'SYNC_BATCH_PUSH' | 'DEVICE_REVOKE' | 'REVIEW_SUBMIT';
  resourceType: 'LESSON' | 'DEVICE' | 'ASSESSMENT' | 'USER_SESSION' | 'OFFLINE_PACKAGE';
  resourceId: string;
  details: Record<string, any>;
  ipAddress: string;
  traceId: string;
}

@Injectable()
export class AuditService {
  private auditLogs: AuditEventRecord[] = [
    {
      eventId: 'AUD-001',
      timestamp: '2026-08-28T06:00:00.000Z',
      actorId: 'USR-001',
      actorRole: 'TEACHER',
      schoolId: 'SCH-DUMKA-042',
      action: 'LOGIN',
      resourceType: 'USER_SESSION',
      resourceId: 'SES-994182',
      details: { method: 'ARGON2ID_AUTH' },
      ipAddress: '10.14.22.8',
      traceId: 'trace-4bf92f3577b34da6a3ce929d0e0e4736'
    },
    {
      eventId: 'AUD-002',
      timestamp: '2026-08-28T06:15:00.000Z',
      actorId: 'USR-001',
      actorRole: 'TEACHER',
      schoolId: 'SCH-DUMKA-042',
      action: 'LESSON_APPROVE',
      resourceType: 'LESSON',
      resourceId: 'LES-001',
      details: { status: 'APPROVED', language: 'SANTHALI', qualityScore: 0.94 },
      ipAddress: '10.14.22.8',
      traceId: 'trace-7bf92f3577b34da6a3ce929d0e0e9912'
    }
  ];

  findAll(): AuditEventRecord[] {
    return this.auditLogs;
  }

  logEvent(event: Partial<AuditEventRecord>): AuditEventRecord {
    const record: AuditEventRecord = {
      eventId: `AUD-${Date.now().toString().slice(-6)}`,
      timestamp: new Date().toISOString(),
      actorId: event.actorId || 'SYSTEM',
      actorRole: event.actorRole || 'SYSTEM_DAEMON',
      schoolId: event.schoolId || 'SCH-DUMKA-042',
      action: event.action || 'LOGIN',
      resourceType: event.resourceType || 'LESSON',
      resourceId: event.resourceId || 'UNKNOWN',
      details: event.details || {},
      ipAddress: event.ipAddress || '127.0.0.1',
      traceId: event.traceId || `trace-${Date.now()}`
    };
    this.auditLogs.unshift(record);
    return record;
  }
}
