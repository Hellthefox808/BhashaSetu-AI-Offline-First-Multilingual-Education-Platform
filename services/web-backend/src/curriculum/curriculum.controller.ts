import { Controller, Get, Param, Query } from '@nestjs/common';
import { CurriculumService } from './curriculum.service';
import { ApiTags, ApiOperation, ApiResponse, ApiQuery } from '@nestjs/swagger';

@ApiTags('curriculum')
@Controller('curriculum')
export class CurriculumController {
  constructor(private readonly curriculumService: CurriculumService) {}

  @Get()
  @ApiOperation({ summary: 'List all state-prescribed JCERT curriculum nodes and LO codes' })
  @ApiQuery({ name: 'grade', required: false, enum: ['GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5'] })
  @ApiQuery({ name: 'subject', required: false, enum: ['LANGUAGE_FLN', 'MATHEMATICS', 'ENVIRONMENTAL_STUDIES', 'TRIBAL_HERITAGE'] })
  @ApiResponse({ status: 200, description: 'Curriculum nodes retrieved successfully' })
  getCurriculum(@Query('grade') grade?: string, @Query('subject') subject?: string) {
    const data = this.curriculumService.findAll(grade, subject);
    return { count: data.length, data };
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a specific curriculum node by ID' })
  getNodeById(@Param('id') id: string) {
    return this.curriculumService.findOne(id);
  }
}
