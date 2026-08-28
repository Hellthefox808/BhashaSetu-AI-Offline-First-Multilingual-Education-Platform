import { Injectable, NotFoundException } from '@nestjs/common';
import { OfflinePackageManifest, TargetLanguage } from '@bhashasetu/contracts';

@Injectable()
export class OfflinePacksService {
  private packages: OfflinePackageManifest[] = [
    {
      packageId: 'PKG-SANTHALI-3.0.0',
      version: '3.0.0-PROD',
      targetLanguage: 'SANTHALI',
      gradeLevels: ['GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5'],
      lessonCount: 15,
      audioAssetsCount: 30,
      packageSizeBytes: 14680064, // 14.0 MB
      sha256Checksum: 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855',
      signature: 'SIG_ED25519_JH_EDU_PORTAL_VALIDATED',
      minimumAppVersion: '1.0.0',
      createdAt: '2026-08-28T00:00:00.000Z'
    },
    {
      packageId: 'PKG-HO-3.0.0',
      version: '3.0.0-PROD',
      targetLanguage: 'HO',
      gradeLevels: ['GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5'],
      lessonCount: 15,
      audioAssetsCount: 30,
      packageSizeBytes: 13840120, // 13.2 MB
      sha256Checksum: 'a7c9f44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b112',
      signature: 'SIG_ED25519_JH_EDU_PORTAL_VALIDATED',
      minimumAppVersion: '1.0.0',
      createdAt: '2026-08-28T00:00:00.000Z'
    },
    {
      packageId: 'PKG-MUNDARI-3.0.0',
      version: '3.0.0-PROD',
      targetLanguage: 'MUNDARI',
      gradeLevels: ['GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5'],
      lessonCount: 15,
      audioAssetsCount: 30,
      packageSizeBytes: 13950000, // 13.3 MB
      sha256Checksum: 'f1b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b999',
      signature: 'SIG_ED25519_JH_EDU_PORTAL_VALIDATED',
      minimumAppVersion: '1.0.0',
      createdAt: '2026-08-28T00:00:00.000Z'
    }
  ];

  findAll(): OfflinePackageManifest[] {
    return this.packages;
  }

  findByLanguage(language: TargetLanguage): OfflinePackageManifest {
    const pkg = this.packages.find((p) => p.targetLanguage === language.toUpperCase());
    if (!pkg) {
      throw new NotFoundException(`Offline bundle for language ${language} not found`);
    }
    return pkg;
  }

  buildBundle(language: TargetLanguage): OfflinePackageManifest {
    const pkg = this.findByLanguage(language);
    return {
      ...pkg,
      createdAt: new Date().toISOString()
    };
  }
}
