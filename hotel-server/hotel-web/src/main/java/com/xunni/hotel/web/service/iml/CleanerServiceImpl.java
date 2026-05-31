package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.entity.Cleaner;
import com.xunni.hotel.web.mapper.CleanerMapper;
import com.xunni.hotel.web.service.CleanerService;
import org.springframework.stereotype.Service;

@Service
public class CleanerServiceImpl extends ServiceImpl<CleanerMapper, Cleaner> implements CleanerService {
}
