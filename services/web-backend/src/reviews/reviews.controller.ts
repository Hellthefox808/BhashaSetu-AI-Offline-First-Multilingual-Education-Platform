import { Controller, Get, Post, Body, Param } from '@nestjs/common';
import { ReviewsService, ReviewItem } from './reviews.service';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('reviews')
@Controller('reviews')
export class ReviewsController {
  constructor(private readonly reviewsService: ReviewsService) {}

  @Get()
  @ApiOperation({ summary: 'List all native linguist review records and quality audits' })
  @ApiResponse({ status: 200, description: 'Review list retrieved' })
  listReviews() {
    const data = this.reviewsService.findAll();
    return { count: data.length, data };
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get specific review item by review ID' })
  getReview(@Param('id') id: string) {
    return this.reviewsService.findOne(id);
  }

  @Post()
  @ApiOperation({ summary: 'Submit native linguist review, orthographic corrections, and feedback' })
  submitReview(@Body() body: Partial<ReviewItem>) {
    return this.reviewsService.submitReview(body);
  }
}
