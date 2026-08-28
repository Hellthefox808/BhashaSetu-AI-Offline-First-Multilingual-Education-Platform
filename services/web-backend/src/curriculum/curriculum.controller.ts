import { Controller, Get, Param, Query } from '@nestjs/common';
import { CurriculumService } from './curriculum.service';
import { ApiTags, ApiOperation, ApiResponse, ApiQuery } from '@nestjs/swagger';

@ApiTags('curriculum')
@Controller('curriculum')
export class CurriculumController {
  constructor(private readonly curriculumService: CurriculumService) {}

  @Get()
  @ApiOperation({ summary: 'List all state-prescribed JCERT curriculum nodes and LO codes with rich metadata filters' })
  @ApiQuery({ name: 'grade', required: false, enum: ['GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5'] })
  @ApiQuery({ name: 'subject', required: false, enum: ['LANGUAGE_FLN', 'MATHEMATICS', 'ENVIRONMENTAL_STUDIES', 'TRIBAL_HERITAGE'] })
  @ApiQuery({ name: 'district', required: false, example: 'Dumka' })
  @ApiQuery({ name: 'competencyCategory', required: false, enum: ['FLN_NUMERACY', 'FLN_LITERACY', 'EVS_ENVIRONMENT', 'TRIBAL_HERITAGE', 'SCIENCE_NATURE'] })
  @ApiQuery({ name: 'bloomTaxonomyLevel', required: false, enum: ['REMEMBER', 'UNDERSTAND', 'APPLY', 'ANALYZE', 'EVALUATE'] })
  @ApiResponse({ status: 200, description: 'Curriculum nodes retrieved successfully' })
  getCurriculum(
    @Query('grade') grade?: string,
    @Query('subject') subject?: string,
    @Query('district') district?: string,
    @Query('competencyCategory') competencyCategory?: string,
    @Query('bloomTaxonomyLevel') bloomTaxonomyLevel?: string
  ) {
    const data = this.curriculumService.findAll({ grade, subject, district, competencyCategory, bloomTaxonomyLevel });
    return { count: data.length, data };
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a specific curriculum node by ID' })
  getNodeById(@Param('id') id: string) {
    return this.curriculumService.findOne(id);
  }
}
