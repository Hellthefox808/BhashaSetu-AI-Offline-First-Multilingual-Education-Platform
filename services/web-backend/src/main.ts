import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();
  app.setGlobalPrefix('api/v1');

  // OpenAPI 3.1 Swagger Documentation Setup
  const config = new DocumentBuilder()
    .setTitle('BhashaSetu AI — Web Backend Gateway API')
    .setDescription(
      'Mother-Tongue-Based Multilingual Education (MTB-MLE) API for Primary Schools in Jharkhand (SIH26042)',
    )
    .setVersion('3.0.0-PROD')
    .addTag('auth', 'Authentication, Sessions & RBAC')
    .addTag('curriculum', 'State JCERT / NCERT Curriculum Nodes & Learning Outcomes')
    .addTag('lessons', 'Lesson Lifecycle State Machine, HITL Review & Publishing')
    .addTag('sync', 'Durable Outbox Reconciliation & Delta Generator')
    .addTag('analytics', 'District FLN Telemetry & Sync Health Metrics')
    .addTag('devices', 'Tablet Fleet Management, Hardware Health & Remote Lock')
    .addTag('reviews', 'Native Language Linguist Verification & Review Workflow')
    .addTag('offline-packs', 'Signed Offline Content Bundle Bundler & Distribution')
    .addTag('audit', 'Immutable Security Audit Trail & OpenTelemetry Tracing')
    .addBearerAuth()
    .build();

  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api/docs', app, document);

  const port = process.env.PORT || 3001;
  await app.listen(port);
  console.log(`BhashaSetu Web Backend running on http://localhost:${port}/api/v1`);
  console.log(`Swagger OpenAPI Documentation available at http://localhost:${port}/api/docs`);
}
bootstrap();
