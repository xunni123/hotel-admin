package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.entity.CleaningTask;
import com.xunni.hotel.web.mapper.CleaningTaskMapper;
import com.xunni.hotel.web.service.CleaningTaskService;
import org.springframework.stereotype.Service;

@Service
public class CleaningTaskServiceImpl extends ServiceImpl<CleaningTaskMapper, CleaningTask> implements CleaningTaskService {
}
