import { Controller, Get, Post, Put, Body, Param } from '@nestjs/common';
import { DevicesService, TabletDevice } from './devices.service';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('devices')
@Controller('devices')
export class DevicesController {
  constructor(private readonly devicesService: DevicesService) {}

  @Get()
  @ApiOperation({ summary: 'List all registered school tablets and hardware health' })
  @ApiResponse({ status: 200, description: 'Devices list retrieved' })
  listDevices() {
    const data = this.devicesService.findAll();
    return { count: data.length, data };
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get specific tablet health by device ID' })
  getDevice(@Param('id') id: string) {
    return this.devicesService.findOne(id);
  }

  @Post('heartbeat')
  @ApiOperation({ summary: 'Tablet heartbeat, resource telemetry, and registration' })
  heartbeat(@Body() body: Partial<TabletDevice>) {
    return this.devicesService.registerOrHeartbeat(body);
  }

  @Put(':id/revoke')
  @ApiOperation({ summary: 'Revoke and remotely lock lost or decommissioned tablet' })
  revoke(@Param('id') id: string) {
    return this.devicesService.revoke(id);
  }
}
