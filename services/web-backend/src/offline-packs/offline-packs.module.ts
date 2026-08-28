import { Module } from '@nestjs/common';
import { OfflinePacksService } from './offline-packs.service';
import { OfflinePacksController } from './offline-packs.controller';

@Module({
  controllers: [OfflinePacksController],
  providers: [OfflinePacksService],
  exports: [OfflinePacksService]
})
export class OfflinePacksModule {}
