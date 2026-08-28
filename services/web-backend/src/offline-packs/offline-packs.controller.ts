import { Controller, Get, Post, Param } from '@nestjs/common';
import { OfflinePacksService } from './offline-packs.service';
import { TargetLanguage } from '@bhashasetu/contracts';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('offline-packs')
@Controller('offline-packs')
export class OfflinePacksController {
  constructor(private readonly offlinePacksService: OfflinePacksService) {}

  @Get()
  @ApiOperation({ summary: 'List all signed offline content package manifests' })
  @ApiResponse({ status: 200, description: 'Offline packs retrieved' })
  listPackages() {
    const data = this.offlinePacksService.findAll();
    return { count: data.length, data };
  }

  @Get(':language')
  @ApiOperation({ summary: 'Get signed offline package manifest for specific target language' })
  getPackage(@Param('language') language: TargetLanguage) {
    return this.offlinePacksService.findByLanguage(language);
  }

  @Post(':language/build')
  @ApiOperation({ summary: 'Trigger build and signing of offline content bundle' })
  buildPackage(@Param('language') language: TargetLanguage) {
    return this.offlinePacksService.buildBundle(language);
  }
}
