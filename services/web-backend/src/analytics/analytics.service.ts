import { Injectable } from '@nestjs/common';

@Injectable()
export class AnalyticsService {
  getDistrictSummary() {
    return {
      state: 'JHARKHAND',
      districts_covered: ['Dumka', 'West Singhbhum', 'Khunti', 'Chaibasa', 'Pakur'],
      total_active_schools: 142,
      active_tablets: 386,
      offline_sync_health_percentage: 98.4,
      total_lessons_generated: 1248,
      language_distribution: {
        SANTHALI: 58,
        HO: 26,
        MUNDARI: 16
      },
      fln_grade2_attainment_rate: {
        baseline: 28.5,
        current_with_bhashasetu: 71.2,
        gain_percentage: 42.7
      },
      nipun_bharat_milestones: [
        { milestone: 'Oral Reading Fluency (Tribal Language)', target: '30-35 wpm', achieved: '33.4 wpm' },
        { milestone: 'Number Recognition (1-99)', target: '80% mastery', achieved: '84.6% mastery' },
        { milestone: 'Bilingual Word Association', target: '75% comprehension', achieved: '82.1% comprehension' }
      ]
    };
  }
}
