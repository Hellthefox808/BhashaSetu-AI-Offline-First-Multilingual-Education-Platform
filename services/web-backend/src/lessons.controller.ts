import { Controller, Get, Post, Body } from '@nestjs/common';

@Controller('lessons')
export class LessonsController {
  private lessons = [
    {
      id: 'LES-001',
      title: 'पेड़ और पत्तियाँ (Trees and Leaves)',
      grade: 'GRADE_2',
      subject: 'ENVIRONMENTAL_STUDIES',
      language: 'SANTHALI',
      script: 'OL_CHIKI',
      status: 'APPROVED'
    }
  ];

  @Get()
  listLessons() {
    return { count: this.lessons.length, data: this.lessons };
  }

  @Post()
  createLesson(@Body() body: any) {
    const newLesson = { id: 'LES-' + Date.now(), ...body, status: 'APPROVED' };
    this.lessons.push(newLesson);
    return { success: true, lesson: newLesson };
  }
}
