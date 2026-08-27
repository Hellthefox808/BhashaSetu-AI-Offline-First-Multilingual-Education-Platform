import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { LessonsController } from './lessons.controller';

@Module({
  imports: [],
  controllers: [AppController, LessonsController],
  providers: [AppService],
})
export class AppModule {}
