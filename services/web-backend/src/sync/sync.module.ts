import { Module } from '@nestjs/common';
import { SyncService } from './sync.service';
import { SyncController } from './sync.controller';
import { LessonsModule } from '../lessons/lessons.module';
import { CurriculumModule } from '../curriculum/curriculum.module';

@Module({
  imports: [LessonsModule, CurriculumModule],
  controllers: [SyncController],
  providers: [SyncService],
  exports: [SyncService],
})
export class SyncModule {}
